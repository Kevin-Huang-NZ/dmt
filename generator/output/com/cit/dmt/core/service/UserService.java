package com.cit.dmt.core.service;

import com.cit.dmt.core.model.User;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface UserService {
	Page<User> selectPaged(CommonRequestParam crp, User criteria) throws BusinessException;
	
	User selectById(Long id);

	int save(User user, User loginUser) throws BusinessException;

	int update(User user, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
