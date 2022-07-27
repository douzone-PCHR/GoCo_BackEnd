package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Vacation;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long>{

}
