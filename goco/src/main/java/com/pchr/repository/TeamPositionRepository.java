package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.TeamPosition;

@Repository
public interface TeamPositionRepository extends JpaRepository<TeamPosition, Long>{

}
