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

	public Unit toUnit(UnitDTO unitDto) {
		Unit unit = null;
		// 업데이트 시 unitId를 필요로 하기 때문에 새로운 build 생성
		if (unitDto.getUnitId() != null) {
			unit = Unit.builder().unitId(unitDto.getUnitId()).unitName(unitDto.getUnitName()).unitType(true)
					.parentUnit(unitDto.getParentUnit().toParentUnit(unitDto.getParentUnit())).build();
			return unit;
		}
		// 팀 일 경우
		if (unitDto.getParentUnit() != null) {
			System.out.println(unitDto.getParentUnit());
			unit = Unit.builder().unitName(unitDto.getUnitName()).unitType(true)
					.parentUnit(unitDto.getParentUnit().toParentUnit(unitDto.getParentUnit())).build();
		}
		// 부서 일 경우
		else {
			unit = toParentUnit(unitDto);
		}
		return unit;
	}

	// 부모에 대한 값 추가 시
	private Unit toParentUnit(UnitDTO parentUnit) {
		Unit Parentunit = Unit.builder().unitId(parentUnit.getUnitId() == null ? null : parentUnit.getUnitId())
				.unitName(parentUnit.getUnitName()).unitType(false).build();

		return Parentunit;
	}

	// ToEntity 과정에서는 Unit에 대한 Insert Update만 이뤄지기 때문에 UnitId값만 받으면 됨
	public Unit toFKUnit(UnitDTO unitDto) {
		Unit unit = Unit.builder().unitId(unitDto.getUnitId()).build();
		return unit;
	}

}
