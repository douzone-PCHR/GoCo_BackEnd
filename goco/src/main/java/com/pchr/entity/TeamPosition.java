package com.pchr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.pchr.dto.TeamPositionDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamPosition {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_position_id")
	private Long teamPositionId;
	
	@Column(name = "team_position_name")
	private String teampPositionName;
	
	public TeamPositionDTO toDTO(TeamPosition teamPosition) {
		TeamPositionDTO teamPositionDTO=TeamPositionDTO.builder()
										.teamPositionId(teamPosition.getTeamPositionId())
										.teampPositionName(teamPosition.getTeampPositionName())
										.build();
		return teamPositionDTO;
	}
}
