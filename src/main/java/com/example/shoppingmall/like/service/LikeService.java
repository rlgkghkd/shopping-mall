package com.example.shoppingmall.like.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.dto.LeaveLikeResponseDto;
import com.example.shoppingmall.like.entity.Like;
import com.example.shoppingmall.like.repository.LikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final LikeRepository likeRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public LeaveLikeResponseDto leaveLikeOnItem(Long itemId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Claims claims = jwtUtil.extractClaim(token);

		Item item = itemRepository.findById(itemId).orElseThrow();
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (likeRepository.searchLikeByUserAndContent(user.getId(), null, item)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Like like = new Like(null, item, user);
		Like saved = likeRepository.save(like);
		item.increaseLikeCount(1L);

		LeaveLikeResponseDto responseDto = new LeaveLikeResponseDto(saved.getItem().getClass().toString(),
			saved.getItem().getId(), saved.getId(), saved.getCreatedAt());
		return responseDto;
	}

	@Transactional
	public LeaveLikeResponseDto leaveLikeOnComment(Long commentId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Claims claims = jwtUtil.extractClaim(token);

		Comment comment = commentRepository.findById(commentId).orElseThrow();
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (likeRepository.searchLikeByUserAndContent(user.getId(), comment, null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Like like = new Like(comment, null, user);
		Like saved = likeRepository.save(like);
		comment.increaseLikeCount(1L);

		LeaveLikeResponseDto responseDto = new LeaveLikeResponseDto(saved.getComment().getClass().toString(),
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

		Like like = likeRepository.findById(likeId).orElseThrow();
		Item item = like.getItem();

		if (!like.getUser().getId().equals(user.getId())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		likeRepository.delete(like);
		item.decreaseLikeCount(1L);
		itemRepository.save(item);
	}

	public void deleteLikeOnComment(Long likeId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		Claims claims = jwtUtil.extractClaim(token);
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		Like like = likeRepository.findById(likeId).orElseThrow();
		Comment comment = like.getComment();

		if (!like.getUser().getId().equals(user.getId())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		likeRepository.delete(like);
		comment.decreaseLikeCount(1L);
		commentRepository.save(comment);
	}
}
