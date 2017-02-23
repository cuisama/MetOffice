package com.weather.xm.leavebill.dao;

import java.util.List;
import java.util.Map;

import com.weather.xm.leavebill.model.LeaveBill;

public interface ILeaveBillDao {

	List<LeaveBill> findLeaveBillList(String username) throws Exception;

	int saveLeaveBill(LeaveBill leaveBill) throws Exception;

	LeaveBill findLeaveBillById(Long id) throws Exception;

	int updateLeaveBill(LeaveBill leaveBill) throws Exception;

	int deleteLeaveBillById(Long id) throws Exception;

	void startAndSubmit(long billId, String username) throws Exception;

	void startProcess(long billId, String username) throws Exception;
	
	List<LeaveBill> listMyTask(String username) throws Exception;
	
	void completeMyTask(String username,String billId,Map<String,Object> variables) throws Exception;


}
