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

import com.cit.dmt.core.model.Country;
import com.cit.dmt.core.service.CountryService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/country")
public class CountryController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CountryController.class);

	@Autowired
	private CountryService countryService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, Country criteria) throws BusinessException {

		Page<Country> searchResult = this.countryService.selectPaged(crp, criteria);

		ListData<Country> wrapper = new ListData<Country>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		Country country = this.countryService.selectById(id);
		if (country == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(country);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody Country country) throws BusinessException {
		this.checkCreate(country);

		int result = this.countryService.save(country, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody Country country) throws BusinessException {
		this.checkCreate(country);

		int result = this.countryService.update(country, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.countryService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}

	@GetMapping(value = { "/gbuk" })
	public Root getByUniqueKey(String countryCode) throws BusinessException {

		Country country = this.countryService.selectByCode(countryCode);
		if (country == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(country);
	}
	
	private void checkCreate(Country country) throws BusinessException {
		if (country == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(country.getCountryCode())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(country.getCountryCode(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????20????????????");
		}
		if (isEmpty(country.getCountryName())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(country.getCountryName(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????100????????????");
		}
	}

	
//	private void checkUpdate(Country country) throws BusinessException {
//		if (country == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
////		if (country.getUserName() != null) {
////			if (isEmpty(country.getUserName())) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
////			}
////			if (overLength(country.getUserName(), 50)) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????50????????????");
////			}
////		}
//	}
}
