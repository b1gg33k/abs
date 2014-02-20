package org.abs.web;

import org.abs.consumer.entities.Persona;
import org.abs.consumer.managers.PersonaManager;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
		if (servletRequest instanceof HttpServletRequest){
			preProcess(servletRequest, (HttpServletResponse) servletResponse);
			filterChain.doFilter(servletRequest, servletResponse);
			postProcess(servletRequest);
		} else {
			//Do you even lift Bro? Pass it through.
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	private void preProcess(ServletRequest servletRequest, HttpServletResponse servletResponse){
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
			sessionId = generateSessionId((HttpServletRequest) servletRequest);
			if (null == sessionId){
				sessionId = ((HttpServletRequest) servletRequest).getSession().getId();
			}
			Cookie cookie = new Cookie(userCookie,sessionId);
			cookie.setMaxAge(Integer.MAX_VALUE);
			servletResponse.addCookie(cookie);
		}
		Persona persona = PersonaManager.getInstance().loadPersona(sessionId);
		servletRequest.setAttribute(attributeName, persona);
	}

	private void postProcess(ServletRequest servletRequest){

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
