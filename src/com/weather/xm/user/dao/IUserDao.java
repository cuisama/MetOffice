package com.weather.xm.user.dao;

import java.util.List;

import com.weather.xm.user.model.User;

public interface IUserDao {
	
	public User getUser(String username) throws Exception;
	public List<User> listUser(String positionId) throws Exception;
	public int countUser() throws Exception;
	public int insertUser(User user) throws Exception;
	public int updateUser(User user) throws Exception;
	public int deleteUser(String username) throws Exception;

}
