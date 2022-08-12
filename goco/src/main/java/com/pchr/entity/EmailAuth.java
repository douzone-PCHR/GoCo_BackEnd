package com.pchr.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;

import com.pchr.dto.EmailAuthDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor 
@DynamicInsert// emplyee 신규로 컬럼 생성하면서 값 넣을 때 null인건 default로 적용시키기 위한 것
public class EmailAuth {
	
	@Id 
	@Column(name = "email")
	private String email;
	
	@Column(name = "authentication_number")
	private String authenticationNumber;
	
	@Column(name = "valid_time")
	 private LocalDateTime validTime;
	
	@Column(name = "count")
	 private int count;
	
	public EmailAuthDTO toDTO(EmailAuth emailAuth) {
		return EmailAuthDTO.builder()
				.email(emailAuth.getEmail())
				.authenticationNumber(emailAuth.getAuthenticationNumber())
				.validTime(emailAuth.getValidTime())
				.count(emailAuth.getCount())
				.build();
	}
	
}
