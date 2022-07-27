package com.pchr.entity;

import java.time.LocalDateTime;

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
public class Comment {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long comment_id;
	
	@Column(name = "comment_content")
	private String comment_content;
	
	@Column(name = "registered_date")
	private LocalDateTime registeredDate;
	
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;
	

	
	
	
}
