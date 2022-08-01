package com.pchr.dto;



import com.pchr.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Response를 보낼때 쓰이는 dto다.
public class MemberResponseDTO {
    private String email;
    private String nickname;

    public static MemberResponseDTO of(Member member) {
        return MemberResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}