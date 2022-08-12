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
import com.pchr.dto.BusinessTripDTO;

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
public class BusinessTrip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "business_trip_id")
	private Long businessTripId;

	@Column(name = "business_trip_start_date", nullable = false)
	private Date businessTripStartDate;

	@Column(name = "business_trip_end_date", nullable = false)
	private Date businessTripEndDate;

	@Column(name = "approve_yn", columnDefinition = "varchar(255) default 'APPROVE_WAITTING'")
	@Enumerated(EnumType.STRING)
	private ApproveEnum approveYn;

	@Column(name = "business_trip_content")
	private String businessTripContent;

	@CreatedDate
	@Column(name = "business_trip_request_date", nullable = false)
	private LocalDateTime businessTripRequestDate;

	@LastModifiedDate
	@Column(name = "business_trip_approve_date", insertable = false)
	private LocalDateTime businessTripApproveDate;

	@ManyToOne
	@JoinColumn(name = "emp_num", nullable = false, updatable = false)
	private Employee employee;

	@OneToOne
	@JoinColumn(name = "file_id", unique = true)
	private File file;

	// toDTO
	public BusinessTripDTO toBusinessTripDTO(BusinessTrip businessTripEntity) {
		BusinessTripDTO businessTripDTO = BusinessTripDTO.builder()
				.businessTripId(businessTripEntity.getBusinessTripId())
				.businessTripStartDate(businessTripEntity.getBusinessTripStartDate())
				.businessTripEndDate(businessTripEntity.getBusinessTripEndDate())
				.approveYn(businessTripEntity.getApproveYn())
				.businessTripContent(businessTripEntity.getBusinessTripContent())
				.businessTripRequestDate(businessTripEntity.getBusinessTripRequestDate())
				.businessTripApproveDate(businessTripEntity.getBusinessTripApproveDate())
				.employee(businessTripEntity.getEmployee() != null
						? businessTripEntity.getEmployee().toFKDTO(businessTripEntity.getEmployee())
						: null)
				.file(businessTripEntity.getFile() != null
						? businessTripEntity.getFile().toFileDTO(businessTripEntity.getFile())
						: null)
				.build();
		return businessTripDTO;
	}
}
