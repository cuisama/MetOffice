package com.weather.framework.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class DeviceExceptionMapper implements ExceptionMapper<Exception>{

	@Override
	public Response toResponse(Exception e) {
	       Response.ResponseBuilder ResponseBuilder = null;

	        if (e instanceof DeviceException){

	            //截取自定义类型
	            DeviceException exp = (DeviceException) e;
	            ErrorEntity entity = new ErrorEntity(exp.getCode(),exp.getMessage());
	            ResponseBuilder = Response.ok(entity, MediaType.APPLICATION_JSON);
	        }else {
	            ErrorEntity entity = new ErrorEntity(ErrorCode.OTHER_ERR.getCode(),e.getMessage());
	            ResponseBuilder = Response.ok(entity, MediaType.APPLICATION_JSON);
	        }
	        System.out.println("执行自定义异常");
	        System.err.println(e.toString());
	        System.err.println(e.getMessage());
	        return ResponseBuilder.build();
	}

}
