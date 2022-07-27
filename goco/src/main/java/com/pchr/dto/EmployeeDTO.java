package com.pchr.dto;


import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
	
	private Long empNum;
	
	private String empId;
	
	private String password;
	
	private String email;
	
	private String name;
	
	private String phoneNumber;
	
	private boolean delete_yn;
	
	private Date update_datetime;
	
	private Date hiredate;
	
	private int authority;
	
	private int vacation_count;
	
}


