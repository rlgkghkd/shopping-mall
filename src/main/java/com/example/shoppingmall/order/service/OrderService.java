package com.example.shoppingmall.order.service;

import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.order.dto.OrderResponseDto;
import com.example.shoppingmall.order.dto.PostOrderRequestDto;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.exception.OrderErrorCode;
import com.example.shoppingmall.order.repository.OrderRepository;
import com.example.shoppingmall.order.type.OrderStatus;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	// 주문 생성(로그인 유저만 가능)
	@Transactional
	public OrderResponseDto createOrder(Long userId, PostOrderRequestDto request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(OrderErrorCode.USER_NOT_FOUND));

		if (user.getUserRole() != UserRole.USER) {
			throw new CustomException(OrderErrorCode.FORBIDDEN_ORDER_CREATE); // 새 에러 코드 필요
		}


		Item item = itemRepository.findById(request.getItemId())
				.orElseThrow(() -> new CustomException(OrderErrorCode.ITEM_NOT_FOUND));

		Order order = new Order(user, item, request.getOrderAddress(),
				request.getOrderStatus(), request.getOrderPrice());

		Order saved = orderRepository.save(order);
		return new OrderResponseDto(saved);
	}

	// 주문 상태 수정(관리자만 가능)
	@Transactional
	public OrderResponseDto updateOrder(Long orderId, OrderStatus status, UserRole role) {
		if (status == null) {
			throw new CustomException(OrderErrorCode.ORDER_STATUS_REQUIRED);
		}
		if (role == UserRole.USER) {
			throw new CustomException(OrderErrorCode.FORBIDDEN_ORDER_STATUS_UPDATE);
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

		order.updateStatus(status);
		return new OrderResponseDto(order, "주문 상태 변경 완료");
	}

	// 주문 취소
	@Transactional
	public String cancelOrder(Long orderId, Long userId, UserRole role) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

		OrderStatus status = order.getOrderStatus();

		if (status == OrderStatus.CANCELED) {
			throw new CustomException(OrderErrorCode.ALREADY_CANCELED); // 새 에러코드
		}

		if (role == UserRole.USER && status != OrderStatus.PENDING) {
			throw new CustomException(OrderErrorCode.INVALID_CANCEL_STATUS_USER);
		}

		if (role == UserRole.ADMIN && !(status == OrderStatus.PENDING
				|| status == OrderStatus.PROCESSING)) {
			throw new CustomException(OrderErrorCode.INVALID_CANCEL_STATUS_ADMIN);
		}

		if (role == UserRole.USER && !order.getUser().getId().equals(userId)) {
			throw new CustomException(OrderErrorCode.FORBIDDEN_ORDER_ACCESS);
		}

		order.updateStatus(OrderStatus.CANCELED);
		return "주문이 성공적으로 취소되었습니다.";
	}

	// 전체 주문 조회 (관리자용)
	@Transactional(readOnly = true)
	public List<OrderResponseDto> getAllOrders() {
		return orderRepository.findAll().stream()
				.map(OrderResponseDto::new)
				.toList();
	}

	// 본인 주문 목록 조회 (유저용)
	@Transactional(readOnly = true)
	public List<OrderResponseDto> getOrdersByUserId(Long userId) {
		return orderRepository.findAll().stream()
				.filter(order -> order.getUser().getId().equals(userId))
				.map(OrderResponseDto::new)
				.toList();
	}

	// 주문 상세 조회 (유저는 본인만, 관리자는 모두)
	@Transactional(readOnly = true)
	public OrderResponseDto getOrder(Long id, Long userId, UserRole role) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

		if (role == UserRole.USER && !order.getUser().getId().equals(userId)) {
			throw new CustomException(OrderErrorCode.FORBIDDEN_ORDER_ACCESS);
		}

		return new OrderResponseDto(order);
	}
}
