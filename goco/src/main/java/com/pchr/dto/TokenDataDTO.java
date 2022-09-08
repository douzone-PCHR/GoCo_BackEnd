package com.pchr.dto;

import java.util.Date;

import com.pchr.entity.Board;
import com.pchr.entity.TokenData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDataDTO {
	
	private Long tokenDataNum;
	private String empId;
	private String refreshToken;	
	private String accessToken;
	private Date expireTime;	
	
	public  TokenData toEntity(TokenDataDTO tokenDataDTO) {
		return TokenData.builder()
				.tokenDataNum(tokenDataDTO.tokenDataNum)
				.empId(tokenDataDTO.empId)
				.refreshToken(tokenDataDTO.refreshToken)
				.accessToken(tokenDataDTO.accessToken)
				.expireTime(tokenDataDTO.expireTime)
				.build();
	}
}
