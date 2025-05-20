package com.example.shoppingmall.item.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.item.dto.ItemRequestDto;
import com.example.shoppingmall.item.dto.ItemResponseDto;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final SearchService searchService;

	public ItemResponseDto addItem(ItemRequestDto dto) {

		Item item = Item.builder()
			.itemName(dto.getItemName())
			.content(dto.getContent())
			.price(dto.getPrice())
			.category(dto.getCategory())
			.build();

		Item saved = itemRepository.save(item);

		return ItemResponseDto.fromEntity(saved);

	}

	@Transactional(readOnly = true)
	public Page<ItemResponseDto> getAllItems(Pageable pageable) {
		Page<Item> items = itemRepository.findAll(pageable);
		return items.map(ItemResponseDto::fromEntity);
	}

	@Transactional(readOnly = true)
	public ItemResponseDto findById(long id) {
		Item item = itemRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));
		return ItemResponseDto.fromEntity(item);
	}

	@Transactional(readOnly = true)
	public Page<ItemResponseDto> search(String keyword, Pageable pageable) {
		if (keyword != null && !keyword.trim().isEmpty()) {
			searchService.saveSearchKeyword(keyword.trim());
			Page<Item> items = itemRepository.findByItemNameContaining(keyword.trim(), pageable);
			return items.map(ItemResponseDto::fromEntity);
		}

		// 키워드 없을 경우 전체 상품 조회
		Page<Item> items = itemRepository.findAll(pageable);

		return items.map(ItemResponseDto::fromEntity);
	}

	@Cacheable(
		value = "searchCache",
		key = "#keyword != null && !#keyword.trim().isEmpty() " +
			"? #keyword.trim() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize " +
			": 'all_' + #pageable.pageNumber + '_' + #pageable.pageSize"
	)
	@Transactional(readOnly = true)
	public Page<ItemResponseDto> searchV2(String keyword, Pageable pageable) {

		String trimmedKeyword = (keyword != null) ? keyword.trim() : null;
		boolean isValidKeyword = trimmedKeyword != null && !trimmedKeyword.isEmpty();

		// 인기 검색어 저장 (캐시와는 별개로 동작)
		if (isValidKeyword) {
			searchService.saveSearchKeyword(trimmedKeyword);
		}

		// 키워드 존재 시 검색, 없으면 전체 검색
		Page<Item> items = isValidKeyword
			? itemRepository.findByItemNameContaining(trimmedKeyword, pageable)
			: itemRepository.findAll(pageable);

		return items.map(ItemResponseDto::fromEntity);
	}

	@CacheEvict(value = "searchCache", allEntries = true)
	//기존 캐시 데이터 전체를 비워서 새로운 검색 결과가 DB를 기준으로 재캐싱되도록 유도
	@Transactional
	public ItemResponseDto updateItem(long id, ItemRequestDto dto) {
		Item item = itemRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

		item.update(dto.getItemName(), dto.getContent(), dto.getPrice(), dto.getCategory());

		return ItemResponseDto.fromEntity(item);
	}

	@Transactional
	public void deleteItem(long id) {
		Item item = itemRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

		itemRepository.delete(item);
	}
}
