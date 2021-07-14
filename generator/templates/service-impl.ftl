package [=serviceImplPkg];

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import [=mapperPkg].[=targetName]Mapper;
import [=modelPkg].[=targetName];
import [=servicePkg].[=targetName]Service;
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;
import mahara.web.response.error.EmBusinessError;

@Service
public class [=targetName]ServiceImpl implements [=targetName]Service {
	Logger logger = LoggerFactory.getLogger([=targetName]Service.class);
	private final String defaultOrderBy = "t_id desc";

	@Autowired
	private [=targetName]Mapper mapper;
	
	@Override
	public Page<[=targetName]> selectPaged(CommonRequestParam crp, [=targetName] qp) throws BusinessException {
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
		
		Page<[=targetName]> resultList = null;
		if (crp.getPageNum() != -1) {
			PageHelper.startPage(crp.getPageNum(), crp.getPageSize());
			resultList = mapper.selectPaged(criteria);
		} else {
			resultList = mapper.selectPaged(criteria);
		}
		return resultList;
	}

	@Override
	public [=targetName] selectById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int save([=targetName] [=lcTargetName], User loginUser) throws BusinessException {
//		[=targetName] checkConflict = mapper.selectByPhone([=lcTargetName].getPhone(), null);
//		if (checkConflict != null) {
//			throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//		}
		return mapper.insert([=lcTargetName]);
	}

	@Override
	public int update([=targetName] [=lcTargetName], User loginUser) throws BusinessException {
		[=targetName] checkExist = mapper.selectByPrimaryKey([=lcTargetName].getId());
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		
//		if ([=lcTargetName].getPhone() != null) {
//			[=targetName] checkConflict = mapper.selectByPhone([=lcTargetName].getPhone(), [=lcTargetName].getId());
//			if (checkConflict != null) {
//				throw new BusinessException(EmBusinessError.DATA_CONFLICT, "手机号重复。");
//			}
//		}
		return mapper.updateByPrimaryKeySelective([=lcTargetName]);
	}

	@Override
	public int delete(Long id, User loginUser) throws BusinessException {
		[=targetName] checkExist = mapper.selectByPrimaryKey(id);
		if (checkExist == null) {
			throw new BusinessException(EmBusinessError.DATA_NOT_EXIST);
		}
		return mapper.deleteByPrimaryKey(id);
	}

}

