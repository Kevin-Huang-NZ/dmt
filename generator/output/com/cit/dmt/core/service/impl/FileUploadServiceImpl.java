package com.cit.dmt.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.cit.dmt.core.dao.FileUploadMapper;
import com.cit.dmt.core.model.FileUpload;
import com.cit.dmt.core.service.FileUploadService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	Logger logger = LoggerFactory.getLogger(FileUploadService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private FileUploadMapper mapper;
	
	@Override
	public Page<FileUpload> selectPaged(CommonRequestParam crp, FileUpload qp) throws BusinessException {
		if (crp == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(crp.getOrder())) {
			criteria.put("orderByClause", crp.getOrder() + "," + defaultOrderBy);
		} else {
			criteria.put("orderByClause", defaultOrderBy);
		}
//		if (qp != null) {
//			if (!StringUtils.isEmpty(qp.getPhone())) {
//				criteria.put("phone", qp.getPhone() + "%");
//			}
//		}
		
		Page<FileUpload> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public FileUpload selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(FileUpload fileUpload, User loginUser) throws BusinessException {
//		FileUpload checkConflict = mapper.selectByPhone(fileUpload.getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert(fileUpload);
	}

	@Override
	public int update(FileUpload fileUpload, User loginUser) throws BusinessException {
		FileUpload checkExist = mapper.selectByPrimaryKey(fileUpload.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if (fileUpload.getPhone() != null) {
//			FileUpload checkConflict = mapper.selectByPhone(fileUpload.getPhone(), fileUpload.getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective(fileUpload);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		FileUpload checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

