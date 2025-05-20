package com.example.shoppingmall.comment.exception;

import com.example.shoppingmall.common.exception.Errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements Errors {

	NAUTHORIZED_COMMENT_CREATION(403, "CM01", "댓글을 작성할 권한이 없습니다. 주문자만 댓글을 작성할 수 있습니다."),
	UNAUTHORIZED_REPLY_CREATION(403, "CM02", "답글은 관리자만 달 수 있습니다."),
	REPLY_ALREADY_EXISTS(400, "CM03", "이미 답글이 존재합니다."),
	COMMENT_NOT_FOUND(404, "CM04", "댓글을 찾을 수 없습니다."),
	ITEM_NOT_FOUND(404, "CM05", "상품을 찾을 수 없습니다."),
	ORDER_NOT_FOUND(404, "CM06", "주문 내역을 찾을 수 없습니다."),
	USER_NOT_FOUND(404, "CM07", "유저를 찾을 수 없습니다."),
	UNAUTHORIZED_COMMENT_VIEW(403, "CM08", "댓글을 조회할 권한이 없습니다. 작성자 또는 관리자만 조회할 수 있습니다."),
	UNAUTHORIZED_COMMENT_UPDATE(403, "CM09", "댓글을 수정할 권한이 없습니다. 작성자만 댓글을 수정할 수 있습니다."),
	UNAUTHORIZED_COMMENT_DELETE(403, "CM10", "댓글을 삭제할 권한이 없습니다. 작성자 또는 관리자만 댓글을 삭제할 수 있습니다.");

	private final int Status;
	private final String code;
	private final String message;
}
