package com.example.shoppingmall.like.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.dto.LeaveLikeResponseDto;
import com.example.shoppingmall.like.entity.Like;
import com.example.shoppingmall.like.repository.LikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final LikeRepository likeRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	public LeaveLikeResponseDto leaveLikeOnItem(Long itemId, Long userId) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		User user = userRepository.findById(userId).orElseThrow();
		if (likeRepository.searchLikeByUserAndContent(userId, null, itemId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		Like like = new Like(null, item, user);
		Like saved = likeRepository.save(like);
		LeaveLikeResponseDto responseDto = new LeaveLikeResponseDto(saved.getItem().getClass().toString(),
			saved.getItem().getId(), saved.getId(), saved.getCreatedAt());
		return responseDto;
	}

	public LeaveLikeResponseDto leaveLikeOnComment(Long commentId, Long userId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow();
		User user = userRepository.findById(userId).orElseThrow();
		if (likeRepository.searchLikeByUserAndContent(userId, commentId, null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		Like like = new Like(comment, null, user);
		Like saved = likeRepository.save(like);
		LeaveLikeResponseDto responseDto = new LeaveLikeResponseDto(saved.getComment().getClass().toString(),
			saved.getComment().getId(), saved.getId(), saved.getCreatedAt());
		return responseDto;
	}

	public void deleteLike(Long likeId, long userId) {
		Like like = likeRepository.findById(likeId).orElseThrow();
		User user = userRepository.findById(userId).orElseThrow();
		if (!like.getUser().equals(user)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		likeRepository.delete(like);
	}
}
