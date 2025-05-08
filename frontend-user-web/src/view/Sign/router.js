export default [
    {
        path: '/sign',
        component: () => import('./index.vue'),
        children: [
            {
                path: '',
                name: 'SignIndex',
                redirect: '/sign/email'
            },
            {
                path: 'email',
                name: 'EmailLogin',
                component: () => import('./view/EmailLogin.vue')
            },
            {
                path: 'password',
                name: 'PasswordLogin',
                component: () => import('./view/PasswordLogin.vue')
            },
            {
                path: 'register',
                name: 'Register',
                component: () => import('./view/Register.vue'),
                meta: {
                    title: '用户注册'
                }
            },
            {
                path: 'GitHubCallback',
                name: 'GitHubCallback',
                component: () => import('./view/GitHubCallback.vue'),
                meta: {
                    title: 'github'
                }
            }
        ]
    }
];

