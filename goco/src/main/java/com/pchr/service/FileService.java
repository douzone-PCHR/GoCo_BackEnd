package com.pchr.service;

import com.pchr.dto.FileDTO;

public interface FileService {

	// 파일 DB 추가
	public FileDTO insertFile(FileDTO fileDTO);

	// 파일 DB 검색
	public FileDTO findFile(Long fileId);

	// 파일 DB 수정
	public void updateFile(Long fileId, String originalName);

	// 파일 DB 삭제
	public void deleteFile(Long fileId);

}
