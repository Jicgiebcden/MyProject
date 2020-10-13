package com.huangym.dao;

import org.springframework.stereotype.Repository;

import com.huangym.dao.base.MongoDAO;
import com.huangym.dto.TestDTO;

@Repository("testDao")
public class TestDao extends MongoDAO {

	private static final String COLLECTION_NAME = "test";
	
	private static final String ID = "id";
	
	public boolean add(TestDTO dto) {
		return save(TestDTO.class, COLLECTION_NAME, dto);
	}
	
	public boolean update(TestDTO dto) {
		return updateVO(COLLECTION_NAME, ID, dto);
	}
	
	public boolean updateSet(TestDTO dto) {
		return update(TestDTO.class, COLLECTION_NAME, dto);
	}
}
