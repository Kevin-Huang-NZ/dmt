
export function list[=targetName](query) {
  return request({
    url: '/api/[=lowerTargetName]/l',
    method: 'get',
    params: query
  })
}

export function get[=targetName](id) {
  return request({
    url: '/api/[=lowerTargetName]/r',
    method: 'get',
    params: { id }
  })
}

export function create[=targetName](data) {
  return request({
    url: '/api/[=lowerTargetName]/c',
    method: 'post',
    data
  })
}

export function update[=targetName](data) {
  return request({
    url: '/api/[=lowerTargetName]/u',
    method: 'post',
    data
  })
}

export function delete[=targetName](id) {
  return request({
    url: '/api/[=lowerTargetName]/d',
    method: 'post',
    data: { id }
  })
}
