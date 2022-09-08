package com.pchr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//토큰의 값을 헤더에서 뽑거나 헤더에서 삽입할때 쓰는 dto다.
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long tokenExpiresIn;
}