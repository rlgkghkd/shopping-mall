package com.example.shoppingmall.like.commentLike.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.like.commentLike.dto.LeaveCommentLikeResponseDto;
import com.example.shoppingmall.like.commentLike.entity.CommentLike;
import com.example.shoppingmall.like.commentLike.repository.CommentLikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
	private final CommentLikeRepository commentLikeRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public LeaveCommentLikeResponseDto leaveLikeOnItem(Long commentId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Claims claims = jwtUtil.extractClaim(token);

		Comment comment = commentRepository.findById(commentId).orElseThrow();
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (commentLikeRepository.searchLikeByUserAndComment(user.getId(), comment)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		CommentLike commentLike = new CommentLike(comment, user);
		CommentLike saved = commentLikeRepository.save(commentLike);
		comment.increaseLikeCount(1L);

		LeaveCommentLikeResponseDto responseDto = new LeaveCommentLikeResponseDto(
			saved.getComment().getClass().toString(),
			saved.getComment().getId(), saved.getId(), saved.getCreatedAt());
		return responseDto;
	}

	@DistributedLock(key = "delete Like")
	public void deleteLikeOnItem(Long likeId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Claims claims = jwtUtil.extractClaim(token);
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		CommentLike commentLike = commentLikeRepository.findById(likeId).orElseThrow();
		Comment comment = commentLike.getComment();

		if (!commentLike.getUser().getId().equals(user.getId())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		commentLikeRepository.delete(commentLike);
		comment.decreaseLikeCount(1L);
		commentRepository.save(comment);
	}
}
