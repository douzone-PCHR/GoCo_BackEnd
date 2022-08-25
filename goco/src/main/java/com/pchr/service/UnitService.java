package com.pchr.service;

import java.util.List;

import com.pchr.dto.UnitDTO;
import com.pchr.dto.UnitEmpVO;

public interface UnitService {
	
	public List<UnitDTO> unitAll();
	public List<UnitDTO> deptUnit(Long id);
	public UnitDTO getOneUnit(UnitDTO unitDto);
	public boolean unitUpdate(Long unitId, UnitDTO newUnitDTO);
	public boolean unitInsert(UnitEmpVO unitVo);
	public int unitDelete(int type, Long unitId);
}
