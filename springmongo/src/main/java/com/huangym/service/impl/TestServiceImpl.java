package com.huangym.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huangym.dao.TestDao;
import com.huangym.dto.TestDTO;
import com.huangym.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDao testDao;
	
	@Override
	public boolean addTest() {
		TestDTO dto = new TestDTO();
		dto.setId("xxxxxx");
		dto.setName("xxx-name");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -29);
		dto.setBirthday(c.getTime());
		dto.setAge(29);
		dto.setAddress("广州市黄埔区大沙地");
		return testDao.add(dto);
	}

	@Override
	public boolean updateTest() {
		TestDTO dto = new TestDTO();
		dto.setId("xxxxxx");
		dto.setName("xxx-name");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -29);
		dto.setBirthday(c.getTime());
		dto.setAge(29);
		dto.setAddress("广州市黄埔区大沙地");
		return testDao.update(dto);
	}

	@Override
	public boolean updateSetTest() {
		TestDTO dto = new TestDTO();
		dto.setId("xxxxxx");
		dto.setName("xxx-name");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -29);
		dto.setBirthday(c.getTime());
		dto.setAge(29);
		dto.setAddress("广州市黄埔区大沙地");
		return testDao.updateSet(dto);
	}

}
