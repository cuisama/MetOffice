package com.weather.framework.util;

import java.util.List;

import com.weather.xm.user.dao.IUserDao;
import com.weather.xm.user.dao.UserDaoImpl;
import com.weather.xm.user.model.User;

public class Assignee {
	
	public static List<User> get(String workFlowName, String positionId) throws Exception{
		String condition ="";
		switch (workFlowName) {
		case "LeaveBill":
			condition = positionId.substring(0, positionId.length()-2);
			break;

		default:
			break;
		}
		IUserDao dao = new UserDaoImpl();
		List<User> list = dao.listUser(condition);
		
		//return "zhangsan,lisi,zhaoda";
		return list;
	}
}
