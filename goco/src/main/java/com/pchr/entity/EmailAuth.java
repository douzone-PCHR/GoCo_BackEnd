package com.pchr.entity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
