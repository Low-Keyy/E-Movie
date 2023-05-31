import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '',
      name: 'Layout',
      component: () => import('../layout/Layout.vue'),
      redirect: '/home',
      children:[
        {
          path: '/home',
          name: 'Home',
          component: () => import('../views/HomeView.vue')
        },
        {
          path: '/watchList',
          name: 'WatchList',
          component: () => import('../views/WatchList.vue')
        },
        // {
        //   path: '/searchList',
        //   name: 'SearchList',
        //   component: () => import('../views/SearchList.vue')
        // },
        {
          path: '/searchResults',
          name: 'SearchResults',
          component: () => import('../views/SearchResults.vue')
        },
        {
          path: '/searchPerson',
          name: 'SearchPerson',
          component: () => import('../views/SearchPerson.vue')
        },
        {
          path: '/personCenter',
          name: 'PersonCenter',
          component: () => import('../views/PersonCenter.vue'),
        },
        {
          path: '/detail',
          name: 'Detail',
          component: () => import('../views/Detail.vue')
        },
      ]
    },
    {
      path: '/results',
      name: 'Results',
      component: () => import('../views/SearchResults.vue')
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/Register.vue')
    },
    {
      path: '/404',
      name: '404',
      component: () => import('../views/404.vue')
    },

  ]
})

export default router
