package mahara.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetMethodConvertingFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(GetMethodConvertingFilter.class);

	@Override
	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
		logger.info("go through GetMethodConvertingFilter ......");
		chain.doFilter(wrapRequest((HttpServletRequest) request), response);
	}

	@Override
	public void destroy() {
		// do nothing
	}

	private static HttpServletRequestWrapper wrapRequest(HttpServletRequest request) {
		return new HttpServletRequestWrapper(request) {
			@Override
			public String getMethod() {
				return "GET";
			}
		};
	}
}