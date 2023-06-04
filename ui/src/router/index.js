import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/pages/user/index.vue'),
    },
    {
      path: '/login',
      component: () => import('@/pages/login/index.vue'),
    },
  ],
})

export default router
