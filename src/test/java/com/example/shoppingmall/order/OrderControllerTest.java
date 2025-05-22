package com.example.shoppingmall.order;

import com.example.shoppingmall.order.controller.OrderController;
import com.example.shoppingmall.order.dto.PostOrderRequestDto;
import com.example.shoppingmall.order.dto.OrderResponseDto;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.service.OrderService;
import com.example.shoppingmall.order.type.OrderStatus;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.order.exception.OrderErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	private User user;
	private Item item;
	private Order order;

	@BeforeEach
	void setUp() {
		user = new User("user", "user@email.com", UserRole.USER, "encodedPassword");
		setField(user, "id", 1L);

		item = new Item();
		setField(item, "id", 100L);

		order = new Order(user, item, "서울시 강남구", OrderStatus.PENDING, 15000);
		setField(order, "id", 10L);
	}

	@Test
	@DisplayName(" 주문 생성 성공")
	void createOrder_success() throws Exception {
		PostOrderRequestDto requestDto = new PostOrderRequestDto(100L, "서울시 강남구", OrderStatus.PENDING, 15000);
		OrderResponseDto responseDto = new OrderResponseDto(order);

		Mockito.when(orderService.createOrder(eq(1L), any(PostOrderRequestDto.class)))
				.thenReturn(responseDto);

		mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestDto))
						.requestAttr("userId", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value(10L));
	}

	@Test
	@DisplayName(" 주문 생성 실패 - 유저 없음")
	void createOrder_fail_userNotFound() throws Exception {
		PostOrderRequestDto requestDto = new PostOrderRequestDto(100L, "서울시", OrderStatus.PENDING, 10000);

		Mockito.when(orderService.createOrder(eq(1L), any(PostOrderRequestDto.class)))
				.thenThrow(new CustomException(OrderErrorCode.USER_NOT_FOUND));

		mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestDto))
						.requestAttr("userId", 1L))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(OrderErrorCode.USER_NOT_FOUND.getMessage()));
	}

	@Test
	@DisplayName(" 주문 목록 조회 성공")
	void getOrdersByUserId_success() throws Exception {
		Order order2 = new Order(user, item, "서울시 서초구", OrderStatus.PROCESSING, 20000);
		setField(order2, "id", 11L);

		List<OrderResponseDto> orders = List.of(
				new OrderResponseDto(order),
				new OrderResponseDto(order2)
		);

		Mockito.when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

		mockMvc.perform(get("/orders")
						.requestAttr("userId", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].orderId").value(10L))
				.andExpect(jsonPath("$[1].orderId").value(11L));
	}

	@Test
	@DisplayName(" 주문 상세 조회 성공")
	void getOrder_success() throws Exception {
		OrderResponseDto dto = new OrderResponseDto(order);

		Mockito.when(orderService.getOrder(10L, 1L, UserRole.USER)).thenReturn(dto);

		mockMvc.perform(get("/orders/10")
						.requestAttr("userId", 1L)
						.requestAttr("userRole", UserRole.USER))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value(10L));
	}

	@Test
	@DisplayName(" 주문 상세 조회 실패 - 접근 불가")
	void getOrder_forbidden() throws Exception {
		Mockito.when(orderService.getOrder(10L, 1L, UserRole.USER))
				.thenThrow(new CustomException(OrderErrorCode.FORBIDDEN_ORDER_ACCESS));

		mockMvc.perform(get("/orders/10")
						.requestAttr("userId", 1L)
						.requestAttr("userRole", UserRole.USER))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(OrderErrorCode.FORBIDDEN_ORDER_ACCESS.getMessage()));
	}

	@Test
	@DisplayName("🗑 주문 취소 성공")
	void cancelOrder_success() throws Exception {
		Mockito.when(orderService.cancelOrder(10L, 1L, UserRole.USER))
				.thenReturn("주문이 성공적으로 취소되었습니다.");

		mockMvc.perform(delete("/orders/10")
						.requestAttr("userId", 1L)
						.requestAttr("userRole", UserRole.USER))
				.andExpect(status().isOk())
				.andExpect(content().string("주문이 성공적으로 취소되었습니다."));
	}

	@Test
	@DisplayName(" 주문 취소 실패 - 잘못된 상태")
	void cancelOrder_invalidStatus() throws Exception {
		Mockito.when(orderService.cancelOrder(10L, 1L, UserRole.USER))
				.thenThrow(new CustomException(OrderErrorCode.INVALID_CANCEL_STATUS_USER));

		mockMvc.perform(delete("/orders/10")
						.requestAttr("userId", 1L)
						.requestAttr("userRole", UserRole.USER))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(OrderErrorCode.INVALID_CANCEL_STATUS_USER.getMessage()));
	}

	@Test
	@DisplayName(" 주문 상태 변경 성공 - 관리자")
	void updateOrderStatus_success() throws Exception {
		order.updateStatus(OrderStatus.PROCESSING);
		OrderResponseDto dto = new OrderResponseDto(order);

		Mockito.when(orderService.updateOrder(10L, OrderStatus.PROCESSING, UserRole.ADMIN)).thenReturn(dto);

		mockMvc.perform(patch("/orders/10")
						.param("status", "PROCESSING")
						.requestAttr("userRole", UserRole.ADMIN))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderStatus").value("PROCESSING"));
	}

	@Test
	@DisplayName(" 주문 상태 변경 실패 - 일반 유저")
	void updateOrderStatus_forbidden() throws Exception {
		Mockito.when(orderService.updateOrder(10L, OrderStatus.PROCESSING, UserRole.USER))
				.thenThrow(new CustomException(OrderErrorCode.FORBIDDEN_ORDER_STATUS_UPDATE));

		mockMvc.perform(patch("/orders/10")
						.param("status", "PROCESSING")
						.requestAttr("userRole", UserRole.USER))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message").value(OrderErrorCode.FORBIDDEN_ORDER_STATUS_UPDATE.getMessage()));
	}

	// 리플렉션 유틸
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
