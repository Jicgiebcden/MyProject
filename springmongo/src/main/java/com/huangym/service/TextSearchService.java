package com.huangym.service;

import java.util.List;

import com.huangym.dto.IssueDTO;

public interface TextSearchService {
	
	public void addIndex();

	public boolean addContent();
	
	public List<IssueDTO> search(String keyword);
}
