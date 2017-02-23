package com.weather.framework.conn;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.weather.framework.util.StaticParameter;

public class DaoManager {
	private static Log logger = LogFactory.getLog(DaoManager.class);
	private static SqlSessionFactory sqlSessionFactory = null;
	private DaoManager(){}

	private static void init() {
		try {
			// 加载mybatis的配置文件（它也加载关联的映射文件）
			InputStream is =Resources.getResourceAsStream(StaticParameter.sqlConfMappingFile);
			// 构建sqlSession的工厂
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
			is.close();
		} catch (IOException e) {
			logger.info("");
		}
	}
	
	public static SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			init();
		}
		return sqlSessionFactory;
	}
}
