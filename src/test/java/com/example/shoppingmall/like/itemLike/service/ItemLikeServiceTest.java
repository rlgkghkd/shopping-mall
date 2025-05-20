package com.example.shoppingmall.like.itemLike.service;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.like.commentLike.controller.CommentLikeController;
import com.example.shoppingmall.like.commentLike.service.CommentLikeService;

@WebMvcTest(CommentLikeController.class)
public class ItemLikeServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private RedisTemplate redisTemplate;
	@MockitoBean
	private CommentLikeService commentLikeService;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	void givenNoToken_whenDeleteLikeOnItem_thenReturns404NotFound() throws Exception {
		Long likeId = 1L;

		// Given: jwtUtil이 null 값을 반환하도록 설정
		when(jwtUtil.subStringToken(any())).thenReturn(null);

		// When: 좋아요 삭제 요청을 보냈을 때
		// Then: HTTP 404 응답을 반환해야 함
		mockMvc.perform(delete("/comment/" + likeId + "/likes")
				.with(csrf())) // CSRF 토큰 추가
			.andExpect(status().isNotFound()) // 404 상태 코드 검증
			.andExpect(jsonPath("$.message").value("토큰을 찾을 수 없습니다.")); // 메시지 검증
	}
}
