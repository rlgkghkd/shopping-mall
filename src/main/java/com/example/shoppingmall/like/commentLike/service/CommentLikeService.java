package com.example.shoppingmall.like.commentLike.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.exception.CommentErrorCode;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.like.commentLike.dto.LeaveCommentLikeResponseDto;
import com.example.shoppingmall.like.commentLike.entity.CommentLike;
import com.example.shoppingmall.like.commentLike.repository.CommentLikeRepository;
import com.example.shoppingmall.like.exception.LikesErrors;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.exception.UserErrorCode;
import com.example.shoppingmall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
	private final CommentLikeRepository commentLikeRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	@Transactional
	public LeaveCommentLikeResponseDto leaveLikeOnComment(Long commentId, CustomUserDetails userDetails) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

		User user = userRepository.findById(userDetails.getUserId())
			.orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));

		if (commentLikeRepository.searchLikeByUserAndComment(user.getId(), comment)) {
			throw new CustomException(LikesErrors.ALREADY_LIKED);
		}

		CommentLike commentLike = new CommentLike(comment, user);
		CommentLike saved = commentLikeRepository.save(commentLike);
		comment.increaseLikeCount(1L);

		LeaveCommentLikeResponseDto responseDto = new LeaveCommentLikeResponseDto(
			saved.getComment().getId(),
			saved.getId(),
			saved.getCreatedAt());
		return responseDto;
	}

	@DistributedLock(key = "delete Like")
	public void deleteLikeOnComment(Long likeId, CustomUserDetails userDetails) {

		User user = userRepository.findById(userDetails.getUserId())
			.orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));

		CommentLike commentLike = commentLikeRepository.findById(likeId)
			.orElseThrow(() -> new CustomException(LikesErrors.NOT_FOUND_LIKE));
		if (!commentLike.getUser().getId().equals(user.getId())) {
			throw new CustomException(LikesErrors.OTHER_USERS_LIKE);
		}

		Comment comment = commentLike.getComment();

		commentLikeRepository.delete(commentLike);
		comment.decreaseLikeCount(1L);
		commentRepository.save(comment);
	}
}
