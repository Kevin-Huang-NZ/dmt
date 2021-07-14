package com.cit.dmt.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.ahocorasick.trie.PayloadEmit;
import org.ahocorasick.trie.PayloadTrie;
import org.ahocorasick.trie.PayloadTrie.PayloadTrieBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.cit.dmt.core.dao.CtCommonMapper;
import com.cit.dmt.core.dao.CtRomanMapper;
import com.cit.dmt.core.dao.CtTransliterationMapper;
import com.cit.dmt.core.dao.ProjectMapper;
import com.cit.dmt.core.dao.TaskDetailMapper;
import com.cit.dmt.core.dao.TaskMapper;
import com.cit.dmt.core.model.CtCommon;
import com.cit.dmt.core.model.CtRoman;
import com.cit.dmt.core.model.CtTransliteration;
import com.cit.dmt.core.model.Project;
import com.cit.dmt.core.model.Task;
import com.cit.dmt.core.model.TaskDetail;
import com.cit.dmt.core.service.TaskService;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.util.DateTimeUtil;
import mahara.util.ListUtils;
import mahara.util.NumberUtils;
import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class TaskServiceImpl implements TaskService {
	Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private TaskMapper mapper;
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private TaskDetailMapper taskDetailMapper;
	@Autowired
	private CtRomanMapper ctRomanMapper;
	@Autowired
	private CtCommonMapper ctCommonMapper;
	@Autowired
	private CtTransliterationMapper ctTransliterationMapper;

	@Autowired
	private DataSourceTransactionManager dataSourceTransactionManager;
	@Autowired
	private TransactionDefinition transactionDefinition;

	@Override
	public Page<Task> selectPaged(CommonRequestParam crp, Task qp) throws BusinessException {
		if (crp == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(crp.getOrder())) {
			criteria.put("orderByClause", crp.getOrder() + "," + defaultOrderBy);
		} else {
			criteria.put("orderByClause", defaultOrderBy);
		}
		if (qp != null) {
			if (!NumberUtils.isNull(qp.getProjectId())) {
				criteria.put("projectId", qp.getProjectId());
			}
			if (!StringUtils.isEmpty(qp.getTaskName())) {
				criteria.put("taskName", "%" + qp.getTaskName() + "%");
			}
			if (!StringUtils.isEmpty(qp.getStatus())) {
				criteria.put("status", qp.getStatus());
			}
		}

		Page<Task> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public Task selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save(Task task, User loginUser) throws BusinessException {
		Task checkConflict = mapper.selectByName(task.getProjectId(), task.getTaskName(), null);
		if (checkConflict != null) {
			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "同一项目下的任务名称重复。");
		}
		Project project = projectMapper.selectByPrimaryKey(task.getProjectId());
		if (project == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "项目不存在。");
		}
		task.setCreateDate(DateTimeUtil.getCurrentTimeYMD());
		return mapper.insert(task);
	}

	@Override
	public int update(Task task, User loginUser) throws BusinessException {
		Task checkExist = mapper.selectByPrimaryKey(task.getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}

		if (task.getTaskName() != null) {
			Task checkConflict = mapper.selectByName(task.getProjectId(), task.getTaskName(), checkExist.getId());
			if (checkConflict != null) {
				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "同一项目下的任务名称重复。");
			}
		}
		if (!NumberUtils.equals(task.getProjectId(), checkExist.getProjectId())) {
			Project project = projectMapper.selectByPrimaryKey(task.getProjectId());
			if (project == null) {
				throw new BusinessException(EmBusinessError.DATA_NOT_EXIST, "项目不存在。");
			}
		}
		return mapper.updateByPrimaryKeySelective(task);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		Task checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void execRoman(Long id) throws BusinessException {
		Task task = mapper.selectByPrimaryKey(id);
		Project project = projectMapper.selectByPrimaryKey(task.getProjectId());
		List<TaskDetail> taskDetails = this.getTaskDetails(task.getId());
		List<CtRoman> ctRomans = this.getCtRomans(project.getCountryCode(), task.getLanguageCode());
		if (!ListUtils.isEmpty(taskDetails) && !ListUtils.isEmpty(ctRomans)) {
			RomanConverter rc = new RomanConverter(ctRomans);
			

		    List<List<TaskDetail>> sepratedList = ListUtils.splitListByCapacity(taskDetails, 100);
		    for (List<TaskDetail> subList : sepratedList) {

				TransactionStatus transactionStatus = null;
				try {
					transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
					
					for (TaskDetail td : subList) {
						td.setRoman(rc.convert(td.getOriginal()));
						td.setRomanStatus("1");
						taskDetailMapper.updateByPrimaryKeySelective(td);
					}
					
					dataSourceTransactionManager.commit(transactionStatus);
				} catch (Exception e) {
					dataSourceTransactionManager.rollback(transactionStatus);
					throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "罗马字母转换过程出错。");
				}
		    }
			
		}
	}

	@Override
	public void execTrans(Long id) throws BusinessException {
		Task task = mapper.selectByPrimaryKey(id);
		Project project = projectMapper.selectByPrimaryKey(task.getProjectId());
		List<TaskDetail> taskDetails = getTaskDetails(task.getId());
		List<CtCommon> ctCommons = this.getCtCommons(project.getCountryCode(), task.getLanguageCode());
		List<CtTransliteration> ctTransliterations = this.getCtTransliterations(project.getCountryCode(), task.getLanguageCode());


		if (!ListUtils.isEmpty(taskDetails) && !(ListUtils.isEmpty(ctTransliterations) && ListUtils.isEmpty(ctCommons))) {
			Translator tt = new Translator(ctTransliterations, ctCommons);
			

		    List<List<TaskDetail>> sepratedList = ListUtils.splitListByCapacity(taskDetails, 100);
		    for (List<TaskDetail> subList : sepratedList) {

				TransactionStatus transactionStatus = null;
				try {
					transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
					
					for (TaskDetail td : subList) {
						TransResult tr = tt.convert(td.getOriginal());
						td.setTransliteration(tr.getTransliteration());
						td.setFreeTranslation(tr.getFreeTranslation());
						td.setTransStatus("1");
						taskDetailMapper.updateByPrimaryKeySelective(td);
					}
					
					dataSourceTransactionManager.commit(transactionStatus);
				} catch (Exception e) {
					dataSourceTransactionManager.rollback(transactionStatus);
					throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "初译过程出错。");
				}
		    }
			
		}
	}

	private List<TaskDetail> getTaskDetails(Long taskId) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("taskId", taskId);

		Page<TaskDetail> resultList = taskDetailMapper.selectPaged(criteria);

		return resultList;
	}

	private List<CtRoman> getCtRomans(String countryCode, String languageCode) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("countryCode", countryCode);
		criteria.put("languageCode", languageCode);

		Page<CtRoman> resultList = ctRomanMapper.selectPaged(criteria);

		return resultList;
	}

	private List<CtCommon> getCtCommons(String countryCode, String languageCode) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("countryCode", countryCode);
		criteria.put("languageCode", languageCode);

		Page<CtCommon> resultList = ctCommonMapper.selectPaged(criteria);

		return resultList;
	}

	private List<CtTransliteration> getCtTransliterations(String countryCode, String languageCode) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("countryCode", countryCode);
		criteria.put("languageCode", languageCode);

		Page<CtTransliteration> resultList = ctTransliterationMapper.selectPaged(criteria);

		return resultList;
	}

	class RomanConverter {
		private Map<String, String> romanMap = new HashMap<String, String>();

		public RomanConverter(List<CtRoman> ctRomans) {
			ctRomans.forEach(ctRoman -> this.romanMap.put(ctRoman.getOriginalAlpha(), ctRoman.getRomanAlpha()));
		}

		public String convert(String originalString) {
			if (StringUtils.isEmpty(originalString)) {
				return originalString;
			}
			
			Character[] originalChars = ArrayUtils.toObject(originalString.toCharArray());
			
			Stream<Character> s = Arrays.stream(originalChars);
			String roman = s.map(temp -> {
				String p = String.valueOf(temp);
				String v = romanMap.get(p);
				if (StringUtils.isEmpty(v)) {
					return p;
				} else {
					return v;
				}
			}).collect(Collectors.joining());
			return roman;
		}
	}

	class Translator {
		private PayloadTrie<TranslateRule> trie = null;

		public Translator(List<CtTransliteration> ctTransliterations, List<CtCommon> ctCommons) {
			PayloadTrieBuilder<TranslateRule> tb = PayloadTrie.<TranslateRule>builder().ignoreCase().ignoreOverlaps();
			ctTransliterations.forEach(ct -> tb.addKeyword(ct.getOriginal(), new TranslateRule(ct.getOriginal(), "0", ct.getMatchWay(), ct.getMatchParams(), ct.getChinese(), "")));
			ctCommons.forEach(ct -> tb.addKeyword(ct.getOriginal(), new TranslateRule(ct.getOriginal(), ct.getOriginalType(), ct.getMatchWay(), ct.getMatchParams(), ct.getTransliteration(), ct.getFreeTranslation())));
			trie = tb.build();
		}

		public TransResult convert(String originalString) {
			if (StringUtils.isEmpty(originalString)) {
				return null;
			}
			TransResult transResult = null;
			if (this.trie != null) {
				originalString = originalString.toLowerCase();
				Collection<PayloadEmit<TranslateRule>> emits = trie.parseText(originalString);
				List<String> tArr = new ArrayList<String>();
				List<String> ftArr = new ArrayList<String>();
				emits.forEach(pe -> {
					TranslateRule tr = pe.getPayload();
					tArr.add(tr.getTransliteration());
					if (StringUtils.isEmpty(tr.getFreeTranslation())) {
						ftArr.add(tr.getTransliteration());
					} else {
						ftArr.add(tr.getFreeTranslation());
					}
				});
				String transliteration = String.join("", tArr);
				String freeTranslation = String.join("", ftArr);
				if (StringUtils.equals(transliteration, freeTranslation)) {
					transResult = new TransResult(transliteration, "");
				} else {
					transResult = new TransResult(transliteration, freeTranslation);
				}
				
			}
			
			return transResult;
		}
	}
	
	class TranslateRule {
		private String original;
		// 0-音译表；1-人名；2-通名；3-形容词；x-其它
		private String originalType;
		// 匹配方式：1-精确；2-前缀（词头）；3-后缀（词尾）；4-前置（在xxx之前）；5-后置（在xxx之后）
	    private String matchWay;
	    private String matchParams; 
	    private String transliteration;
	    private String freeTranslation;
	    
	    public TranslateRule(String original, String originalType, String matchWay, String matchParams, String transliteration, String freeTranslation) {
	    	this.original = StringUtils.defaultString(original);
	    	this.originalType = StringUtils.defaultString(originalType);
	    	this.matchWay = StringUtils.defaultString(matchWay);
	    	this.matchParams = StringUtils.defaultString(matchParams);
	    	this.transliteration = StringUtils.defaultString(transliteration);
	    	this.freeTranslation = StringUtils.defaultString(freeTranslation);
	    }

	    public String getOriginal() {
	        return original;
	    }
	    public String getOriginalType() {
	        return originalType;
	    }
	    public String getMatchWay() {
	        return matchWay;
	    }
	    public String getMatchParams() {
	        return matchParams;
	    }
	    public String getTransliteration() {
	        return transliteration;
	    }
	    public String getFreeTranslation() {
	        return freeTranslation;
	    }
	}
	
	class TransResult {
	    private String transliteration;
	    private String freeTranslation;
	    
	    public TransResult(String transliteration, String freeTranslation) {
	    	this.transliteration = StringUtils.defaultString(transliteration);
	    	this.freeTranslation = StringUtils.defaultString(freeTranslation);
	    }
	    public String getTransliteration() {
	        return transliteration;
	    }
	    public String getFreeTranslation() {
	        return freeTranslation;
	    }
	}
}
