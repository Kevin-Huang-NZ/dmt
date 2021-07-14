package [=servicePkg];

import [=modelPkg].[=targetName];
import com.cit.dmt.core.model.User;
import com.github.pagehelper.Page;

import mahara.web.param.CommonRequestParam;
import mahara.web.response.error.BusinessException;

public interface [=targetName]Service {
	Page<[=targetName]> selectPaged(CommonRequestParam crp, [=targetName] criteria) throws BusinessException;
	
	[=targetName] selectById(Long id);

	int save([=targetName] [=lcTargetName], User loginUser) throws BusinessException;

	int update([=targetName] [=lcTargetName], User loginUser) throws BusinessException;

	int delete(Long id, User loginUser) throws BusinessException;
}
