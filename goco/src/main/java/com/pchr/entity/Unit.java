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

	@Column(name = "unit_name",nullable = false)
	private String unitName;

	@Column(name = "unit_type",nullable = false)
	private boolean unitType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "parent_unit", referencedColumnName = "unit_id")
	@JsonIgnore
	private Unit parentUnit;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentUnit")
	private List<Unit> children;

	public UnitDTO toDTO(Unit unit) {
		// 부서와 팀에 따라 빌더
		// 부서
		if (unit.isUnitType()) {
			UnitDTO unitDTO = UnitDTO.builder().unitId(unit.getUnitId())
					.unitName(unit.getUnitName()).unitType(unit.isUnitType())
					.build();
			return unitDTO;
		}

		// 팀
		UnitDTO parentUnitDTO = ParentUnitDTO(unit.getParentUnit());

		UnitDTO unitDTO = UnitDTO.builder().unitId(unit.getUnitId())
				.unitName(unit.getUnitName()).unitType(unit.isUnitType())
				.parentUnit(parentUnitDTO).build();
		return unitDTO;

	}
	
	// 부서의 DTO 변환
	private UnitDTO ParentUnitDTO(Unit parentUnit) {
		UnitDTO parentUnitDTO = new UnitDTO();

		parentUnitDTO.setUnitId(parentUnit.getUnitId());
		parentUnitDTO.setUnitName(parentUnit.getUnitName());
		parentUnitDTO.setUnitType(parentUnitDTO.isUnitType());
		return parentUnitDTO;
	}

}
