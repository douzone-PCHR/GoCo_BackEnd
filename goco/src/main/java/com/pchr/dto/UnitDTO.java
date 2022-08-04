package com.pchr.dto;

import com.pchr.entity.Unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnitDTO {

	private Long unitId;

	private String unitName;

	private boolean unitType;

	private UnitDTO parentUnit; 	

	public Unit toEntity(UnitDTO unitDTO, Unit parentUnit) {
		//새로운 값이 들어온 경우 (Insert)
		if(unitDTO.getUnitId() == null) {
			Unit unit = Unit.builder()
//				.unitId(unitDTO.getUnitId()) // A.I로 줄 예정이라 예외
					.unitName(unitDTO.getUnitName()).unitType(unitDTO.isUnitType())
					.parentUnit(parentUnit).build();
			return unit;			
		}
		// 기존 값을 수정할 경우(Id가 있는 경우)
		System.out.println(unitDTO.isUnitType());
		Unit unit = Unit.builder()
					.unitId(unitDTO.getUnitId())
					.unitName(unitDTO.getUnitName())
					.unitType(unitDTO.isUnitType())
					.parentUnit(parentUnit).build();
		return unit;
	}

	
	
	//ToEntity 과정에서는 Unit에 대한 Insert Update만 이뤄지기 때문에 UnitId값만 받으면 됨
	public Unit toFKUnit(UnitDTO unitDto) {
		Unit unit = Unit.builder()
				.unitId(unitDto.getUnitId())
				.build();
		return unit;
	}
	
}

