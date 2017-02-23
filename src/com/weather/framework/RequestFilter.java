package com.weather.framework;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.message.internal.MediaTypes;
import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;

@PreMatching
public class RequestFilter implements ContainerRequestFilter {

	private Log logger = LogFactory.getLog(RequestFilter.class);
	// Map<String,String[]> userList = new HashMap<>();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if (!MediaTypes.typeEqual(MediaType.APPLICATION_FORM_URLENCODED_TYPE, requestContext.getMediaType())) {
			return;
		}

		String requestStr = this.inputStreamToString(requestContext.getEntityStream());

		if (requestContext.getUriInfo().getPath().equals("user/getValidUserInfo")) {
			String username = getProperty(requestStr, "username");

		} else if (requestContext.getUriInfo().getPath().equals("user/exit")) {
			String username = getProperty(requestStr, "username");

		} else {

		}

		requestContext.setEntityStream(new ByteArrayInputStream(requestStr.getBytes("UTF-8")));

	}

	public String getProperty(String requestStr, String key) throws IOException {

		// ContainerRequest cr = (ContainerRequest) requestContext;
		// cr.bufferEntity();
		// Form form = cr.readEntity(Form.class);
		// return form.asMap().get(key).toString();

		String[] arrs = requestStr.split("&");
		for (String strs : arrs) {
			String[] s = strs.split("=");
			if (s.length == 2 && s[0].equals(key)) {
				System.out.println("key=" + s[1]);
				return s[1];
			}
		}
		return "";
	}

	public String inputStreamToString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

}
