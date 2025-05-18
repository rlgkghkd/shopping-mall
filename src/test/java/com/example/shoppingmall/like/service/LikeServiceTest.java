package com.example.shoppingmall.like.service;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.entity.Like;
import com.example.shoppingmall.like.repository.LikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest()
class LikeServiceTest {

	@Autowired
	private LikeService likeService;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setup() {
		User user = new User();
		userRepository.save(user);
		Item item = new Item();
		item.setLikeCount(0L);
		itemRepository.save(item);
		for (int i = 0; i < 100; i++) {
			likeRepository.save(new Like(null, item, user));
			item.setLikeCount(item.getLikeCount() + 1);
			itemRepository.save(item);
		}
	}

	@Test
	void conTest() {
		IntStream.rangeClosed(1, 100).parallel().forEach(i -> likeService.deleteLike((long)i, 1L));
	}
}
