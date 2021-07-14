package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cit.dmt.core.model.Country;
import com.cit.dmt.core.model.CountryLanguage;
import com.cit.dmt.core.model.Language;
import com.cit.dmt.core.service.CountryLanguageService;
import com.cit.dmt.core.service.CountryService;
import com.cit.dmt.core.service.LanguageService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/ct")
public class CountryLanguageController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CountryLanguageController.class);

	@Autowired
	private CountryLanguageService countryLanguageService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private LanguageService languageService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, CountryLanguage criteria) throws BusinessException {

		Page<CountryLanguage> searchResult = this.countryLanguageService.selectPaged(crp, criteria);

		ListData<CountryLanguage> wrapper = new ListData<CountryLanguage>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody CountryLanguage countryLanguage) throws BusinessException {
		this.checkCreate(countryLanguage);

		int result = this.countryLanguageService.save(countryLanguage, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody CountryLanguage countryLanguage) throws BusinessException {
		this.checkCreate(countryLanguage);

		int result = this.countryLanguageService.updateStatus(countryLanguage, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody CountryLanguage countryLanguage) throws BusinessException {
		this.checkDelete(countryLanguage);
		
		int result = this.countryLanguageService.deleteByCode(countryLanguage, authContext.get());
		return Root.create(result);
	}

	@GetMapping(value = { "/countries" })
	public Root countries(String status) throws BusinessException {
		List<Country> searchResult = this.countryService.selectHaveCt(status);

		ListData<Country> wrapper = new ListData<Country>();
		wrapper.setDataList(searchResult); 

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/languages" })
	public Root languages(String countryCode, String status) throws BusinessException {
		List<Language> searchResult = this.languageService.selectByCountry(countryCode, status);

		ListData<Language> wrapper = new ListData<Language>();
		wrapper.setDataList(searchResult); 

		return Root.create(wrapper);
	}
	
	private void checkCreate(CountryLanguage countryLanguage) throws BusinessException {
		if (countryLanguage == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (isEmpty(countryLanguage.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请选择国家。");
		}
		if (overLength(countryLanguage.getCountryCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
		}
		if (isEmpty(countryLanguage.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请选择语种。");
		}
		if (overLength(countryLanguage.getLanguageCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法参数。");
		}
		if (!(strEquals("0", countryLanguage.getStatus()) || strEquals("1", countryLanguage.getStatus()))) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请选择国家语种的状态。");
		}
	}

	
	private void checkDelete(CountryLanguage countryLanguage) throws BusinessException {
		if (countryLanguage == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		if (isEmpty(countryLanguage.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		if (isEmpty(countryLanguage.getLanguageCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
	}
}
