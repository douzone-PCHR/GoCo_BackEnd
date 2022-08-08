package com.pchr.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.EmployeeDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.dto.UnitDTO;
import com.pchr.entity.Authority;
import com.pchr.entity.Employee;
import com.pchr.entity.Resignation;
import com.pchr.service.impl.EmpolyServiceImpl;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
public class EmpRestController {
	 private final EmpolyServiceImpl empolyServiceImpl;
    ///////////////////////////////////유저 권한 이하 회원 정보 수정 //////////////////////////////
    @DeleteMapping("/user/delete") // 아이디 삭제
    public int delete() {
    	return empolyServiceImpl.delete();
    }    // http://localhost:8080/user/delete 

    @PutMapping("/user/changePwd")//비번변경
    public int setPassword(@RequestParam String password) {
      return empolyServiceImpl.setPassword(password);
    } //http://localhost:8080/user/changePwd?password=1234
    
    @PutMapping("/user/changeEmail")//메일변경
    public int setEmail(@RequestParam String email) {
      return empolyServiceImpl.emailChange(email);
    } //http://localhost:8080/user/changeEmail?email=kyjdummy@gmail.com   
    
    @PutMapping("/user/changePhone")//폰번호변경
    public int setPhone(@RequestParam String phoneNumber) {
      return empolyServiceImpl.setPhone(phoneNumber);
    } //http://localhost:8080/user/changePhone?phoneNumber=kyjdummy@gmail.com   
    
    @GetMapping("/user/me") //  메일, id, unit 반환 해줌
    public ResponseEntity<EmployeeResponseDTO> getMyMemberInfo() {
    	EmployeeResponseDTO myInfoBySecurity = empolyServiceImpl.getMyInfoBySecurity();
        return ResponseEntity.ok((myInfoBySecurity));
    } // http://localhost:8080/user/me
    ///////////////////////////////// 메니저 권한 ////////////////////////////////////   
    // 내가 팀장이면 내 팀원
   
    ///////////////////////////////// 어드민 권한 ////////////////////////////////////
    @PutMapping("/admin/role")//권한변경
    public int setRol(@RequestParam Authority authority,@RequestParam String empId) {
      return empolyServiceImpl.changeRole(authority,empId);
    } //http://localhost:8080/admin/role?authority=ROLE_MANAGER&empId=kyj
    
    @GetMapping("/admin/findAll")//직원전체조회
    public List<EmployeeDTO> findAll() {
        return empolyServiceImpl.findAll();
    } //http://localhost:8080/admin/findAll
    
    @GetMapping("/admin/ResignationAll") // 퇴사자 전체 조회
    public List<Resignation> ResignationAll() {
        return empolyServiceImpl.ResignationAll();
    }    //http://localhost:8080/admin/ResignationAll
    
    @PutMapping("/admin/changeData")//1휴가 , 2메일 , 3아이디, 4입사일, 5이름 , 6핸드폰 번호 변경
    public int changeVacation(@RequestParam int number,@RequestParam Long empNum,@RequestParam String data) {
      return empolyServiceImpl.changeVacation(number,empNum,data);
    } //http://localhost:8080/admin/changeData?number=1&empNum=14&data=10
    
    @DeleteMapping("/admin/delete") // 아이디 삭제
    public int delete(@RequestParam Long empNum) {
    	return empolyServiceImpl.adminDelete(empNum);
    }    // http://localhost:8080/admin/delete?empNum=14
    
}
