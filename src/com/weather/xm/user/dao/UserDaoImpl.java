package com.weather.xm.user.dao;

import java.util.List;

import com.weather.framework.dao.DaoImpl;
import com.weather.xm.user.model.User;

public class UserDaoImpl extends DaoImpl implements IUserDao{

	@Override
	public User getUser(String username) throws Exception {
		return (User) super.queryEntity("User.getUser", username);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUser(String positionId) throws Exception {
		return super.queryList("User.listUser", positionId);
	}

	@Override
	public int countUser() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteUser(String username) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
