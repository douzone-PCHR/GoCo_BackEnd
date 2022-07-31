package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTrip {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "business_trip_id")
	private Long businessTripId;
	
	@Column(name = "business_trip_start_date",nullable = false)
	private Date businessTripStartDate; 
	
	@Column(name = "business_trip_end_date",nullable = false)
	private Date businessTripEndDate; 
	
	@Column(name = "approve_yn")
	private boolean approveYn;
	
	@Column(name = "business_trip_content")
	private String businessTripContent;
	
	@Column(name = "business_trip_request_date",nullable = false)
	private LocalDateTime businessTripRequestDate;
	
	@Column(name = "business_trip_approve_date")
	private LocalDateTime businessTripApproveDate;
	
	@ManyToOne
	@JoinColumn(name = "emp_num",nullable = false)
	private Employee emp;
}
