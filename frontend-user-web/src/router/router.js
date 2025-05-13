import {createRouter, createWebHistory} from 'vue-router'

import HomeRoute from '../view/HomePage/router.js'
import SignRoute from '../view/Sign/router.js'
import CallbackRoute from '../view/Callback/router.js'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        ...HomeRoute,
        ...SignRoute,
        ...CallbackRoute,
        // 添加默认重定向
        {
            path: '/',
            redirect: '/home'
        }
    ],
})

// // 添加路由守卫
// router.beforeEach((to, from, next) => {
//     // 检查用户是否已登录
//     const isAuthenticated = localStorage.getItem('token');
//
//     // 如果用户未登录且访问的不是登录页面，则重定向到登录页
//     if (!isAuthenticated && !to.path.startsWith('/sign')) {
//         next('/sign/email');
//     } else {
//         next();
//     }
// });

export default router;