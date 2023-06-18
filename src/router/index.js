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
          component: () => import('../views/RecommendList.vue')
        },
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
          path: '/searchCompanies',
          name: 'SearchCompanies',
          component: () => import('../views/SearchCompanies.vue')
        },
        {
          path: '/searchKeywords',
          name: 'SearchKeywords',
          component: () => import('../views/SearchKeywords.vue')
        },
        {
          path: '/history',
          name: 'History',
          component: () => import('../views/History.vue')
        },
        {
          path: '/personCenter',
          name: 'PersonCenter',
          component: () => import('../views/PersonCenter.vue'),
        },
        {
          path: '/movies/details/:id',
          name: 'Detail',
          component: () => import('../views/Detail.vue')
        },
      ]
    },
    {
      path: '/management',
      name: 'Management',
      component: () => import('../layout/Management.vue'),
      children:[
        {
          path: 'welcome',
          name: 'Welcome',
          component: () => import('../views/Welcome.vue')
        },
        {
          path: 'user',
          name: 'User',
          component: () => import('../views/ManageUser.vue')
        },
        {
          path: 'movie',
          name: 'Movie',
          component: () => import('../views/ManageMovie.vue')
        },
        {
          path: 'search',
          name: 'Search',
          component: () => import('../views/ManageSearch.vue')
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
    {
      path: '/user',
      name: 'user',
      component: () => import('../views/ManageUser.vue')
    },

  ]
})

// 路由守卫
// router.beforeEach((to, from, next) => {
//   const noPermissionPaths = ['/login', '/404', '/home', '/detail', '/search', '/register']   // 定义无需登录的路由
//   if ( !noPermissionPaths.includes(to.path) ) { // 用户没有登录.假如当前正在跳转login页面，login页面无用户信息，此时再去往login页面跳转，会发生无限循环跳转
//     next("/login")
//   } else {
//     if (!to.matched.length) {
//       next('/404')
//     } else {
//       next()
//     }
//   }
// })

export default router
