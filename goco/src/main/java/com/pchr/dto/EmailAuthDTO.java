package com.pchr.dto;

import java.time.LocalDateTime;

import com.pchr.entity.EmailAuth;
import com.pchr.entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthDTO {
	
	private String email;
	private String authenticationNumber;
	private LocalDateTime validTime;
	private int count;
	 
	public EmailAuth toEntity(EmailAuthDTO emailAuthDTO) {
					return EmailAuth.builder()
							.email(emailAuthDTO.getEmail())
							.authenticationNumber(emailAuthDTO.getAuthenticationNumber())
							.validTime(emailAuthDTO.getValidTime())
							.count(emailAuthDTO.getCount())
							.build();

	}
			
}
