package com.pchr.repository;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchr.entity.TokenData;

@Repository
public interface TokenDataRepository extends JpaRepository<TokenData, Long>{
	int deleteByAccessToken(String accessToken);
	int deleteByRefreshToken(String refreshToken);
	int deleteByEmpId(String empId);
	Optional<TokenData> findByRefreshToken(String refreshToken);
	
	
}
