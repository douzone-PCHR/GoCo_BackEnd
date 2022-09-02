package com.pchr.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
public class VacationAndBusinessVO {

	private BigInteger empNum;

	private String name;

	private String vacation_approve;

	private String business_approve;

	private LocalDateTime clock_in;

	private LocalDateTime clock_out;

	private Date startDate;

	private Date endDate;

	private Timestamp vacation_start_date;

	private Timestamp vacation_end_date;

	private Timestamp business_trip_start_date;

	private Timestamp business_trip_end_date;

	private BigDecimal commute_work_time;

	private Float vacation_count;

	private Float vacation_count2;

	private String approveEnum;

	private String vacationType;

	public List<VacationAndBusinessVO> entityVo(List<Map<String, Object>> list) {
		List<VacationAndBusinessVO> result = new ArrayList<VacationAndBusinessVO>();
		for (int i = 0; i < list.size(); i++) {

			VacationAndBusinessVO vo = VacationAndBusinessVO.builder().empNum((BigInteger) list.get(i).get("emp_num"))
					.name((String) list.get(i).get("name"))
					.vacation_approve((String) list.get(i).get("vacation_approve"))
					.business_approve((String) list.get(i).get("business_approve"))
					.clock_in(((Timestamp) list.get(i).get("clock_in")).toLocalDateTime())
					.clock_out(((Timestamp) list.get(i).get("clock_out")).toLocalDateTime())
					.vacation_start_date((Timestamp) list.get(i).get("vacation_start_date"))
					.vacation_end_date((Timestamp) list.get(i).get("vacation_end_date"))
					.business_trip_start_date((Timestamp) list.get(i).get("business_trip_start_date"))
					.business_trip_end_date((Timestamp) list.get(i).get("business_trip_end_date"))
					.commute_work_time((BigDecimal) list.get(i).get("commute_work_time"))
					.vacation_count((Float) list.get(i).get("vacation_count")).build();
			result.add(vo);
		}
		return result;

	}
}
