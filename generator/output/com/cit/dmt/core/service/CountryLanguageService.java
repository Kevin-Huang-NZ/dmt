package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CountryLanguage;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CountryLanguageService {
	Page<CountryLanguage> selectPaged(CommonRequestParam crp, CountryLanguage criteria) throws BusinessException;
	
	CountryLanguage selectById(Long id);

	int save(CountryLanguage countryLanguage, User loginUser) throws BusinessException;

	int update(CountryLanguage countryLanguage, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
