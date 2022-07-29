package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchr.dto.UnitDTO;
import com.pchr.entity.Unit;
import com.pchr.repository.UnitRepository;
import com.pchr.service.UnitService;

@Service
public class UnitServiceImpl implements UnitService {

	@Autowired
	UnitRepository unitrepo;

	@Override
	public List<UnitDTO> unitAll() {
		List<UnitDTO> unitDTO = new ArrayList<UnitDTO>();
		for (Unit unit : unitrepo.findAll()) {
			unitDTO.add(unit.toDTO(unit));
		}
		return unitDTO;
	}
	
	@Override
	public void unitInsert(UnitDTO unitDTO) {

		// A.I로 처리할 예정이라 중복 PK 없음
//		if (!unitrepo.findById(unitDTO.getUnitId()).isPresent()) {
		if (unitDTO.getParentUnit() != null) {
			Unit parentUnit = unitrepo
					.findById(unitDTO.getParentUnit().getUnitId()).get();
			if (parentUnit != null) {
				Unit unit = unitDTO.toEntity(unitDTO, parentUnit);
				unitrepo.save(unit);
			}
			return;
		}
		unitrepo.save(unitDTO.toEntity(unitDTO, null));
//		}
	}

	@Override
	public void unitUpdate(UnitDTO unitDTO) {
		Unit parentUnit = unitrepo.findById(unitDTO.getParentUnit().getUnitId())
				.get();
		
		Unit unit = unitDTO.toEntity(unitDTO,parentUnit);
		unitrepo.save(unit);
		
	}

	@Override
	public List<UnitDTO> deptUnit(UnitDTO unitDTO) {
		List<UnitDTO> deptUnitDTO = new ArrayList<UnitDTO>();
		Unit unit = unitDTO.toEntity(unitDTO, null);
		for(Unit units:unitrepo.findByparentUnit(unit)) {
			deptUnitDTO.add(unit.toDTO(units));
		}
		return deptUnitDTO;
	}

	@Override
	public void unitDelete(UnitDTO unitDTO) {
		Unit unit = unitDTO.toEntity(unitDTO, null);
		unitrepo.delete(unit);
	}
	
	
}
 