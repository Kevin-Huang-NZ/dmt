package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CheckRound;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CheckRoundService {
	Page<CheckRound> selectPaged(CommonRequestParam crp, CheckRound criteria) throws BusinessException;
	
	CheckRound selectById(Long id);

	int save(CheckRound checkRound, User loginUser) throws BusinessException;

	int update(CheckRound checkRound, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
