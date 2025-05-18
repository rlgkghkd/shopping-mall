package com.example.shoppingmall.order.controller;

import com.example.shoppingmall.order.dto.OrderResponseDto;
import com.example.shoppingmall.order.dto.PostOrderRequestDto;
import com.example.shoppingmall.order.dto.PutOrderRequestDto;
import com.example.shoppingmall.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponseDto> create(@RequestBody @Valid PostOrderRequestDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
	}

	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> getAll() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrder(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<OrderResponseDto> update(@PathVariable Long id,
			@RequestBody @Valid PutOrderRequestDto dto) {
		return ResponseEntity.ok(orderService.updateOrder(id, dto.getOrderStatus()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
		String msg = orderService.deleteOrder(id);
		return ResponseEntity.ok(Map.of("message", msg));
	}
}
