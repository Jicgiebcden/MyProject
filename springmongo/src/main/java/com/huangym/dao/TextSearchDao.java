package com.huangym.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.huangym.base.util.MongoUtil;
import com.huangym.dao.base.MongoDAO;
import com.huangym.dto.IssueDTO;

@Repository("textSearchDao")
public class TextSearchDao extends MongoDAO {

	private static final String COLLECTION_NAME = "text_test";
	
	// 启用文本索引
	// db.adminCommand({setParameter : 1, textSearchEnabled : true})
	
	// 创建文本索引
	public void addTextIndex() {
		String json = "{content : \"text\"}";
		mongoTemplate.getCollection(COLLECTION_NAME).ensureIndex(MongoUtil.toDbo(json));
	}
	
	public boolean addContent(IssueDTO issue) {
		return save(IssueDTO.class, COLLECTION_NAME, issue);
	}
	
	public List<IssueDTO> search(String keyword) {
		String json = "{$text : {$search : \"小鸡\"}";
		return find(IssueDTO.class, COLLECTION_NAME, null, MongoUtil.toDbo(json), null, null);
	}
}
