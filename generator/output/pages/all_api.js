
export function listCheckAssignmentDetail(query) {
  return request({
    url: '/api/checkassignmentdetail/l',
    method: 'get',
    params: query
  })
}

export function getCheckAssignmentDetail(id) {
  return request({
    url: '/api/checkassignmentdetail/r',
    method: 'get',
    params: { id }
  })
}

export function createCheckAssignmentDetail(data) {
  return request({
    url: '/api/checkassignmentdetail/c',
    method: 'post',
    data
  })
}

export function updateCheckAssignmentDetail(data) {
  return request({
    url: '/api/checkassignmentdetail/u',
    method: 'post',
    data
  })
}

export function deleteCheckAssignmentDetail(id) {
  return request({
    url: '/api/checkassignmentdetail/d',
    method: 'post',
    data: { id }
  })
}

export function listCheckAssignment(query) {
  return request({
    url: '/api/checkassignment/l',
    method: 'get',
    params: query
  })
}

export function getCheckAssignment(id) {
  return request({
    url: '/api/checkassignment/r',
    method: 'get',
    params: { id }
  })
}

export function createCheckAssignment(data) {
  return request({
    url: '/api/checkassignment/c',
    method: 'post',
    data
  })
}

export function updateCheckAssignment(data) {
  return request({
    url: '/api/checkassignment/u',
    method: 'post',
    data
  })
}

export function deleteCheckAssignment(id) {
  return request({
    url: '/api/checkassignment/d',
    method: 'post',
    data: { id }
  })
}

export function listCheckRound(query) {
  return request({
    url: '/api/checkround/l',
    method: 'get',
    params: query
  })
}

export function getCheckRound(id) {
  return request({
    url: '/api/checkround/r',
    method: 'get',
    params: { id }
  })
}

export function createCheckRound(data) {
  return request({
    url: '/api/checkround/c',
    method: 'post',
    data
  })
}

export function updateCheckRound(data) {
  return request({
    url: '/api/checkround/u',
    method: 'post',
    data
  })
}

export function deleteCheckRound(id) {
  return request({
    url: '/api/checkround/d',
    method: 'post',
    data: { id }
  })
}

export function listCountryLanguage(query) {
  return request({
    url: '/api/countrylanguage/l',
    method: 'get',
    params: query
  })
}

export function getCountryLanguage(id) {
  return request({
    url: '/api/countrylanguage/r',
    method: 'get',
    params: { id }
  })
}

export function createCountryLanguage(data) {
  return request({
    url: '/api/countrylanguage/c',
    method: 'post',
    data
  })
}

export function updateCountryLanguage(data) {
  return request({
    url: '/api/countrylanguage/u',
    method: 'post',
    data
  })
}

export function deleteCountryLanguage(id) {
  return request({
    url: '/api/countrylanguage/d',
    method: 'post',
    data: { id }
  })
}

export function listCountry(query) {
  return request({
    url: '/api/country/l',
    method: 'get',
    params: query
  })
}

export function getCountry(id) {
  return request({
    url: '/api/country/r',
    method: 'get',
    params: { id }
  })
}

export function createCountry(data) {
  return request({
    url: '/api/country/c',
    method: 'post',
    data
  })
}

export function updateCountry(data) {
  return request({
    url: '/api/country/u',
    method: 'post',
    data
  })
}

export function deleteCountry(id) {
  return request({
    url: '/api/country/d',
    method: 'post',
    data: { id }
  })
}

export function listCtCommon(query) {
  return request({
    url: '/api/ctcommon/l',
    method: 'get',
    params: query
  })
}

export function getCtCommon(id) {
  return request({
    url: '/api/ctcommon/r',
    method: 'get',
    params: { id }
  })
}

export function createCtCommon(data) {
  return request({
    url: '/api/ctcommon/c',
    method: 'post',
    data
  })
}

export function updateCtCommon(data) {
  return request({
    url: '/api/ctcommon/u',
    method: 'post',
    data
  })
}

export function deleteCtCommon(id) {
  return request({
    url: '/api/ctcommon/d',
    method: 'post',
    data: { id }
  })
}

export function listCtRoman(query) {
  return request({
    url: '/api/ctroman/l',
    method: 'get',
    params: query
  })
}

export function getCtRoman(id) {
  return request({
    url: '/api/ctroman/r',
    method: 'get',
    params: { id }
  })
}

export function createCtRoman(data) {
  return request({
    url: '/api/ctroman/c',
    method: 'post',
    data
  })
}

export function updateCtRoman(data) {
  return request({
    url: '/api/ctroman/u',
    method: 'post',
    data
  })
}

export function deleteCtRoman(id) {
  return request({
    url: '/api/ctroman/d',
    method: 'post',
    data: { id }
  })
}

export function listCtTransliteration(query) {
  return request({
    url: '/api/cttransliteration/l',
    method: 'get',
    params: query
  })
}

export function getCtTransliteration(id) {
  return request({
    url: '/api/cttransliteration/r',
    method: 'get',
    params: { id }
  })
}

export function createCtTransliteration(data) {
  return request({
    url: '/api/cttransliteration/c',
    method: 'post',
    data
  })
}

export function updateCtTransliteration(data) {
  return request({
    url: '/api/cttransliteration/u',
    method: 'post',
    data
  })
}

export function deleteCtTransliteration(id) {
  return request({
    url: '/api/cttransliteration/d',
    method: 'post',
    data: { id }
  })
}

export function listFileUpload(query) {
  return request({
    url: '/api/fileupload/l',
    method: 'get',
    params: query
  })
}

export function getFileUpload(id) {
  return request({
    url: '/api/fileupload/r',
    method: 'get',
    params: { id }
  })
}

