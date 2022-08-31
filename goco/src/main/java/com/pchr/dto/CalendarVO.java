package com.pchr.dto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
	
	
//	public List<CalendarVO> entitytoVO(List<Map<String, Object>> list) {
//		List<CalendarVO> result = new ArrayList<CalendarVO>();
//		for (int i = 0; i < list.size(); i++) {
//			CalendarVO calenaderVO = CalendarVO.builder()
//					.id((BigInteger) list.get(i).get("id"))
//					.title((String) list.get(i).get("title"))
//					.start((LocalDateTime) list.get(i).get("start"))
//					.end((LocalDateTime) list.get(i).get("end"))
//					.build();
//			
//			result.add(calenaderVO);
//		}
//		
//		return result;
//	}
	
	
	
}
