package com.cit.dmt.core.service;

import com.cit.dmt.core.model.Project;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface ProjectService {
	Page<Project> selectPaged(CommonRequestParam crp, Project criteria) throws BusinessException;
	
	Project selectById(Long id);

	int save(Project project, User loginUser) throws BusinessException;

	int update(Project project, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
