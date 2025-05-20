package com.example.shoppingmall.order.controller;

import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.order.dto.OrderResponseDto;
import com.example.shoppingmall.order.dto.PostOrderRequestDto;
import com.example.shoppingmall.order.dto.PutOrderRequestDto;
import com.example.shoppingmall.order.exception.OrderErrorCode;
import com.example.shoppingmall.order.exception.OrderException;
import com.example.shoppingmall.order.service.OrderService;
import com.example.shoppingmall.user.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final JwtUtil jwtUtil;
	// 주문 생성(로그인 유저만 가능)
	@PostMapping
	public ResponseEntity<OrderResponseDto> create(@RequestBody @Valid PostOrderRequestDto dto,
			HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Long userId = jwtUtil.getUserIdFromToken(token);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(orderService.createOrder(userId, dto));
	}
	// 주문 상태 수정(관리자만 가능)
	@PutMapping("/{id}")
	public ResponseEntity<OrderResponseDto> update(@PathVariable Long id,
			@RequestBody @Valid PutOrderRequestDto dto,
			HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		UserRole role = jwtUtil.getUserRoleFromToken(token);

		return ResponseEntity.ok(orderService.updateOrder(id, dto.getOrderStatus(), role));
	}
	// 주문취소(유저는 PENDING 상태만, 관리자는 PROCESSING 까지)
	@PatchMapping("/{id}/cancel")
	public ResponseEntity<Map<String, String>> cancel(@PathVariable Long id,
			HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Long userId = jwtUtil.getUserIdFromToken(token);
		UserRole role = jwtUtil.getUserRoleFromToken(token);

		String msg = orderService.cancelOrder(id, userId, role);
		return ResponseEntity.ok(Map.of("message", msg));
	}

	// 본인 주문 목록 조회 (유저용)
	@GetMapping("/my")
	public ResponseEntity<List<OrderResponseDto>> getMyOrders(HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Long userId = jwtUtil.getUserIdFromToken(token);
		return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
	}

	// 전체 주문 조회 (관리자용)
	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> getAllOrders(HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		UserRole role = jwtUtil.getUserRoleFromToken(token);

		if (role != UserRole.ADMIN) {
			throw new OrderException(OrderErrorCode.FORBIDDEN_ORDER_LIST_ACCESS);
		}

		return ResponseEntity.ok(orderService.getAllOrders());
	}

	// 주문 상세 조회 (유저는 본인만, 관리자는 모두)
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDto> getOne(@PathVariable Long id,
			HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Long userId = jwtUtil.getUserIdFromToken(token);
		UserRole role = jwtUtil.getUserRoleFromToken(token);

		return ResponseEntity.ok(orderService.getOrder(id, userId, role));
	}
}
