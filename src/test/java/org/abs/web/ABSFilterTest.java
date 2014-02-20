package org.abs.web;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/19/14
 * Time: 5:56 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class ABSFilterTest extends Assert {
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;

	@Test
	public void testGenerateSessionId() throws Exception {
		when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.102 Safari/537.36");
		when(request.getRemoteAddr()).thenReturn("75.174.121.12");
		when(request.getRemoteHost()).thenReturn("computer.server.tld");

		ABSFilter filter = new ABSFilter();
		assertNotNull(filter.generateSessionId(request));
	}
}
