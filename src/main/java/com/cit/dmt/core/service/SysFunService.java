package com.cit.dmt.core.service;

import com.cit.dmt.core.model.SysFun;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface SysFunService {
	Page<SysFun> selectPaged(CommonRequestParam crp, SysFun criteria) throws BusinessException;
	
	SysFun selectById(Long id);

	int save(SysFun sysFun, User loginUser) throws BusinessException;

	int update(SysFun sysFun, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
