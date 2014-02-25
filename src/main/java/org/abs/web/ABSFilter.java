package org.abs.web;

import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Group;
import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;
import org.abs.consumer.managers.PersonaManager;
import org.abs.consumer.persistance.EntityDAO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/17/14
 * Time: 12:01 AM
 */
public class ABSFilter implements Filter {
	protected static Logger log = Logger.getLogger(ABSFilter.class);
	public static final String USER_COOKIE = "userCookie";
	public static final String ATTRIBUTE_NAME = "attributeName";
	private FilterConfig filterConfig = null;
	private String userCookie = null;
	private String attributeName = "abtest";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		userCookie = filterConfig.getInitParameter(USER_COOKIE);
		if (null == userCookie){
			userCookie = "abs";
		}

		String attributeName = filterConfig.getInitParameter(ATTRIBUTE_NAME);
		if (null != attributeName){
			this.attributeName = attributeName;
		}

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		long start = System.currentTimeMillis();
		if (servletRequest instanceof HttpServletRequest){
			preProcess(servletRequest, (HttpServletResponse) servletResponse);
			filterChain.doFilter(servletRequest, servletResponse);
			postProcess(servletRequest);
		} else {
			//Do you even lift Bro? Pass it through.
			filterChain.doFilter(servletRequest, servletResponse);
		}
		long end = System.currentTimeMillis();
		log.debug("ABS took " + (end-start) + "ms");
	}

	private void preProcess(ServletRequest servletRequest, HttpServletResponse servletResponse){
		String sessionId = null;
		if (null != userCookie){
			Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
			if (null != cookies){
				for (Cookie cookie : cookies){
					if (cookie.getName().equals(userCookie)){
						sessionId = cookie.getValue();
						break;
					}
				}
			}
		}
		if (null == sessionId){
			((HttpServletRequest) servletRequest).getSession().getId();
			sessionId = generateSessionId((HttpServletRequest) servletRequest);
			if (null == sessionId){
				sessionId = ((HttpServletRequest) servletRequest).getSession().getId();
			}
			Cookie cookie = new Cookie(userCookie,sessionId);
			cookie.setMaxAge(Integer.MAX_VALUE);
			servletResponse.addCookie(cookie);
		}
		String[] overideGroups = servletRequest.getParameterValues("aboveride");
		Persona persona = PersonaManager.getInstance().loadPersona(sessionId, overideGroups);

		servletRequest.setAttribute(attributeName, persona);
	}

	private void postProcess(ServletRequest servletRequest){
		Object personaObject = servletRequest.getAttribute(attributeName);
		if (personaObject instanceof  Persona){
			PersonaManager.getInstance().savePersona((Persona) personaObject);
		}
	}

	public String generateSessionId(HttpServletRequest servletRequest){
		StringBuilder sb = new StringBuilder(servletRequest.getRemoteAddr());
		sb.append(System.currentTimeMillis());
		sb.append(servletRequest.getHeader("User-Agent"));
		sb.append(servletRequest.getRemoteHost());

		return DigestUtils.shaHex(sb.toString()).toUpperCase();
	}

	@Override
	public void destroy() {

	}
}
