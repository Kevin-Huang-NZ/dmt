import request from '@/utils/request'
// conversion table start
export function listCountryLanguage(query) {
  return request({
    url: '/api/ct/l',
    method: 'get',
    params: query
  })
}

export function getCountryLanguage(id) {
  return request({
    url: '/api/ct/r',
    method: 'get',
    params: { id }
  })
}

export function createCountryLanguage(data) {
  return request({
    url: '/api/ct/c',
    method: 'post',
    data
  })
}

export function updateCountryLanguage(data) {
  return request({
    url: '/api/ct/u',
    method: 'post',
    data
  })
}

export function deleteCountryLanguage(data) {
  return request({
    url: '/api/ct/d',
    method: 'post',
    data
  })
}

export function getCountries(status) {
  return request({
    url: '/api/ct/countries',
    method: 'get',
    params: { status }
  })
}

export function getLanguages(countryCode, status) {
  return request({
    url: '/api/ct/languages',
    method: 'get',
    params: { countryCode, status }
  })
}
// conversion table end

// ct roman start
export function listCtRoman(query) {
  return request({
    url: '/api/ct/roman/l',
    method: 'get',
    params: query
  })
}

export function getCtRoman(id) {
  return request({
    url: '/api/ct/roman/r',
    method: 'get',
    params: { id }
  })
}

export function createCtRoman(data) {
  return request({
    url: '/api/ct/roman/c',
    method: 'post',
    data
  })
}

export function updateCtRoman(data) {
  return request({
    url: '/api/ct/roman/u',
    method: 'post',
    data
  })
}

export function deleteCtRoman(id) {
  return request({
    url: '/api/ct/roman/d',
    method: 'post',
    data: { id }
  })
}

export function importCtRoman(data) {
  return request({
    url: '/api/ct/roman/import',
    method: 'post',
    data
  })
}

export function clearCtRoman(data) {
  return request({
    url: '/api/ct/roman/clear',
    method: 'post',
    data
  })
}
// ct roman end

// ct common start
export function listCtCommon(query) {
  return request({
    url: '/api/ct/common/l',
    method: 'get',
    params: query
  })
}

export function getCtCommon(id) {
  return request({
    url: '/api/ct/common/r',
    method: 'get',
    params: { id }
  })
}

export function createCtCommon(data) {
  return request({
    url: '/api/ct/common/c',
    method: 'post',
    data
  })
}

export function updateCtCommon(data) {
  return request({
    url: '/api/ct/common/u',
    method: 'post',
    data
  })
}

export function deleteCtCommon(id) {
  return request({
    url: '/api/ct/common/d',
    method: 'post',
    data: { id }
  })
}

export function importCtCommon(data) {
  return request({
    url: '/api/ct/common/import',
    method: 'post',
    data
  })
}

export function clearCtCommon(data) {
  return request({
    url: '/api/ct/common/clear',
    method: 'post',
    data
  })
}
// ct common end

// ct transliteration start
export function listCtTransliteration(query) {
  return request({
    url: '/api/ct/transliteration/l',
    method: 'get',
    params: query
  })
}

export function getCtTransliteration(id) {
  return request({
    url: '/api/ct/transliteration/r',
    method: 'get',
    params: { id }
  })
}

export function createCtTransliteration(data) {
  return request({
    url: '/api/ct/transliteration/c',
    method: 'post',
    data
  })
}

export function updateCtTransliteration(data) {
  return request({
    url: '/api/ct/transliteration/u',
    method: 'post',
    data
  })
}

export function deleteCtTransliteration(id) {
  return request({
    url: '/api/ct/transliteration/d',
    method: 'post',
    data: { id }
  })
}

export function importCtTransliteration(data) {
  return request({
    url: '/api/ct/transliteration/import',
    method: 'post',
    data
  })
}

export function clearCtTransliteration(data) {
  return request({
    url: '/api/ct/transliteration/clear',
    method: 'post',
    data
  })
}
// ct transliteration end
