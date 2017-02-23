package com.weather.xm.newsbill.dao;

import java.util.List;

import com.weather.xm.newsbill.model.NewsBill;

public interface INewsBillDao {

	public List<NewsBill> findNewsBillList() throws Exception;

	public void saveNewsBill(NewsBill NewsBill);

	public NewsBill findNewsBillById(Long id);

	public void updateNewsBill(NewsBill NewsBill);

	public void deleteNewsBillById(Long id);


}
