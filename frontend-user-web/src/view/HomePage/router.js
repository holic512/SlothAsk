export default [
    {
        path: '/',
        name: 'HomePage',
        component: () =>
            import(
                /* webpackChunkName: "home" */ './index.vue'
                ),
        redirect: '/study', // 默认重定向到学习页面，避免加载所有子路由
        children: [
            // 学习页面
            {
                path: 'study',
                name: 'StudyPage',
                component: () =>
                    import(
                        /* webpackChunkName: "study" */ './view/StudyPage/index.vue'
                        )
            },

            // 题库模块
            {
                path: 'questionbank',
                name: 'QuestionBankPage',
                component: () =>
                    import(
                        /* webpackChunkName: "questionbank" */ './view/QuestionBankPage/index.vue'
                        ),
                redirect: '/questionbank/questionbank', // 默认子路由
                children: [
                    // 题库首页（题目分类总览）
                    {
                        path: 'questionbank',
                        name: 'QuestionBankIndex',
                        component: () =>
                            import(
                                /* webpackChunkName: "category" */ '@/view/HomePage/view/QuestionBankPage/BankPage/index.vue'
                                ),
                        props: true
                    },
                    // 分类详情
                    {
                        path: 'category/:id',
                        name: 'CategoryDetail',
                        component: () =>
                            import(
                                /* webpackChunkName: "category" */ '@/view/HomePage/view/QuestionBankPage/BankPage/components/CategoryDetail.vue'
                                ),
                        props: true
                    },
                    //我的收藏
                    {
                        path: 'myFavoritesQuestion',
                        name: 'MyFavoritesQuestion',
                        component: () =>
                            import(
                                /* webpackChunkName: "question" */ '@/view/HomePage/view/QuestionBankPage/MyFavoritesQuestion/MyFavoritesQuestion.vue'
                                ),
                        props: true
                    },
                ]
            },

            // 题目详情
            {
                path: 'question/:questionId',
                name: 'QuestionPage',
                component: () =>
                    import(
                        /* webpackChunkName: "question" */ './view/QuestionPage/index.vue'
                        ),
                props: true
            },

            // 比赛页面
            {
                path: 'contest',
                name: 'ContestPage',
                component: () =>
                    import(
                        /* webpackChunkName: "contest" */ './view/ContestPage/index.vue'
                        )
            },

            // 讨论区
            {
                path: 'discussion',
                name: 'DiscussionPage',
                component: () =>
                    import(
                        /* webpackChunkName: "discussion" */ './view/DiscussionPage/index.vue'
                        )
            },

            // 面试专区
            {
                path: 'interview',
                name: 'InterviewPage',
                component: () =>
                    import(
                        /* webpackChunkName: "interview" */ './view/InterviewPage/index.vue'
                        )
            },

            // 个人中心
            {
                path: 'account',
                name: 'AccountPage',
                component: () =>
                    import(
                        /* webpackChunkName: "account" */ './view/AccountPage/index.vue'
                        ),
                children: [
                    // 个人资料
                    {
                        path: 'profile',
                        name: 'AccountProfile',
                        component: () =>
                            import(
                                '@/view/HomePage/view/AccountPage/components/Profile/Profile.vue'
                                )
                    },
                    // 错题本
                    {
                        path: 'wrong-questions',
                        name: 'AccountWrongQuestions',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-wrong-questions" */ '@/view/HomePage/view/AccountPage/components/WrongQuestions.vue'
                                )
                    },
                    // 学习进度
                    {
                        path: 'progress',
                        name: 'AccountProgress',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-progress" */ '@/view/HomePage/view/AccountPage/components/Progress.vue'
                                )
                    },
                    // 答题记录
                    {
                        path: 'record',
                        name: 'AccountHistory',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-history" */ '@/view/HomePage/view/AccountPage/components/Record.vue'
                                )
                    },
                    // 浏览历史
                    {
                        path: 'history',
                        name: 'AccountBrowseHistory',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-browse-history" */ '@/view/HomePage/view/AccountPage/components/History/History.vue'
                                )
                    },
                    // 我的题库
                    {
                        path: 'my-bank',
                        name: 'AccountMyBank',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-my-bank" */ '@/view/HomePage/view/AccountPage/components/MyBank.vue'
                                )
                    },
                    // 我的项目
                    {
                        path: 'projects',
                        name: 'AccountProjects',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-projects" */ '@/view/HomePage/view/AccountPage/components/Projects.vue'
                                )
                    },
                    // 设置
                    {
                        path: 'settings',
                        name: 'AccountSettings',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-settings" */ '@/view/HomePage/view/AccountPage/components/AccountSetting/AccountSetting.vue'
                                )
                    },
                    // 外观主题
                    {
                        path: 'appearance',
                        name: 'AccountAppearance',
                        component: () =>
                            import(
                                /* webpackChunkName: "account-appearance" */ '@/view/HomePage/view/AccountPage/components/Appearance.vue'
                                )
                    }
                ]
            },

            // 用户协议
            {
                path: 'terms',
                name: 'Terms',
                component: () =>
                    import(
                        /* webpackChunkName: "terms" */ './view/Terms/Terms.vue'
                        )
            },

            // 隐私政策
            {
                path: 'privacy',
                name: 'Privacy',
                component: () =>
                    import(
                        /* webpackChunkName: "privacy" */ './view/Terms/Privacy.vue'
                        )
            },

            // 我的主页
            {
                path: 'userProfile/:username',
                name: 'userProfile',
                component: () =>
                    import(
                        /* webpackChunkName: "privacy" */ './view/UserProfile/index.vue'
                        )
            },

            // 消息页面
            {
                path: 'messages',
                name: 'MessagePage',
                component: () =>
                    import(
                        /* webpackChunkName: "messages" */ './view/MessagePage/index.vue'
                        )
            },

            // AI解析消息组件演示
            {
                path: 'test',
                name: 'test',
                component: () =>
                    import(
                        './components/Test/Test.vue'
                        )
            },
        ]
    }
];
