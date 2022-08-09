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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "parent_unit", referencedColumnName = "unit_id")
	@JsonIgnore
	private Unit parentUnit;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentUnit")
	private List<Unit> children;

	// Unit이 사용할 빌더 패턴
	// Entity -> DTO (Select에 대해 사용)
	public UnitDTO toUnitDTO(Unit unit) {//용주쓸꺼
		UnitDTO unitDto = null;
		if (unit.getParentUnit() != null) {
			unitDto = UnitDTO.builder().unitName(unit.getUnitName())
					.unitId(unit.getUnitId())
					.parentUnit(parentUnitDTO(unit.getParentUnit())).build();
		} else {
			unitDto = parentUnitDTO(unit);
		}
		return unitDto;
	}

	// 부서의 DTO 변환
	// 생성자 패턴
	// 셀프참조를 하고 있기 때문에 부서의 경우에는 parentUnit에 대한 정의가 없어야 한다.
	// 만약 존재할 경우 Stack Overflow / NullPointException 발생 (스스로를 계속 참조하기 때문에)
	private UnitDTO parentUnitDTO(Unit parentUnit) {
		UnitDTO parentUnitDTO = new UnitDTO();

		parentUnitDTO.setUnitId(parentUnit.getUnitId());
		parentUnitDTO.setUnitName(parentUnit.getUnitName());
		parentUnitDTO.setUnitType(parentUnitDTO.isUnitType());
		return parentUnitDTO;
	}

	// Select / Update 과정에서 필요하기 때문에 모든 데이터를 출력해준다
//	public UnitDTO toFKUnitDto(Unit unit) {
//		UnitDTO unitDto = null;
//		// 팀일 경우
//		if (unit.getParentUnit() != null) {
//
//			unitDto = UnitDTO.builder().unitId(unit.getUnitId())
//					.unitName(unit.getUnitName()).unitType(unit.isUnitType())
//					.parentUnit(parentUnitDTO(unit.getParentUnit())).build();
//		}
//		// 부서일 경우
//		else {
//			unitDto = UnitDTO.builder().unitId(unit.getUnitId())
//					.unitName(unit.getUnitName()).unitType(unit.isUnitType())
//					.build();
//		}
//		return unitDto;
//	}
}
