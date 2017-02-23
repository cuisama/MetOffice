package com.weather.xm.newsbill.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.weather.xm.newsbill.dao.INewsBillDao;
import com.weather.xm.newsbill.dao.NewsBillDaoImpl;
import com.weather.xm.newsbill.model.NewsBill;

public class NewsBillResource{

	private NewsBill newsBill = new NewsBill();
	
	private INewsBillDao newsBillDao = new NewsBillDaoImpl();

	@Path("/queryNewsBill")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String home(){
		//1：查询所有的请假信息（对应a_leavebill），返回List<NewsBill>
		List<NewsBill> list = newsBillDao.findNewsBillList(); 

		return "home";
	}
	
	@Path("/queryOneNewsBill")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String input(){
		//1：获取请假单ID
		Long id = newsBill.getId();
		//修改
		if(id!=null){
			//2：使用请假单ID，查询请假单信息，
			NewsBill bill = newsBillDao.findNewsBillById(id);

		}
		//新增
		return "input";
	}
	
	@Path("/saveNewsBill")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String save() {
		//执行保存
		newsBillDao.saveNewsBill(newsBill);
		return "save";
	}
	
	@Path("/deleteNewsBill")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(){
		//1：获取请假单ID
		Long id = newsBill.getId();
		//执行删除
		newsBillDao.deleteNewsBillById(id);
		return "save";
	}
	
}
