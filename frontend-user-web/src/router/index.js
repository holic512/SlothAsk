const routes = [
    // ... existing routes ...
    {
        path: '/categories',
        name: 'Categories',
        component: () => import('@/view/HomePage/view/CategoriesPage/index.vue')
    }
] 