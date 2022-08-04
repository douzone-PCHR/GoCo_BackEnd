package com.pchr.dto;


import com.pchr.entity.TeamPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamPositionDTO {
	
	private Long teamPositionId;
	
	private String teampPositionName;
	public TeamPosition toEntity(TeamPositionDTO teamPositionDTO) {
		TeamPosition teamPosition=TeamPosition.builder()
										.teamPositionId(teamPositionDTO.getTeamPositionId())
										.build();
		return teamPosition;
	}
}
