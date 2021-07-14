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

import com.cit.dmt.core.model.Language;
import com.cit.dmt.core.service.LanguageService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/language")
public class LanguageController extends BaseController {

	Logger logger = LoggerFactory.getLogger(LanguageController.class);

	@Autowired
	private LanguageService languageService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, Language criteria) throws BusinessException {

		Page<Language> searchResult = this.languageService.selectPaged(crp, criteria);

		ListData<Language> wrapper = new ListData<Language>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		Language language = this.languageService.selectById(id);
		if (language == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(language);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody Language language) throws BusinessException {
		this.checkCreate(language);

		int result = this.languageService.save(language, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody Language language) throws BusinessException {
		this.checkUpdate(language);

		int result = this.languageService.update(language, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.languageService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(Language language) throws BusinessException {
		if (language == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(language.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(language.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, language.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(Language language) throws BusinessException {
		if (language == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (language.getUserName() != null) {
//			if (isEmpty(language.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(language.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
