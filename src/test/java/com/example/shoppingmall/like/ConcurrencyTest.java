package com.example.shoppingmall.like;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.itemLike.entity.ItemLike;
import com.example.shoppingmall.like.itemLike.repository.ItemLikeRepository;
import com.example.shoppingmall.like.itemLike.service.ItemLikeService;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.repository.UserRepository;

@SpringBootTest()
class ConcurrencyTest {

	@Autowired
	private ItemLikeService itemLikeService;
	@Autowired
	private ItemLikeRepository itemLikeRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		user = new User("test", "test@mail.com", UserRole.USER, "asdasd");
		userRepository.save(user);
		Item item = new Item(null, "testName", "testContent", 12345, Category.Beauty, 0L);
		itemRepository.save(item);
		for (int i = 0; i < 100; i++) {
			itemLikeRepository.save(new ItemLike(item, user));
		}
		item.increaseLikeCount(100L);
		itemRepository.save(item);
	}

	@Test
	void conTest() {
		CustomUserDetails userDetails = new CustomUserDetails(1L, "ADMIN", "test@mail.com");
		IntStream.rangeClosed(1, 100).parallel().forEach(i -> itemLikeService.deleteLikeOnItem((long)i, userDetails));
	}
}
