package com.pchr.service;
import com.pchr.dto.EmployeeResponseDTO;


public interface MemberService {


//getMyInfoBySecurity는 헤더에 있는 token값을 토대로 Member의 data를 건내주는 메소드다
    public EmployeeResponseDTO getMyInfoBySecurity();
//changeMemberPassword는 패스워드 변경이다. 패스워드 변경 또한 token값을 토대로 찾아낸 member를 찾아낸 다음 제시된 예전 패스워드와 DB를 비교한다.
    public EmployeeResponseDTO changeMemberPassword(String exPassword, String newPassword);
}