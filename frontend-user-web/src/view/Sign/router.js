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
            }
        ]
    }
];

