package com.example.shoppingmall.like.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Comment comment;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Item item;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@NotEmpty
	private User user;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	public Like(Comment comment, Item item, User user) {
		this.comment = comment;
		this.item = item;
		this.user = user;
	}
}
