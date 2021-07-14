package com.cit.dmt.core.service;

import com.cit.dmt.core.model.SysRole;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface SysRoleService {
	Page<SysRole> selectPaged(CommonRequestParam crp, SysRole criteria) throws BusinessException;
	
	SysRole selectById(Long id);

	int save(SysRole sysRole, User loginUser) throws BusinessException;

	int update(SysRole sysRole, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
