package com.pchr.entity;

import java.time.LocalDateTime;

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
public class Board {
	
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long boardId;
	
	@Column(name = "board_title",nullable = false)
	private String boardTitle;
	
	@Column(name = "board_content")
	private String boardContent;
	
	@Column(name = "registered_date")
	private LocalDateTime registeredDate;
	
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;
	
	@Column(name = "count")
	private int count;
	
	@ManyToOne
	@JoinColumn(name = "emp_num" ,nullable = false)
	private Employee emp;
	
}
