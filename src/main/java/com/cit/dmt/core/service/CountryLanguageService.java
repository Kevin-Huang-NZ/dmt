package com.cit.dmt.core.service;

import java.util.List;

import com.cit.dmt.core.model.CountryLanguage;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CountryLanguageService {
	Page<CountryLanguage> selectPaged(CommonRequestParam crp, CountryLanguage criteria) throws BusinessException;

	List<CountryLanguage> selectByCountryCode(String countryCode) throws BusinessException;
	
	int save(CountryLanguage countryLanguage, User loginUser) throws BusinessException;
	
	int updateStatus(CountryLanguage countryLanguage, User loginUser) throws BusinessException;

	int deleteByCode(CountryLanguage countryLanguage, User loginUser) throws BusinessException;
}
