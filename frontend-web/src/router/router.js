import {createWebHistory, createRouter} from 'vue-router'

import HomeRoute from '../view/HomePage/router.js'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        ...HomeRoute
    ],
})
export default router;