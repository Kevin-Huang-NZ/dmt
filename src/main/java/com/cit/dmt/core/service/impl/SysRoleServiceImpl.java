package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import com.cit.dmt.core.dao.SysRoleFunMapper;
import com.cit.dmt.core.dao.SysRoleMapper;
import com.cit.dmt.core.model.SysRole;
import com.cit.dmt.core.service.SysRoleService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	Logger logger = LoggerFactory.getLogger(SysRoleService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private SysRoleMapper mapper;
	@Autowired
	private SysRoleFunMapper sysRoleFunMapper;
	
	@Override
	public Page<SysRole> selectPaged(CommonRequestParam crp, SysRole qp) throws BusinessException {
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
			if (!StringUtils.isEmpty(qp.getRoleNo())) {
				criteria.put("roleNo", qp.getRoleNo());
			}
			if (!StringUtils.isEmpty(qp.getRoleName())) {
				criteria.put("roleName", qp.getRoleName() + "%");
			}
		}
		
		Page<SysRole> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public SysRole selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(SysRole sysRole, User loginUser) throws BusinessException {
		SysRole checkConflict = mapper.selectByRoleNo(sysRole.getRoleNo(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "角色编号重复。");
		}
		return mapper.insert(sysRole);
	}

	@Override
	public int update(SysRole sysRole, User loginUser) throws BusinessException {
		SysRole checkExist = mapper.selectByPrimaryKey(sysRole.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
		if (sysRole.getRoleNo() != null) {
			SysRole checkConflict = mapper.selectByRoleNo(sysRole.getRoleNo(), sysRole.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "角色编号重复。");
			}
		}
		return mapper.updateByPrimaryKeySelective(sysRole);
	}

	@Override
	@Transactional
	public int delete(Long id, User loginUser) throws BusinessException {
		SysRole checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		this.sysRoleFunMapper.deleteByRoleNo(checkExist.getRoleNo());
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public SysRole selectByRoleNo(String roleNo) {
		return mapper.selectByRoleNo(roleNo, null);
	}

}

