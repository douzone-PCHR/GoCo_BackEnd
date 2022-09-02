package com.pchr.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
	private String backgroundColor;
	private String textColor;
	
	

	public List<CalendarVO> entitytoVO(List<Map<String, Object>> list) {
		List<CalendarVO> result = new ArrayList<CalendarVO>();
		for (int i = 0; i < list.size(); i++) {
			CalendarVO calenaderVO = CalendarVO.builder().id((BigInteger) list.get(i).get("id"))
					.title((String) list.get(i).get("title"))
					.start(((Timestamp) list.get(i).get("start")).toLocalDateTime())
					.end(((Timestamp) list.get(i).get("end")).toLocalDateTime())
					.workType((BigDecimal) list.get(i).get("work_type")).build();

			result.add(calenaderVO);
		}

		return result;
	}
	
	public List<CalendarVO> entityCalendartoVO(List<Map<String, Object>> list) {
		List<CalendarVO> result = new ArrayList<CalendarVO>();
		for (int i = 0; i < list.size(); i++) {
			CalendarVO calenaderVO = CalendarVO.builder().id((BigInteger) list.get(i).get("id"))
					.title((String) list.get(i).get("title"))
					.start(((Timestamp) list.get(i).get("start")).toLocalDateTime())
					.end(((Timestamp) list.get(i).get("end")).toLocalDateTime())
					.workType((BigDecimal) list.get(i).get("work_type"))
					.backgroundColor((String) list.get(i).get("backgroundColor"))
					.textColor((String) list.get(i).get("textColor"))
					.build();
				
			result.add(calenaderVO);
		}

		return result;
	}

}
