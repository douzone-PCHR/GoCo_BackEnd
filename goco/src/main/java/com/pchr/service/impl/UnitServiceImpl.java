package com.pchr.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.pchr.dto.EmployeeDTO;
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
//				// 매니저에 대한 정보 업데이트

				Employee newManager = empRepo.findByEmpNum(unitVo.getManagerNum()).get(); // 매니저 정보 조회
				if(newManager.getManager() == null) { // 매니저로 임명되는 사원이 팀장이라면. 
					
				}
//				EmployeeDTO managerDto = newManager.toDTO(newManager); // 매니저 DTO 변환
//				managerDto.setUnit(saveUnitDto); // 매니저 팀 설정
//				managerDto.setTeamPosition(new TeamPositionDTO(1L, null)); // 매니저에 대한 팀장 설정
//				managerDto.setAuthority(Authority.ROLE_MANAGER);
//				empRepo.save(managerDto.toEntity(managerDto)); // 팀장에 대한 값 db에 추가
//				// ========================== 팀장 => 팀장일 경우 ===============================
//				List<Employee> ogrMembers = empRepo.findAllByManager(unitVo.getManagerNum());
//
//				if (!ogrMembers.isEmpty()) {
//					List<EmployeeDTO> ogrMembersDto = new ArrayList<EmployeeDTO>();
//
//					for (Employee ogrMember : ogrMembers) {
//						EmployeeDTO ogrMemberDto = ogrMember.toDTO(ogrMember);
//						ogrMemberDto.setManager(null);
//						ogrMembersDto.add(ogrMemberDto);
//					}
//
////					for()
//					List<Employee> setOgrMembers = new ArrayList<Employee>();
//					ogrMembersDto.forEach(o -> {
//						setOgrMembers.add(o.toEntity(o));
//					});
//					empRepo.saveAll(setOgrMembers);
//				}
//
//				// ========================================================================
//
//				// 사원에 대한 정보 업데이트
//				List<EmployeeDTO> empDtoList = new ArrayList<EmployeeDTO>(); // EmpDTO리스트 생성
//				List<Employee> empList = empRepo.findAllByEmpNums(unitVo.getEmployeeList()); // 팀원에 대한 정보 가져오기
//				for (Employee emp : empList) { // 팀원에 대한 정보 업데이트
//					if (emp.getTeamPosition().getTeamPositionId() == 1L) {
//						List<EmployeeDTO> otherTeamMembersDto = new ArrayList<EmployeeDTO>();
//						List<Employee> otherTeamMembers = empRepo.findAllByManager(emp.getEmpNum());
//						for (Employee otherTeamMember : otherTeamMembers) {
//							EmployeeDTO otherTeamMemberDto = otherTeamMember.toDTO(otherTeamMember);
//							otherTeamMemberDto.setManager(null);
//							otherTeamMembersDto.add(otherTeamMemberDto);
//						}
//						empRepo.saveAll(otherTeamMembers);
//					}
//					EmployeeDTO empDto = emp.toDTO(emp); // DTO 변환
//					empDto.setUnit(saveUnit.toUnitDTO(saveUnit)); // 팀 설정
//					empDto.setTeamPosition(new TeamPositionDTO(2L, null)); // 사원에 대한 팀원 설정
//					empDto.setAuthority(Authority.ROLE_USER);
//					empDto.setManager(managerDto); // 팀장 설정
//					empDtoList.add(empDto); // 변환값 리스트에 추가
//				}
//				// 수정된 Dto를 Entity로 변환하여 update
//				List<Employee> resultEmpList = new ArrayList<Employee>();
//				for (EmployeeDTO empDto : empDtoList) {
//					resultEmpList.add(empDto.toEntity(empDto));
//				}
//				empRepo.saveAll(resultEmpList);
			}
			return true;
		}
		return false;
	}

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
			unitRepo.deleteById(unitId);
			return 2;
		} catch (Exception e) {
			return -1;
		}
	}

}
