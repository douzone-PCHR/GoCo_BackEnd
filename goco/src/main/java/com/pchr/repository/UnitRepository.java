package com.pchr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{

	public List<Unit> findByparentUnit(Unit unitId);
	
}
