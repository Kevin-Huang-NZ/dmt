package com.cit.dmt.core.service;

import com.cit.dmt.core.model.Language;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface LanguageService {
	Page<Language> selectPaged(CommonRequestParam crp, Language criteria) throws BusinessException;
	
	Language selectById(Long id);

	int save(Language language, User loginUser) throws BusinessException;

	int update(Language language, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
