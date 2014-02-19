package org.abs.web;

import org.abs.consumer.entities.Persona;
import org.abs.consumer.managers.PersonaManager;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/17/14
 * Time: 12:01 AM
 */
public class ABSFilter implements Filter {
	public static final String USER_COOKIE = "userCookie";
	public static final String ATTRIBUTE_NAME = "attributeName";
	private FilterConfig filterConfig = null;
	private String userCookie = null;
	private String attributeName = "abtest";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		userCookie = filterConfig.getInitParameter(USER_COOKIE);
		String attributeName = filterConfig.getInitParameter(ATTRIBUTE_NAME);
		if (null != attributeName){
			this.attributeName = attributeName;
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (servletRequest instanceof HttpServletRequest){
			preProcess(servletRequest);
			filterChain.doFilter(servletRequest, servletResponse);
			postProcess(servletRequest);
		} else {
			//Do you even lift Bro? Pass it through.
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	private void preProcess(ServletRequest servletRequest){
		String sessionId = null;
		if (null != userCookie){
			Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
			for (Cookie cookie : cookies){
				if (cookie.getName().equals(userCookie)){
					sessionId = cookie.getValue();
					break;
				}
			}
		}
		if (null == sessionId){
			((HttpServletRequest) servletRequest).getSession().getId();
			sessionId = ((HttpServletRequest) servletRequest).getSession().getId();
		}
		Persona persona = PersonaManager.getInstance().loadPersona(sessionId);
		servletRequest.setAttribute(attributeName,persona);
	}

	private void postProcess(ServletRequest servletRequest){

	}

	@Override
	public void destroy() {

	}
}
