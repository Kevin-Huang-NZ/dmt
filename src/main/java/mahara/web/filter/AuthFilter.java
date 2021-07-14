package mahara.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.cit.dmt.core.model.User;
import com.cit.dmt.core.service.AuthService;

//import com.fasterxml.jackson.databind.ObjectMapper;

import mahara.common.GlobalConst;
import mahara.util.JwtUtil;
import mahara.util.RedisUtil;
//import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ErrorInfo;
import mahara.web.response.json.Root;

//@Order(2)
//@WebFilter(filterName = "authFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Autowired
	private AuthService authService;    
	
	@Autowired
	private JwtUtil jwtUtil;

	@Value("${jwt.tokenheader:x-auth-token}")
	private String tokenHeader;
  
	@Autowired
	private RedisUtil redisUtil;

	private static final Set<String> ALLOWED_ACTIONS = Collections.unmodifiableSet(new HashSet<>(
	    Arrays.asList("auth/signin", "auth/signout", "uploaded/image")));

	private static final Set<String> ALLOWED_LOGINED_ACTIONS = Collections.unmodifiableSet(new HashSet<>(
	    Arrays.asList("auth/chgpwd","auth/currentUser","statistics/index")));

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	    throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
//		logger.info("request path is : {}", path);
		String[] sepPath = path.split("/");
		if (sepPath == null || sepPath.length < 3) {
			accessDeny(request, response, EmBusinessError.BAD_REQUEST);
			return;
		}
		String action = sepPath[2] + "/" + sepPath[3];
		logger.info("request action is : {}", action);

		if (ALLOWED_ACTIONS.contains(action)) {
			filterChain.doFilter(request, response);
//			logger.info("request path is allowed.");
			return;
		}
		String jwtToken = request.getHeader(tokenHeader);
		String tokenStatus = jwtUtil.verifyToken(jwtToken);
		if ("0".equals(tokenStatus)) {
			accessDeny(request, response, EmBusinessError.UNAUTHORIZED);
			return;
		}
		User loginUser = redisUtil.get(jwtToken, User.class);
		
		if (loginUser == null) {
			accessDeny(request, response, EmBusinessError.UNAUTHORIZED);
			return;
		}
		request.setAttribute(GlobalConst.SESSION_LOGIN_USER, loginUser);

		if (ALLOWED_LOGINED_ACTIONS.contains(action)) {
			filterChain.doFilter(request, response);
//			logger.info("request path is allowed.");
			return;
		}
		
////		logger.info("why go here.");
//		HttpSession session = request.getSession();
//		Object sessionLoginUser = session.getAttribute(GlobalConst.SESSION_LOGIN_USER);
//		if (sessionLoginUser == null) {
//			accessDeny(request, response, EmBusinessError.UNAUTHORIZED);
//			return;
//		}
//		LoginUser loginUser = (LoginUser) sessionLoginUser;
////		logger.info("signed in user is : {}", loginUser.getIdentification());
		
		if (authService.checkAuth(loginUser.getRoles(), action)) {
			filterChain.doFilter(request, response);
		} else {
			accessDeny(request, response, EmBusinessError.FORBIDDEN);
		}
	}

	private void accessDeny(HttpServletRequest request, HttpServletResponse response, EmBusinessError err)
	    throws IOException {
		List<String> headers = Arrays.asList(tokenHeader, "Accept", "Access-Control-Request-Method", 
				  "Access-Control-Request-Headers", "Accept-Language", "Authorization", 
				  "Content-Type", "Request-Name", "Request-Surname", 
				  "Origin");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", String.join(",", headers));
		response.setContentType("application/json;charset=UTF-8");

		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrCode(err.getErrCode());
		errorInfo.setErrMsg(err.getErrMsg());
		Root responseWrap = Root.create(Root.RESPONSE_STATUS_ERROR, errorInfo);
		response.getWriter().write(JSON.toJSONString(responseWrap));
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}
}
