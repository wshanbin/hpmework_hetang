package com.struts.userAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ModelDriven;
import com.struts.dao.UserDao;
import com.struts.dao.impl.UserDaoImpl;
import com.struts2.po.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UserAction implements ModelDriven<User> {
	private UserDao userDao;
	private User user;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getModel() {
		return user;
	}

	public void submit() {
		HttpServletRequest httpServletRequest = ServletActionContext
				.getRequest();
		String flag = httpServletRequest.getParameter("flag");
		String id = httpServletRequest.getParameter("id");
		if (id == null || id.equals("")) {
			return;
		}
		String name = httpServletRequest.getParameter("name");
		String userType = httpServletRequest.getParameter("userType");
		String createTime = httpServletRequest.getParameter("createTime");
		String state = httpServletRequest.getParameter("state");
		String lastLoginTime = httpServletRequest.getParameter("lastLoginTime");
		String remark = httpServletRequest.getParameter("remark");
		String message = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (flag != null && flag.equals("submit")) {
			List poList = userDao.getUserById(new Integer(id));
			User po = (User) poList.get(0);
			po.setName(name);
			po.setRemark(remark);
			po.setState(state);
			po.setUserType(userType);
			Date date = null;
			try {
				date = sdf.parse(lastLoginTime);
				po.setLastLoginTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			po.setLastLoginTime(date);
			try {
				date = sdf.parse(createTime);
				po.setCreateTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userDao.update(po);
			message += "修改成功！";
		} else {
			message += "修改失败！";
		}
		JSONObject o = new JSONObject();
		o.put("message", message);
		o.put("flag", "2");
		printWriterInfo(o);
	}

	public void edit() {
		HttpServletRequest httpServletRequest = ServletActionContext
				.getRequest();
		String flag = httpServletRequest.getParameter("flag");
		String id = httpServletRequest.getParameter("id");
		JSONObject o = new JSONObject();
		if (flag != null && flag.equals("preEdit")) {
			if (id != null && !id.equals("")) {
				List userList = userDao.getUserById(new Integer(id));
				if (userList != null && userList.size() > 0) {
					User po = (User) userList.get(0);

					o.put("id", po.getId().toString());
					o.put("name", po.getName());
					o.put("lastLoginTime", po.getLastLoginTime().toString());
					o.put("remark", po.getRemark());
					o.put("state", po.getState());
					o.put("userType", po.getUserType());
					o.put("createTime", po.getCreateTime().toString());
					o.put("flag", "edit");
				}
			}
		} else {
			o.put("flag", "2");
		}
		printWriterInfo(o);
	}

	public void add() {
		HttpServletRequest httpServletRequest = ServletActionContext
				.getRequest();
		String flag = httpServletRequest.getParameter("flag");
		String name = httpServletRequest.getParameter("name");
		String userType = httpServletRequest.getParameter("userType");
		String createTime = httpServletRequest.getParameter("createTime");
		String state = httpServletRequest.getParameter("state");
		String lastLoginTime = httpServletRequest.getParameter("lastLoginTime");
		String remark = httpServletRequest.getParameter("remark");
		String message = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (flag != null && flag.equals("add")) {
			User po = new User();
			po.setName(name);
			po.setRemark(remark);
			po.setState(state);
			po.setUserType(userType);

			Date date = null;
			try {
				date = sdf.parse(lastLoginTime);
				po.setLastLoginTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			po.setLastLoginTime(date);
			try {
				date = sdf.parse(createTime);
				po.setCreateTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userDao.create(po);
			message += "添加成功！";
		} else {
			message += "添加失败！";
		}
		JSONObject o = new JSONObject();
		o.put("message", message);
		o.put("flag", "2");
		printWriterInfo(o);

	}

	public void delete() {
		HttpServletRequest httpServletRequest = ServletActionContext
				.getRequest();
		String id = httpServletRequest.getParameter("id");
		String mess = "";
		String flag = httpServletRequest.getParameter("flag");
		if (flag != null && flag.equals("1")) {

			if (id != null && !id.equals("")) {
				System.out.println(id);
				List userList = userDao.getUserById(Integer.parseInt(id));
				User user = (User) userList.get(0);
				userDao.delete(user);
				mess += "删除成功！";
			} else {
				mess += "删除失败！";
			}
			JSONObject o = new JSONObject();
			o.put("message", mess);
			o.put("flag", "2");
			printWriterInfo(o);

		}
	}

	public void list() {
		System.out.println("1");
		List list = userDao.getUsers();
		JSONArray array = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {

				User po = (User) list.get(i);

				JSONObject o = new JSONObject();
				o.put("id", po.getId());
				o.put("name", po.getName());
				String data = po.getCreateTime().toString();
				String str[] = data.split(" ");
				o.put("createTime", str[0]);
				data  = po.getLastLoginTime().toString();
				str = data.split(" ");
				o.put("lastLoginName",str[0]);
				o.put("remark", po.getRemark());
				o.put("state", po.getState());
				o.put("userType", po.getUserType());
				array.add(o);
			}
		}
		// setAllEmployee(array);
		JSONObject ob = new JSONObject();
		ob.put("array", array);
		System.out.println(ob.toString());
		printWriterInfo(ob);
	}

	private void printWriterInfo(JSONObject o) {
		try {
			HttpServletResponse httpResponse = ServletActionContext
					.getResponse();
			httpResponse.setContentType("application/json; charset=UTF-8");
			httpResponse.setCharacterEncoding("UTF-8");
			PrintWriter out = httpResponse.getWriter();
			out.print(o.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
