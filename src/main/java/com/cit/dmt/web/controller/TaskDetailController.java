package com.cit.dmt.web.controller;

import static mahara.util.CheckUtil.*;

import java.io.IOException;
import java.io.InputStream;
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
import com.cit.dmt.core.model.TaskDetail;
import com.cit.dmt.core.service.TaskDetailService;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.param.IdWrap;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;
import mahara.web.response.json.ListData;
import mahara.web.response.json.PaginationInfo;
import mahara.web.response.json.Root;

@RestController
@RequestMapping("/api/taskdetail")
public class TaskDetailController extends BaseController {

	Logger logger = LoggerFactory.getLogger(TaskDetailController.class);

	@Autowired
	private TaskDetailService taskDetailService;
	
	@GetMapping(value = { "/l" })
	public Root list(CommonRequestParam crp, TaskDetail criteria) throws BusinessException {

		Page<TaskDetail> searchResult = this.taskDetailService.selectPaged(crp, criteria);

		ListData<TaskDetail> wrapper = new ListData<TaskDetail>();
		wrapper.setDataList(searchResult.getResult()); 

		PaginationInfo paginationInfo = convertPage(searchResult);
		wrapper.setPagination(paginationInfo);

		return Root.create(wrapper);
	}

	@GetMapping(value = { "/r" })
	public Root read(Long id) throws BusinessException {

		TaskDetail taskDetail = this.taskDetailService.selectById(id);
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		return Root.create(taskDetail);
	}
	
	@PostMapping(value = { "/c" })
	public Root create(@RequestBody TaskDetail taskDetail) throws BusinessException {
		this.checkCreate(taskDetail);

		int result = this.taskDetailService.save(taskDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/u" })
	public Root update(@RequestBody TaskDetail taskDetail) throws BusinessException {
		this.checkCreate(taskDetail);

		int result = this.taskDetailService.update(taskDetail, authContext.get());

		return Root.create(result);
	}

	@PostMapping(value = { "/d" })
	public Root delete(@RequestBody IdWrap id) throws BusinessException {
		int result = this.taskDetailService.delete(id.getId(), authContext.get());
		return Root.create(result);
	}
	/**
	 * ??????????????????
	 * 
	 * @param task
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(value = { "/ft" })
	public Root finalTrans(@RequestBody TaskDetail taskDetail) throws BusinessException {
		this.checkFinalTrans(taskDetail);

		int result = this.taskDetailService.finalTrans(taskDetail.getId(), taskDetail.getTransResult(), authContext.get());

		return Root.create(result);
	}


	@PostMapping("/import")
	public Root handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("projectId") Long projectId, @RequestParam("taskId") Long taskId) throws BusinessException {

		if (isEmpty(projectId)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		if (isEmpty(taskId)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		try {
//			List<Map<Integer, String>> sheet = null;
			List<TaskDetail> sheet = null;
			try (InputStream inputStream = file.getInputStream()) {
				
				sheet = EasyExcel.read(inputStream).head(TaskDetail.class).sheet().headRowNumber(1).doReadSync();
			}
			for(int i = 0; i < sheet.size(); i++) {
				TaskDetail taskDetail = sheet.get(i);
				taskDetail.setProjectId(projectId);
				taskDetail.setTaskId(taskId);
				// ???????????????
				taskDetail.setTransStatus("0");
				taskDetail.setCheckStatus("0");
				checkImport(taskDetail, i);
			}
			if (sheet == null || sheet.size() == 0) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "????????????????????????????????????????????????");
			}
			this.taskDetailService.saveBatch(projectId, taskId, sheet, authContext.get());
		}
		catch (IOException e) {
			throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "????????????????????????????????????????????????");
		}

		return Root.create(null);
	}
	
	@PostMapping(value = { "/clear" })
	public Root deleteAll(@RequestBody TaskDetail taskDetail) throws BusinessException {
		this.checkDelete(taskDetail);
		int result = this.taskDetailService.deleteBatch(taskDetail.getProjectId(), taskDetail.getTaskId(), authContext.get());
		return Root.create(result);
	}
	
	private void checkCreate(TaskDetail taskDetail) throws BusinessException {
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}

		if (isEmpty(taskDetail.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		
		if (isEmpty(taskDetail.getTaskId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
		}
		
		if (isEmpty(taskDetail.getOriginal())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(taskDetail.getOriginal(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????200????????????");
		}

		if (overLength(taskDetail.getCountry(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????100????????????");
		}

		if (overLength(taskDetail.getLanguage(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????100????????????");
		}

		if (overLength(taskDetail.getGec(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????100????????????");
		}
		
		if (overLength(taskDetail.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500????????????");
		}
	}
	
	
	private void checkFinalTrans(TaskDetail taskDetail) throws BusinessException {
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}

		if (isEmpty(taskDetail.getId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"ID???????????????");
		}
		
		if (isEmpty(taskDetail.getTransResult())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????");
		}
		if (overLength(taskDetail.getTransResult(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????200????????????");
		}
	}

	private void checkDelete(TaskDetail taskDetail) throws BusinessException {
		if (taskDetail == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		if (isEmpty(taskDetail.getProjectId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
		if (isEmpty(taskDetail.getTaskId())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
		}
	}

	private void checkImport(TaskDetail taskDetail, int index) throws BusinessException {
		int rowNo = index + 2;

		if (isEmpty(taskDetail.getOriginal())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????: "+rowNo);
		}
		if (overLength(taskDetail.getOriginal(), 200)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????????????????200??????????????????: "+rowNo);
		}

		if (overLength(taskDetail.getCountry(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????100??????????????????: "+rowNo);
		}

		if (overLength(taskDetail.getLanguage(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????100??????????????????: "+rowNo);
		}

		if (overLength(taskDetail.getGec(), 100)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????????????????100??????????????????: "+rowNo);
		}
		
		if (overLength(taskDetail.getMemo(), 500)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????500??????????????????: "+rowNo);
		}
		
	}
	
//	private void checkUpdate(TaskDetail taskDetail) throws BusinessException {
//		if (taskDetail == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"???????????????");
//		}
////		if (taskDetail.getUserName() != null) {
////			if (isEmpty(taskDetail.getUserName())) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????");
////			}
////			if (overLength(taskDetail.getUserName(), 50)) {
////				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"?????????????????????50????????????");
////			}
////		}
//	}
}
