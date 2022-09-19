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

	/**
	 * 회원탈퇴
	 * 
	 * @return int
	 */
	@DeleteMapping("/user/delete")
	public int delete() {
		return empolyServiceImpl.delete();
	} 

	/**
	 * 비번변경
	 * 
	 * @return int
	 */
	@PutMapping("/user/changepwd")
	public int setPassword(@RequestBody HashMap<String, String> map) {
		return empolyServiceImpl.setPassword(map.get("password"), map.get("password2"));
	}

	/**
	 * 메일변경
	 * 
	 * @return int
	 */
	@PutMapping("/user/changeemail")
	public int setEmail(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.emailChange(e.getEmail());
	}

	/**
	 * 번호변경
	 * 
	 * @return int
	 */
	@PutMapping("/user/changephone")
	public int setPhone(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.setPhone(e.getPhoneNumber());
	}

	/**
	 * userMe
	 * 
	 * @return ResponseEntity<EmployeeResponseDTO>
	 */
	@GetMapping("/user/me") 
	public ResponseEntity<EmployeeResponseDTO> getMyMemberInfo() {
		EmployeeResponseDTO myInfoBySecurity = empolyServiceImpl.getMyInfoBySecurity();
		return ResponseEntity.ok((myInfoBySecurity));
	}

	///////////////////////////////// 어드민 권한 ////////////////////////////////////
	/**
	 * 권한변경
	 * 
	 * @return int
	 */
	@PutMapping("/admin/role") // 권한변경
	public int setRol(@RequestBody EmployeeDTO e) {
		System.out.println("e.getAuthority() = " + e.getAuthority());
		return empolyServiceImpl.changeRole(e.getAuthority(), e.getEmpId());
	}


	@GetMapping("/admin/findmanager/{unitid}")
	public List<EmployeeDTO> findManager(@PathVariable("unitid") Long unitId){
		return empolyServiceImpl.findManager(unitId);
	}
	
	/**
	 * 직원전체조회
	 * 
	 * @return List<EmployeeDTO>
	 */
	@GetMapping("/admin/findall")
	public List<EmployeeDTO> findAll() {
		return empolyServiceImpl.findAll();
	} 

	/**
	 * 퇴사자 전체 조회
	 * 
	 * @return List<Resignation>
	 */
	@GetMapping("/admin/resignationall")
	public List<Resignation> ResignationAll() {
		return empolyServiceImpl.ResignationAll();
	}

	@PutMapping("/admin/changedata/{number}") // 1휴가 , 2메일 , 3아이디, 4입사일, 5이름 , 6핸드폰 번호 변경
	public int changeData(@PathVariable(name = "number") int number, @RequestBody HashMap<Object, String> map) {
		return empolyServiceImpl.changeData(number, map.get("empNum"), map.get("data"));
	} // http://localhost:8080/admin/changeData/1


	@PutMapping("/admin/changemanager") // 메니저 변경
	public int changeManager(@RequestBody EmployeeDTO e) {
		return empolyServiceImpl.changeManager(e.getEmpNum(), e.getUnit());
	}// http://localhost:8080/admin/changeManager

	@PutMapping("/admin/changunitid") // Unit_id 변경 메소드
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

	@DeleteMapping("/admin/delete/{id}") // 아이디 삭제
	public int delete(@PathVariable("id") Long id) {
		System.out.println(id);
		return empolyServiceImpl.adminDelete(id);
	} // http://localhost:8080/admin/delete?empNum=14
  

	@PutMapping("/admin/emp/jobtitle/{id}/{type}/{value}")
	public boolean updateEmpJobTitle(@PathVariable("id") Long id, @PathVariable("type") int type,
			@PathVariable("value") Long value) {
		return empolyServiceImpl.updateAdminEmp(id,type,value);
	}
}
