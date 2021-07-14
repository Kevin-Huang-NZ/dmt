package com.cit.dmt.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.SysRoleFunMapper;
import com.cit.dmt.core.model.SysRoleFun;
import com.cit.dmt.core.service.SysRoleFunService;
import com.cit.dmt.core.model.User;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class SysRoleFunServiceImpl implements SysRoleFunService {
	Logger logger = LoggerFactory.getLogger(SysRoleFunService.class);

	@Autowired
	private SysRoleFunMapper mapper;
	
	@Override
	public List<SysRoleFun> selectByRoleNo(String roleNo) throws BusinessException {
		if (StringUtils.isEmpty(roleNo)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}

		return mapper.selectByRoleNo(roleNo);
	}

	@Override
	public int save(SysRoleFun sysRoleFun, User loginUser) throws BusinessException {
//		if (sysRoleFun == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//		}
//		if (StringUtils.isEmpty(sysRoleFun.getRoleNo()) || StringUtils.isEmpty(sysRoleFun.getFunNo())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//		}
		List<SysRoleFun> checkConflict = mapper.selectByNo(sysRoleFun.getRoleNo(), sysRoleFun.getFunNo());
		if (checkConflict != null && checkConflict.size() > 0) {
			return 0;
		}
		return mapper.insert(sysRoleFun);
	}
	@Override
	public int deleteByNo(SysRoleFun sysRoleFun, User loginUser) throws BusinessException {
//		if (sysRoleFun == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//		}
//		if (StringUtils.isEmpty(sysRoleFun.getRoleNo()) || StringUtils.isEmpty(sysRoleFun.getFunNo())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//		}
		return mapper.deleteByNo(sysRoleFun.getRoleNo(), sysRoleFun.getFunNo());
	}

}

