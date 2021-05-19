package cn.richinfo.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.richinfo.core.mybatis.extend.dao.MybaitsDao;

@Repository
public class LogDao {
	
	@Resource
	private MybaitsDao mybaitsDao;
	
	
}