package com.pchr.dto;

import com.pchr.entity.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

	private Long fileId;

	private String filePath;

	private String fileName;

	private String originalName;

//	private VacationDTO vacationDTO;
//
//	private BusinessTripDTO businessTripDTO;

	// toEntity
	public File toFileEntity(FileDTO fileDTO) {
		File fileEntity = File.builder().fileId(fileDTO.getFileId()).fileName(fileDTO.getFileName())
				.filePath(fileDTO.getFilePath()).originalName(fileDTO.getOriginalName()).build();
		return fileEntity;

	}
}
