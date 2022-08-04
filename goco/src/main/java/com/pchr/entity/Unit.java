package com.pchr.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pchr.dto.UnitDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unit")
public class Unit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unit_id")
	private Long unitId;

	@Column(name = "unit_name", nullable = false)
	private String unitName;

	@Column(name = "unit_type", nullable = false)
	private boolean unitType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "parent_unit", referencedColumnName = "unit_id")
	@JsonIgnore
	private Unit parentUnit;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentUnit")
	private List<Unit> children;

	// 부서의 DTO 변환
	private UnitDTO parentUnitDTO(Unit parentUnit) {
		UnitDTO parentUnitDTO = new UnitDTO();

		parentUnitDTO.setUnitId(parentUnit.getUnitId());
		parentUnitDTO.setUnitName(parentUnit.getUnitName());
		parentUnitDTO.setUnitType(parentUnitDTO.isUnitType());
		return parentUnitDTO;
	}

	// Select / Update 과정에서 필요하기 때문에 모든 데이터를 출력해준다
	public UnitDTO toFKUnitDto(Unit unit) {
		UnitDTO unitDto = null;
		// 팀일 경우
		if (unit.getParentUnit() != null) {

			unitDto = UnitDTO.builder().unitId(unit.getUnitId())
					.unitName(unit.getUnitName()).unitType(unit.isUnitType())
					.parentUnit(parentUnitDTO(unit.getParentUnit())).build();
		}
		// 부서일 경우
		else {
			unitDto = UnitDTO.builder().unitId(unit.getUnitId())
					.unitName(unit.getUnitName()).unitType(unit.isUnitType())
					.build();
		}
		return unitDto;
	}
}
