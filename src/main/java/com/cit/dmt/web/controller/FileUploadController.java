package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cit.dmt.core.model.FileUpload;
import com.cit.dmt.core.service.FileUploadService;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/file")
public class FileUploadController extends BaseController {

	Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping("/upload")
	public Root handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("tableName") String tableName, @RequestParam("rowId") Long rowId) throws BusinessException {
		FileUpload params = new FileUpload(tableName, rowId);
		check(params);
		FileUpload fileUploaded = fileUploadService.store(file, params);

		return Root.create(fileUploaded);
	}
	
	private void check(FileUpload fileUpload) throws BusinessException {
		if (fileUpload == null || isEmpty(fileUpload.getTableName()) || fileUpload.getRowId() == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "未指定上传文件用途，上传失败。");
		}
	}
	
}
