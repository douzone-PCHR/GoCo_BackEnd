package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pchr.dto.UnitDTO;
import com.pchr.entity.Unit;
import com.pchr.repository.UnitRepository;
import com.pchr.service.UnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

	private final UnitRepository unitRepo;

	@Override
	public List<UnitDTO> unitAll() {
		List<UnitDTO> unitListDto = new ArrayList<UnitDTO>();

		for (Unit unit : unitRepo.findAll()) {
			unitListDto.add(unit.toUnitDTO(unit));
		}

		return unitListDto;
	}

	@Override
	public List<UnitDTO> deptUnit(Long id) {
		List<UnitDTO> unitListDto = new ArrayList<UnitDTO>();

		for (Unit unit : unitRepo.findAllByParentUnitUnitId(id)) {
			unitListDto.add(unit.toUnitDTO(unit));
		}
		return unitListDto;
	}

	@Override
	// DB에 같은 이름이 들어올 경우 그 부서로 이동하기 위한 값 
	public UnitDTO getOneUnit(UnitDTO unitDto) {
			//이름이 들어올 경우
		Unit unit = unitRepo.findByUnitName(unitDto.getUnitName());
		return unit.toUnitDTO(unit);
	}

	// 부서 및 팀 추가
	@Override
	public UnitDTO unitInsert(UnitDTO unitDto) {

		Unit unit = unitDto.toUnit(unitDto);
		if (!unitRepo.existsByUnitName(unitDto.getUnitName())) {
			Unit resultUnit = unitRepo.save(unit);
			return resultUnit.toUnitDTO(resultUnit);
		} else {
			// 이름이 존재할 경우 이름에 해당하는 유닛을 리턴 (부서로 이동)
			return getOneUnit(unitDto);
		}
	}
	
	//부서 및 팀 업데이트
	@Override
	public void unitUpdate(Long unitId,UnitDTO newUnitDTO) {
		Unit unit = unitRepo.findById(unitId).get();
		UnitDTO unitDto = unit.toUnitDTO(unit);
		
		if(newUnitDTO.getUnitName() != null) {
			unitDto.setUnitName(newUnitDTO.getUnitName());
		}
		if(newUnitDTO.getParentUnit() != null) {
			unitDto.setParentUnit(newUnitDTO.getParentUnit());
		}
		unitRepo.save(unitDto.toUnit(unitDto));
		
	}

	// 부서 및 팀 삭제 (부서 삭제시 cascade 걸려있어서 자동으로 팀 삭제됨)
	@Override
	public void unitDelete(UnitDTO unitDTO) {
		unitRepo.deleteById(unitDTO.getUnitId());
	}

}
