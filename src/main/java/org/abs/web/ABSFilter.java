package org.abs.web;

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
	public static final String JSESSIONID = "JSESSIONID";
	private FilterConfig filterConfig = null;
	private String userCookie = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		userCookie = filterConfig.getInitParameter(USER_COOKIE);
		if (null == userCookie){
			userCookie = JSESSIONID;
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (servletRequest instanceof HttpServletRequest){
			HttpServletRequest webRequest = (HttpServletRequest) servletRequest;
			String userId = null;
			Cookie[] cookies = webRequest.getCookies();
			for (Cookie cookie : cookies){
				if (cookie.getName().equals(userCookie)){
					userId = cookie.getValue();
					break;
				}
			}
			if (null == userId){
				((HttpServletRequest) servletRequest).getSession().getId();
				userId = ((HttpServletRequest) servletRequest).getSession().getId();
			}
			//Do before stuff
			filterChain.doFilter(servletRequest, servletResponse);
			//Do after stuff
		} else {
			//Do you even lift Bro? Pass it through.
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy() {

	}
}
