package com.pchr.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UnitEmpVO {
	private String unitName;
	private UnitDTO parentUnit;
	private Long managerNum;
	private List<Long> employeeList;
}
