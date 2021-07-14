package com.cit.dmt.core.service;

import java.util.List;

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
	
	int finalTrans(Long id, String transResult, User loginUser) throws BusinessException;

	int saveBatch(Long projectId, Long taskId, List<TaskDetail> taskDetails, User loginUser) throws BusinessException;

	int deleteBatch(Long projectId, Long taskId, User loginUser) throws BusinessException;
}
