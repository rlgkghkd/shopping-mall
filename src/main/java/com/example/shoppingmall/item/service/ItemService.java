package com.example.shoppingmall.item.service;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemResponseDto addItem(ItemRequestDto dto) {

		Item item = Item.builder()
			.itemName(dto.getItemName())
			.content(dto.getContent())
			.price(dto.getPrice())
			.category(dto.getCategory())
			.likeCount(0L)
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

	@Transactional
	public ItemResponseDto updateItem(long id, ItemResponseDto dto) {
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
