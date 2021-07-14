package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cit.dmt.core.model.FileUpload;
import com.cit.dmt.core.service.FileUploadService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/fileupload")
public class FileUploadController extends BaseController {

	Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FileUploadService fileUploadService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, FileUpload criteria) throws BusinessException {

		Page<FileUpload> searchResult = this.fileUploadService.selectPaged(crp, criteria);

		ListData<FileUpload> wrapper = new ListData<FileUpload>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		FileUpload fileUpload = this.fileUploadService.selectById(id);
		if (fileUpload == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(fileUpload);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody FileUpload fileUpload) throws BusinessException {
		this.checkCreate(fileUpload);

		int result = this.fileUploadService.save(fileUpload, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody FileUpload fileUpload) throws BusinessException {
		this.checkUpdate(fileUpload);

		int result = this.fileUploadService.update(fileUpload, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.fileUploadService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(FileUpload fileUpload) throws BusinessException {
		if (fileUpload == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(fileUpload.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(fileUpload.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, fileUpload.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(FileUpload fileUpload) throws BusinessException {
		if (fileUpload == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (fileUpload.getUserName() != null) {
//			if (isEmpty(fileUpload.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(fileUpload.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
