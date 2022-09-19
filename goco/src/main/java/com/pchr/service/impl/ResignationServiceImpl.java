package com.pchr.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pchr.entity.Resignation;
import com.pchr.repository.ResignationRepository;
import com.pchr.service.ResignationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ResignationServiceImpl implements ResignationService {

	@Autowired
	private ResignationRepository resignationRepository;
	
	@Override
	public List<Resignation> findAll() {
		return resignationRepository.findAll();
	}
	@Override
	public void save(Resignation r) {
		resignationRepository.save(r);
	}
	@Override
	public void deleteByEmpNum(Long empNum) {
		resignationRepository.deleteByEmpNum(empNum);
	}	
	
	@Override
    @Scheduled(cron = "0 0 4 * * *")//매일 새벽 4시 실행
    public void deleteData() {
		Date base = java.sql.Timestamp.valueOf(LocalDateTime.now().minusYears(3));// 기준시간을 3년 전으로하여 db데이터 
    	resignationRepository.findAll().forEach(r->{
    		if(base.compareTo(r.getResignationDate())>0) {// 퇴사일이 3년 후일 때 데이터 삭제
    			deleteByEmpNum(r.getEmpNum());
    		}
    	});
    }

}
