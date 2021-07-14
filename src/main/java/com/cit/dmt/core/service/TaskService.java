package com.cit.dmt.core.service;

import com.cit.dmt.core.model.Task;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface TaskService {
	Page<Task> selectPaged(CommonRequestParam crp, Task criteria) throws BusinessException;
	
	Task selectById(Long id);

	int save(Task task, User loginUser) throws BusinessException;

	int update(Task task, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
	
	void execRoman(Long id) throws BusinessException;
	
	void execTrans(Long id) throws BusinessException;
}
