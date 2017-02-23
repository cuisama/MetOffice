package com.weather.framework.workflow;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.sun.media.jfxmedia.Media;
import com.weather.xm.leavebill.dao.ILeaveBillDao;
import com.weather.xm.leavebill.dao.LeaveBillDaoImpl;
import com.weather.xm.leavebill.model.LeaveBill;

@SuppressWarnings("serial")
@Path("/WorkflowResource")
public class WorkflowResource {

	//private WorkflowBean workflowBean = new WorkflowBean();
	
	private IWorkflowService workflowService = new WorkflowServiceImpl();
	
	private ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();
	
	// 启动流程
	@Path("/startAndSubmit")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String startProcess(@FormParam("billId") final long billId,
			@FormParam("username") final String username) throws Exception{
		//更新请假状态，启动流程实例，让启动的流程实例关联业务
		WorkflowBean workflowBean = new WorkflowBean();
		workflowBean.setId(billId);
		workflowService.saveStartProcess(workflowBean,username);
		return "success";
	}
	
	
	
	/**
	 * 任务管理首页显示
	 * @return
	 */
	@Path("/listTask")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> listTask(@FormParam("username") final String username){
		//使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>
		List<Task> list = workflowService.findTaskListByName(username); 

		return list;
	}


	
	/**
	 * 提交任务
	 * @throws Exception 
	 */
	@POST
	@Path("/submitTask")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String submitTask(@FormParam("deploymentId") String deploymentId,
			@FormParam("userId") String userId) throws Exception{
		WorkflowBean workflowBean = new WorkflowBean();
		workflowBean.setDeploymentId(deploymentId);
		//TODO
		workflowService.saveSubmitTask(workflowBean, userId);
		return "listTask";
	}
	

}
