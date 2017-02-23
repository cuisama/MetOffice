package com.weather.framework.workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import com.weather.xm.leavebill.dao.ILeaveBillDao;
import com.weather.xm.leavebill.dao.LeaveBillDaoImpl;
import com.weather.xm.leavebill.model.LeaveBill;

public class WorkflowServiceImpl implements IWorkflowService {
	
	private ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();
	
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private RuntimeService runtimeService = processEngine.getRuntimeService();
	private TaskService taskService = processEngine.getTaskService();
	private FormService formService = processEngine.getFormService();
	private HistoryService historyService = processEngine.getHistoryService();
	
	/**本地 部署流程定义
	 * filePath: com/weather/xm/newsbill/diagram
	 * */
	@Override
	public void saveNewDeploye(String filename, String filePath) {
		try {
			repositoryService.createDeployment()
							.name(filename)
							.addClasspathResource(filePath+".bpmn")
							.addClasspathResource(filePath+".png")
							.deploy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**更新请假状态，启动流程实例，让启动的流程实例关联业务
	 * @throws Exception */
	@Override
	public void saveStartProcess(WorkflowBean workflowBean,String UserId) throws Exception {
		//1：获取请假单ID，使用请假单ID，查询请假单的对象LeaveBill
		Long id = workflowBean.getId();
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(id);
		//2：更新请假单的请假状态从0变成1（初始录入-->审核中）
		leaveBill.setState(1);
		leaveBillDao.updateLeaveBill(leaveBill);
		//3：使用当前对象获取到流程定义的key（对象的名称就是流程定义的key）
		String key = leaveBill.getClass().getSimpleName();
		/**
		 * 4：从Session中获取当前任务的办理人，使用流程变量设置下一个任务的办理人
			    * inputUser是流程变量的名称，
			    * 获取的办理人是流程变量的值
		 */
		Map<String, Object> variables = new HashMap<String,Object>();
		//variables.put("inputUser", UserId);//表示惟一用户
		/**
		 * 5：	(1)使用流程变量设置字符串（格式：LeaveBill.id的形式），通过设置，让启动的流程（流程实例）关联业务
   				(2)使用正在执行对象表中的一个字段BUSINESS_KEY（Activiti提供的一个字段），让启动的流程（流程实例）关联业务
		 */
		//格式：LeaveBill.id的形式（使用流程变量）
		String objId = key+"."+id;
		variables.put("objId", objId);
		//6：使用流程定义的key，启动流程实例，同时设置流程变量，同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务
		runtimeService.startProcessInstanceByKey(key,objId,variables);
		
	}
	
	/**2：使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>*/
	@Override
	public List<Task> findTaskListByName(String name) {
		List<Task> list = taskService.createTaskQuery()//
					.taskAssignee(name)//指定个人任务查询
					.orderByTaskCreateTime().asc()//
					.list();
		return list;
	}
	
	
	/**一：使用任务ID，查找请假单ID，从而获取请假单信息
	 * @throws Exception 
	 * @throws NumberFormatException */
	@Override
	public LeaveBill findLeaveBillByTaskId(String taskId) throws NumberFormatException, Exception {
		//1：使用任务ID，查询任务对象Task
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		//2：使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.singleResult();
		//4：使用流程实例对象获取BUSINESS_KEY
		String buniness_key = pi.getBusinessKey();
		//5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
		String id = "";
		if(StringUtils.isNotBlank(buniness_key)){
			//截取字符串，取buniness_key小数点的第2个值
			id = buniness_key.split("\\.")[1];
		} 
		//查询请假单对象
		//使用hql语句：from LeaveBill o where o.id=1
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(Long.parseLong(id));
		return leaveBill;
	}
	

	/**1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象*/
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//查询流程定义的对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef 
					.processDefinitionId(processDefinitionId)//使用流程定义ID查询
					.singleResult();
		return pd;
	}
	
}
