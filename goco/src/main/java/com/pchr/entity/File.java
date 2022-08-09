package com.pchr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.pchr.dto.FileDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long fileId;

	@Column(name = "file_path", columnDefinition = "TEXT", nullable = false)
	private String filePath;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "original_name", nullable = false)
	private String originalName;

	@OneToOne(mappedBy = "file")
	private Vacation vacation;

	@OneToOne(mappedBy = "file")
	private BusinessTrip businessTrip;

	// toDTO
	public FileDTO toFileDTO(File fileEntity) {
		FileDTO fileDTO = FileDTO.builder().fileId(fileEntity.getFileId()).fileName(fileEntity.getFileName())
				.filePath(fileEntity.getFilePath()).originalName(fileEntity.getOriginalName()).build();
		return fileDTO;

	}

}
