package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.CtRomanMapper;
import com.cit.dmt.core.model.CtRoman;
import com.cit.dmt.core.service.CtRomanService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class CtRomanServiceImpl implements CtRomanService {
	Logger logger = LoggerFactory.getLogger(CtRomanService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private CtRomanMapper mapper;
	
	@Override
	public Page<CtRoman> selectPaged(CommonRequestParam crp, CtRoman qp) throws BusinessException {
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
		
		Page<CtRoman> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public CtRoman selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(CtRoman ctRoman, User loginUser) throws BusinessException {
//		CtRoman checkConflict = mapper.selectByPhone(ctRoman.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(ctRoman);
	}

	@Override
	public int update(CtRoman ctRoman, User loginUser) throws BusinessException {
		CtRoman checkExist = mapper.selectByPrimaryKey(ctRoman.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (ctRoman.getPhone() != null) {
//			CtRoman checkConflict = mapper.selectByPhone(ctRoman.getPhone(), ctRoman.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(ctRoman);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		CtRoman checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

