import Layout from '@/layout'

const finalTransRouter = {
  path: '/final-trans',
  component: Layout,
  redirect: '/final-trans/index',
  alwaysShow: true,
  meta: {
    title: '终译管理',
    icon: 'project',
    roles: ['Z', 'M']
  },
  children: [
    {
      path: 'index',
      component: () => import('@/views/final-trans/index'),
      name: 'FinalTask',
      meta: { title: '终译轮数' }
    },
    {
      path: 'assignment',
      component: () => import('@/views/final-trans/assignment'),
      name: 'Assignment',
      meta: { title: '分配管理' }
    },
    {
      path: 'ad',
      component: () => import('@/views/final-trans/assignment-detail'),
      name: 'AssignmentDetail',
      meta: { title: '终译地名' }
    }
  ]
}

export default finalTransRouter
