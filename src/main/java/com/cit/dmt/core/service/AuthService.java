package com.cit.dmt.core.service;

import java.util.Map;

import com.cit.dmt.core.model.User;

import mahara.web.response.error.BusinessException;

/**
 * @author huanghao
 *
 */
public interface AuthService {

	Map<String, String> getAndCacheRoleFuns();

	boolean checkAuth(String roles, String action);

	User userLogin(String phone, String password) throws BusinessException;
	
	int userChgPwd(Long loginId, String oldPwd, String newPwd) throws BusinessException;
}
