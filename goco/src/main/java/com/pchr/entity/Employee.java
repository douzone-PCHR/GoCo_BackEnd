package com.pchr.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_num",nullable = false)
	private Long empNum;
	
	@Column(name = "emp_id",nullable = false)
	private String empId;
	
	@Column(name = "password",nullable = false)
	private String password;
	
	@Column(name = "email",nullable = false)
	private String email;
	
	@Column(name = "name",nullable = false)
	private String name;
	
	@Column(name = "phone_number",nullable = false)
	private String phoneNumber;
	
	@Column(name = "delete_yn",nullable = false)
	private boolean deleteYn;
	
	@Column(name = "update_datetime",nullable = false)
	private Date updateDatetime;
	
	@CreatedDate
	@Column(name = "hiredate",nullable = false)
	private Date hiredate;
	
	@Column(name = "authority",nullable = false)
	private int authority;
	
	@Column(name = "vacation_count")
	private int vacationCount;
// --------------------------------------
	@ManyToOne(fetch = FetchType.LAZY ,cascade = {CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name ="mgr_id",referencedColumnName = "emp_num")
	@JsonIgnore
	private Employee mgrId;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "mgrId")
	private List<Employee> child;
	
	@ManyToOne
	@JoinColumn(name = "job_title_id",nullable = false)
	private JobTitle jobTitle;
	
	@ManyToOne
	@JoinColumn(name = "team_position_id",nullable = false)
	private TeamPosition teamPosition;
	
	@ManyToOne
	@JoinColumn(name = "unit_id",nullable = false)
	private Unit unit; 
	
}