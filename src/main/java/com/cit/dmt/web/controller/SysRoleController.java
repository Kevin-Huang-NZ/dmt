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

import com.cit.dmt.core.model.SysRole;
import com.cit.dmt.core.service.SysRoleService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/sysrole")
public class SysRoleController extends BaseController {

	Logger logger = LoggerFactory.getLogger(SysRoleController.class);

	@Autowired
	private SysRoleService sysRoleService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, SysRole criteria) throws BusinessException {

		Page<SysRole> searchResult = this.sysRoleService.selectPaged(crp, criteria);

		ListData<SysRole> wrapper = new ListData<SysRole>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		SysRole sysRole = this.sysRoleService.selectById(id);
		if (sysRole == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(sysRole);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody SysRole sysRole) throws BusinessException {
		this.checkCreate(sysRole);

		int result = this.sysRoleService.save(sysRole, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody SysRole sysRole) throws BusinessException {
		this.checkUpdate(sysRole);

		int result = this.sysRoleService.update(sysRole, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.sysRoleService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}

	@GetMapping(value = { "/gbuk" })
	public Root getByUniqueKey(String roleNo) throws BusinessException {

		SysRole sysRole = this.sysRoleService.selectByRoleNo(roleNo);
		if (sysRole == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(sysRole);
	}
	
	private void checkCreate(SysRole sysRole) throws BusinessException {
		if (sysRole == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
		
		if (!patternCheck(PATTERN_ONE_ALPHABET_NUMBER, sysRole.getRoleNo())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请填写正确格式的角色编号,角色编号为1位字母或数字。");
		}
		
		if (isEmpty(sysRole.getRoleName())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"名称不能为空。");
		}
		if (overLength(sysRole.getRoleNo(), 20)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"名称超长，最多20个字符。");
		}
		
		if (overLength(sysRole.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"备注超长，最多500个字符。");
		}
	}

	
	private void checkUpdate(SysRole sysRole) throws BusinessException {
		if (sysRole == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误。");
		}
//		if (sysRole.getUserName() != null) {
//			if (isEmpty(sysRole.getUserName())) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名不能为空。");
//			}
//			if (overLength(sysRole.getUserName(), 50)) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"姓名超长，最多50个字符。");
//			}
//		}
	}
}
