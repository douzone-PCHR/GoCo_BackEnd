package com.pchr.service.impl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pchr.config.SecurityUtil;
import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.entity.Authority;
import com.pchr.entity.BusinessTrip;
import com.pchr.entity.Employee;
import com.pchr.entity.Resignation;
import com.pchr.entity.Vacation;
import com.pchr.repository.BusinessTripRepository;
import com.pchr.repository.EmployeeRepository;
import com.pchr.repository.FileRepository;
import com.pchr.repository.VacationRepository;
import com.pchr.service.EmployeeService;
import com.pchr.util.S3Util;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpolyServiceImpl implements EmployeeService {
	private final PasswordEncoder passwordEncoder;
	private final EmployeeRepository employeeRepository;
	private final ResignationServiceImpl resignationServiceImpl;
	private final VacationRepository vacationRepo;
	private final BusinessTripRepository businessRepo;
	private final FileRepository fileRepo;
	private final S3Util s3Util;
	private final CommentServiceImpl commentServiceImpl;
	private final BoardServiceImpl boardServiceImpl;
	private final CommuteServiceImpl commuteServiceImpl;
	private final WorkServiceImpl workServiceImpl;
	private final VacationServiceImpl vacationServiceImpl;
	private final BusinessTripServiceImpl businessTripServiceImpl;

	@Override
	public Optional<Employee> findByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return employeeRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByEmpId(String empId) {
		return employeeRepository.existsByEmpId(empId);
	}

	@Override
	public Optional<Employee> findByEmpId(String empId) {
		return employeeRepository.findByEmpId(empId);
	}

	@Override
	public Employee findByNameAndEmail(String name, String email) {
		return employeeRepository.findByNameAndEmail(name, email);
	}

	@Override
	public Employee findByEmpIdAndEmail(String empId, String email) {
		return employeeRepository.findByEmpIdAndEmail(empId, email);
	}

	@Override
	public List<EmployeeDTO> findAll() {
		List<Employee> empList = employeeRepository.findAll();
		List<EmployeeDTO> empDTOList = new ArrayList<EmployeeDTO>();
		empList.forEach((emp) -> {
			empDTOList.add(emp.toDTO(emp));
		});
		return empDTOList;
	}

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public int deleteByEmail(String email) {
		return employeeRepository.deleteByEmail(email);
	}

	@Override
	public int deleteByEmpId(String empId) {
		return employeeRepository.deleteByempId(empId);
	}

	@Override
	public int deleteByEmpNum(Long empNum) {
		return employeeRepository.deleteByEmpNum(empNum);
	}

	@Override
	public List<Employee> findManagerByEmpNum(Long empNum) {
		return employeeRepository.findManagerByEmpNum(empNum);
	}

	@Override
	public Optional<Employee> findByEmpNum(Long empNum) {
		return employeeRepository.findByEmpNum(empNum);
	}

	@Override
	public List<Employee> findByManager(Long teamPositionId, Long unitId) {
		return employeeRepository.findByManager(teamPositionId, unitId);
	}

	/////////////////////////////////// 이하 회원 정보 수정 //////////////////////////////
	@Override // 아이디 체크
	public boolean idCheck(String info) {
		if (existsByEmail(info) | existsByEmpId(info)) {
			return false;
		}
		return true; // 중복되는 값이 없을 때 true를 반환
	}

	@Override // 내정보 회원탈퇴
	public int delete() {
		String empId = SecurityUtil.getCurrentMemberId();
		if (existsByEmpId(empId)) { // 탈퇴할 회원이 있는지 먼저 확인
			Employee employee = findByEmpId(empId).get();
			resignationServiceImpl.save(employee.toResignation(employee));// 퇴사자테이블을위해employee테이블을 Resignation객체로 변환
																			// 한다.그 후 저장한다.
			findManagerByEmpNum(employee.getEmpNum()).forEach((e) -> {// 외래키 null을 만들기위해 참조하는 모든 값들을 list형태로 불러옴
				EmployeeDTO dto = e.toDTO(e);
				dto.setManager(null); // 참조하는 값들을 모두 null로 바꿔준다.
				save(dto.toEntity(dto));
			});
			deleteForeignKey(employee.getEmpNum());
			return deleteByEmpId(empId);
			// return 1;
		}
		return 0;
	}

	// 계정 삭제를 위해 외래키 참조하는 모든것들 삭제하는 것
	@Override
	public void deleteForeignKey(Long empNum) {
		deleteComment(empNum);
		boardServiceImpl.deleteBoardByEmpNum(empNum);
		commuteServiceImpl.deleteCommuteByEmpNum(empNum);
		workServiceImpl.deleteWorkByEmpNum(empNum);
		vacationServiceImpl.deleteVacationByEmpNum(empNum);
		businessTripServiceImpl.deleteBusinessTripByEmpNum(empNum);
	}

	@Override
	public void deleteComment(Long empNum) {// 게시글을 참조하는 모든 댓글을 같이 삭제 해야되기 때문에 만들어진 함수
		commentServiceImpl.deleteCommentByEmpNum(empNum);
		List<Long> boardIdData = boardServiceImpl.findAllByBoardId(empNum);
		for (Long boardId : boardIdData) {
			List<Long> commentIdData = commentServiceImpl.findByBoardId(boardId);
			for (Long commentId : commentIdData) {
				commentServiceImpl.deleteComment(commentId);
			}
		}
	}

	@Override // 내정보 비번 변경
	public int setPassword(String password, String password2) {
		if (!password.equals(password2)) {
			return -1;
		}
		String EmpId = SecurityUtil.getCurrentMemberId();
		EmployeeDTO employeeDTO = findByEmpId(EmpId).map(emp -> emp.toDTO(emp))
				.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
		employeeDTO.setPassword(passwordEncoder.encode((password)));
		save(employeeDTO.toEntity(employeeDTO));
		return 1;
	}

	@Override // 내정보 반환 하는 메소드
	public EmployeeResponseDTO getMyInfoBySecurity() {
		return findByEmpId(SecurityUtil.getCurrentMemberId()).map(EmployeeResponseDTO::of)
				.orElseThrow(() -> new RuntimeException("403"));
	}

	@Override // 내정보 이메일변경
	public int emailChange(String email) {
		if (existsByEmail(email)) {
			throw new RuntimeException("이미 가입되어 있는 이메일 입니다.");
		}
		String empId = SecurityUtil.getCurrentMemberId();
		EmployeeDTO employeeDTO = findByEmpId(empId).map(emp -> emp.toDTO(emp))
				.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
		employeeDTO.setEmail(email);
		save(employeeDTO.toEntity(employeeDTO));
		return 1;
	}

	@Override // 내정보 폰번호변경
	public int setPhone(String phoneNumber) {
		String EmpId = SecurityUtil.getCurrentMemberId();
		EmployeeDTO employeeDTO = findByEmpId(EmpId).map(emp -> emp.toDTO(emp))
				.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
		employeeDTO.setPhoneNumber(phoneNumber);
		save(employeeDTO.toEntity(employeeDTO));
		return 1;
	}

	@Override // 관리자의 Role 권한변경권
	public int changeRole(Authority authority, String empId) {
		EmployeeDTO employeeDTO = findByEmpId(empId).map(emp -> emp.toDTO(emp))
				.orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
		employeeDTO.setAuthority(authority);
		save(employeeDTO.toEntity(employeeDTO));
		return 1;
	}

	@Override // 관리자의 휴가 , 메일 , 아이디, 입사일, 이름 , 핸드폰 번호 변경
	public int changeData(int number, String empNum, String data) {
		EmployeeDTO employeeDTO = findByEmpNum(Long.parseLong(empNum)).map(emp -> emp.toDTO(emp))
				.orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));
		switch (number) {
		case 1:// 휴가 변경
			employeeDTO.setVacationCount(Float.parseFloat(data));
			break;
		case 2:// 메일 변경
			employeeDTO.setEmail(data);
			break;
		case 3:// 아이디 변경
			employeeDTO.setEmpId(data);
			break;
		case 4:// 입사일 변경
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			try {
				employeeDTO.setHiredate(fm.parse(data));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case 5:// 이름 변경
			employeeDTO.setName(data);
			break;
		case 6:// 핸드폰 번호 변경
			employeeDTO.setPhoneNumber(data);
			break;
		default:// 에러
			return 0;
		}
		save(employeeDTO.toEntity(employeeDTO));
		return 1;
	}

	@Override // 관리자의 유저 데이터 삭제
	public int adminDelete(Long empNum) {

		Employee employee = findByEmpNum(empNum).orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));
		resignationServiceImpl.save(employee.toResignation(employee));// 퇴사자테이블을위해employee테이블을 Resignation객체로 변환 한다.그 후
																		// 저장한다.
		findManagerByEmpNum(employee.getEmpNum()).forEach((e) -> {// 외래키 null을 만들기위해 참조하는 모든 값들을 list형태로 불러옴
			EmployeeDTO dto = e.toDTO(e);
			dto.setManager(null); // 참조하는 값들을 모두 null로 바꿔준다.
			save(dto.toEntity(dto));
		});

		List<Vacation> vacations = vacationRepo.findAllByEmployeeEmpNumOrderByVacationRequestDateDesc(empNum);
		List<BusinessTrip> businesses = businessRepo.findAllByEmployeeEmpNumOrderByBusinessTripRequestDateDesc(empNum);
		List<Long> fileIds = new ArrayList<Long>();
		if (!vacations.isEmpty()) {
			for (Vacation vacation : vacations) {
//				vacation
				if (vacation.getFile() != null) {
					s3Util.deleteFile("vacation/" + vacation.getFile().getFileName()); // S3 데이터 삭제
					fileIds.add(vacation.getFile().getFileId());
				}

			}
		}
		if (!businesses.isEmpty()) {
			for (BusinessTrip business : businesses) {
				if (business.getFile() != null) {
					s3Util.deleteFile("business/" + business.getFile().getFileName()); // S3 데이터 삭제
					fileIds.add(business.getFile().getFileId());
				}
			}
		}
		if (!fileIds.isEmpty()) {
			fileRepo.deleteAllById(fileIds); // 모든 파일 DB 삭제
			vacationRepo.deleteAll(vacations);
			businessRepo.deleteAll(businesses);
		}
		return deleteByEmpNum(empNum);
	}

	@Override // 관리자의 퇴사자 확인
	public List<Resignation> ResignationAll() {
		return resignationServiceImpl.findAll();
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Unit_id 변경 (팀 변경)
	public int changUnitId(Long empNum, UnitDTO unit) {
		Employee employee = findByEmpNum(empNum).orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));
		// 내가 팀장이면 팀장 해제 후 가능하다고 떠야 한다.
		if (employee.getTeamPosition().getTeamPositionId() == 1L) {
			return -1; // 팀장이기 때문에 팀원으로 변경 후 가능
		}
		setUnitID(employee.toDTO(employee), unit);// 나의 unit_id를 수정하는 코드

		// 4) 해당 unit_id의 팀장 정보를 팀원들의 manager에 걸어주는 코드
		List<Employee> leader = findByManager(1L, unit.getUnitId());
		if (leader.size() == 1) {
			setLeader(leader.get(0), unit.getUnitId());
		} else {
			return -2; // 해당 부서에 매니저가 2명이상 이거나 없을 때 에러 코드
		}
		return 1;
	}

	@Override // 나의 unit_id를 수정하는 코드
	public void setUnitID(EmployeeDTO employeeDTO, UnitDTO unit) {
		employeeDTO.setManager(null);// 매니저를 null로 바꿔준다.
		employeeDTO.getTeamPosition().setTeamPositionId(2L);// 팀원으로 변경하는 것
		employeeDTO.setTeamPosition(employeeDTO.getTeamPosition());// 팀원으로 변경하는 것
		employeeDTO.setUnit(unit); // 팀 변경 한다.
		save(employeeDTO.toEntity(employeeDTO));
	}

	@Override // unit_id 수정 후 팀원들의 팀장을 찾아 지정해주는 코드
	public void setLeader(Employee employee, Long unitId) {
		findByManager(2L, unitId).forEach(e -> {
			EmployeeDTO empDTO = e.toDTO(e);
			empDTO.setManager2(employee.toDTO(employee));
			save(empDTO.toEntity(empDTO));
		});
	}

	@Override // 매니저 변경 함수
	public int changeManager(Long empNum, UnitDTO unit) {
		Employee employee = findByEmpNum(empNum).orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));
		LeaderToMember(unit.getUnitId());// 기존 데이터의 팀장을 팀원으로 바꾸는 코드
		MemberToLeader(employee, unit);// 전달 받은 empNum을 통해 팀장 지정
		setLeader(employee, unit.getUnitId());// 팀원들의 팀장을 찾아 지정해주는 코드
		return 1;
	}

	@Override // 기존 데이터의 팀장을 팀원으로 바꾸는 코드
	public void LeaderToMember(Long unitId) {
		findByManager(1L, unitId).forEach(e -> {
			EmployeeDTO empDTO = e.toDTO(e);
			empDTO.getTeamPosition().setTeamPositionId(2L);
			save(empDTO.toEntity(empDTO));
		});
	}

	@Override // 전달 받은 empNum을 통해 해당 empNum의 주체를 팀장으로 지정
	public void MemberToLeader(Employee employee, UnitDTO unit) {
		EmployeeDTO employeeDTO = employee.toDTO(employee);
		employeeDTO.setManager(null);// 팀장이 될거기 때문에 manager는 null
		employeeDTO.getTeamPosition().setTeamPositionId(1L);// 팀장이기 때문에 teamPosition은 1
		employeeDTO.setUnit(unit);// 해당 팀으로 unit 대입
		save(employeeDTO.toEntity(employeeDTO));
	}

	@Override
	public void updateJobTitle(EmployeeDTO emp) {
		// emp에 대한 정보를 받아온다.
		Employee employee = employeeRepository.findByEmpId(emp.getEmpId()).get();
		// DTO로 변환
		EmployeeDTO empDto = employee.toDTO(employee);
		// JobTitle 변경
		empDto.setJobTitle(emp.getJobTitle());
		// 저장
		employeeRepository.save(empDto.toEntity(empDto));
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean updateAdminEmp(Long id, int type, Long value) {
		Employee emp = employeeRepository.findByEmpNum(id).get();
		EmployeeDTO empDto = emp.toDTO(emp);
		switch (type) {
		case 1: // 부서
			return updateAdminDept(empDto, value);
		case 2: // 직책
			return updateAdminTeamPosition(empDto, value);

		case 3: // 직급
			return updateAdminJobTitle(empDto, value);
		default:
			return false;
		}

	}

	public boolean updateAdminDept(EmployeeDTO empDto, Long unitId) {
		if (empDto.getUnit().getUnitId() != unitId) { // 팀을 변경하는데 같은 팀이 아닐 경우
			Employee manager = employeeRepository.findByUnitUnitIdAndTeamPositionTeamPositionId(unitId, 1L);
			// 만약 사원의 직급이 팀장일 경우
			if (empDto.getTeamPosition().getTeamPositionId() == 1L) {
				// 기존 팀에 있던 사원들 모두 찾아오기
				List<Employee> empMembers = employeeRepository.findAllByTeamPositionTeamPositionIdAndUnitUnitId(2L,
						empDto.getUnit().getUnitId());
				List<Employee> updateEmpMembers = new ArrayList<Employee>();
				for (Employee empMember : empMembers) {
					EmployeeDTO empDtoMember = empMember.toDTO(empMember);
					// 매니저를 전부 null로 처리
					empDtoMember.setManager(null);
					updateEmpMembers.add(empDtoMember.toEntity(empDtoMember));
				}
				// 팀원 저장
				employeeRepository.saveAll(updateEmpMembers);

				// 직급을 2로 바꿔준다(팀원으로 초기화 시켜줌)
				empDto.getTeamPosition().setTeamPositionId(2L);

				// 팀원으로 됐기 때문에 유저로 바꿔줌
				empDto.setAuthority(Authority.ROLE_USER);
			}
			// 매니저가 있을 경우
			if (manager != null) {
				empDto.setManager(manager.toDTO(manager));
			}
			// ==== 윗부분은 매니저를 바꿔주는 로직 ====
			// 부서 지정
			empDto.getUnit().setUnitId(unitId);
			employeeRepository.save(empDto.toEntity(empDto));
			return true;
		}
		return false;

	}

	public boolean updateAdminTeamPosition(EmployeeDTO empDto, Long teamPositionId) {
		List<Employee> updateEmpMembers = new ArrayList<Employee>();

		if (empDto.getTeamPosition().getTeamPositionId() == teamPositionId) {
			return false;
		}

		if (empDto.getTeamPosition().getTeamPositionId() == 1L) {
			List<Employee> empMembers = employeeRepository.findAllByTeamPositionTeamPositionIdAndUnitUnitId(2L,
					empDto.getUnit().getUnitId());
			for (Employee empMember : empMembers) {
				EmployeeDTO empDtoMember = empMember.toDTO(empMember);
				empDtoMember.setManager(null);
				updateEmpMembers.add(empDtoMember.toEntity(empDtoMember));
			}
			empDto.getTeamPosition().setTeamPositionId(teamPositionId);
			empDto.setAuthority(Authority.ROLE_USER);

		} else { // 팀원 -> 팀장으로 변경될 경우
			empDto.getTeamPosition().setTeamPositionId(teamPositionId);
			List<Employee> empMembers = employeeRepository.findAllByUnitUnitId(empDto.getUnit().getUnitId());
			for (Employee empMember : empMembers) {
				if (empMember.getEmpNum() != empDto.getEmpNum()) {
					EmployeeDTO empDtoMember = empMember.toDTO(empMember);
					empDtoMember.setManager(empDto);
					if (empDtoMember.getTeamPosition().getTeamPositionId() == 1L) {
						empDtoMember.getTeamPosition().setTeamPositionId(2L); // 기존 팀장을 팀원으로 변경
						empDtoMember.setManager(empDto);
					}
					updateEmpMembers.add(empDtoMember.toEntity(empDtoMember));
				}
			}

			empDto.getTeamPosition().setTeamPositionId(1L);
			empDto.setManager(null);
			empDto.setAuthority(Authority.ROLE_MANAGER);
		}
		updateEmpMembers.add(empDto.toEntity(empDto));

		employeeRepository.saveAll(updateEmpMembers);

		return true;

	}

	public boolean updateAdminJobTitle(EmployeeDTO empDto, Long jobTitleId) {
		if (empDto.getJobTitle().getJobTitleId() == jobTitleId) {
			return false;
		}
		empDto.getJobTitle().setJobTitleId(jobTitleId);
		employeeRepository.save(empDto.toEntity(empDto));

		return true;
	}

	public List<EmployeeDTO> findManager(Long unitId) {
		List<Employee> managers = employeeRepository.findAllByUnitParentUnitId(unitId);
		List<EmployeeDTO> managersDto = new ArrayList<EmployeeDTO>();
		for (Employee manager : managers) {
			managersDto.add(manager.toDTO(manager));
		}
		return managersDto;
	}
}