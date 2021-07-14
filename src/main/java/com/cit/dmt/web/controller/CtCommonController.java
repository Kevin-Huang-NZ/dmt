package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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

import com.alibaba.excel.EasyExcel;
import com.cit.dmt.core.model.CtCommon;
import com.cit.dmt.core.service.CtCommonService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/ct/common")
public class CtCommonController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CtCommonController.class);

	@Autowired
	private CtCommonService ctCommonService;
	
	private final String[] ORIGINAL_TYPE = {"1", "2", "3", "x"};
	
	private final String[] MATCH_WAY = {"1", "2", "3", "4", "5"};
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CtCommon criteria) throws BusinessException {

		Page<CtCommon> searchResult = this.ctCommonService.selectPaged(crp, criteria);

		ListData<CtCommon> wrapper = new ListData<CtCommon>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CtCommon ctCommon = this.ctCommonService.selectById(id);
		if (ctCommon == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(ctCommon);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CtCommon ctCommon) throws BusinessException {
		this.checkCreate(ctCommon);

		int result = this.ctCommonService.save(ctCommon, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CtCommon ctCommon) throws BusinessException {
		this.checkCreate(ctCommon);

		int result = this.ctCommonService.update(ctCommon, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.ctCommonService.delete(id.getId(), authContext.get());
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
			List<CtCommon> sheet = null;
			try (InputStream inputStream = file.getInputStream()) {
				
				sheet = EasyExcel.read(inputStream).head(CtCommon.class).sheet().headRowNumber(1).doReadSync();
			}
			for(int i = 0; i < sheet.size(); i++) {
				CtCommon ctCommon = sheet.get(i);
				ctCommon.setCountryCode(countryCode);
				ctCommon.setLanguageCode(languageCode);
				checkImport(ctCommon, i);
			}
			if (sheet == null || sheet.size() == 0) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传文件为空，没有导入任何数据。");
			}
			this.ctCommonService.saveBatch(sheet, authContext.get());
		}
		catch (IOException e) {
			throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "上传文件读取失败，请联系管理员。");
		}

		return Root.create(null);
	}
	
	@PostMapping(value = { "/clear" })
	public Root deleteAll(@RequestBody CtCommon ctCommon) throws BusinessException {
		this.checkDelete(ctCommon);
		int result = this.ctCommonService.deleteBatch(ctCommon.getCountryCode(), ctCommon.getLanguageCode(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CtCommon ctCommon) throws BusinessException {
		if (ctCommon == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (isEmpty(ctCommon.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"国家不能为空。");
		}
		if (overLength(ctCommon.getCountryCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
		}
		if (isEmpty(ctCommon.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
		}
		if (overLength(ctCommon.getLanguageCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
		}
		
		if (isEmpty(ctCommon.getOriginal())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文不能为空。");
		}
		if (overLength(ctCommon.getOriginal(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文超长，最多200个字符。");
		}
		
		if (overLength(ctCommon.getOriginalAbbr(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文缩写超长，最多100个字符。");
		}
		
		if (isEmpty(ctCommon.getOriginalType())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文类型不能为空。");
		}
		if (!Arrays.asList(ORIGINAL_TYPE).contains(ctCommon.getOriginalType())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文类型参数非法。");
		}
		
		if (overLength(ctCommon.getRoman(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马转写超长，最多200个字符。");
		}
		
		if (isEmpty(ctCommon.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式不能为空。");
		}
		if (!Arrays.asList(MATCH_WAY).contains(ctCommon.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式参数非法。");
		}
		
		if (strEquals(ctCommon.getMatchWay(), "4") || strEquals(ctCommon.getMatchWay(), "5")) {
			if (isEmpty(ctCommon.getMatchParams())) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式为前置/后置时，匹配参数不能为空。");
			}
			if (overLength(ctCommon.getMatchParams(), 200)) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配参数超长，最多200个字符。");
			}
		}
		
		if (isEmpty(ctCommon.getTransliteration()) && isEmpty(ctCommon.getFreeTranslation())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"音译和意译至少有一个不为空。");
		}
		if (overLength(ctCommon.getTransliteration(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"音译超长，最多200个字符。");
		}
		if (overLength(ctCommon.getFreeTranslation(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"意译超长，最多200个字符。");
		}
	}

	
//	private void checkUpdate(CtCommon ctCommon) throws BusinessException {
//		if (ctCommon == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
//		}
//		if (ctCommon.getCountryCode() != null) {
//			if (isEmpty(ctCommon.getCountryCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"国家不能为空。");
//			}
//			if (overLength(ctCommon.getCountryCode(), 20)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
//			}
//		}
//		if (ctCommon.getLanguageCode() != null) {
//			if (isEmpty(ctCommon.getLanguageCode())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"语种不能为空。");
//			}
//			if (overLength(ctCommon.getLanguageCode(), 20)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
//			}
//		}
//
//		if (ctCommon.getOriginal() != null) {
//			if (isEmpty(ctCommon.getOriginal())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文不能为空。");
//			}
//			if (overLength(ctCommon.getOriginal(), 200)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文超长，最多200个字符。");
//			}
//		}
//
//		if (ctCommon.getOriginalAbbr() != null) {
//			if (overLength(ctCommon.getOriginalAbbr(), 100)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文缩写超长，最多100个字符。");
//			}
//		}
//
//		if (ctCommon.getOriginalType() != null) {
//			if (isEmpty(ctCommon.getOriginalType())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文类型不能为空。");
//			}
//			if (!Arrays.asList(ORIGINAL_TYPE).contains(ctCommon.getOriginalType())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文类型参数非法。");
//			}
//		}
//
//		if (ctCommon.getRoman() != null) {
//			if (overLength(ctCommon.getRoman(), 200)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马转写超长，最多200个字符。");
//			}
//		}
//
//		if (ctCommon.getMatchWay() != null) {
//			if (isEmpty(ctCommon.getMatchWay())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式不能为空。");
//			}
//			if (!Arrays.asList(MATCH_WAY).contains(ctCommon.getMatchWay())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式参数非法。");
//			}
//		}
//
//		if (ctCommon.getMatchParams() != null) {
//			if (strEquals(ctCommon.getMatchWay(), "4") || strEquals(ctCommon.getMatchWay(), "5")) {
//				if (isEmpty(ctCommon.getMatchParams())) {
//					throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式为前置/后置时，匹配参数不能为空。");
//				}
//				if (overLength(ctCommon.getMatchParams(), 200)) {
//					throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配参数超长，最多200个字符。");
//				}
//			}
//		}
//
//		if (ctCommon.getTransliteration() != null || ctCommon.getFreeTranslation() != null) {
//			if (isEmpty(ctCommon.getTransliteration()) && isEmpty(ctCommon.getFreeTranslation())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"音译和意译至少有一个不为空。");
//			}
//			if (overLength(ctCommon.getTransliteration(), 200)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"音译超长，最多200个字符。");
//			}
//			if (overLength(ctCommon.getFreeTranslation(), 200)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"意译超长，最多200个字符。");
//			}
//		}
//	}
	
	private void checkDelete(CtCommon ctCommon) throws BusinessException {
		if (ctCommon == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		if (isEmpty(ctCommon.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		if (isEmpty(ctCommon.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
	}

	private void checkImport(CtCommon ctCommon, int index) throws BusinessException {
		int rowNo = index + 2;
		
		if (isEmpty(ctCommon.getOriginal())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文不能为空。行号: "+rowNo);
		}
		if (overLength(ctCommon.getOriginal(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文超长，最多200个字符。行号: "+rowNo);
		}
		
		if (overLength(ctCommon.getOriginalAbbr(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文缩写超长，最多100个字符。行号: "+rowNo);
		}
		
		if (isEmpty(ctCommon.getOriginalType())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文类型不能为空。行号: "+rowNo);
		}
		if (!Arrays.asList(ORIGINAL_TYPE).contains(ctCommon.getOriginalType())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"原文类型参数非法。行号: "+rowNo);
		}
		
		if (overLength(ctCommon.getRoman(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"罗马转写超长，最多200个字符。行号: "+rowNo);
		}
		
		if (isEmpty(ctCommon.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式不能为空。行号: "+rowNo);
		}
		if (!Arrays.asList(MATCH_WAY).contains(ctCommon.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式参数非法。行号: "+rowNo);
		}
		
		if (strEquals(ctCommon.getMatchWay(), "4") || strEquals(ctCommon.getMatchWay(), "5")) {
			if (isEmpty(ctCommon.getMatchParams())) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配方式为前置/后置时，匹配参数不能为空。行号: "+rowNo);
			}
			if (overLength(ctCommon.getMatchParams(), 200)) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"匹配参数超长，最多200个字符。行号: "+rowNo);
			}
		}
		
		if (isEmpty(ctCommon.getTransliteration()) && isEmpty(ctCommon.getFreeTranslation())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"音译和意译至少有一个不为空。行号: "+rowNo);
		}
		if (overLength(ctCommon.getTransliteration(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"音译超长，最多200个字符。行号: "+rowNo);
		}
		if (overLength(ctCommon.getFreeTranslation(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"意译超长，最多200个字符。行号: "+rowNo);
		}
	}
}
