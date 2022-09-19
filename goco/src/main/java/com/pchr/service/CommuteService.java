package com.pchr.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.pchr.dto.CommuteDTO;
import com.pchr.dto.VacationAndBusinessVO;
import com.pchr.dto.WorkDTO;
import com.pchr.dto.WorkTimeVO;
import com.pchr.entity.Commute;
import com.pchr.response.Message;

public interface CommuteService {
	
	public List<CommuteDTO> findAll();
	
	public ResponseEntity<Message> updateCommute(CommuteDTO commuteDTO);
	
	public void deleteCommute(CommuteDTO commuteDTO);
	
	public VacationAndBusinessVO findWorkTime();

	public List<CommuteDTO> findAllCommuteAdmin();
	
	public List<Map<String, Object>> findAllCommuteAndVacationAndBusiness();
	
	public List<CommuteDTO> findMenuCommuteStatus();
}
