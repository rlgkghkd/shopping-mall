package com.example.shoppingmall.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.order.dto.OrderResponseDto;
import com.example.shoppingmall.order.dto.PostOrderRequestDto;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.exception.OrderErrorCode;
import com.example.shoppingmall.order.repository.OrderRepository;
import com.example.shoppingmall.order.service.OrderService;
import com.example.shoppingmall.order.type.OrderStatus;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ItemRepository itemRepository;

	private User user;
	private User admin;
	private Item item;
	private Order order;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User("user1", "user@email.com", UserRole.USER, "encodedPassword");
		setField(user, "id", 1L);

		admin = new User("admin1", "admin@email.com", UserRole.ADMIN, "encodedPassword");
		setField(admin, "id", 2L);

		item = new Item();
		setField(item, "id", 100L);

		order = new Order(user, item, "주소", OrderStatus.PENDING, 10000);
		setField(order, "id", 10L);
	}

	@Nested
	class CreateOrderTest {

		@Test
		@DisplayName("주문 생성 성공")
		void createOrder_success() {
			PostOrderRequestDto request = new PostOrderRequestDto(item.getId(), "주소",
					OrderStatus.PENDING, 10000);
			when(userRepository.findById(1L)).thenReturn(Optional.of(user));
			when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
			when(orderRepository.save(any())).thenReturn(order);

			OrderResponseDto response = orderService.createOrder(1L, request);

			assertThat(response.getOrderId()).isEqualTo(order.getId());
			verify(orderRepository).save(any());
		}

		@Test
		@DisplayName("유저 없음")
		void createOrder_userNotFound() {
			when(userRepository.findById(1L)).thenReturn(Optional.empty());
			PostOrderRequestDto request = new PostOrderRequestDto(item.getId(), "주소",
					OrderStatus.PENDING, 10000);

			assertThatThrownBy(() -> orderService.createOrder(1L, request))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.USER_NOT_FOUND.getMessage());
		}

		@Test
		@DisplayName("아이템 없음")
		void createOrder_itemNotFound() {
			when(userRepository.findById(1L)).thenReturn(Optional.of(user));
			when(itemRepository.findById(100L)).thenReturn(Optional.empty());
			PostOrderRequestDto request = new PostOrderRequestDto(item.getId(), "주소",
					OrderStatus.PENDING, 10000);

			assertThatThrownBy(() -> orderService.createOrder(1L, request))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.ITEM_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class CancelOrderTest {

		@Test
		@DisplayName("유저가 주문 취소 성공")
		void cancelOrder_user_success() {
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
			String message = orderService.cancelOrder(10L, 1L, UserRole.USER);
			assertThat(message).isEqualTo("주문이 성공적으로 취소되었습니다.");
		}

		@Test
		@DisplayName("유저 - 취소불가 상태")
		void cancelOrder_user_invalidStatus() {
			order.updateStatus(OrderStatus.PROCESSING);
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

			assertThatThrownBy(() -> orderService.cancelOrder(10L, 1L, UserRole.USER))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.INVALID_CANCEL_STATUS_USER.getMessage());
		}

		@Test
		@DisplayName("유저 - 본인 주문 아님")
		void cancelOrder_user_notOwner() {
			setField(order.getUser(), "id", 99L);
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

			assertThatThrownBy(() -> orderService.cancelOrder(10L, 1L, UserRole.USER))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.FORBIDDEN_ORDER_ACCESS.getMessage());
		}

		@Test
		@DisplayName("관리자 - 성공")
		void cancelOrder_admin_success() {
			order.updateStatus(OrderStatus.PROCESSING);
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
			String message = orderService.cancelOrder(10L, 2L, UserRole.ADMIN);
			assertThat(message).isEqualTo("주문이 성공적으로 취소되었습니다.");
		}

		@Test
		@DisplayName("관리자 - 취소불가 상태")
		void cancelOrder_admin_invalidStatus() {
			order.updateStatus(OrderStatus.DELIVERED);
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

			assertThatThrownBy(() -> orderService.cancelOrder(10L, 2L, UserRole.ADMIN))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.INVALID_CANCEL_STATUS_ADMIN.getMessage());
		}
	}

	@Nested
	class UpdateOrderStatusTest {

		@Test
		@DisplayName("주문 상태 변경 성공")
		void updateOrder_success() {
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
			OrderResponseDto dto = orderService.updateOrder(10L, OrderStatus.PROCESSING,
					UserRole.ADMIN);
			assertThat(dto.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING.name());
		}

		@Test
		@DisplayName("상태값 없음")
		void updateOrder_nullStatus() {
			assertThatThrownBy(() -> orderService.updateOrder(10L, null, UserRole.ADMIN))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.ORDER_STATUS_REQUIRED.getMessage());
		}

		@Test
		@DisplayName("유저가 상태 변경 시도")
		void updateOrder_userNotAllowed() {
			assertThatThrownBy(
					() -> orderService.updateOrder(10L, OrderStatus.PROCESSING, UserRole.USER))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.FORBIDDEN_ORDER_STATUS_UPDATE.getMessage());
		}
	}

	@Nested
	class GetOrderTest {

		@Test
		@DisplayName("유저 본인 주문 목록 조회")
		void getOrdersByUserId_success() {
			when(orderRepository.findAll()).thenReturn(List.of(order));
			List<OrderResponseDto> result = orderService.getOrdersByUserId(1L);
			assertThat(result).hasSize(1);
		}

		@Test
		@DisplayName("전체 주문 조회 (관리자)")
		void getAllOrders_success() {
			when(orderRepository.findAll()).thenReturn(List.of(order));
			List<OrderResponseDto> result = orderService.getAllOrders();
			assertThat(result).hasSize(1);
		}

		@Test
		@DisplayName("주문 상세 조회 - 본인")
		void getOrder_userSuccess() {
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
			OrderResponseDto dto = orderService.getOrder(10L, 1L, UserRole.USER);
			assertThat(dto.getOrderId()).isEqualTo(10L);
		}

		@Test
		@DisplayName("주문 상세 조회 - 관리자")
		void getOrder_adminSuccess() {
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
			OrderResponseDto dto = orderService.getOrder(10L, 2L, UserRole.ADMIN);
			assertThat(dto.getOrderId()).isEqualTo(10L);
		}

		@Test
		@DisplayName("주문 상세 조회 - 유저 다른사람")
		void getOrder_userInvalidAccess() {
			setField(order.getUser(), "id", 99L);
			when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

			assertThatThrownBy(() -> orderService.getOrder(10L, 1L, UserRole.USER))
					.isInstanceOf(CustomException.class)
					.hasMessage(OrderErrorCode.FORBIDDEN_ORDER_ACCESS.getMessage());
		}
	}


	private void setField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
