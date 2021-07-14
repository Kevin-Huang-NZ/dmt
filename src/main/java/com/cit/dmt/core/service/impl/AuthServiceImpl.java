package com.cit.dmt.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cit.dmt.core.dao.SysRoleFunMapper;
import com.cit.dmt.core.dao.UserMapper;
import com.cit.dmt.core.model.SysRoleFunIJFun;
import com.cit.dmt.core.model.User;
import com.cit.dmt.core.service.AuthService;

import mahara.common.GlobalConst;
import mahara.util.RedisUtil;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class AuthServiceImpl implements AuthService {
	Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private SysRoleFunMapper sysRoleFunMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisUtil redisUtil;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getAndCacheRoleFuns() {
		Map<String, String> roleFuns = redisUtil.get(GlobalConst.KEY_ROLE_FUNS, Map.class);

		if (roleFuns == null) {
			roleFuns = new HashMap<String, String>();

			Map<String, List<String>> tmpMap = new HashMap<String, List<String>>();
			List<SysRoleFunIJFun> lsRoleFun = sysRoleFunMapper.loadSysRoleFun();
			if (lsRoleFun != null && lsRoleFun.size() > 0) {
				for (SysRoleFunIJFun one : lsRoleFun) {
					List<String> oneRoleFuns = tmpMap.get(one.getRoleNo());
					if (oneRoleFuns == null) {
						oneRoleFuns = new ArrayList<String>();
						tmpMap.put(one.getRoleNo(), oneRoleFuns);
					}
					oneRoleFuns.add("'" + one.getFunNo() + "'");
				}

				for (String roleNo : tmpMap.keySet()) {
					roleFuns.put(roleNo, String.join(";", tmpMap.get(roleNo)));
				}
			}

			redisUtil.set(GlobalConst.KEY_ROLE_FUNS, roleFuns);
		}

		return roleFuns;
	}

	@Override
	public boolean checkAuth(String roles, String action) {
//		logger.info("roleNo is {}", roleNo);
		if (StringUtils.isEmpty(roles)) {
			return false;
		}
		Map<String, String> roleFuns = this.getAndCacheRoleFuns();
		if (roleFuns == null) {
			return false;
		}
		
		String[] roleArr = roles.split(";");
		
		boolean hasAuth = false;
		for (String role : roleArr) {
			String funs = roleFuns.get(role);
			if (!StringUtils.isEmpty(funs)) {
				if (StringUtils.isEmpty(action)) {
					hasAuth = true;
					break;
				} else {
					if (funs.indexOf("'" + action + "'") != -1) {
						hasAuth = true;
						break;
					}
				}
			}
		}
		
		return hasAuth;
	}
	
	@Override
	public User userLogin(String phone, String password) throws BusinessException {
		if (StringUtils.isEmpty(phone)) {
			return null;
		}

		User user = userMapper.selectByPhone(phone, null);
		if (user == null) {
			return null;
		}

		if (StringUtils.equals(password, user.getPassword()) && StringUtils.equals("1", user.getStatus())) {
			return user;
		} else {
			return null;
		}
	}
	
	@Override
	public int userChgPwd(Long loginId, String oldPwd, String newPwd) throws BusinessException {
		User user = userMapper.selectByPrimaryKey(loginId);
		
		if (user == null ) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "用户不存在。");
		}
		
		if (!StringUtils.equals(user.getPassword(), oldPwd)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "原密码错误。");
		}
		user.setPassword(newPwd);

		return userMapper.updateByPrimaryKey(user);
	}

}
