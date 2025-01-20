import MainHomePage from './index.vue'
import Dashboard from './dashboard/index.vue'
import UserList from './user/list/index.vue'
import UserAchievements from './user/achievements/index.vue'
import UserSign from './user/sign/index.vue'
import UserFollow from './user/follow/index.vue'
import QuestionProject from './question/project/index.vue'
import QuestionCategory from './question/category/index.vue'
import QuestionList from './question/list/index.vue'
import CommentsQuestion from './comments/question/index.vue'
import CommentsCategory from './comments/category/index.vue'
import CommentsRating from './comments/rating/index.vue'
import MemberLevel from './member/level/index.vue'
import MemberList from './member/list/index.vue'
import MemberPoints from './member/points/index.vue'
import StatsQuestion from './stats/question/index.vue'
import StatsSign from './stats/sign/index.vue'
import StatsWrong from './stats/wrong/index.vue'
import SystemAdmin from './system/admin/index.vue'
import SystemRoles from './system/roles/index.vue'
import SystemLogs from './system/logs/index.vue'
import SystemConfig from './system/config/index.vue'

export default [
    {
        path: '/main',
        name: 'MainHomePage',
        component: MainHomePage,
        redirect: '/main/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: Dashboard
            },
            // 用户管理
            {
                path: 'user/list',
                name: 'UserList',
                component: UserList
            },
            {
                path: 'user/achievements',
                name: 'UserAchievements',
                component: UserAchievements
            },
            {
                path: 'user/sign',
                name: 'UserSign',
                component: UserSign
            },
            {
                path: 'user/follow',
                name: 'UserFollow',
                component: UserFollow
            },
            // 题库管理
            {
                path: 'question/project',
                name: 'QuestionProject',
                component: QuestionProject
            },
            {
                path: 'question/category',
                name: 'QuestionCategory',
                component: QuestionCategory
            },
            {
                path: 'question/list',
                name: 'QuestionList',
                component: QuestionList
            },
            // 评论管理
            {
                path: 'comments/question',
                name: 'CommentsQuestion',
                component: CommentsQuestion
            },
            {
                path: 'comments/category',
                name: 'CommentsCategory',
                component: CommentsCategory
            },
            {
                path: 'comments/rating',
                name: 'CommentsRating',
                component: CommentsRating
            },
            // 会员管理
            {
                path: 'member/level',
                name: 'MemberLevel',
                component: MemberLevel
            },
            {
                path: 'member/list',
                name: 'MemberList',
                component: MemberList
            },
            {
                path: 'member/points',
                name: 'MemberPoints',
                component: MemberPoints
            },
            // 统计分析
            {
                path: 'stats/question',
                name: 'StatsQuestion',
                component: StatsQuestion
            },
            {
                path: 'stats/sign',
                name: 'StatsSign',
                component: StatsSign
            },
            {
                path: 'stats/wrong',
                name: 'StatsWrong',
                component: StatsWrong
            },
            // 系统管理
            {
                path: 'system/admin',
                name: 'SystemAdmin',
                component: SystemAdmin
            },
            {
                path: 'system/roles',
                name: 'SystemRoles',
                component: SystemRoles
            },
            {
                path: 'system/logs',
                name: 'SystemLogs',
                component: SystemLogs
            },
            {
                path: 'system/config',
                name: 'SystemConfig',
                component: SystemConfig
            }
        ]
    }
]