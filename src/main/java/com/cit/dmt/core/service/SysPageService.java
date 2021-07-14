package com.cit.dmt.core.service;

import com.cit.dmt.core.model.SysPage;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface SysPageService {
	Page<SysPage> selectPaged(CommonRequestParam pp, SysPage criteria) throws BusinessException;
	
	SysPage selectById(Long id);

	int save(SysPage sysPage, User loginUser) throws BusinessException;

	int update(SysPage sysPage, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
