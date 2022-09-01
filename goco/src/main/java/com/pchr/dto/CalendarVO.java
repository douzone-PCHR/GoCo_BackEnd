package com.pchr.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalendarVO {
	
	private BigInteger id;
	private String title;
	private LocalDateTime start;
	private LocalDateTime end;
	private BigDecimal workType;
	
	
	public CalendarVO entitytoVO(Map<String, Object> map) {
		Map<String, Object> hashmap = new HashMap<String, Object>();
			CalendarVO calenaderVO = CalendarVO.builder()
					.id((BigInteger) hashmap.get("id"))
					.title((String) hashmap.get("title"))
					.start((LocalDateTime) hashmap.get("start"))
					.end((LocalDateTime) hashmap.get("end"))
					.build();
			
		return calenaderVO;
	}
	
	
	
}
