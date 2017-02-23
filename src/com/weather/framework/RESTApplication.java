package com.weather.framework;


import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.weather.framework.exception.DeviceException;
import com.weather.framework.exception.DeviceExceptionMapper;
import com.weather.framework.workflow.WorkflowResource;
import com.weather.xm.leavebill.resource.LeaveBillResource;


public class RESTApplication extends ResourceConfig  {
	
    public RESTApplication() {

    	register(LoggingFilter.class);
    	
    	register(DeviceExceptionMapper.class);
    	
    	register(MultiPartFeature.class);
    	register(RolesAllowedDynamicFeature.class);
        register(JacksonJsonProvider.class);
        
        register(LeaveBillResource.class);
        register(WorkflowResource.class);
        
        register(RequestFilter.class);

    }
}
