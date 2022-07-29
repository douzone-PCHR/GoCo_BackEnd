package com.pchr.entity;

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
public class File {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long fileId;
	
	@Column(name = "upload_path",nullable = false)
	private String uploadPath;
	
	@Column(name = "file_name",nullable = false)
	private String fileName;
	
	@ManyToOne
	@JoinColumn(name = "business_trip_id")
	private BusinessTrip businessTrip;
	
	@ManyToOne
	@JoinColumn(name = "vacation_id")
	private Vacation vacation;
	
}
