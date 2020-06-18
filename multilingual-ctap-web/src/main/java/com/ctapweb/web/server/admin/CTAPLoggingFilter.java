package com.ctapweb.web.server.admin;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.ThreadContext;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A http servlet filter that adds the user's ip address to logging. It can be
 * used to track a certain user when a lot of requests are fired from multiple
 * users.
 * 
 * @author xiaobin
 *
 */
public class CTAPLoggingFilter implements Filter, IsSerializable, Serializable{
	
	public CTAPLoggingFilter() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void destroy() {
		// Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			// MDC.put("userIP", request.getRemoteAddr()); //log4j changed to
			// Threadcontext
			ThreadContext.put("userIP", request.getRemoteAddr());

			filterChain.doFilter(request, response);
		} finally {
			ThreadContext.remove("userIP");

		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
