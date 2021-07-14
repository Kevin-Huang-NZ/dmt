package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
//import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cit.dmt.core.model.CtRoman;
import com.cit.dmt.core.service.CtRomanService;
import com.github.pagehelper.Page;

import com.alibaba.excel.EasyExcel;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/ct/roman")
public class CtRomanController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CtRomanController.class);

	@Autowired
	private CtRomanService ctRomanService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CtRoman criteria) throws BusinessException {

		Page<CtRoman> searchResult = this.ctRomanService.selectPaged(crp, criteria);

		ListData<CtRoman> wrapper = new ListData<CtRoman>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CtRoman ctRoman = this.ctRomanService.selectById(id);
		if (ctRoman == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(ctRoman);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CtRoman ctRoman) throws BusinessException {
		this.checkCreate(ctRoman);

		int result = this.ctRomanService.save(ctRoman, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CtRoman ctRoman) throws BusinessException {
		this.checkCreate(ctRoman);

		int result = this.ctRomanService.update(ctRoman, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.ctRomanService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}

	@PostMapping("/import")
	public Root handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("countryCode") String countryCode, @RequestParam("languageCode") String languageCode) throws BusinessException {

		if (isEmpty(countryCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"国家不能为空。");
		}
		if (overLength(countryCode, 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"国家参数非法。");
		}
		if (isEmpty(languageCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
		}
		if (overLength(languageCode, 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种参数非法。");
		}
		try {
//			List<Map<Integer, String>> sheet = null;
			List<CtRoman> sheet = null;
			try (InputStream inputStream = file.getInputStream()) {
				
				sheet = EasyExcel.read(inputStream).head(CtRoman.class).sheet().headRowNumber(1).doReadSync();
			}
			for(int i = 0; i < sheet.size(); i++) {
//			for (CtRoman ctRoman : sheet) {
				CtRoman ctRoman = sheet.get(i);
				ctRoman.setCountryCode(countryCode);
				ctRoman.setLanguageCode(languageCode);
				checkImport(ctRoman, i);
			}
			if (sheet == null || sheet.size() == 0) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传文件为空，没有导入任何数据。");
			}
			this.ctRomanService.saveBatch(sheet, authContext.get());
		}
		catch (IOException e) {
			throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "上传文件读取失败，请联系管理员。");
		}

		return Root.create(null);
	}
	
	@PostMapping(value = { "/clear" })
	public Root deleteAll(@RequestBody CtRoman ctRoman) throws BusinessException {
		this.checkDelete(ctRoman);
		int result = this.ctRomanService.deleteBatch(ctRoman.getCountryCode(), ctRoman.getLanguageCode(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CtRoman ctRoman) throws BusinessException {
		if (ctRoman == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (isEmpty(ctRoman.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"国家不能为空。");
		}
		if (overLength(ctRoman.getCountryCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
		}
		if (isEmpty(ctRoman.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
		}
		if (overLength(ctRoman.getLanguageCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
		}

		if (isEmpty(ctRoman.getOriginalAlpha())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文字母不能为空。");
		}
		if (overLength(ctRoman.getOriginalAlpha(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文字母超长，最多10个字符。");
		}

		if (isEmpty(ctRoman.getRomanAlpha())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母不能为空。");
		}
		if (overLength(ctRoman.getRomanAlpha(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母超长，最多10个字符。");
		}
		
	}

	
//	private void checkUpdate(CtRoman ctRoman) throws BusinessException {
//		if (ctRoman == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
//		}
//		if (ctRoman.getCountryCode() != null) {
//			if (isEmpty(ctRoman.getCountryCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"国家不能为空。");
//			}
//			if (overLength(ctRoman.getCountryCode(), 20)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
//			}
//		}
//		if (ctRoman.getLanguageCode() != null) {
//			if (isEmpty(ctRoman.getLanguageCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
//			}
//			if (overLength(ctRoman.getLanguageCode(), 20)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
//			}
//		}
//		if (ctRoman.getOriginalAlpha() != null) {
//			if (isEmpty(ctRoman.getOriginalAlpha())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文字母不能为空。");
//			}
//			if (overLength(ctRoman.getOriginalAlpha(), 10)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文字母超长，最多10个字符。");
//			}
//		}
//		if (ctRoman.getRomanAlpha() != null) {
//			if (isEmpty(ctRoman.getRomanAlpha())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母不能为空。");
//			}
//			if (overLength(ctRoman.getRomanAlpha(), 10)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母超长，最多10个字符。");
//			}
//		}
//	}
	
	private void checkDelete(CtRoman ctRoman) throws BusinessException {
		if (ctRoman == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		if (isEmpty(ctRoman.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		if (isEmpty(ctRoman.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
	}

	private void checkImport(CtRoman ctRoman, int index) throws BusinessException {
		int rowNo = index + 2;
		if (isEmpty(ctRoman.getOriginalAlpha())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文字母不能为空。行号: "+rowNo);
		}
		if (overLength(ctRoman.getOriginalAlpha(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文字母超长，最多10个字符。行号: "+rowNo);
		}

		if (isEmpty(ctRoman.getRomanAlpha())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母不能为空。行号: "+rowNo);
		}
		if (overLength(ctRoman.getRomanAlpha(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马字母超长，最多10个字符。行号: "+rowNo);
		}
		
	}
}
