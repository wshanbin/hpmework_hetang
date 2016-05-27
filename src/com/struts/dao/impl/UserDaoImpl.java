package com.struts.dao.impl;

import java.util.List;

import com.struts.dao.UserDao;
import com.struts2.po.User;

import cn.edu.sdu.framework.dao.GenericDao;
import cn.edu.sdu.framework.dao.GenericHibernateDao;

public class UserDaoImpl extends GenericHibernateDao<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	@Override
	public List<User> getUsers() {
		String sql = "from User where 1=1";
		List list = this.queryForList(sql, null);
		if (list != null)
			return list;
		return null;
	}

	@Override
	public List<User> getUserById(Integer id) {
		String sql = "from User where 1=1 and id = " + id;
		List list = this.queryForList(sql, null);
		if (list != null)
			return list;
		return null;
	}

}