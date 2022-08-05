package com.pchr.service;

import java.util.List;

import com.pchr.entity.Resignation;

public interface ResignationService {
	List<Resignation> findAll();
	public void save(Resignation r);
}
