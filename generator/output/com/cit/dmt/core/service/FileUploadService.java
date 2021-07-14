package com.cit.dmt.core.service;

import com.cit.dmt.core.model.FileUpload;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface FileUploadService {
	Page<FileUpload> selectPaged(CommonRequestParam crp, FileUpload criteria) throws BusinessException;
	
	FileUpload selectById(Long id);

	int save(FileUpload fileUpload, User loginUser) throws BusinessException;

	int update(FileUpload fileUpload, User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
