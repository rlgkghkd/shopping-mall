package com.example.shoppingmall.like.service;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.entity.Like;
import com.example.shoppingmall.like.repository.LikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
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

	private User user;

	@BeforeEach
	void setup() {
		user = new User("test", "test@mail.com", UserRole.USER, "asdasd");
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
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("DELETE");  // HTTP 메서드 설정
		request.addHeader("Authorization",
			"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QG1haWwuY29tIiwidXNlclJvbGUiOiJBRE1JTiIsImV4cCI6MTc0NzY1NDk1NiwiaWF0IjoxNzQ3NjUxMzU2fQ.eRRXY2miNpFJqe3Qaj_p9-qEpD6kzDdYNqaqb_pJOnw"); // 헤더 설정
		request.addParameter("key", "value");

		IntStream.rangeClosed(1, 100).parallel().forEach(i -> likeService.deleteLikeOnItem((long)i, request));
	}
}
