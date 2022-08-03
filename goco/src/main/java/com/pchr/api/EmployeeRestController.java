package com.pchr.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchr.dto.ChangePasswordRequestDTO;
import com.pchr.dto.EmployeeResponseDTO;
import com.pchr.repository.EmployeeRepository;
import com.pchr.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class EmployeeRestController {
	
    private final MemberService memberService;
    private final EmployeeRepository emprepo;
    
	@GetMapping(value = "/test")
	public String test() {
		System.out.println("ㅇㅇㅇ");
		return "ok";
	}
	
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDTO> getMyMemberInfo() {
        EmployeeResponseDTO myInfoBySecurity = memberService.getMyInfoBySecurity();
        return ResponseEntity.ok((myInfoBySecurity));
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }


    @PostMapping("/password")
    public ResponseEntity<EmployeeResponseDTO> setMemberPassword(@RequestBody ChangePasswordRequestDTO request) {
        return ResponseEntity.ok(memberService.changeMemberPassword(request.getExPassword(), request.getNewPassword()));
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	System.out.println(id);
    	emprepo.deleteById(id);
    }
    

}
