import StudyPage from './view/StudyPage/index.vue'
import HomePage from './index.vue'
import ContestPage from './view/ContestPage/index.vue'
import DiscussionPage from './view/DiscussionPage/index.vue'
import InterviewPage from './view/InterviewPage/index.vue'
import QuestionBankPage from './view/QuestionBankPage/index.vue'
import CategoryDetail from './view/QuestionBankPage/components/CategoryDetail.vue'
import QuestionDetail from './view/QuestionBankPage/components/QuestionDetail.vue'

import AccountPage from './view/AccountPage/index.vue'
import AccountProfile from '@/view/HomePage/view/AccountPage/components/Profile.vue'
import AccountProblemList from '@/view/HomePage/view/AccountPage/components/ProblemList.vue'
import AccountFavorites from '@/view/HomePage/view/AccountPage/components/Favorites.vue'
import AccountWrongQuestions from '@/view/HomePage/view/AccountPage/components/WrongQuestions.vue'
import AccountProgress from '@/view/HomePage/view/AccountPage/components/Progress.vue'
import AccountHistory from '@/view/HomePage/view/AccountPage/components/History.vue'
import AccountMyBank from '@/view/HomePage/view/AccountPage/components/MyBank.vue'
import AccountProjects from '@/view/HomePage/view/AccountPage/components/Projects.vue'
import AccountSettings from '@/view/HomePage/view/AccountPage/components/Settings.vue'
import AccountAppearance from '@/view/HomePage/view/AccountPage/components/Appearance.vue'

export default [
    {
        path: '/',
        name: 'HomePage',
        component: HomePage,
        children: [
            {
                path: '/',
                name: 'StudyPage',
                component: StudyPage
            },
            {
                path: '/questionbank',
                name: 'QuestionBankPage',
                component: QuestionBankPage,
            },
            {
                path: '/questionbank/category/:id',
                name: 'CategoryDetail',
                component: CategoryDetail,
                props: true
            },
            {
                path: '/questionbank/question/:questionId',
                name: 'QuestionDetail',
                component: QuestionDetail,
                props: true
            },
            {
                path: '/contest',
                name: 'ContestPage',
                component: ContestPage
            },
            {
                path: '/discussion',
                name: 'DiscussionPage',
                component: DiscussionPage
            },
            {
                path: '/interview',
                name: 'InterviewPage',
                component: InterviewPage
            },
            {
                path: '/account',
                name: 'AccountPage',
                component: AccountPage,
                children: [
                    {
                        path: '',
                        name: 'AccountProfile',
                        component: AccountProfile
                    },
                    {
                        path: 'problem-list',
                        name: 'AccountProblemList',
                        component: AccountProblemList
                    },
                    {
                        path: 'favorites',
                        name: 'AccountFavorites',
                        component: AccountFavorites
                    },
                    {
                        path: 'wrong-questions',
                        name: 'AccountWrongQuestions',
                        component: AccountWrongQuestions
                    },
                    {
                        path: 'progress',
                        name: 'AccountProgress',
                        component: AccountProgress
                    },
                    {
                        path: 'history',
                        name: 'AccountHistory',
                        component: AccountHistory
                    },
                    {
                        path: 'my-bank',
                        name: 'AccountMyBank',
                        component: AccountMyBank
                    },
                    {
                        path: 'projects',
                        name: 'AccountProjects',
                        component: AccountProjects
                    },
                    {
                        path: 'settings',
                        name: 'AccountSettings',
                        component: AccountSettings
                    },
                    {
                        path: 'appearance',
                        name: 'AccountAppearance',
                        component: AccountAppearance
                    }
                ]
            }
        ]
    },
];