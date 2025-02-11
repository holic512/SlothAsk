import StudyPage from './view/StudyPage/index.vue'
import HomePage from './index.vue'
import ContestPage from './view/ContestPage/index.vue'
import DiscussionPage from './view/DiscussionPage/index.vue'
import InterviewPage from './view/InterviewPage/index.vue'
import QuestionBankPage from './view/QuestionBankPage/index.vue'
import CategoryDetail from './view/QuestionBankPage/components/CategoryDetail.vue'
import QuestionDetail from './view/QuestionBankPage/components/QuestionDetail.vue'

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
            }
        ]
    },
];