package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CtTransliteration;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CtTransliterationService {
	Page<CtTransliteration> selectPaged(CommonRequestParam crp, CtTransliteration criteria) throws BusinessException;
	
	CtTransliteration selectById(Long id);

	int save(CtTransliteration ctTransliteration, User loginUser) throws BusinessException;

	int update(CtTransliteration ctTransliteration, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
