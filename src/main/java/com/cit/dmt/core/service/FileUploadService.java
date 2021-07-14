package com.cit.dmt.core.service;

import org.springframework.web.multipart.MultipartFile;

import com.cit.dmt.core.model.FileUpload;

import mahara.web.response.error.BusinessException;

public interface FileUploadService {
	void init() throws BusinessException;
	FileUpload store(MultipartFile file, FileUpload record) throws BusinessException;
}
