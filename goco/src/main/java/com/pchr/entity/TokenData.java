package com.pchr.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.pchr.dto.TokenDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert // emplyee 신규로 컬럼 생성하면서 값 넣을 때 null인건 default로 적용시키기 위한 것
@EntityListeners(AuditingEntityListener.class)
public class TokenData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_data_num")
	private Long tokenDataNum;
	
	@Column(name = "emp_id")
	private String empId;
	
	@Column(name = "refresh_token")
	private String refreshToken;	
	
	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "expire_time")
	private Date expireTime;	
	
	public  TokenDataDTO toDTO(TokenData tokenData) {
		return TokenDataDTO.builder()
				.tokenDataNum(tokenData.tokenDataNum)
				.empId(tokenData.empId)
				.refreshToken(tokenData.refreshToken)
				.accessToken(tokenData.accessToken)
				.expireTime(tokenData.expireTime)
				.build();
	}
}
