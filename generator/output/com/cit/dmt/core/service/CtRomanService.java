package com.cit.dmt.core.service;

import com.cit.dmt.core.model.CtRoman;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface CtRomanService {
	Page<CtRoman> selectPaged(CommonRequestParam crp, CtRoman criteria) throws BusinessException;
	
	CtRoman selectById(Long id);

	int save(CtRoman ctRoman, User loginUser) throws BusinessException;

	int update(CtRoman ctRoman, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
