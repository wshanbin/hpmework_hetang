package com.struts.dao;

import java.util.List;

import cn.edu.sdu.framework.dao.GenericDao;

import com.struts2.po.User;

public interface UserDao extends GenericDao<User> {
	// 查询
	public List<User> getUsers();
	
	public List<User> getUserById(Integer id);

}
