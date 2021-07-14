package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.SysFunMapper;
import com.cit.dmt.core.dao.SysRoleFunMapper;
import com.cit.dmt.core.model.SysFun;
import com.cit.dmt.core.service.SysFunService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class SysFunServiceImpl implements SysFunService {
	Logger logger = LoggerFactory.getLogger(SysFunService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private SysFunMapper mapper;
	@Autowired
	private SysRoleFunMapper sysRoleFunMapper;
	
	@Override
	public Page<SysFun> selectPaged(CommonRequestParam crp, SysFun qp) throws BusinessException {
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
			if (!StringUtils.isEmpty(qp.getPageName())) {
				criteria.put("pageName", qp.getPageName());
			}
		}

		Page<SysFun> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public SysFun selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(SysFun sysFun, User loginUser) throws BusinessException {
		SysFun checkConflict = mapper.selectByFunNo(sysFun.getFunNo(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "功能重复。");
		}
		return mapper.insert(sysFun);
	}

	@Override
	public int update(SysFun sysFun, User loginUser) throws BusinessException {
		SysFun checkExist = mapper.selectByPrimaryKey(sysFun.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		if (sysFun.getFunNo() != null) {
			SysFun checkConflict = mapper.selectByFunNo(sysFun.getFunNo(), sysFun.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "功能重复。");
			}
		}
		return mapper.updateByPrimaryKeySelective(sysFun);
	}

	@Override
	@Transactional
	public int delete(Long id, User loginUser) throws BusinessException {
		SysFun checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		this.sysRoleFunMapper.deleteByFunNo(checkExist.getFunNo());
		return mapper.deleteByPrimaryKey(id);
	}

}

