package com.example.shoppingmall.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FOOD("음식"),
    Clothing("의류"),
    Beauty("뷰티"),
    FURNITURE("가구"),
    ELECTRONICS("전자제품"),
    Hobby("취미");

    private final String displayName;
}
