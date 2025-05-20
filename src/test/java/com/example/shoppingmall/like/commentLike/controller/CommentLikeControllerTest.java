package com.example.shoppingmall.like.commentLike.controller;

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
import com.example.shoppingmall.like.commentLike.dto.LeaveCommentLikeResponseDto;
import com.example.shoppingmall.like.commentLike.service.CommentLikeService;

@WebMvcTest(CommentLikeController.class)
class CommentLikeControllerTest {

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
	void leaveLikeOnComment() throws Exception {
		Long commentId = 1L;
		when(commentLikeService.leaveLikeOnItem(anyLong(), any())).thenReturn(new LeaveCommentLikeResponseDto());

		mockMvc.perform(post("/comment/" + commentId + "/likes")
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	void deleteLikeOnComment() {
	}
}
