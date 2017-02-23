package com.weather.framework.dao;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface Dao {

	public List queryList(String sqlId) throws Exception;

	public List queryList(String sqlId, Object paras) throws Exception;

	public Object queryEntity(String sqlId, String unique) throws Exception;

	public Object addEntity(String sqlId, Object paras) throws Exception;

	public int updateEntity(String sqlId, Object paras) throws Exception;

	public int deleteEntity(String sqlId, Object paras) throws Exception;

	public int getCount(String sqlId) throws Exception;
	
	public int getCount(String sqlId, Object paras) throws Exception;
}