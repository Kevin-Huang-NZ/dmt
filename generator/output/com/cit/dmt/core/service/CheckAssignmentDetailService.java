package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CheckAssignmentDetail;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CheckAssignmentDetailService {
	Page<CheckAssignmentDetail> selectPaged(CommonRequestParam crp, CheckAssignmentDetail criteria) throws BusinessException;
	
	CheckAssignmentDetail selectById(Long id);

	int save(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException;

	int update(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
