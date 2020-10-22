package com.huangym.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huangym.dao.TextSearchDao;
import com.huangym.dto.IssueDTO;
import com.huangym.service.TextSearchService;

@Service("textSearchService")
public class TextSearchServiceImpl implements TextSearchService {

	@Autowired
	private TextSearchDao dao;
	
	@Override
	public void addIndex() {
		dao.addTextIndex();
	}

	@Override
	public boolean addContent() {
		String title = "文本搜索标题";
		String[] contentList = {"小鸡", "小鸭", "小猪", "小鹅", "小鸟", "小牛", "小马", "小羊"};
		for (int i = 0; i < 20; i++) {
			IssueDTO dto = new IssueDTO();
			dto.setTitle(title + i);
			Random r = new Random(System.currentTimeMillis());
			int count = r.nextInt(5) + 1;
			String content = "";
			for (int j = 0; j < count; j++) {
				content = content + ", " + contentList[r.nextInt(8)];
			}
			dto.setContent(content);
			dto.setCreateTime(new Date());
			dao.addContent(dto);
		}
		return true;
	}

	@Override
	public List<IssueDTO> search(String keyword) {
		return dao.search(keyword);
	}

}
