import Layout from '@/layout'

const projectRouter = {
  path: '/project',
  component: Layout,
  redirect: '/project/index',
  alwaysShow: true,
  meta: {
    title: '项目管理',
    icon: 'project',
    roles: ['Z', 'M']
  },
  children: [
    {
      path: 'index',
      component: () => import('@/views/project/index'),
      name: 'Project',
      meta: { title: '项目' }
    },
    {
      path: 'task/:projectId?',
      component: () => import('@/views/project/task'),
      name: 'Task',
      meta: { title: '任务' }
    },
    {
      path: 'td',
      component: () => import('@/views/project/task-detail'),
      name: 'TaskDetail',
      meta: { title: '外文地名' }
    }
  ]
}

export default projectRouter
