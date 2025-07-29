export default [
    {
        path: '/intro',
        name: 'IntroPage',
        component: () =>
            import(
                /* webpackChunkName: "intro" */ './index.vue'
            )
    }
];