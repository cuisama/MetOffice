package com.weather.xm.newsbill.dao;

import java.util.List;

import com.weather.framework.dao.DaoImpl;
import com.weather.xm.newsbill.model.NewsBill;


public class NewsBillDaoImpl extends DaoImpl implements INewsBillDao{

	@Override
	public List<NewsBill> findNewsBillList() throws Exception {
		super.queryList("billdao.xml.query");
		return null;
	}

	@Override
	public void saveNewsBill(NewsBill NewsBill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NewsBill findNewsBillById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateNewsBill(NewsBill NewsBill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNewsBillById(Long id) {
		// TODO Auto-generated method stub
		
	}}
