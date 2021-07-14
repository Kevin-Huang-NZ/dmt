import request from '@/utils/request'
// project start
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
// project end
// task start
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

export function execRoman(id) {
  return request({
    url: '/api/task/er',
    method: 'post',
    data: { id }
  })
}

export function execTrans(id) {
  return request({
    url: '/api/task/et',
    method: 'post',
    data: { id }
  })
}
// task end
// task detail start
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

export function importTaskDetail(data) {
  return request({
    url: '/api/taskdetail/import',
    method: 'post',
    data
  })
}

export function clearTaskDetail(data) {
  return request({
    url: '/api/taskdetail/clear',
    method: 'post',
    data
  })
}
// task detail end
