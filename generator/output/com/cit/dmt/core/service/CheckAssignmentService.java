package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CheckAssignment;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CheckAssignmentService {
	Page<CheckAssignment> selectPaged(CommonRequestParam crp, CheckAssignment criteria) throws BusinessException;
	
	CheckAssignment selectById(Long id);

	int save(CheckAssignment checkAssignment, User loginUser) throws BusinessException;

	int update(CheckAssignment checkAssignment, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
