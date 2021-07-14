package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CountryLanguageMapper;
import com.cit.dmt.core.model.CountryLanguage;
import com.cit.dmt.core.service.CountryLanguageService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CountryLanguageServiceImpl implements CountryLanguageService {
	Logger logger = LoggerFactory.getLogger(CountryLanguageService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CountryLanguageMapper mapper;
	
	@Override
	public Page<CountryLanguage> selectPaged(CommonRequestParam crp, CountryLanguage qp) throws BusinessException {
		if (crp == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(crp.getOrder())) {
			criteria.put("orderByClause", crp.getOrder() + "," + defaultOrderBy);
		} else {
			criteria.put("orderByClause", defaultOrderBy);
		}
//		if (qp != null) {
//			if (!StringUtils.isEmpty(qp.getPhone())) {
//				criteria.put("phone", qp.getPhone() + "%");
//			}
//		}
		
		Page<CountryLanguage> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CountryLanguage selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CountryLanguage countryLanguage, User loginUser) throws BusinessException {
//		CountryLanguage checkConflict = mapper.selectByPhone(countryLanguage.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(countryLanguage);
	}

	@Override
	public int update(CountryLanguage countryLanguage, User loginUser) throws BusinessException {
		CountryLanguage checkExist = mapper.selectByPrimaryKey(countryLanguage.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (countryLanguage.getPhone() != null) {
//			CountryLanguage checkConflict = mapper.selectByPhone(countryLanguage.getPhone(), countryLanguage.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(countryLanguage);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CountryLanguage checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

