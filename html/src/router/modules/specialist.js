import Layout from '@/layout'

const specialistRouter = {
  path: '/specialist',
  component: Layout,
  redirect: '/specialist/index',
  alwaysShow: true,
  meta: {
    title: '终译',
    icon: 'project',
    roles: ['Z', 'A']
  },
  children: [
    {
      path: 'index',
      component: () => import('@/views/specialist/index'),
      name: 'TranslationCheck',
      meta: { title: '译名核对' }
    }
  ]
}

export default specialistRouter
