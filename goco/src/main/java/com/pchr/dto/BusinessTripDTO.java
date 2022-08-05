package com.pchr.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.pchr.entity.BusinessTrip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTripDTO {
	private Long businessTripId;

	private Date businessTripStartDate;

	private Date businessTripEndDate;

	private ApproveEnum approveYn;

	private String businessTripContent;

	private LocalDateTime businessTripRequestDate;

	private LocalDateTime businessTripApproveDate;

	private EmployeeDTO employee;

	// toEntity
	public BusinessTrip toBusinessTripEntity(BusinessTripDTO businessTripDTO) {
		BusinessTrip businessTripEntity = BusinessTrip.builder().businessTripId(businessTripDTO.getBusinessTripId())
				.businessTripStartDate(businessTripDTO.getBusinessTripStartDate())
				.businessTripEndDate(businessTripDTO.getBusinessTripEndDate()).approveYn(businessTripDTO.getApproveYn())
				.businessTripContent(businessTripDTO.getBusinessTripContent())
				.businessTripRequestDate(businessTripDTO.getBusinessTripRequestDate())
				.businessTripApproveDate(businessTripDTO.getBusinessTripApproveDate())
				.employee(businessTripDTO.getEmployee().toFKEmployee(businessTripDTO.getEmployee())).build();
		return businessTripEntity;
	}
}
