package com.pchr.service;

import java.util.List;

import com.pchr.dto.UnitDTO;

public interface UnitService {
	
	public List<UnitDTO> unitAll();
	public void unitInsert(UnitDTO unitDTO);
	public void unitUpdate(UnitDTO unitDTO);
	public List<UnitDTO> deptUnit(UnitDTO unit);
	public void unitDelete(UnitDTO unitDTO);
}
