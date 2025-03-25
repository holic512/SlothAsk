export default [
    {
        path: '/',
        name: 'HomePage',
        component: () => import(/* webpackChunkName: "home" */ './index.vue'),
        redirect: '/study', // 避免默认加载所有子页面
        children: [
            {
                path: '/study',
                name: 'StudyPage',
                component: () => import(/* webpackChunkName: "study" */ './view/StudyPage/index.vue')
            },
            {
                path: '/questionbank',
                name: 'QuestionBankPage',
                component: () => import(/* webpackChunkName: "questionbank" */ './view/QuestionBankPage/index.vue'),
            },
            {
                path: '/questionbank/category/:id',
                name: 'CategoryDetail',
                component: () => import(/* webpackChunkName: "category" */ './view/QuestionBankPage/components/CategoryDetail.vue'),
                props: true
            },
            {
                path: '/questionpage/question/:questionId',
                name: 'QuestionPage',
                component: () => import(/* webpackChunkName: "question" */ './view/QuestionPage/index.vue'),
                props: true
            },
            {
                path: '/contest',
                name: 'ContestPage',
                component: () => import(/* webpackChunkName: "contest" */ './view/ContestPage/index.vue')
            },
            {
                path: '/discussion',
                name: 'DiscussionPage',
                component: () => import(/* webpackChunkName: "discussion" */ './view/DiscussionPage/index.vue')
            },
            {
                path: '/interview',
                name: 'InterviewPage',
                component: () => import(/* webpackChunkName: "interview" */ './view/InterviewPage/index.vue')
            },
            {
                path: '/account',
                name: 'AccountPage',
                component: () => import(/* webpackChunkName: "account" */ './view/AccountPage/index.vue'),
                children: [
                    {
                        path: 'profile',
                        name: 'AccountProfile',
                        component: () => import(/* webpackChunkName: "account-profile" */ '@/view/HomePage/view/AccountPage/components/Profile/Profile.vue')
                    },
                    {
                        path: 'problem-list',
                        name: 'AccountProblemList',
                        component: () => import(/* webpackChunkName: "account-problem-list" */ '@/view/HomePage/view/AccountPage/components/ProblemList.vue')
                    },
                    {
                        path: 'favorites',
                        name: 'AccountFavorites',
                        component: () => import(/* webpackChunkName: "account-favorites" */ '@/view/HomePage/view/AccountPage/components/Favorites.vue')
                    },
                    {
                        path: 'wrong-questions',
                        name: 'AccountWrongQuestions',
                        component: () => import(/* webpackChunkName: "account-wrong-questions" */ '@/view/HomePage/view/AccountPage/components/WrongQuestions.vue')
                    },
                    {
                        path: 'progress',
                        name: 'AccountProgress',
                        component: () => import(/* webpackChunkName: "account-progress" */ '@/view/HomePage/view/AccountPage/components/Progress.vue')
                    },
                    {
                        path: 'history',
                        name: 'AccountHistory',
                        component: () => import(/* webpackChunkName: "account-history" */ '@/view/HomePage/view/AccountPage/components/History.vue')
                    },
                    {
                        path: 'my-bank',
                        name: 'AccountMyBank',
                        component: () => import(/* webpackChunkName: "account-my-bank" */ '@/view/HomePage/view/AccountPage/components/MyBank.vue')
                    },
                    {
                        path: 'projects',
                        name: 'AccountProjects',
                        component: () => import(/* webpackChunkName: "account-projects" */ '@/view/HomePage/view/AccountPage/components/Projects.vue')
                    },
                    {
                        path: 'settings',
                        name: 'AccountSettings',
                        component: () => import(/* webpackChunkName: "account-settings" */ '@/view/HomePage/view/AccountPage/components/Settings.vue')
                    },
                    {
                        path: 'appearance',
                        name: 'AccountAppearance',
                        component: () => import(/* webpackChunkName: "account-appearance" */ '@/view/HomePage/view/AccountPage/components/Appearance.vue')
                    }
                ]
            }
        ]
    },
];
