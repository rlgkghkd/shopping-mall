package com.example.shoppingmall.order.service;

import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.order.dto.OrderResponseDto;
import com.example.shoppingmall.order.dto.PostOrderRequestDto;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.repository.OrderRepository;
import com.example.shoppingmall.order.type.OrderStatus;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

	@Transactional
	public OrderResponseDto createOrder(PostOrderRequestDto request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new EntityNotFoundException("유저 없음"));
		Item item = itemRepository.findById(request.getItemId())
				.orElseThrow(() -> new EntityNotFoundException("상품 없음"));

		Order order = new Order(user, item, request.getOrderAddress(),
				request.getOrderStatus(), request.getOrderPrice());

		Order saved = orderRepository.save(order);

		return new OrderResponseDto(saved);
	}

	public List<OrderResponseDto> getAllOrders() {
		return orderRepository.findAll().stream()
				.map(OrderResponseDto::new)
				.toList();
	}

	public OrderResponseDto getOrder(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("주문 없음"));
		return new OrderResponseDto(order);
	}

	@Transactional
	public OrderResponseDto updateOrder(Long id, OrderStatus status) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("주문 없음"));
		order.updateStatus(status);
		return new OrderResponseDto(order, "주문 상태 변경 완료");
	}

	@Transactional
	public String deleteOrder(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("주문 없음"));
		orderRepository.delete(order);
		return "주문 삭제 완료";
	}

}

