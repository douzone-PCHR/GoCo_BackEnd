package com.pchr.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchr.dto.FileDTO;
import com.pchr.entity.File;
import com.pchr.repository.FileRepository;
import com.pchr.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileRepository fileRepository;

	// 파일 추가
	@Override
	@Transactional
	public FileDTO insertFile(FileDTO fileDTO) {
		if (findFile(fileDTO.getFileId()) == null) {
			File fileEntity = fileRepository.save(fileDTO.toFileEntity(fileDTO));
			return fileEntity == null ? null : fileEntity.toFileDTO(fileEntity);
		}
		return null;
	}

	// 파일 검색
	@Override
	@Transactional
	public FileDTO findFile(Long fileId) {
		File file = fileRepository.findByFileId(fileId);
		return file == null ? null : file.toFileDTO(file);
	}

	// 파일 수정
	@Override
	@Transactional
	public void updateFile(Long fileId, String originalName) {
		FileDTO fileDTO = findFile(fileId);
		fileDTO.setOriginalName(originalName);
		if (fileDTO != null) {
			fileRepository.save(fileDTO.toFileEntity(fileDTO));
		}

	}

	// 파일 삭제
	@Override
	@Transactional
	public void deleteFile(Long fileId) {
		fileRepository.deleteById(fileId);
	}
}
