package com.weather.framework.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

import com.weather.framework.workflow.IWorkflowService;
import com.weather.framework.workflow.WorkflowServiceImpl;

public class InitDb {
	
	@Test
	public void initDb(){
		ProcessEngineConfiguration con = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		con.setJdbcDriver("com.mysql.jdbc.Driver");
		con.setJdbcUrl("jdbc:mysql://localhost:3306/metoffice");
		con.setJdbcUsername("root");
		con.setJdbcPassword("root");
		con.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		con.buildProcessEngine();
	}

	@Test
	public void newdeploy(){
		String filename = "LeaveBill";
		String filePath = "com/weather/xm/leavebill/diagram/LeaveBill";
		
		IWorkflowService impl = new WorkflowServiceImpl();
		impl.saveNewDeploye(filename, filePath);
	}
}
