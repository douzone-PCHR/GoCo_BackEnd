package com.pchr.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Resignation;

@Repository
public interface ResignationRepository extends JpaRepository<Resignation, Long>{
	public int deleteByEmpNum(Long empNum);
}
