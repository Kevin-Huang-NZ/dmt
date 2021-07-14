import Layout from '@/layout'

const ctRouter = {
  path: '/ct',
  component: Layout,
  redirect: '/ct/index',
  children: [
    {
      path: 'index',
      component: () => import('@/views/ct/index'),
      name: 'ConversionTable',
      meta: { title: '译写表', icon: 'excel', roles: ['Z', 'M'] }
    },
    {
      path: 'roman/:countryCode/:languageCode',
      component: () => import('@/views/ct/roman'),
      name: 'CTRoman',
      meta: { title: '罗马字母转写表', activeMenu: '/ct/index' },
      hidden: true
    },
    {
      path: 'transliteration/:countryCode/:languageCode',
      component: () => import('@/views/ct/transliteration'),
      name: 'CTTransliteration',
      meta: { title: '音译表', activeMenu: '/ct/index' },
      hidden: true
    },
    {
      path: 'common/:countryCode/:languageCode',
      component: () => import('@/views/ct/common'),
      name: 'CTCommon',
      meta: { title: '常用词', activeMenu: '/ct/index' },
      hidden: true
    }
  ]
}

export default ctRouter
