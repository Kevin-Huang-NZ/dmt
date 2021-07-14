package com.cit.dmt.core.service;

import java.util.List;

import com.cit.dmt.core.model.Country;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CountryService {
	Page<Country> selectPaged(CommonRequestParam crp, Country criteria) throws BusinessException;
	
	Country selectById(Long id);

	int save(Country country, User loginUser) throws BusinessException;

	int update(Country country, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
	
	Country selectByCode(String countryCode);
	
	List<Country> selectHaveCt(String status);
}
