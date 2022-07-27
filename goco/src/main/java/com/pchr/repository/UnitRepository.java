package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{

}
