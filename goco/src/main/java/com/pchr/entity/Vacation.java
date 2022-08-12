package com.pchr.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pchr.dto.ApproveEnum;
import com.pchr.dto.VacationDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Vacation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_id")
	private Long vacationId;

	@Column(name = "vacation_start_date")
	private Date vacationStartDate;

	@Column(name = "vacation_end_date")
	private Date vacationEndDate;

	@Column(name = "approve_yn", columnDefinition = "varchar(255) default 'APPROVE_WAITTING'")
	@Enumerated(EnumType.STRING)
	private ApproveEnum approveYn;

	@Column(name = "vacation_type")
	private String vacationType;

	@Column(name = "vacation_content")
	private String vacationContent;

	@CreatedDate
	@Column(name = "vacation_request_date", nullable = false)
	private LocalDateTime vacationRequestDate;

	@LastModifiedDate
	@Column(name = "vacation_approve_date", insertable = false)
	private LocalDateTime vacationApproveDate;

	@ManyToOne
	@JoinColumn(name = "emp_num", nullable = false)
	private Employee employee;

	@OneToOne
	@JoinColumn(name = "file_id", unique = true)
	private File file;

	// toDTO
	public VacationDTO toVacationDTO(Vacation vacationEntity) {
		VacationDTO vacationDTO = VacationDTO.builder().vacationId(vacationEntity.getVacationId())
				.vacationStartDate(vacationEntity.getVacationStartDate())
				.vacationEndDate(vacationEntity.getVacationEndDate()).approveYn(vacationEntity.getApproveYn())
				.vacationType(vacationEntity.getVacationType()).vacationContent(vacationEntity.getVacationContent())
				.vacationRequestDate(vacationEntity.getVacationRequestDate())
				.vacationApproveDate(vacationEntity.getVacationApproveDate())
				.employee(vacationEntity.getEmployee() != null
						? vacationEntity.getEmployee().toFKDTO(vacationEntity.getEmployee())
						: null)
				.file(vacationEntity.getFile() != null ? vacationEntity.getFile().toFileDTO(vacationEntity.getFile())
						: null)
				.build();
		return vacationDTO;
	}

}