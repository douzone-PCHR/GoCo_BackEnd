package com.pchr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pchr.dto.CommuteDTO;
import com.pchr.entity.Commute;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Long>{
	
	
	public Optional<CommuteDTO> findByCommuteId(Long commuteId);
	
	
	
}
