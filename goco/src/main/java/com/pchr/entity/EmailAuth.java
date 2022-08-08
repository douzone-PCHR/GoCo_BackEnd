package com.pchr.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor 
public class EmailAuth {
	
	@Id 
	@Column(name = "email")
	private String email;
	
	@Column(name = "authentication_number")
	private String authenticationNumber;
	
	@Column(name = "valid_time")
	 private LocalDateTime validTime;
	
}
