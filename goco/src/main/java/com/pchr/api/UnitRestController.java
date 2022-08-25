package com.pchr.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.UnitDTO;
import com.pchr.dto.UnitEmpVO;
import com.pchr.service.impl.UnitServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/admin/unit")
@RequiredArgsConstructor
public class UnitRestController {
	private final UnitServiceImpl unitImpl;

	// 모든 데이터 출력 Clear
	@GetMapping
	public List<UnitDTO> allUnit() {
		return unitImpl.unitAll();
	}

	// 부서에 대한 팀 조회 Clear
	// 모든 데이터를 조회 시 데이터를 다 넘기기 때문에 굳이 필요한지 잘 모르겠음 .
	// 일단 만들어 두긴함.
	@GetMapping(value = "/{unitid}")
	public List<UnitDTO> deptUnit(@PathVariable(name = "unitid") Long unitId) {
		return unitImpl.deptUnit(unitId);
	}

	// 부서 및 팀 추가 Clear
	@PostMapping
	public boolean insertUnit(@RequestBody UnitEmpVO unitVO) {
		return unitImpl.unitInsert(unitVO);
		//true = 정상처리
		//false = 중복 이름
	}

//	팀추가 { 
//	     "unitName":"인사3팀",
//	     "unitType":1,
//	     "parentUnit":{"unitId":"4"}
//	 }  
//	부서추가 {
//	     "unitName":"회계팀"
//	 }  

	// Unit 업데이트
	@PutMapping(value = "/{unitid}")
	public boolean updateUnit(@PathVariable(name = "unitid") Long unitId, @RequestBody UnitDTO unitDTO) {
		// 부서,팀 이름 변경 및 팀에 대한 부서이동
		return unitImpl.unitUpdate(unitId, unitDTO);
	}

	// Unit(부서 및 팀) 삭제
	@DeleteMapping(value = "/{type}/{unitid}")
	public int deleteUnit(@PathVariable(name ="type") int type,@PathVariable(name = "unitid") Long unitId) {
		return unitImpl.unitDelete(type,unitId);
	}
}
