package com.cit.dmt.core.service;

import com.cit.dmt.core.model.TaskDetail;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface TaskDetailService {
	Page<TaskDetail> selectPaged(CommonRequestParam crp, TaskDetail criteria) throws BusinessException;
	
	TaskDetail selectById(Long id);

	int save(TaskDetail taskDetail, User loginUser) throws BusinessException;

	int update(TaskDetail taskDetail, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
