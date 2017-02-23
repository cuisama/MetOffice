package com.weather.framework.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.weather.framework.conn.DaoFactory;

@SuppressWarnings("rawtypes")
public class DaoImpl implements Dao {

	@Override
	public List queryList(String sqlId) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){	
			return sqlSession.selectList(sqlId);
		}
	}

	@Override
	public List queryList(String sqlId, Object paras) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			List result = sqlSession.selectList(sqlId,paras);
			return result;			
		}
	}

	@Override
	public Object queryEntity(String sqlId, String unique) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			return sqlSession.selectOne(sqlId, unique);			
		}
	}

	@Override
	public Object addEntity(String sqlId, Object paras) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			int result = sqlSession.insert(sqlId,paras);
			sqlSession.commit();
			return result;			
		}
	}

	@Override
	public int updateEntity(String sqlId, Object paras) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			int result = sqlSession.update(sqlId,paras);
			sqlSession.commit();
			return result;	
		}
	}

	@Override
	public int deleteEntity(String sqlId, Object paras) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			int result = sqlSession.delete(sqlId,paras);
			sqlSession.commit();
			return result;	
		}
	}

	@Override
	public int getCount(String sqlId) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			return sqlSession.selectOne(sqlId);
		}
	}

	@Override
	public int getCount(String sqlId, Object paras) throws Exception {
		try(SqlSession sqlSession = DaoFactory.getInstance().openSession()){
			return sqlSession.selectOne(sqlId,paras);
		}
	}
}
