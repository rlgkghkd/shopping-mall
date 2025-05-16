package com.example.shoppingmall.like.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Comment comment;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@NotNull
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
