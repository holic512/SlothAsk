import {createWebHistory, createRouter} from 'vue-router'

import LoginRouter from '../view/LoginView/router.js'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        ...LoginRouter
    ],
})
export default router;