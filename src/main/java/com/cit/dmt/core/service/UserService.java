package com.cit.dmt.core.service;

import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface UserService {
	Page<User> selectPaged(CommonRequestParam pp, User criteria) throws BusinessException;
	
	User selectById(Long id);

	int save(User record, User loginUser) throws BusinessException;

	int update(User record, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
