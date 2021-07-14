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
import com.cit.dmt.core.dao.CtCommonMapper;
import com.cit.dmt.core.dao.CtRomanMapper;
import com.cit.dmt.core.dao.CtTransliterationMapper;
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
	@Autowired
	private CtCommonMapper ctCommonMapper;
	@Autowired
	private CtRomanMapper ctRomanMapper;
	@Autowired
	private CtTransliterationMapper ctTransliterationMapper;
	
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
		if (qp != null) {
			if (!StringUtils.isEmpty(qp.getCountryCode())) {
				criteria.put("countryCode", qp.getCountryCode());
			}
			if (!StringUtils.isEmpty(qp.getLanguageCode())) {
				criteria.put("languageCode", qp.getLanguageCode());
			}
		}
		
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
	public int save(CountryLanguage countryLanguage, User loginUser) throws BusinessException {
		CountryLanguage checkConflict = mapper.selectByCode(countryLanguage.getCountryCode(), countryLanguage.getLanguageCode());
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "数据重复。1个国家，每个语种最多选择1次。");
		}
		return mapper.insert(countryLanguage);
	}

	@Override
	public int updateStatus(CountryLanguage countryLanguage, User loginUser) throws BusinessException {

		CountryLanguage checkExist = mapper.selectByCode(countryLanguage.getCountryCode(), countryLanguage.getLanguageCode());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		checkExist.setStatus(countryLanguage.getStatus());
		
		return mapper.updateByPrimaryKey(checkExist);
	}

	@Override
	public List<CountryLanguage> selectByCountryCode(String countryCode) throws BusinessException {
		if (StringUtils.isEmpty(countryCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "参数错误。国家代码不能为空。");
		}
		return mapper.selectByCountryCode(countryCode);
	}

	@Override
	@Transactional
	public int deleteByCode(CountryLanguage countryLanguage, User loginUser) throws BusinessException {
		ctCommonMapper.deleteBatch(countryLanguage.getCountryCode(), countryLanguage.getLanguageCode());
		ctRomanMapper.deleteBatch(countryLanguage.getCountryCode(), countryLanguage.getLanguageCode());
		ctTransliterationMapper.deleteBatch(countryLanguage.getCountryCode(), countryLanguage.getLanguageCode());
		return mapper.deleteByCode(countryLanguage.getCountryCode(), countryLanguage.getLanguageCode());
	}

}

