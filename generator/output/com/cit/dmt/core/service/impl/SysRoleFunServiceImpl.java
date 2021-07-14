package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.SysRoleFunMapper;
import com.cit.dmt.core.model.SysRoleFun;
import com.cit.dmt.core.service.SysRoleFunService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class SysRoleFunServiceImpl implements SysRoleFunService {
	Logger logger = LoggerFactory.getLogger(SysRoleFunService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private SysRoleFunMapper mapper;
	
	@Override
	public Page<SysRoleFun> selectPaged(CommonRequestParam crp, SysRoleFun qp) throws BusinessException {
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
		
		Page<SysRoleFun> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public SysRoleFun selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(SysRoleFun sysRoleFun, User loginUser) throws BusinessException {
//		SysRoleFun checkConflict = mapper.selectByPhone(sysRoleFun.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(sysRoleFun);
	}

	@Override
	public int update(SysRoleFun sysRoleFun, User loginUser) throws BusinessException {
		SysRoleFun checkExist = mapper.selectByPrimaryKey(sysRoleFun.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (sysRoleFun.getPhone() != null) {
//			SysRoleFun checkConflict = mapper.selectByPhone(sysRoleFun.getPhone(), sysRoleFun.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(sysRoleFun);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		SysRoleFun checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

