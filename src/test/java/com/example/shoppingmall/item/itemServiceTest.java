package com.example.shoppingmall.item;

import static com.example.shoppingmall.item.Category.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.shoppingmall.item.dto.ItemRequestDto;
import com.example.shoppingmall.item.dto.ItemResponseDto;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.item.service.ItemService;
import com.example.shoppingmall.search.service.SearchService;

public class itemServiceTest {

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private SearchService searchService;

	@InjectMocks
	private ItemService itemService;

	@BeforeEach
	void setUp() {
		itemRepository = mock(ItemRepository.class);
		searchService = mock(SearchService.class);
		itemService = new ItemService(itemRepository, searchService);
	}

	@Test
	@DisplayName("상품 추가 - 성공")
	void addItem_success() {
		// given
		ItemRequestDto request = new ItemRequestDto("아이템", "내용", 10000, FOOD);

		given(itemRepository.existsByItemName("아이템")).willReturn(false);
		given(itemRepository.save(any(Item.class))).willAnswer(invocation -> {
			Item item = invocation.getArgument(0);
			item.setId(1L); return item;
		});

		// when
		ItemResponseDto response = itemService.addItem(request);

		// then
		assertThat(response.getItemName()).isEqualTo("아이템");
		assertThat(response.getPrice()).isEqualTo(10000);
	}

	@Test
	@DisplayName("상품 전체 조회")
	void getAllItems() {
		// given
		PageRequest pageable = PageRequest.of(0, 10);

		Item item = Item.builder()
			.id(1L)
			.itemName("테스트")
			.content("내용")
			.price(5000)
			.category(Category.FOOD)
			.likeCount(0L)
			.build();

		Page<Item> itemPage = new PageImpl<>(Collections.singletonList(item));

		given(itemRepository.findAll(pageable)).willReturn(itemPage);

		// when
		Page<ItemResponseDto> result = itemService.getAllItems(pageable);

		// then
		assertThat(result).hasSize(1);
		assertThat(result.getContent().get(0).getItemName()).isEqualTo("테스트");
	}

	@Test
	@DisplayName("상품 검색 - 키워드 포함")
	void search_withKeyword() {
		// given
		String keyword = "치킨";
		// given
		PageRequest pageable = PageRequest.of(0, 10);

		Item item = Item.builder()
			.id(1L)
			.itemName("치킨")
			.content("내용")
			.price(5000)
			.category(Category.FOOD)
			.likeCount(0L)
			.build();

		Page<Item> itemPage = new PageImpl<>(Collections.singletonList(item));

		given(itemRepository.findByItemNameContaining(keyword, pageable)).willReturn(itemPage);

		// when
		Page<ItemResponseDto> result = itemService.search(keyword, pageable);

		// then
		verify(searchService, times(1)).saveSearchKeyword(keyword);
		assertThat(result.getContent().get(0).getItemName()).contains("치킨");
	}

	@Test
	@DisplayName("상품 검색 - 키워드 없음")
	void search_withoutKeyword() {
		// given
		PageRequest pageable = PageRequest.of(0, 10);
		Page<Item> itemPage = new PageImpl<>(Collections.emptyList());
		given(itemRepository.findAll(pageable)).willReturn(itemPage);

		// when
		Page<ItemResponseDto> result = itemService.search(null, pageable);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("상품 수정 - 성공")
	void updateItem_success() {
		// given
		long id = 1L;
		ItemRequestDto dto = new ItemRequestDto("수정된 이름", "새 내용", 20000, Clothing);
		Item item = Item.builder().id(id).itemName("기존이름").price(10000).build();

		given(itemRepository.findById(id)).willReturn(Optional.of(item));

		// when
		ItemResponseDto updated = itemService.updateItem(id, dto);

		// then
		assertThat(updated.getItemName()).isEqualTo("수정된 이름");
		assertThat(updated.getPrice()).isEqualTo(20000);
	}

	@Test
	@DisplayName("상품 삭제 - 성공")
	void deleteItem_success() {
		// given
		long id = 1L;
		Item item = Item.builder().id(id).itemName("삭제할 아이템").build();

		given(itemRepository.findById(id)).willReturn(Optional.of(item));

		// when
		itemService.deleteItem(id);

		// then
		verify(itemRepository, times(1)).delete(item);
	}

}
