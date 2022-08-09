package com.pchr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pchr.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {

	// 파일 검색
	public File findByFileId(Long fileId);

}
