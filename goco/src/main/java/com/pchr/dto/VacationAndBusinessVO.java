package com.pchr.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
	
	private int vacation_count;
	
	private String approveEnum;

	private String vacationType;


	
	
}
