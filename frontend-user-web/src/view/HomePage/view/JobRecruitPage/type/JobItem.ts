/**
 * 排序方式枚举
 * 定义岗位列表的排序选项
 */
enum JobSortBy {
    /** 最晚发布 - 按发布时间降序排列 */
    LATEST_PUBLISHED = '最晚发布',
    /** 最早发布 - 按发布时间升序排列 */
    EARLIEST_PUBLISHED = '最早发布',
    /** 最晚结束 - 按截止时间降序排列 */
    LATEST_END = '最晚结束',
    /** 最早结束 - 按截止时间升序排列 */
    EARLIEST_END = '最早结束'
}

/**
 * 岗位类型枚举
 * 定义招聘岗位的类型选项
 */
enum JobType {
    /** 校招 - 校园招聘 */
    CAMPUS_RECRUITMENT = '校招',
    /** 社招 - 社会招聘 */
    SOCIAL_RECRUITMENT = '社招',
    /** 暑期实习 - 暑假期间的实习岗位 */
    SUMMER_INTERNSHIP = '暑期实习',
    /** 寒假实习 - 寒假期间的实习岗位 */
    WINTER_INTERNSHIP = '寒假实习',
    /** 春招 - 春季招聘 */
    SPRING_RECRUITMENT = '春招',
    /** 秋招 - 秋季招聘 */
    AUTUMN_RECRUITMENT = '秋招',
    /** 日常实习 - 日常实习岗位 */
    DAILY_INTERNSHIP = '日常实习'
}

/**
 * 申请状态枚举
 * 定义求职申请的进度状态
 */
enum ApplicationStatus {
    /** 待投递 - 尚未投递简历 */
    PENDING_SUBMISSION = '待投递',
    /** 投递中 - 已投递简历，等待回复 */
    SUBMITTED = '投递中',
    /** 待笔试 - 通过初筛，等待笔试 */
    PENDING_WRITTEN_TEST = '待笔试',
    /** 笔试中 - 正在进行笔试 */
    WRITTEN_TEST_IN_PROGRESS = '笔试中',
    /** 一面 - 第一轮面试 */
    FIRST_INTERVIEW = '一面',
    /** 二面 - 第二轮面试 */
    SECOND_INTERVIEW = '二面',
    /** 三面 - 第三轮面试 */
    THIRD_INTERVIEW = '三面'
}

/**
 * 岗位信息接口
 * 用于定义招聘岗位的完整数据结构，包含岗位基本信息、公司信息、申请状态等
 */
interface JobItem {
    /** 岗位唯一标识符，用于区分不同的招聘岗位 */
    id: number
    
    /** 公司名称，发布该岗位的企业名称 */
    companyName: string
    
    /** 岗位名称，具体的职位标题 */
    jobName: string
    
    /** 岗位描述，详细的工作内容和职责说明（可选） */
    jobDescription?: string
    
    /** 岗位类型，如：校招、社招、暑期实习等 */
    jobType: string
    
    /** 福利待遇，公司提供的各项福利和待遇列表（可选） */
    benefits?: string[]
    
    /** 工作地点，岗位所在的城市或具体地址 */
    location: string
    
    /** 薪资范围，岗位的薪酬区间描述 */
    salaryRange: string
    
    /** 发布时间，岗位信息的发布日期时间 */
    publishTime: string
    
    /** 申请状态，当前用户对该岗位的申请状态（如：未申请、已申请、已面试等） */
    applicationStatus: string
    
    /** 申请链接，用户点击申请时跳转的URL地址 */
    applyUrl: string
    
    /** 推荐码，用于标识推荐来源或内推信息 */
    referralCode: string
    
    /** 截止时间，岗位申请的最后期限 */
    endTime: string
}

/**
 * 岗位搜索过滤条件接口
 * 用于定义岗位列表的搜索和筛选参数
 */
interface JobSearchFilter {
    /** 关键词搜索，支持岗位名称、公司名称、岗位描述、工作地点的模糊匹配 */
    keyword?: string
    
    /** 申请状态筛选，可选择多个申请状态进行过滤 */
    applicationStatuses?: string[]
    
    /** 岗位类型筛选，可选择多个岗位类型进行过滤 */
    jobTypes?: string[]
}

/**
 * 分页查询参数接口
 * 用于定义列表数据的分页参数
 */
interface PaginationParams {
    /** 当前页码，从1开始 */
    page: number
    
    /** 每页显示数量，默认10条 */
    pageSize: number
    
    /** 排序字段，默认为最晚发布 */
    sortBy?: JobSortBy

}

/**
 * 岗位列表查询接口
 * 结合搜索过滤和分页功能的完整查询参数
 */
interface JobListQuery extends PaginationParams {
    /** 搜索过滤条件 */
    filter?: JobSearchFilter
}

// 导出所有类型和枚举
export type { JobItem, JobSearchFilter, PaginationParams, JobListQuery };
export { JobSortBy, JobType, ApplicationStatus };
