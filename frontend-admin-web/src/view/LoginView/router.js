import HomePage from './index.vue'
import LoginPage from './view/login/index.vue'

export default [
    {
        path: '/',
        name: 'LoginHomePage',
        component: HomePage,
        children: [
            {
                path: '/',
                name: 'LoginPage',
                component: LoginPage
            },
            {
                path: '/mailLogin',
                name: 'mailLogin',
                component: () => import('./view/mail-login/index.vue'),
            },
        ]
    },
];