package com.example.academylms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindUserPassword {
	
	@NotBlank(message = "아이디는 필수입니다.")
	private String id;
	
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = " 올바른 이메일 형식을 입력해주세요")
	private String email;
}
