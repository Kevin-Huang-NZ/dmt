package com.cit.dmt.core.service;

import java.util.List;

import com.cit.dmt.core.model.CtCommon;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CtCommonService {
	Page<CtCommon> selectPaged(CommonRequestParam crp, CtCommon criteria) throws BusinessException;
	
	CtCommon selectById(Long id);

	int save(CtCommon ctCommon, User loginUser) throws BusinessException;

	int update(CtCommon ctCommon, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;

	int saveBatch(List<CtCommon> ctCommons, User loginUser) throws BusinessException;

	int deleteBatch(String countryCode, String languageCode, User loginUser) throws BusinessException;
}
