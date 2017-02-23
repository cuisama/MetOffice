package com.weather.xm.leavebill.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.aop.ThrowsAdvice;

import com.weather.framework.dao.DaoImpl;
import com.weather.framework.exception.DeviceException;
import com.weather.framework.util.Assignee;
import com.weather.xm.leavebill.model.LeaveBill;
import com.weather.xm.user.dao.IUserDao;
import com.weather.xm.user.dao.UserDaoImpl;
import com.weather.xm.user.model.User;

public class LeaveBillDaoImpl extends DaoImpl implements ILeaveBillDao {

	/**查询自己的请假单的信息
	 * @throws Exception */
	@SuppressWarnings("unchecked")
	@Override
	public List<LeaveBill> findLeaveBillList(String username) throws Exception {
		return super.queryList("LeaveBill.findLeaveBillList",username);
	}
	
	/**保存请假单
	 * @throws Exception */
	@Override
	public int saveLeaveBill(LeaveBill leaveBill) throws Exception {
		return (int) super.addEntity("LeaveBill.saveLeaveBill",leaveBill);
	}
	
	/**使用请假单ID，查询请假单的对象
	 * @throws Exception */
	@Override
	public LeaveBill findLeaveBillById(Long id) throws Exception {
		return (LeaveBill) super.queryEntity("LeaveBill.findLeaveBillById",id.toString());
	}
	
	/**更新请假单
	 * @throws Exception */
	@Override
	public int updateLeaveBill(LeaveBill leaveBill) throws Exception {
		return super.updateEntity("LeaveBill.updateLeaveBill", leaveBill);
	}
	
	/**使用请假单ID，删除请假单
	 * @throws Exception */
	@Override
	public int deleteLeaveBillById(Long id) throws Exception {
		return super.deleteEntity("LeaveBill.updateLeaveBill", id.toString());
	}

	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private RuntimeService runtimeService = processEngine.getRuntimeService();
	private TaskService taskService = processEngine.getTaskService();
	private FormService formService = processEngine.getFormService();
	private HistoryService historyService = processEngine.getHistoryService();
	

	/* (non-Javadoc)
	 * @see com.weather.xm.leavebill.dao.ILeaveBillDao#startAndSubmit(long, java.lang.String)
	 * 开始请假流程 并提交 (完成第一阶段)
	 */
	@Override
	public void startAndSubmit(long billId, String username) throws Exception {
		ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(billId);
		leaveBill.setState(1);
		leaveBillDao.updateLeaveBill(leaveBill);

		String key = leaveBill.getClass().getSimpleName();
		Map<String, Object> variables = new HashMap<>();
		variables.put("initiator", username);
		//格式：LeaveBill.id的形式（使用流程变量）
		String objId = key+"."+billId;
		variables.put("objId", objId);
		runtimeService.startProcessInstanceByKey(key,objId,variables);
		
		
		//ProcessInstance pis = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(objId).singleResult();
		String taskId = taskService.createTaskQuery().processInstanceBusinessKey(objId).singleResult().getId();
		variables = new HashMap<>();
		IUserDao userDao = new UserDaoImpl();
		String assignee = Assignee.get("LeaveBill",userDao.getUser(username).getPositionId()).get(0).getUsername();
		variables.put("assignee",assignee);
		variables.put("objId", objId);
		taskService.complete(taskId,variables);
	}
	
	/* (non-Javadoc)
	 * @see com.weather.xm.leavebill.dao.ILeaveBillDao#startProcess(long, java.lang.String)
	 * 开始流程
	 */
	@Override
	public void startProcess(long billId, String username) throws Exception {

		String businessKey = "LeaveBill."+billId;
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).singleResult();
		
		if(pi!=null){
			throw new DeviceException(444, "该流程已经被启动");
		}else{
			Map<String, Object> variables = new HashMap<>();
			variables.put("initiator", username);
			variables.put("objId", businessKey);
			runtimeService.startProcessInstanceByKey("LeaveBill",businessKey,variables);
		
		pi = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).singleResult();
			if(pi!=null){
				ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();
				LeaveBill leaveBill = leaveBillDao.findLeaveBillById(billId);
				leaveBill.setState(1);
				leaveBillDao.updateLeaveBill(leaveBill);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.weather.xm.leavebill.dao.ILeaveBillDao#listMyTask(java.lang.String)
	 * 查看用户的任务
	 */
	@Override
	public List<LeaveBill> listMyTask(String username) throws Exception{
		List<Task> tasks =  taskService.createTaskQuery()
						.taskAssignee(username)
						.list();
		List<LeaveBill> lists = new ArrayList<>();
		//ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();
		for(Task t:tasks){
			String billId = taskService.getVariable(t.getId(), "objId").toString().split("\\.")[1];
			lists.add(findLeaveBillById(Long.parseLong(billId)));
		}
		return lists;
	} 
	
	/* (non-Javadoc)
	 * @see com.weather.xm.leavebill.dao.ILeaveBillDao#completeMyTask(java.lang.String, java.util.Map)
	 * 用户完成自己指定id的任务
	 */
	@Override
	public void completeMyTask(String username,String billId, Map<String,Object> variables) throws Exception{
		String businessKey = "LeaveBill."+billId;
		String taskId = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult().getId();
		//variables.put("objId","LeaveBill."+billId);
		
		
		IUserDao userDao = new UserDaoImpl();
		List<User> leader = Assignee.get("LeaveBill",userDao.getUser(username).getPositionId());
		if(leader!=null && leader.size()>0){
			String assignee = leader.get(0).getUsername();
			variables.put("assignee",assignee);			
		}
		
		taskService.complete(taskId,variables);
		
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
			.processInstanceBusinessKey(businessKey).singleResult();
		if(pi==null){
			ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();
			LeaveBill leaveBill = leaveBillDao.findLeaveBillById(Long.parseLong(billId));
			leaveBill.setState(2);
			leaveBillDao.updateLeaveBill(leaveBill);
		}

	}



	
	
	
}
