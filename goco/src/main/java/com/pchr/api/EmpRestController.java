package com.pchr.api;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.entity.Resignation;
import com.pchr.service.impl.EmpolyServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class EmpRestController {
	private final EmpolyServiceImpl empolyServiceImpl;

	/////////////////////////////////// 유저 권한 이하 회원 정보 수정
	/////////////////////////////////// //////////////////////////////
	@DeleteMapping("/user/delete") // 아이디 삭제
	public int delete() {
		return empolyServiceImpl.delete();
	} // http://localhost:8080/user/delete

	@PutMapping("/user/changePwd") // 비번변경
	public int setPassword(@RequestBody HashMap<String, String> map) {/////// ★★★
		return empolyServiceImpl.setPassword(map.get("password"), map.get("password2"));
	} // http://localhost:8080/user/changePwd

	@PutMapping("/user/changeEmail") // 메일변경
	public int setEmail(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.emailChange(e.getEmail());
	} // http://localhost:8080/user/changeEmail

	@PutMapping("/user/changePhone") // 폰번호변경
	public int setPhone(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.setPhone(e.getPhoneNumber());
	} // http://localhost:8080/user/changePhone

	@GetMapping("/user/me") // 메일, id, unit 반환 해줌
	public ResponseEntity<EmployeeResponseDTO> getMyMemberInfo() {
		EmployeeResponseDTO myInfoBySecurity = empolyServiceImpl.getMyInfoBySecurity();
		return ResponseEntity.ok((myInfoBySecurity));
	} // http://localhost:8080/user/me
		///////////////////////////////// 메니저 권한 ////////////////////////////////////
		// 내가 팀장이면 내 팀원

	///////////////////////////////// 어드민 권한 ////////////////////////////////////
	@PutMapping("/admin/role") // 권한변경
//    public int setRol(@RequestParam Authority authority,@RequestParam String empId) {
	public int setRol(@RequestBody EmployeeDTO e) {
		System.out.println("e.getAuthority() = " + e.getAuthority());
		return empolyServiceImpl.changeRole(e.getAuthority(), e.getEmpId());
	} // http://localhost:8080/admin/role
//    {
//        "empId":"kyj",
//        "authority":"ROLE_ADMIN"
//    }

	@GetMapping("/admin/findAll") // 직원전체조회
	public List<EmployeeDTO> findAll() {
		return empolyServiceImpl.findAll();
	} // http://localhost:8080/admin/findAll

	@GetMapping("/admin/ResignationAll") // 퇴사자 전체 조회
	public List<Resignation> ResignationAll() {
		return empolyServiceImpl.ResignationAll();
	} // http://localhost:8080/admin/ResignationAll

	@PutMapping("/admin/changeData/{number}") // 1휴가 , 2메일 , 3아이디, 4입사일, 5이름 , 6핸드폰 번호 변경
	public int changeData(@PathVariable(name = "number") int number, @RequestBody HashMap<Object, String> map) {
		return empolyServiceImpl.changeData(number, map.get("empNum"), map.get("data"));
	} // http://localhost:8080/admin/changeData/1
//    {
//        "empNum":"25",
//        "data":"10"
//    }   

	@PutMapping("/admin/changeManager") // 메니저 변경
	public int changeManager(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.changeManager(e.getEmpNum(), e.getUnit());
	}// http://localhost:8080/admin/changeManager
//    {
//        "empNum":"26",
//        "unit":{
//               "unitId" : "3"
//           }
//    }

	@PutMapping("/admin/changUnitId") // Unit_id 변경 메소드
	public int changUnitId(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.changUnitId(e.getEmpNum(), e.getUnit());
	}// http://localhost:8080/admin/changUnitId

	@PutMapping("/admin/changejobtitle")
	public void updateJobTitle(@RequestBody EmployeeDTO e) {
		// 넘겨 받아야하는 것 : 사원 ID, 직책
		// repo에 접근을 하여 employee에 대한 정보 받아와야함,
		// password와 다른 정보들은 front에 없기 때문에 null 처리가 안되기 위해선 db에 접근해야함.
		empolyServiceImpl.updateJobTitle(e);
		
	}
	
	@DeleteMapping("/admin/delete") // 아이디 삭제
	public int delete(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.adminDelete(e.getEmpNum());
	} // http://localhost:8080/admin/delete?empNum=14
//    {
//        "empNum":"22"
//    }   
	
	@PutMapping("/admin/emp/{id}")
	public void updateEmp(@PathVariable("id") Long id,@RequestBody EmployeeDTO emp) {
		emp.setEmpNum(id);
		System.out.println(emp);
		empolyServiceImpl.updateAllEmp(emp);
	}
}
