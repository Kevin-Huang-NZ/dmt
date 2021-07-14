import request from '@/utils/request'

// user start
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
// user end

// upload file start
export function uploadFile(data) {
  return request({
    url: '/api/file/upload',
    method: 'post',
    data
  })
}
// upload file end

// country start
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

export function getCountryByCode(countryCode) {
  return request({
    url: '/api/country/gbuk',
    method: 'get',
    params: { countryCode }
  })
}
// country end

// language start
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

export function getLanguageByCode(languageCode) {
  return request({
    url: '/api/language/gbuk',
    method: 'get',
    params: { languageCode }
  })
}
// language end
