import {
  createRouter,
  createWebHistory
} from 'vue-router'
import Layout from '@/layout/Layout.vue'
const routes = [{
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login.vue")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("@/views/Register.vue")
  },
  {
    path: '/forgetPassword',
    name: 'ForgetPassword',
    component: () => import("@/views/ForgetPassword.vue")
  },
  {
    path: '/404',
    name: '404',
    component: () => import("@/views/404.vue")
  },
]

//children: 子路由路径 redirect: 重定向
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})
//添加动态 路由 防止刷新丢失
if (localStorage.getItem('newRouterArr')) {
  let values = JSON.parse(localStorage.getItem('newRouterArr'))
  const children = [];
  values.forEach(item => {
    children.push({
      path: item.url,
      name: item.name,
      component: () => import("@/views/" + item.name + ".vue"),
    })
  });
  let value = {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/login',
    children: children
  }
  router.addRoute(value)
}

router.beforeEach((to, from, next) => {
  //储存当前路由，便于在重新打开时候跳到上次关闭的页面（用户主动退出除外）
  if (to.path === '/login' || to.path === '/register' || to.path === '/forgetPassword') {
    next();
  } else {
    //判断token是否存在
    let token = localStorage.getItem("token")
    if (token === null || token === '') {
      next('/login')
    }
    //判断session 用户是否存在
    let phone = sessionStorage.getItem("phone")
    if (phone === null || phone === '') {
      next('/login')
    }

    if (to.matched.length === 0) {
      next('/404') // 判断此跳转路由的来源路由是否存在，存在的情况跳转到来源路由，否则跳转到404页面
    }
    //允许执行
    next();
  }
})
export default router