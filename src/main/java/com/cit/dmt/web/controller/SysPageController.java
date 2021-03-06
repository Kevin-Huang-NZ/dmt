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

import com.cit.dmt.core.model.SysPage;
import com.cit.dmt.core.service.SysPageService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/syspage")
public class SysPageController extends BaseController {

	Logger logger = LoggerFactory.getLogger(SysPageController.class);

	@Autowired
	private SysPageService sysPageService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, SysPage criteria) throws BusinessException {

		Page<SysPage> searchResult = this.sysPageService.selectPaged(crp, criteria);

		ListData<SysPage> wrapper = new ListData<SysPage>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		SysPage sysPage = this.sysPageService.selectById(id);
		if (sysPage == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(sysPage);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody SysPage sysPage) throws BusinessException {
		this.checkCreate(sysPage);

		int result = this.sysPageService.save(sysPage, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody SysPage sysPage) throws BusinessException {
		this.checkCreate(sysPage);

		int result = this.sysPageService.update(sysPage, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.sysPageService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(SysPage sysPage) throws BusinessException {
		if (sysPage == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		
		if (isEmpty(sysPage.getPageName())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(sysPage.getPageName(), 50)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????50????????????");
		}
		
		if (isEmpty(sysPage.getPageTitle())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (overLength(sysPage.getPageTitle(), 50)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????50????????????");
		}
		if (overLength(sysPage.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
		}
//		if (!patternCheck(PATTERN_MOBILE, sysPage.getPhone())) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"????????????????????????????????????");
//		}
	}

	
//	private void checkUpdate(SysPage sysPage) throws BusinessException {
//		if (sysPage == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
////		if (sysPage.getUserName() != null) {
////			if (isEmpty(sysPage.getUserName())) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
////			}
////			if (overLength(sysPage.getUserName(), 50)) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????50????????????");
////			}
////		}
//	}
}
