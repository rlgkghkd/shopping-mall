package com.example.shoppingmall.auth.dto.request;

import com.example.shoppingmall.user.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
	@NotBlank(message = "이름을 입력해주세요")
	private String userName;
	@NotBlank(message = "이메일을 입력해주세요")
	private String email;
	@NotBlank(message = "비밀번호를 입력해주세요")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문자, 숫자, 특수문자를 포함해 8~20자여야 합니다."
	)
	private String password;

	@NotBlank
	private UserRole userRole;
}
