package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Commute;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Long>{

}
