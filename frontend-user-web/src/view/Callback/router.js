export default [
    {
        path: '/callback',
        children: [
            {
                path: 'GitHubCallback',
                name: 'GitHubCallback',
                component: () => import('./GitHubCallback.vue'),
                meta: {
                    title: 'GitHub'
                }
            }
        ]
    }
]
