package com.example.academylms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLogin {
	
	@NotBlank(message = " 아이디는 필수입니다. ")
	private String id;
	
	@NotBlank(message = "비밀번호는 필수입니다. ")
	private String password;
	
	private int userId;
}
