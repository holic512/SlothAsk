import StudyPage from './view/StudyPage/index.vue'
import HomePage from './index.vue'
import ContestPage from './view/ContestPage/index.vue'
import DiscussionPage from './view/DiscussionPage/index.vue'
import InterviewPage from './view/InterviewPage/index.vue'

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
                path: '/problemset',
                name: 'ProblemsetPage',
                component: () => import('./view/ProblemsetPage/index.vue'),
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