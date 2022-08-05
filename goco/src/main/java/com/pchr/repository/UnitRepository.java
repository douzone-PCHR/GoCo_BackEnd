package com.pchr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.dto.UnitDTO;
import com.pchr.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

	List<Unit> findAllByParentUnitUnitId(Long id);

	boolean existsByUnitName(String unitName);

	Unit findByUnitName(String unitName);

}
