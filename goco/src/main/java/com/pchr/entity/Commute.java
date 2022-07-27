package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Commute {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "commute_id")
	private Long commuteId;
	
	@Column(name = "clock_in")
	private LocalDateTime clock_in;
	
	@Column(name = "clock_out")
	private LocalDateTime clock_out;
	
	@Column(name = "commute_status")
	private String commuteStatus;
	
	
	
}
