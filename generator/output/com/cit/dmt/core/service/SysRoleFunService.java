package com.cit.dmt.core.service;

import com.cit.dmt.core.model.SysRoleFun;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface SysRoleFunService {
	Page<SysRoleFun> selectPaged(CommonRequestParam crp, SysRoleFun criteria) throws BusinessException;
	
	SysRoleFun selectById(Long id);

	int save(SysRoleFun sysRoleFun, User loginUser) throws BusinessException;

	int update(SysRoleFun sysRoleFun, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
