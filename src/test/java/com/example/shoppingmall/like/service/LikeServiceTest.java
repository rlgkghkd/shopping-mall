package com.example.shoppingmall.like.service;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.entity.Like;
import com.example.shoppingmall.like.repository.LikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

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

	@BeforeEach
	void setup() {
		User user = new User();
		userRepository.save(user);
		Item item = new Item(null, "testName", "testContent", 12345, Category.Beauty, 0L);
		itemRepository.save(item);
		for (int i = 0; i < 100; i++) {
			likeRepository.save(new Like(null, item, user));
		}
		item.increaseLikeCount(100L);
		itemRepository.save(item);
	}

	@Test
	void conTest() {
		IntStream.rangeClosed(1, 100).parallel().forEach(i -> likeService.deleteLikeOnItem((long)i, 1L));
	}
}
