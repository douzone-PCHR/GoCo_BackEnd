package com.pchr.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.UnitDTO;
import com.pchr.entity.Unit;
import com.pchr.service.impl.UnitServiceImpl;

@RestController
@RequestMapping(value = "/unit")

public class UnitRestController {
	@Autowired
	UnitServiceImpl unitImpl;

	
	//모든 데이터 출력
	@GetMapping
	public List<UnitDTO> allUnit() {
		return unitImpl.unitAll();
	}
	
	//부서에 대한 팀 조회
	@GetMapping(value = "/dept")
	public List<UnitDTO> deptUnit(UnitDTO unitDTO){
		return unitImpl.deptUnit(unitDTO);
	}
	
	//Unit 추가
	@PostMapping
	public void insertUnit(@RequestBody UnitDTO unitDTO){
		unitImpl.unitInsert(unitDTO);
	}
	
	//Unit 업데이트
	@PutMapping
	public void updateUnit(@RequestBody UnitDTO unitDTO) {
		// 부서,팀 이름 변경 및 팀에 대한 부서이동
		unitImpl.unitUpdate(unitDTO);
	}
	
	
	//Unit 삭제
	@DeleteMapping
	public void deleteUnit(UnitDTO unitDTO) {
		unitImpl.unitDelete(unitDTO);
	}
}
