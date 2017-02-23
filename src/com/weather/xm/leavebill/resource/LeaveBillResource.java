package com.weather.xm.leavebill.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.weather.framework.workflow.WorkflowBean;
import com.weather.xm.leavebill.dao.ILeaveBillDao;
import com.weather.xm.leavebill.dao.LeaveBillDaoImpl;
import com.weather.xm.leavebill.model.LeaveBill;

@Path("/leaveBill")
public class LeaveBillResource {
	
	private ILeaveBillDao leaveBillDao = new LeaveBillDaoImpl();

	/**
	 * 请假管理首页显示
	 * @return
	 * @throws Exception 
	 */
	@Path("/getList")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<LeaveBill> home(@FormParam("username") final String username) throws Exception{
		List<LeaveBill> list = leaveBillDao.findLeaveBillList(username); 
		return list;
	}
	
	/**
	 * 保存，请假申请
	 * @throws Exception 
	 * 
	 * */
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(@FormParam("username") final String username,
			@FormParam("days") final Integer days,
			@FormParam("content") final String content,
			@FormParam("remark") final String remark) throws Exception {
		LeaveBill leaveBill = new LeaveBill(days,content,remark,username);
		int result = leaveBillDao.saveLeaveBill(leaveBill);
		return result>0?"success":"failed";
	}
	
	/**
	 * 更新，请假申请
	 * @throws Exception 
	 * 
	 * */
	@Path("/update")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String update(@FormParam("username") final String username,
			@FormParam("days") final Integer days,
			@FormParam("content") final String content,
			@FormParam("remark") final String remark) throws Exception {
		LeaveBill leaveBill = new LeaveBill(days,content,remark,username);
		int result = leaveBillDao.updateLeaveBill(leaveBill);
		return result>0?"success":"failed";
	}
	
	/**
	 * 删除，请假申请
	 * @throws Exception 
	 * 
	 * */
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@FormParam("id") final Long id) throws Exception{
		int result = leaveBillDao.deleteLeaveBillById(id);
		return result>0?"success":"failed";
	}
	
	// 启动流程
	@Path("/startProcess")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String startProcess(@FormParam("billId") final long billId,
			@FormParam("username") final String username) throws Exception{
		//leaveBillDao.startAndSubmit(billId,username);
		
		leaveBillDao.startProcess(billId, username);
		
		Map<String, Object> variables = new HashMap<>();
		leaveBillDao.completeMyTask(username,""+billId, variables);
		return "success";
	}
	
	@Path("/listMyTask")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<LeaveBill> listMyTask(@FormParam("username") final String username) throws Exception{
		return leaveBillDao.listMyTask(username);
	}
	
	@Path("/completeMyTask")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String completeMyTask(@FormParam("username") final String username,
			@FormParam("agree") final String agree,
			@FormParam("billId") final String billId) throws Exception{
		Map<String, Object> variables = new HashMap<>();
		variables.put("agree", agree);
		leaveBillDao.completeMyTask(username,billId, variables);
		return "success";
	}
	
}
