package com.example.shoppingmall.like.commentLike.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.like.commentLike.dto.LeaveCommentLikeResponseDto;
import com.example.shoppingmall.like.commentLike.entity.CommentLike;
import com.example.shoppingmall.like.commentLike.repository.CommentLikeRepository;
import com.example.shoppingmall.like.exception.LikesErrors;
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
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다.");
		}
		Claims claims = jwtUtil.extractClaim(token);

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment 없음"));
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (commentLikeRepository.searchLikeByUserAndComment(user.getId(), comment)) {
			throw new CustomException(LikesErrors.AlreadyLiked);
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
		if (token == null) {
			throw new NoTokenException("토큰을 찾을 수 없습니다.");
		}

		Claims claims = jwtUtil.extractClaim(token);
		String userMail = claims.get("email", String.class);
		if (userMail == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token에 email 없음");
		}
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user 없음"));

		CommentLike commentLike = commentLikeRepository.findById(likeId)
			.orElseThrow(() -> new NoLikeFoundException("좋아요를 찾을 수 없습니다."));
		if (!commentLike.getUser().getId().equals(user.getId())) {
			throw new WrongUsersLikeException("본인이 남긴 좋아요가 아닙니다.");
		}

		Comment comment = commentLike.getComment();

		commentLikeRepository.delete(commentLike);
		comment.decreaseLikeCount(1L);
		commentRepository.save(comment);
	}
}