export function createFileUpload(data) {
  return request({
    url: '/api/fileupload/c',
    method: 'post',
    data
  })
}

export function updateFileUpload(data) {
  return request({
    url: '/api/fileupload/u',
    method: 'post',
    data
  })
}

export function deleteFileUpload(id) {
  return request({
    url: '/api/fileupload/d',
    method: 'post',
    data: { id }
  })
}

export function listLanguage(query) {
  return request({
    url: '/api/language/l',
    method: 'get',
    params: query
  })
}

export function getLanguage(id) {
  return request({
    url: '/api/language/r',
    method: 'get',
    params: { id }
  })
}

export function createLanguage(data) {
  return request({
    url: '/api/language/c',
    method: 'post',
    data
  })
}

export function updateLanguage(data) {
  return request({
    url: '/api/language/u',
    method: 'post',
    data
  })
}

export function deleteLanguage(id) {
  return request({
    url: '/api/language/d',
    method: 'post',
    data: { id }
  })
}

export function listProject(query) {
  return request({
    url: '/api/project/l',
    method: 'get',
    params: query
  })
}

export function getProject(id) {
  return request({
    url: '/api/project/r',
    method: 'get',
    params: { id }
  })
}

export function createProject(data) {
  return request({
    url: '/api/project/c',
    method: 'post',
    data
  })
}

export function updateProject(data) {
  return request({
    url: '/api/project/u',
    method: 'post',
    data
  })
}

export function deleteProject(id) {
  return request({
    url: '/api/project/d',
    method: 'post',
    data: { id }
  })
}

export function listSysFun(query) {
  return request({
    url: '/api/sysfun/l',
    method: 'get',
    params: query
  })
}

export function getSysFun(id) {
  return request({
    url: '/api/sysfun/r',
    method: 'get',
    params: { id }
  })
}

export function createSysFun(data) {
  return request({
    url: '/api/sysfun/c',
    method: 'post',
    data
  })
}

export function updateSysFun(data) {
  return request({
    url: '/api/sysfun/u',
    method: 'post',
    data
  })
}

export function deleteSysFun(id) {
  return request({
    url: '/api/sysfun/d',
    method: 'post',
    data: { id }
  })
}

export function listSysPage(query) {
  return request({
    url: '/api/syspage/l',
    method: 'get',
    params: query
  })
}

export function getSysPage(id) {
  return request({
    url: '/api/syspage/r',
    method: 'get',
    params: { id }
  })
}

export function createSysPage(data) {
  return request({
    url: '/api/syspage/c',
    method: 'post',
    data
  })
}

export function updateSysPage(data) {
  return request({
    url: '/api/syspage/u',
    method: 'post',
    data
  })
}

export function deleteSysPage(id) {
  return request({
    url: '/api/syspage/d',
    method: 'post',
    data: { id }
  })
}

export function listSysRoleFun(query) {
  return request({
    url: '/api/sysrolefun/l',
    method: 'get',
    params: query
  })
}

export function getSysRoleFun(id) {
  return request({
    url: '/api/sysrolefun/r',
    method: 'get',
    params: { id }
  })
}

export function createSysRoleFun(data) {
  return request({
    url: '/api/sysrolefun/c',
    method: 'post',
    data
  })
}

export function updateSysRoleFun(data) {
  return request({
    url: '/api/sysrolefun/u',
    method: 'post',
    data
  })
}

export function deleteSysRoleFun(id) {
  return request({
    url: '/api/sysrolefun/d',
    method: 'post',
    data: { id }
  })
}

export function listSysRole(query) {
  return request({
    url: '/api/sysrole/l',
    method: 'get',
    params: query
  })
}

export function getSysRole(id) {
  return request({
    url: '/api/sysrole/r',
    method: 'get',
    params: { id }
  })
}

export function createSysRole(data) {
  return request({
    url: '/api/sysrole/c',
    method: 'post',
    data
  })
}

export function updateSysRole(data) {
  return request({
    url: '/api/sysrole/u',
    method: 'post',
    data
  })
}

export function deleteSysRole(id) {
  return request({
    url: '/api/sysrole/d',
    method: 'post',
    data: { id }
  })
}

export function listTaskDetail(query) {
  return request({
    url: '/api/taskdetail/l',
    method: 'get',
    params: query
  })
}

export function getTaskDetail(id) {
  return request({
    url: '/api/taskdetail/r',
    method: 'get',
    params: { id }
  })
}

export function createTaskDetail(data) {
  return request({
    url: '/api/taskdetail/c',
    method: 'post',
    data
  })
}

export function updateTaskDetail(data) {
  return request({
    url: '/api/taskdetail/u',
    method: 'post',
    data
  })
}

export function deleteTaskDetail(id) {
  return request({
    url: '/api/taskdetail/d',
    method: 'post',
    data: { id }
  })
}

export function listTask(query) {
  return request({
    url: '/api/task/l',
    method: 'get',
    params: query
  })
}

export function getTask(id) {
  return request({
    url: '/api/task/r',
    method: 'get',
    params: { id }
  })
}

export function createTask(data) {
  return request({
    url: '/api/task/c',
    method: 'post',
    data
  })
}

export function updateTask(data) {
  return request({
    url: '/api/task/u',
    method: 'post',
    data
  })
}

export function deleteTask(id) {
  return request({
    url: '/api/task/d',
    method: 'post',
    data: { id }
  })
}

export function listUser(query) {
  return request({
    url: '/api/user/l',
    method: 'get',
    params: query
  })
}

export function getUser(id) {
  return request({
    url: '/api/user/r',
    method: 'get',
    params: { id }
  })
}

export function createUser(data) {
  return request({
    url: '/api/user/c',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/api/user/u',
    method: 'post',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: '/api/user/d',
    method: 'post',
    data: { id }
  })
}
