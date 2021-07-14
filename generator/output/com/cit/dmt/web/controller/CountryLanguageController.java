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

import com.cit.dmt.core.model.CountryLanguage;
import com.cit.dmt.core.service.CountryLanguageService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/countrylanguage")
public class CountryLanguageController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CountryLanguageController.class);

	@Autowired
	private CountryLanguageService countryLanguageService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CountryLanguage criteria) throws BusinessException {

		Page<CountryLanguage> searchResult = this.countryLanguageService.selectPaged(crp, criteria);

		ListData<CountryLanguage> wrapper = new ListData<CountryLanguage>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		CountryLanguage countryLanguage = this.countryLanguageService.selectById(id);
		if (countryLanguage == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(countryLanguage);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CountryLanguage countryLanguage) throws BusinessException {
		this.checkCreate(countryLanguage);

		int result = this.countryLanguageService.save(countryLanguage, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CountryLanguage countryLanguage) throws BusinessException {
		this.checkUpdate(countryLanguage);

		int result = this.countryLanguageService.update(countryLanguage, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.countryLanguageService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(CountryLanguage countryLanguage) throws BusinessException {
		if (countryLanguage == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
//		if (isEmpty(countryLanguage.getUserName())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//		}
//		if (overLength(countryLanguage.getUserName(), 50)) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//		}
//		if (!patternCheck(PATTERN_MOBILE, countryLanguage.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的手机号。");
//		}
	}

	
	private void checkUpdate(CountryLanguage countryLanguage) throws BusinessException {
		if (countryLanguage == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (countryLanguage.getUserName() != null) {
//			if (isEmpty(countryLanguage.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(countryLanguage.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
