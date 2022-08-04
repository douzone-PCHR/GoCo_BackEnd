package com.pchr.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.UnitDTO;
import com.pchr.entity.Unit;
import com.pchr.service.impl.UnitServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/unit")
@RequiredArgsConstructor
public class UnitRestController {
	private final UnitServiceImpl unitImpl;
	
	//모든 데이터 출력 Clear
	@GetMapping
	public List<UnitDTO> allUnit() {
		return unitImpl.unitAll();
	}
	
	// 부서에 대한 팀 조회  Clear
	// 모든 데이터를 조회 시 데이터를 다 넘기기 때문에 굳이 필요한지 잘 모르겠음 . 
	// 일단 만들어 두긴함.
	@GetMapping(value = "/{unitid}")
	public List<UnitDTO> deptUnit(@PathVariable(name = "unitid") Long unitId){
		return unitImpl.deptUnit(unitId);
	}
	
	//부서 및 팀 추가 Clear
	@PostMapping
	public void insertUnit(@RequestBody UnitDTO unitDTO){
		unitImpl.unitInsert(unitDTO);
	}
	
	//Unit 업데이트
	@PutMapping(value = "/{unitid}")
	public void updateUnit(@PathVariable(name = "unitid") Long unitId,@RequestBody UnitDTO unitDTO) {
		// 부서,팀 이름 변경 및 팀에 대한 부서이동
		unitImpl.unitUpdate(unitId,unitDTO);
	}
	
	
	//Unit(부서 및 팀) 삭제 
	@DeleteMapping
	public void deleteUnit(UnitDTO unitDTO) {
		unitImpl.unitDelete(unitDTO);
	}
}
