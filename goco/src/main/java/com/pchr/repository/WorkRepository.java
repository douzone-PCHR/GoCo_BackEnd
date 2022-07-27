package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long>{

}
