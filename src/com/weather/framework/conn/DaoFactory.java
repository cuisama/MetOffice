package com.weather.framework.conn;

import org.apache.ibatis.session.SqlSessionFactory;

public class DaoFactory {

	private DaoFactory(){}
	
	public static SqlSessionFactory getInstance(){
		return DaoManager.getSqlSessionFactory();
	}

}
