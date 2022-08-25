package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.JobTitleDTO;
import com.pchr.dto.TeamPositionDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.dto.UnitEmpVO;
import com.pchr.entity.Authority;
import com.pchr.entity.Employee;
import com.pchr.entity.Unit;
import com.pchr.repository.EmployeeRepository;
import com.pchr.repository.UnitRepository;
import com.pchr.service.UnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

	private final UnitRepository unitRepo;
	private final EmployeeRepository empRepo;

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
		// 이름이 들어올 경우
		Unit unit = unitRepo.findByUnitName(unitDto.getUnitName());
		return unit.toUnitDTO(unit);
	}

	// 부서 및 팀 추가
	@Override
	public boolean unitInsert(UnitEmpVO unitVo) {
		UnitDTO saveUnitDto = null;
		if (!unitRepo.existsByUnitName(unitVo.getUnitName())) { // 중복된 이름이 아니라면
			Unit unit = unitVo.getParentUnit() == null ?
			// id, 이름, 타입,부모,자식 (매핑을 위한 값) 생성자 경우 null로 지정
					new Unit(null, unitVo.getUnitName(), false, null, null) : // 부서
					new Unit(null, unitVo.getUnitName(), true, unitVo.getParentUnit().toFKUnit(unitVo.getParentUnit()),
							null); // 팀
			Unit saveUnit = unitRepo.save(unit); // 유닛 추가
			saveUnitDto = saveUnit.toUnitDTO(saveUnit); // 유닛 DTO 변환

			// ============================================================================
			if (unitVo.getParentUnit() != null) { // 팀이라면
				// 매니저에 대한 정보 업데이트
				Employee newManager = empRepo.findByEmpNum(unitVo.getManagerNum()).get(); // 매니저 정보 조회
				EmployeeDTO managerDto = newManager.toDTO(newManager); // 매니저 DTO 변환
				managerDto.setUnit(saveUnitDto); // 매니저 팀 설정
				managerDto.setTeamPosition(new TeamPositionDTO(1L, null)); // 매니저에 대한 팀장 설정
				managerDto.setAuthority(Authority.ROLE_MANAGER);
				empRepo.save(managerDto.toEntity(managerDto)); // 팀장에 대한 값 db에 추가

				// 사원에 대한 정보 업데이트
				List<EmployeeDTO> empDtoList = new ArrayList<EmployeeDTO>(); // EmpDTO리스트 생성
				List<Employee> empList = empRepo.findAllByEmpNums(unitVo.getEmployeeList()); // 팀원에 대한 정보 가져오기
				for (Employee emp : empList) { // 팀원에 대한 정보 업데이트
					System.out.println(emp.getEmpId());
					EmployeeDTO empDto = emp.toDTO(emp); // DTO 변환
					empDto.setUnit(saveUnit.toUnitDTO(saveUnit)); // 팀 설정
					empDto.setTeamPosition(new TeamPositionDTO(2L, null)); // 사원에 대한 팀원 설정
					empDto.setAuthority(Authority.ROLE_USER);
					empDto.setManager(managerDto); // 팀장 설정
					empDtoList.add(empDto); // 변환값 리스트에 추가
				}
				// 수정된 Dto를 Entity로 변환하여 update
				List<Employee> resultEmpList = new ArrayList<Employee>();
				for (EmployeeDTO empDto : empDtoList) {
					resultEmpList.add(empDto.toEntity(empDto));
				}
				empRepo.saveAll(resultEmpList);
			}
			return true;
		}
		return false;
//		return saveUnitDto;
	}
//
//		Unit unit = unitDto.toUnit(unitDto);
//		if (!unitRepo.existsByUnitName(unitDto.getUnitName())) {
//			Unit resultUnit = unitRepo.save(unit);
//			//팀일 경우 사원에 대해 팀 값 수정
//			if(unitDto.getEmpNum() != null) {
//				Employee emp = empRepo.findByEmpNum(unitDto.getEmpNum()).get();
//				EmployeeDTO empDto = emp.toDTO(emp);
//				empDto.setUnit(resultUnit.toUnitDTO(resultUnit));
//				empDto.setTeamPosition(new TeamPositionDTO(2L,null));
//				empRepo.save(empDto.toEntity(empDto));
//			}
//			return resultUnit.toUnitDTO(resultUnit);
//		} else {
//			// 이름이 존재할 경우 이름에 해당하는 유닛을 리턴 (부서로 이동)
//			return getOneUnit(unitDto);
//		}

	// 부서 및 팀 업데이트
	@Override
	public boolean unitUpdate(Long unitId, UnitDTO newUnitDTO) {
		Unit unit = unitRepo.findById(unitId).get();
		UnitDTO unitDto = unit.toUnitDTO(unit);
		if (unitRepo.existsByUnitName(newUnitDTO.getUnitName())) {
			return false;
		}

		if (newUnitDTO.getUnitName() != null) {
			unitDto.setUnitName(newUnitDTO.getUnitName());
		}
		if (newUnitDTO.getParentUnit() != null) {
			unitDto.setParentUnit(newUnitDTO.getParentUnit());
		}
		unitRepo.save(unitDto.toUnit(unitDto));
		return true;
	}

	// 부서 및 팀 삭제 (부서 삭제시 cascade 걸려있어서 자동으로 팀 삭제됨)
	@Override
	@Transactional
	// 1 : 팀이나 부서 존재 시
	// 2 : 삭제(정상처리)
	// -1 : 에러
	public int unitDelete(int type, Long unitId) {
		try {
			List<Employee> emps = new ArrayList<Employee>();
			if (type == 1) { // 부서 일 경우
				emps = empRepo.findAllByUnitParentUnitId(unitId);

			} else if (type == 2) { // 팀 일 경우
				emps = empRepo.findAllByUnitUnitId(unitId); // 유닛Id가 n번인 사원 조회
			}
			if (!emps.isEmpty()) {
				return 1;
			}
//		List<EmployeeDTO> empsDto = new ArrayList<EmployeeDTO>(); // Setter를 통해 변경된  DTO 저장 리스트 생성
//		List<Employee> newEmps = new ArrayList<Employee>(); //업데이트 될 emp 

//		for(Employee emp : emps) {	// Entity를 DTO로 변환
//			empsDto.add(emp.toDTO(emp));
//		}

//		for(EmployeeDTO empDto : empsDto) { 
//				
//			empDto.setManager(null); //매니저 초기화
//			if(empDto.getTeamPosition().getTeamPositionId() == 2 ) { 
//				//부서삭제시 팀장인 사람 직책 팀원으로 변경 
//				empDto.setTeamPosition(new TeamPositionDTO(2L,null)); 
//			}
//			//수정된 DTO 새로운 배열에 추가
//			newEmps.add(empDto.toEntity(empDto));
//		}
			// 업데이트
//		empRepo.saveAll(newEmps);
			// 부서 및 팀 삭제
			unitRepo.deleteById(unitId);
			return 2;
		} catch (Exception e) {
			return -1;
		}
	}

}
