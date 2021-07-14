package com.cit.dmt.core.service;

import java.util.List;

import com.cit.dmt.core.model.SysRoleFun;
import com.cit.dmt.core.model.User;
import mahara.web.response.error.BusinessException;

public interface SysRoleFunService {
	List<SysRoleFun> selectByRoleNo(String roleNo) throws BusinessException;
	
	int save(SysRoleFun sysRoleFun, User loginUser) throws BusinessException;

	int deleteByNo(SysRoleFun sysRoleFun, User loginUser) throws BusinessException;
}
