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
import com.cit.dmt.core.model.CtTransliteration;
import com.cit.dmt.core.service.CtTransliterationService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/ct/transliteration")
public class CtTransliterationController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CtTransliterationController.class);

	@Autowired
	private CtTransliterationService ctTransliterationService;

	private final String[] MATCH_WAY = {"1", "2", "3", "4", "5"};
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CtTransliteration criteria) throws BusinessException {

		Page<CtTransliteration> searchResult = this.ctTransliterationService.selectPaged(crp, criteria);

		ListData<CtTransliteration> wrapper = new ListData<CtTransliteration>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CtTransliteration ctTransliteration = this.ctTransliterationService.selectById(id);
		if (ctTransliteration == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(ctTransliteration);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CtTransliteration ctTransliteration) throws BusinessException {
		this.checkCreate(ctTransliteration);

		int result = this.ctTransliterationService.save(ctTransliteration, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CtTransliteration ctTransliteration) throws BusinessException {
		this.checkCreate(ctTransliteration);

		int result = this.ctTransliterationService.update(ctTransliteration, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.ctTransliterationService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}

	@PostMapping("/import")
	public Root handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("countryCode") String countryCode, @RequestParam("languageCode") String languageCode) throws BusinessException {

		if (isEmpty(countryCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(countryCode, 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (isEmpty(languageCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(languageCode, 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		try {
//			List<Map<Integer, String>> sheet = null;
			List<CtTransliteration> sheet = null;
			try (InputStream inputStream = file.getInputStream()) {
				
				sheet = EasyExcel.read(inputStream).head(CtTransliteration.class).sheet().headRowNumber(1).doReadSync();
			}
			for(int i = 0; i < sheet.size(); i++) {
				CtTransliteration ctTransliteration = sheet.get(i);
				ctTransliteration.setCountryCode(countryCode);
				ctTransliteration.setLanguageCode(languageCode);
				checkImport(ctTransliteration, i);
			}
			if (sheet == null || sheet.size() == 0) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "????????????????????????????????????????????????");
			}
			this.ctTransliterationService.saveBatch(sheet, authContext.get());
		}
		catch (IOException e) {
			throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "????????????????????????????????????????????????");
		}

		return Root.create(null);
	}
	
	@PostMapping(value = { "/clear" })
	public Root deleteAll(@RequestBody CtTransliteration ctTransliteration) throws BusinessException {
		this.checkDelete(ctTransliteration);
		int result = this.ctTransliterationService.deleteBatch(ctTransliteration.getCountryCode(), ctTransliteration.getLanguageCode(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CtTransliteration ctTransliteration) throws BusinessException {
		if (ctTransliteration == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(ctTransliteration.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(ctTransliteration.getCountryCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		if (isEmpty(ctTransliteration.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(ctTransliteration.getLanguageCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(ctTransliteration.getOriginal())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(ctTransliteration.getOriginal(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????20????????????");
		}
		
		if (overLength(ctTransliteration.getRoman(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????20????????????");
		}
		
		if (isEmpty(ctTransliteration.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (!Arrays.asList(MATCH_WAY).contains(ctTransliteration.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		
		if (strEquals(ctTransliteration.getMatchWay(), "4") || strEquals(ctTransliteration.getMatchWay(), "5")) {
			if (isEmpty(ctTransliteration.getMatchParams())) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????/???????????????????????????????????????");
			}
			if (overLength(ctTransliteration.getMatchParams(), 20)) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????20????????????");
			}
		}
		
		if (isEmpty(ctTransliteration.getChinese())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(ctTransliteration.getChinese(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????10????????????");
		}
	}
	private void checkDelete(CtTransliteration ctTransliteration) throws BusinessException {
		if (ctTransliteration == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		if (isEmpty(ctTransliteration.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		if (isEmpty(ctTransliteration.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
	}

	private void checkImport(CtTransliteration ctTransliteration, int index) throws BusinessException {
		int rowNo = index + 2;

		if (isEmpty(ctTransliteration.getOriginal())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????: "+rowNo);
		}
		if (overLength(ctTransliteration.getOriginal(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????20??????????????????: "+rowNo);
		}
		
		if (overLength(ctTransliteration.getRoman(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????20??????????????????: "+rowNo);
		}
		
		if (isEmpty(ctTransliteration.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????: "+rowNo);
		}
		if (!Arrays.asList(MATCH_WAY).contains(ctTransliteration.getMatchWay())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????: "+rowNo);
		}
		
		if (strEquals(ctTransliteration.getMatchWay(), "4") || strEquals(ctTransliteration.getMatchWay(), "5")) {
			if (isEmpty(ctTransliteration.getMatchParams())) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????/?????????????????????????????????????????????: "+rowNo);
			}
			if (overLength(ctTransliteration.getMatchParams(), 20)) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????20??????????????????: "+rowNo);
			}
		}
		
		if (isEmpty(ctTransliteration.getChinese())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????: "+rowNo);
		}
		if (overLength(ctTransliteration.getChinese(), 10)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????10??????????????????: "+rowNo);
		}
	}

}
