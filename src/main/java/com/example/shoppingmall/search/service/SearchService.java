package com.example.shoppingmall.search.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.search.dto.PopularKeywordResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final StringRedisTemplate redisTemplate;
	private static final String KEY = "popular_keywords";

	// 검색어 저장
	public void saveSearchKeyword(String keyword) {
		if (keyword == null) return;

		keyword = keyword.trim();
		if (keyword.isEmpty()) return;
		redisTemplate.opsForZSet().incrementScore(KEY, keyword, 1);
	}

	// 인기 검색어 조회
	public List<PopularKeywordResponseDto> getPopularKeywords(int page, int size) {
		long start = (long) page * size;
		long end = start + size - 1;

		Set<ZSetOperations.TypedTuple<String>> results =
			redisTemplate.opsForZSet().reverseRangeWithScores(KEY, start, end);

		if (results == null || results.isEmpty()) return Collections.emptyList();

		return results.stream()
			.map(tuple -> new PopularKeywordResponseDto(tuple.getValue(), tuple.getScore()))
			.collect(Collectors.toList());
	}


}
