package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import com.cit.dmt.core.dao.CountryLanguageMapper;
import com.cit.dmt.core.dao.CountryMapper;
import com.cit.dmt.core.model.Country;
import com.cit.dmt.core.service.CountryService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CountryServiceImpl implements CountryService {
	Logger logger = LoggerFactory.getLogger(CountryService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CountryMapper mapper;
	@Autowired
	private CountryLanguageMapper countryLanguageMapper;
	
	@Override
	public Page<Country> selectPaged(CommonRequestParam crp, Country qp) throws BusinessException {
		if (crp == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(crp.getOrder())) {
			criteria.put("orderByClause", crp.getOrder() + "," + defaultOrderBy);
		} else {
			criteria.put("orderByClause", defaultOrderBy);
		}
		if (qp != null) {
			if (!StringUtils.isEmpty(qp.getCountryCode())) {
				criteria.put("countryCode", qp.getCountryCode());
			}
			if (!StringUtils.isEmpty(qp.getCountryName())) {
				criteria.put("countryName",  "%" + qp.getCountryName() + "%");
			}
		}
		
		Page<Country> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public Country selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(Country country, User loginUser) throws BusinessException {
		Country checkConflict = mapper.selectByCode(country.getCountryCode(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "国家代码重复。");
		}
		checkConflict = mapper.selectByName(country.getCountryName(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "国家名称重复。");
		}
		return mapper.insert(country);
	}

	@Override
	public int update(Country country, User loginUser) throws BusinessException {
		Country checkExist = mapper.selectByPrimaryKey(country.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		Country checkConflict = null;
		if (country.getCountryCode() != null) {
			checkConflict = mapper.selectByCode(country.getCountryCode(), country.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "国家代码重复。");
			}
		}
		if (country.getCountryName() != null) {
			checkConflict = mapper.selectByName(country.getCountryName(), country.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "国家名称重复。");
			}
		}
		return mapper.updateByPrimaryKeySelective(country);
	}

	@Transactional
	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		Country checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		countryLanguageMapper.deleteByCountryCode(checkExist.getCountryCode());
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public Country selectByCode(String countryCode) {
		return mapper.selectByCode(countryCode, null);
	}

	@Override
	public List<Country> selectHaveCt(String status) {
		return mapper.selectHaveCt(status);
	}

}

