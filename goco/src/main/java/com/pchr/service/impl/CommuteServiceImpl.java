package com.pchr.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pchr.config.SecurityUtil;
import com.pchr.dto.CommuteDTO;
import com.pchr.dto.VacationAndBusinessVO;
import com.pchr.entity.Commute;
import com.pchr.repository.CommuteRepository;
import com.pchr.response.Message;
import com.pchr.response.StatusEnum;
import com.pchr.service.CommuteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommuteServiceImpl implements CommuteService {

	private final CommuteRepository commuteRepository;

	@Transactional(readOnly = true)
	@Override
	public List<CommuteDTO> findAll() {
		List<CommuteDTO> result = commuteRepository
				.findAllCommute(SecurityUtil.getCurrentMemberId(),
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth(), 0, 0),
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth() + 1, 0, 0))
				.stream().map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());
		
		return result;
	}

// 우리팀원 - 팀원에 대한 오늘 일자중 출근한 사람들 다 가져옴.
	@Transactional(readOnly = true)
	public List<CommuteDTO> findById(Long empnum) {
		LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
				LocalDateTime.now().getDayOfMonth(), 0, 0);
		LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
				LocalDateTime.now().getDayOfMonth() , 23, 59);
		List<CommuteDTO> findAllByEmployeeEmpNum = commuteRepository.findAllByEmployeeEmpNum(empnum).stream()
				.filter(commute -> (commute.getClockIn().isAfter(startDate) && commute.getClockOut().isBefore(endDate))
						|| (commute.getClockIn().isEqual(startDate) && commute.getClockOut().isEqual(startDate)))
				.map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());

		return findAllByEmployeeEmpNum;
	}

	public ResponseEntity<Message> updateCommute(CommuteDTO commuteDTO) {
//		0 미출근
//		1 지각
//		2 출근
//		3 휴가
//		4 출장
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
     
        
		Commute commuteEntity = commuteDTO.toUpdateCommute(commuteDTO);
		List<CommuteDTO> findCommute = findById(commuteEntity.getEmployee().getEmpNum());
		int ClockInhour = LocalDateTime.now().getHour();
		int ClockOuthour = LocalDateTime.now().getHour();
		commuteDTO.setCommuteId(findCommute.get(0).getCommuteId());
		commuteDTO.setEmployee(findCommute.get(0).getEmployee());
		if(commuteDTO.getClockIn() != null && findCommute.get(0).getCommuteCheck() != 2 && findCommute.get(0).getClockIn().getHour() == 0 ) {
			commuteDTO.setCommuteCheck(2); // 출근 버튼 한번더 누르는거 방지 
			commuteDTO.setClockOut(findCommute.get(0).getClockOut());
			commuteDTO.setClockIn(LocalDateTime.now());
			commuteDTO.setCommuteStatus("2"); // 정상 출근 
			
			message.setStatus(StatusEnum.OK);
	        message.setMessage("정상 출근 처리 되었습니다.");
			if(ClockInhour >= 9 ) {
				commuteDTO.setCommuteStatus("1"); // 지각
				message.setMessage("지각 처리 되었습니다.");
			}
			Commute entity = commuteDTO.toUpdateCommute(commuteDTO);
			commuteRepository.save(entity);
			
		
		}else if(commuteDTO.getClockOut() != null && findCommute.get(0).getCommuteCheck() != 1 && findCommute.get(0).getClockOut().getHour() == 0) {
			if(findCommute.get(0).getCommuteStatus().equals("1") || findCommute.get(0).getCommuteStatus().equals("2")) {
				commuteDTO.setCommuteCheck(1); // 퇴근 버튼 한번더 누르는거 방지 
				commuteDTO.setClockIn(findCommute.get(0).getClockIn());
				commuteDTO.setClockOut(LocalDateTime.now());
				commuteDTO.setCommuteStatus("5");
				message.setMessage("퇴근 처리 되었습니다.");
				message.setStatus(StatusEnum.OK);
				
				Commute entity = commuteDTO.toUpdateCommute(commuteDTO);
				commuteRepository.save(entity);
			}else {
				message.setMessage("출근 후 퇴근 버튼을 클릭 할 수 있습니다.");
				message.setStatus(StatusEnum.OK);
			}
			
		}else if(commuteDTO.getClockIn() != null && (findCommute.get(0).getCommuteCheck() == 1 || findCommute.get(0).getCommuteCheck() == 2) && findCommute.get(0).getClockIn().getHour() != 0){
			message.setMessage("오늘자 출근은 처리가 되었습니다.");
			message.setStatus(StatusEnum.ALREADY_DONE);
			
		}else if(commuteDTO.getClockOut() != null && (findCommute.get(0).getCommuteCheck() == 1 || findCommute.get(0).getCommuteCheck() == 2) && findCommute.get(0).getClockOut().getHour() != 0){
			message.setMessage("오늘자 퇴근은 처리가 되었습니다.");
			message.setStatus(StatusEnum.ALREADY_DONE);
			
		}
		
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	@Transactional()
	public void deleteCommute(CommuteDTO commuteDTO) {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
				LocalDateTime.now().getDayOfMonth(), 0, 0, 0);

//		if (commuteDTO.getCheck() == 1) { // 출근쪽 삭제
//			commuteDTO.setClockIn(localDateTime);
//		} else if (commuteDTO.getCheck() == 2) { // 퇴근쪽 삭제
//			commuteDTO.setClockOut(localDateTime);
//		}
		Commute entity = commuteDTO.toUpdateCommute(commuteDTO);
		commuteRepository.save(entity);

	}

	@Transactional(readOnly = true)
	@Override
	public VacationAndBusinessVO findWorkTime() {

		Map<String, Object> map = commuteRepository.findAllCommuteTime(SecurityUtil.getCurrentMemberId());
		VacationAndBusinessVO vo = VacationAndBusinessVO.builder().startDate((Date) map.get("start_date"))
				.endDate((Date) map.get("end_date"))
				.commute_work_hour((BigDecimal) map.get("commute_work_hour"))
				.commute_work_min((BigDecimal)	map.get("commute_work_min")) 
				.vacation_count((Float) map.get("vacation_count")).build();
		return vo;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Map<String, Object>> findAllCommuteAndVacationAndBusiness() {
		List<Map<String, Object>> list = commuteRepository
				.findAllCommuteAndVacationAndBusiness(SecurityUtil.getCurrentMemberId());
		return list;
	}

	@Override
	public List<CommuteDTO> findAllCommuteAdmin() {
		List<CommuteDTO> result = commuteRepository
				.findAllCommuteAdmin(
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth(), 0, 0),
						LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
								LocalDateTime.now().getDayOfMonth() + 1, 0, 0))
				.stream().map(commute -> commute.toCommuteDto(commute)).collect(Collectors.toList());
		return result;
	}

	@Transactional(readOnly = true)
	public List<VacationAndBusinessVO> findAllMyTeamWorkTime() {

		List<Map<String, Object>> findList = commuteRepository.findAllMyTeamWorkTime(SecurityUtil.getCurrentMemberId());
		VacationAndBusinessVO vo = new VacationAndBusinessVO();
		List<VacationAndBusinessVO> list = vo.entityVo(findList);
		return list;
	}

	public List<CommuteDTO> findMenuCommuteStatus() {
		List<CommuteDTO> list = null;
		if(!"anonymousUser".equals(SecurityUtil.getCurrentMemberId())) {
			list = commuteRepository.findMenuCommuteStatus(SecurityUtil.getCurrentMemberId())
					.stream().map(commute -> commute.toCommuteDto(commute))
					.collect(Collectors.toList());
		}
		return list;
		
	}

	public void deleteCommuteByEmpNum(Long empNum) {
		commuteRepository.deleteByEmpNum(empNum);
	}

}
