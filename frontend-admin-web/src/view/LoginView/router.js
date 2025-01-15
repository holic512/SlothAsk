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
                name: 'Login',
                component: LoginPage
            },
        ]
    },
];