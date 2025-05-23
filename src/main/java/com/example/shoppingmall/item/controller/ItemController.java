package com.example.shoppingmall.item.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.item.dto.ItemResponseDto;
import com.example.shoppingmall.item.dto.ItemRequestDto;
import com.example.shoppingmall.item.service.ItemService;
import com.example.shoppingmall.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	private final SearchService searchService;

	//상품 등록
	@PostMapping
	public ResponseEntity<ItemResponseDto> createItem(@RequestBody ItemRequestDto dto) {
		ItemResponseDto response = itemService.addItem(dto);
		return ResponseEntity.ok(response);
	}

	//모든 상품 조회
	@GetMapping
	public ResponseEntity<Page<ItemResponseDto>> getAllItem(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ItemResponseDto> response = itemService.getAllItems(pageable);
		return ResponseEntity.ok(response);
	}

	//상품 상세 조회
	@GetMapping("/{id}")
	public ResponseEntity<ItemResponseDto> getIdByItem(
		@PathVariable("id") long id) {
		ItemResponseDto responseDto = itemService.findById(id);
		return ResponseEntity.ok(responseDto);
	}

	//검색 기능
	@GetMapping("/search")
	public ResponseEntity<Page<ItemResponseDto>> searchItems(
		@RequestParam(required = false) String keyword,
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ItemResponseDto> result = itemService.search(keyword, pageable);
		return ResponseEntity.ok(result);
	}

	//검색 기능 In-memory Cache 적용 + redis
	@GetMapping("/v2/search")
	public ResponseEntity<Page<ItemResponseDto>> searchItemsV2(
		@RequestParam(required = false) String keyword,
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ItemResponseDto> result = itemService.searchV2(keyword, pageable);
		return ResponseEntity.ok(result);
	}

	//상품 정보 수정
	@PatchMapping("/{id}")
	public ResponseEntity<ItemResponseDto> updateItem(
		@PathVariable("id") Long id,
		@RequestBody ItemRequestDto dto) {
		ItemResponseDto response = itemService.updateItem(id, dto);
		return ResponseEntity.ok(response);
	}

	//상품 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
		itemService.deleteItem(id);
		return ResponseEntity.ok().build();
	}

}
