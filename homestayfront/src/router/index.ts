import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Layout from '../layout/Layout.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: '前台首页',
    component: () => import('@/views/front/index.vue'),
    redirect: '/index',
    children:[
      {
        path: '/index',
        name: '前台首页',
        component: () => import('@/views/front/roomList.vue')
      },
      {
        // 路由配置path:"/home/:id"或者path:"/home.id";路由不配置path中的参数id 第一次可请求,刷新页面id会消失;路由配置path中的参数id 刷新页面id会保留
        path: '/detail/:id(.*)',
        name: 'detail',
        component: () => import('@/views/front/detail.vue')
      },
      {
        path: '/front/userInfo',
        name: '用户信息',
        component: () => import('@/views/front/userInfo.vue')
      },
      {
        path: '/front/forget',
        name: '忘记密码',
        component: () => import('@/views/front/forgetPassword.vue')
      },
      {
        path: '/front/myorder',
        name: '我的订单',
        component: () => import('@/views/front/myOrders.vue')
      },
    ]
  },
  {
    path: '/back',
    name: '后台首页',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/user',
        name: '用户管理',
        component: () => import('@/views/sys/user/SysUserView.vue')
      },
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue')
      },
      {
        path: '/userInfo',
        name: '个人信息',
        component: () => import('@/views/sys/user/SysUserInfoView.vue')
      },
      {
        path: '/sys/menu',
        name: '菜单管理',
        component: () => import('@/views/sys/munu/SysMenuView.vue')
      },
      {
        path: '/sys/role',
        name: '角色管理',
        component: () => import('@/views/sys/role/SysRoleView.vue')
      },
      {
        path: '/sys/log',
        name: '日志管理',
        component: () => import('@/views/sys/log/SysLogView.vue')
      },
      {
        path: '/sys/file',
        name: '文件管理',
        component: () => import('@/views/sys/file/SysFileView.vue')
      },
      {
        path: '/sys/user/admin',
        name: '管理员管理',
        component: () => import('@/views/sys/user/SysUserAdminView.vue')
      },
      {
        path: '/sys/user/staff',
        name: '员工管理',
        component: () => import('@/views/sys/user/SysUserStaffView.vue')
      },
      {
        path: '/h/user',
        name: '前台用户管理',
        component: () => import('@/views/h/user/HUserView.vue')
      },
      {
        path: '/h/room',
        name: '房源管理',
        component: () => import('@/views/h/room/HRoomView.vue')
      },
      {
        path: '/h/room/img',
        name: '房源图片管理',
        component: () => import('@/views/h/room/HRoomImgView.vue')
      },
      {
        path: '/h/order',
        name: '所有订单',
        component: () => import('@/views/h/order/HOrderView.vue')
      },
      {
        path: '/h/order/subscribe',
        name: '民宿预订/入住',
        component: () => import('@/views/h/order/HOrderSubscribeView.vue')
      },
      {
        path: '/h/order/check',
        name: '预订处理',
        component: () => import('@/views/h/order/HOrderCheckView.vue')
      },
      {
        path: '/h/order/checkout',
        name: '入住管理/退房',
        component: () => import('@/views/h/order/HOrderCheckOutView.vue')
      },
      {
        path: '/h/order/echarts',
        name: '财务统计',
        component: () => import('@/views/h/order/HOrderEChartsView.vue')
      },
    ]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/sys/user/Login.vue')
  },
  {
    // 404页面配置
    path: "/:pathMatch(.*)",
    name: "NotFound",
    component: () => import("@/views/error/NotFound.vue"),
  }

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
