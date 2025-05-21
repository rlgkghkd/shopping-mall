package com.example.shoppingmall.comment.entity;

import com.example.shoppingmall.comment.dto.request.UpdateCommentRequestDto;
import com.example.shoppingmall.common.BaseEntity;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@OneToOne(mappedBy = "parentComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Comment reply;

	@OneToOne
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment;

	@Column(columnDefinition = "bigint Default 0")
	private Long likeCount = 0L;

	public void increaseLikeCount(Long amount) {
		this.likeCount += amount;
	}

	public void decreaseLikeCount(Long amount) {
		this.likeCount -= amount;
	}

	public Comment(String content, User user, Item item, Order order) {
		this.content = content;
		this.user = user;
		this.item = item;
		this.order = order;
	}

	public void UpdateComment(UpdateCommentRequestDto updateCommentRequestDto) {
		this.content = updateCommentRequestDto.getContent();
	}

	public void setReply(Comment reply) {
		this.reply = reply;
	}

	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}

}
