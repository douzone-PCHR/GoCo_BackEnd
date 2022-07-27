package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_num")
	private Long empNum;
	
	@Column(name = "emp_id")
	private String empId;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "delete_yn")
	private boolean deleteYN;
	
	@Column(name = "update_datetime")
	private Date updateDatetime;
	
	@Column(name = "hiredate")
	private Date hiredate;
	
	@Column(name = "authority")
	private int authority;
	
	@Column(name = "vacation_count")
	private int vacationCount;

	
	
}