import request from '@/utils/request'
// check round start
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

export function finalTransAssign(data) {
  return request({
    url: '/api/checkround/assign',
    method: 'post',
    data
  })
}
// check round end

// check assignment start
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

export function getCheckAssignmentSts(query) {
  return request({
    url: '/api/checkassignment/sts',
    method: 'get',
    params: query
  })
}

export function finalTransAdd(data) {
  return request({
    url: '/api/checkassignment/add',
    method: 'post',
    data
  })
}

export function finalTransWithdraw(data) {
  return request({
    url: '/api/checkassignment/withdraw',
    method: 'post',
    data
  })
}
// check assignment end

// check assignment detail start
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

export function updateCheckResult(data) {
  return request({
    url: '/api/checkassignmentdetail/uchr',
    method: 'post',
    data
  })
}

export function updateAdoptionStatus(data) {
  return request({
    url: '/api/checkassignmentdetail/uas',
    method: 'post',
    data
  })
}

export function updateAdoptionStatusBatch(data) {
  return request({
    url: '/api/checkassignmentdetail/uasb',
    method: 'post',
    data
  })
}
// check assignment detail end
