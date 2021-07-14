package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CheckAssignmentDetail;
import com.cit.dmt.core.model.FinalTrans;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CheckAssignmentDetailService {
	Page<FinalTrans> selectPaged(CommonRequestParam crp, CheckAssignmentDetail criteria) throws BusinessException;
	
	FinalTrans selectById(Long id);

	int save(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException;

	int update(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
	
	int updateCheckResult(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException;
	
	int updateAdoptionById(Long id, String adoptionStatus, User loginUser) throws BusinessException;
	
	int updateAdoptionAll(CheckAssignmentDetail checkAssignmentDetail, User loginUser) throws BusinessException;
}
