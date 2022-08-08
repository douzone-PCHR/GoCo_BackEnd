package com.pchr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.EmailAuth;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {
	public List<EmailAuth> findAll();
	EmailAuth findByAuthenticationNumber(String authenticationNumber);
	boolean existsByAuthenticationNumber(String authenticationNumber);
	public int deleteByEmail(String email);
	public int deleteByAuthenticationNumber(String authenticationNumber);
}
