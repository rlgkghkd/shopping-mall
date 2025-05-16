package com.example.shoppingmall.comment.entity;

import com.example.shoppingmall.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
	@Id
	private Long Id;
}
