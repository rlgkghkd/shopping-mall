package com.example.shoppingmall.search.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.search.dto.PopularKeywordResponseDto;
import com.example.shoppingmall.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
	private final SearchService searchService;

	// 인기 검색어 조회
	@GetMapping("/popular")
	public ResponseEntity<List<PopularKeywordResponseDto>> getPopularKeywords(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		List<PopularKeywordResponseDto> keywords = searchService.getPopularKeywords(page, size);
		return ResponseEntity.ok(keywords);
	}
}
