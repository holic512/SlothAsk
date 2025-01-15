import HomePage from './index.vue'
import LoginPage from './view/login/index.vue'

export default [
    {
        path: '/',
        name: 'LoginHomePage',
        component: HomePage,
        children: [
            {
                path: 'login',
                name: 'Login',
                component: LoginPage
            },
            {
                path: 'mailLogin',
                name: 'mailLogin',
                component: () => import('./view/mail-login/index.vue'),
            },
        ]
    },
];