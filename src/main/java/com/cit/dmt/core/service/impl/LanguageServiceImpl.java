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
import com.cit.dmt.core.dao.LanguageMapper;
import com.cit.dmt.core.model.Language;
import com.cit.dmt.core.service.LanguageService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class LanguageServiceImpl implements LanguageService {
	Logger logger = LoggerFactory.getLogger(LanguageService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private LanguageMapper mapper;
	@Autowired
	private CountryLanguageMapper countryLanguageMapper;
	
	@Override
	public Page<Language> selectPaged(CommonRequestParam crp, Language qp) throws BusinessException {
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
			if (!StringUtils.isEmpty(qp.getLanguageCode())) {
				criteria.put("languageCode", qp.getLanguageCode());
			}
			if (!StringUtils.isEmpty(qp.getLanguageName())) {
				criteria.put("languageName",  "%" + qp.getLanguageName() + "%");
			}
		}
		
		Page<Language> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public Language selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(Language language, User loginUser) throws BusinessException {
		Language checkConflict = mapper.selectByCode(language.getLanguageCode(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "语种代码重复。");
		}
		checkConflict = mapper.selectByName(language.getLanguageName(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "语种名称重复。");
		}
		return mapper.insert(language);
	}

	@Override
	public int update(Language language, User loginUser) throws BusinessException {
		Language checkExist = mapper.selectByPrimaryKey(language.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		Language checkConflict = null;
		if (language.getLanguageCode() != null) {
			checkConflict = mapper.selectByCode(language.getLanguageCode(), checkExist.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "语种代码重复。");
			}
		}
		if (language.getLanguageName() != null) {
			checkConflict = mapper.selectByName(language.getLanguageName(), checkExist.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "语种名称重复。");
			}
		}
		return mapper.updateByPrimaryKeySelective(language);
	}
	
	@Transactional
	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		Language checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		countryLanguageMapper.deleteByLanguageCode(checkExist.getLanguageCode());
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public Language selectByCode(String languageCode) {
		return mapper.selectByCode(languageCode, null);
	}

	@Override
	public List<Language> selectByCountry(String countryCode, String status) throws BusinessException {

		if (StringUtils.isEmpty(countryCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		
		return mapper.selectByCountry(countryCode, status);
	}

}

