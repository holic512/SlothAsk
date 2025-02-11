import { defineStore } from 'pinia'

interface Question {
    id: number;
    title: string;
    status: 'solved' | 'attempted' | 'unsolved';
    comments: number;
    passRate: number;
    difficulty: '简单' | '中等' | '困难';
    tags: string[];
    type: '单选' | '多选' | '判断' | '简答';
    content: string;
    options?: Array<{
        label: string;
        text: string;
    }>;
    answer: string | string[] | boolean;
    explanation: string;
}

interface Category {
    id: number;
    name: string;
    icon: string;
    count: number;
    description: string;
    questions: Question[];
}

interface PaginationState {
    currentPage: number;
    pageSize: number;
    total: number;
}

interface QuestionBankState {
    categories: Category[];
    pagination: PaginationState;
}

export const useQuestionBankStore = defineStore('questionBank', {
    state: (): QuestionBankState => ({
        categories: [
            {
                id: 1,
                name: '前端开发',
                icon: '💻',
                count: 1234,
                description: '涵盖 Vue、React、TypeScript 等前端主流技术栈，从基础到高级的全面解析',
                questions: [
                    {
                        id: 101,
                        title: 'Vue3的响应式原理是什么？',
                        status: 'solved',
                        comments: 125,
                        passRate: 85,
                        difficulty: '中等',
                        tags: ['Vue3', '响应式', 'Proxy'],
                        type: '简答',
                        content: '请详细解释 Vue3 的响应式原理，包括与 Vue2 的区别。',
                        answer: 'Vue3 使用 Proxy 代替 Vue2 的 Object.defineProperty 来实现响应式...',
                        explanation: '这个问题考察对 Vue3 核心原理的理解...'
                    },
                    {
                        id: 102,
                        title: '以下哪些是 Vue3 的新特性？',
                        status: 'attempted',
                        comments: 98,
                        passRate: 75,
                        difficulty: '简单',
                        tags: ['Vue3', '基础概念'],
                        type: '多选',
                        content: '关于 Vue3 的新特性，下列说法正确的有：',
                        options: [
                            { label: 'A', text: 'Composition API 的引入' },
                            { label: 'B', text: 'Teleport 组件' },
                            { label: 'C', text: 'Fragments 支持' },
                            { label: 'D', text: '更好的 TypeScript 支持' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: '这些都是 Vue3 的重要新特性...'
                    },
                    {
                        id: 103,
                        title: 'Vue3 中 ref 和 reactive 的区别',
                        status: 'unsolved',
                        comments: 156,
                        passRate: 65,
                        difficulty: '中等',
                        tags: ['Vue3', 'Composition API'],
                        type: '单选',
                        content: '关于 Vue3 中 ref 和 reactive 的区别，下列说法正确的是：',
                        options: [
                            { label: 'A', text: 'ref 用于基本类型，reactive 用于对象类型' },
                            { label: 'B', text: 'ref 和 reactive 完全相同' },
                            { label: 'C', text: 'reactive 不能用于基本类型' },
                            { label: 'D', text: 'ref 不能用于对象类型' }
                        ],
                        answer: 'A',
                        explanation: 'ref 主要用于基本类型的响应式...'
                    },
                    {
                        id: 104,
                        title: 'Vue3 是否比 Vue2 性能更好？',
                        status: 'unsolved',
                        comments: 89,
                        passRate: 90,
                        difficulty: '简单',
                        tags: ['Vue3', '性能优化'],
                        type: '判断',
                        content: 'Vue3 在性能上是否优于 Vue2？',
                        answer: 'true',
                        explanation: 'Vue3 通过优化编译器、重写虚拟DOM...'
                    },
                    {
                        id: 105,
                        title: 'React Hooks的使用场景和注意事项',
                        status: 'attempted',
                        comments: 98,
                        passRate: 72,
                        difficulty: '中等',
                        tags: ['React', 'Hooks', '函数组件'],
                        type: '多选',
                        content: '以下关于 React Hooks 的说法，哪些是正确的？',
                        options: [
                            { label: 'A', text: 'Hooks 只能在函数组件中使用' },
                            { label: 'B', text: 'useEffect 可以替代所有生命周期方法' },
                            { label: 'C', text: 'Hooks 必须在组件顶层调用' },
                            { label: 'D', text: '自定义 Hook 必须以 use 开头' }
                        ],
                        answer: ['A', 'C', 'D'],
                        explanation: 'React Hooks 有特定的使用规则...'
                    },
                    {
                        id: 106,
                        title: '浏览器渲染过程详解',
                        status: 'unsolved',
                        comments: 156,
                        passRate: 68,
                        difficulty: '困难',
                        tags: ['浏览器', '渲染引擎', '性能优化'],
                        type: '单选',
                        content: '关于浏览器渲染过程，以下说法正确的是：',
                        options: [
                            { label: 'A', text: 'DOM树和CSSOM树是并行构建的' },
                            { label: 'B', text: '回流必定会触发重绘' },
                            { label: 'C', text: '重绘一定会触发回流' },
                            { label: 'D', text: 'JavaScript执行会阻塞DOM解析' }
                        ],
                        answer: 'B',
                        explanation: '浏览器渲染过程中，回流必定会导致重绘...'
                    },
                    {
                        id: 107,
                        title: 'CSS Grid 和 Flexbox 的使用场景比较',
                        status: 'unsolved',
                        comments: 145,
                        passRate: 75,
                        difficulty: '中等',
                        tags: ['CSS', 'Grid', 'Flexbox', '布局'],
                        type: '多选',
                        content: '以下关于 Grid 和 Flexbox 的说法，哪些是正确的？',
                        options: [
                            { label: 'A', text: 'Grid 更适合整体页面布局' },
                            { label: 'B', text: 'Flexbox 更适合一维布局' },
                            { label: 'C', text: 'Grid 只能创建规则的网格布局' },
                            { label: 'D', text: 'Flexbox 不能实现网格布局' }
                        ],
                        answer: ['A', 'B'],
                        explanation: 'Grid 和 Flexbox 各有其适用场景...'
                    },
                    {
                        id: 108,
                        title: 'TypeScript 高级类型使用指南',
                        status: 'attempted',
                        comments: 167,
                        passRate: 70,
                        difficulty: '困难',
                        tags: ['TypeScript', '类型系统', '泛型'],
                        type: '简答',
                        content: '请解释 TypeScript 中的 Partial、Required、Pick 和 Record 工具类型的作用和使用场景。',
                        answer: 'Partial<T> 将类型 T 的所有属性变为可选...',
                        explanation: '这些工具类型是 TypeScript 中常用的类型转换工具...'
                    },
                    {
                        id: 109,
                        title: 'Web 性能优化最佳实践',
                        status: 'solved',
                        comments: 198,
                        passRate: 82,
                        difficulty: '中等',
                        tags: ['性能优化', '前端优化', 'Web'],
                        type: '判断',
                        content: '使用 CDN 可以有效减少首屏加载时间。',
                        answer: true,
                        explanation: 'CDN 可以通过就近原则提供资源，减少网络延迟...'
                    }
                ]
            },
            {
                id: 2,
                name: '后端开发',
                icon: '🖥️',
                count: 890,
                description: '包含 Java、Spring、数据库等后端核心技术，系统设计到性能优化的深度探讨',
                questions: [
                    {
                        id: 201,
                        title: 'Spring Boot核心注解有哪些？',
                        status: 'solved',
                        comments: 234,
                        passRate: 92,
                        difficulty: '简单',
                        tags: ['Spring Boot', '注解', 'Java'],
                        type: '多选',
                        content: '以下哪些是 Spring Boot 的核心注解？',
                        options: [
                            { label: 'A', text: '@SpringBootApplication' },
                            { label: 'B', text: '@ComponentScan' },
                            { label: 'C', text: '@EnableAutoConfiguration' },
                            { label: 'D', text: '@Configuration' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: '这些都是 Spring Boot 中常用的核心注解，@SpringBootApplication 包含了其他三个注解的功能。'
                    },
                    {
                        id: 202,
                        title: 'Redis的数据类型及应用场景',
                        status: 'attempted',
                        comments: 189,
                        passRate: 78,
                        difficulty: '中等',
                        tags: ['Redis', '缓存', '数据类型'],
                        type: '简答',
                        content: '请详细说明 Redis 的五种基本数据类型及其适用场景。',
                        answer: 'Redis 的五种基本数据类型包括：String、List、Hash、Set、Sorted Set...',
                        explanation: '不同的数据类型适用于不同的业务场景，理解它们的特点对于系统设计很重要。'
                    },
                    {
                        id: 203,
                        title: 'MySQL索引原理及优化策略',
                        status: 'unsolved',
                        comments: 167,
                        passRate: 65,
                        difficulty: '困难',
                        tags: ['MySQL', '索引', '性能优化'],
                        type: '判断',
                        content: 'MySQL的 B+ 树索引在叶子节点存储了完整的数据记录。',
                        answer: false,
                        explanation: 'InnoDB 的 B+ 树索引，叶子节点存储的是主键值，而不是完整的数据记录。'
                    },
                    {
                        id: 204,
                        title: 'Java 并发编程核心概念',
                        status: 'attempted',
                        comments: 178,
                        passRate: 68,
                        difficulty: '困难',
                        tags: ['Java', '并发编程', '多线程'],
                        type: '多选',
                        content: '关于 Java 并发编程，以下哪些说法是正确的？',
                        options: [
                            { label: 'A', text: 'volatile 关键字可以保证可见性' },
                            { label: 'B', text: 'synchronized 方法比 synchronized 块的粒度更细' },
                            { label: 'C', text: 'ReentrantLock 可以实现公平锁' },
                            { label: 'D', text: 'ThreadLocal 可以实现线程隔离' }
                        ],
                        answer: ['A', 'C', 'D'],
                        explanation: 'Java 并发编程涉及多个重要概念，包括可见性、原子性、锁机制等。'
                    },
                    {
                        id: 205,
                        title: 'Spring Security 认证流程',
                        status: 'unsolved',
                        comments: 156,
                        passRate: 72,
                        difficulty: '中等',
                        tags: ['Spring', 'Security', '认证'],
                        type: '简答',
                        content: '请详细描述 Spring Security 的认证流程，包括主要组件和它们的作用。',
                        answer: 'Spring Security 的认证流程主要包括：AuthenticationFilter 拦截请求、AuthenticationManager 进行认证、UserDetailsService 加载用户信息、PasswordEncoder 进行密码校验等...',
                        explanation: '理解 Spring Security 的认证流程对于实现安全的身份验证系统至关重要。'
                    }
                ]
            },
            {
                id: 3,
                name: '人工智能',
                icon: '🤖',
                count: 567,
                description: '从机器学习基础到深度学习进阶，涉及神经网络、计算机视觉等热门领域',
                questions: [
                    {
                        id: 301,
                        title: '神经网络中反向传播算法的原理',
                        status: 'unsolved',
                        comments: 78,
                        passRate: 45,
                        difficulty: '困难',
                        tags: ['神经网络', '反向传播', '深度学习'],
                        type: '简答',
                        content: '请解释神经网络中反向传播算法的原理和计算过程。',
                        answer: '反向传播算法是通过计算损失函数对各层权重的梯度，从后向前逐层更新权重...',
                        explanation: '这是深度学习中最基础也是最重要的算法之一。'
                    },
                    {
                        id: 302,
                        title: '常见的机器学习算法对比',
                        status: 'solved',
                        comments: 145,
                        passRate: 82,
                        difficulty: '中等',
                        tags: ['机器学习', '算法', '模型对比'],
                        type: '多选',
                        content: '以下关于机器学习算法的说法，哪些是正确的？',
                        options: [
                            { label: 'A', text: '决策树适合处理非线性数据' },
                            { label: 'B', text: 'SVM 对特征缩放敏感' },
                            { label: 'C', text: '随机森林不容易过拟合' },
                            { label: 'D', text: '神经网络需要大量训练数据' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: '不同的机器学习算法有其各自的特点和适用场景。'
                    },
                    {
                        id: 303,
                        title: '卷积神经网络结构设计',
                        status: 'attempted',
                        comments: 134,
                        passRate: 65,
                        difficulty: '困难',
                        tags: ['CNN', '深度学习', '神经网络'],
                        type: '多选',
                        content: '设计卷积神经网络时，以下哪些原则是正确的？',
                        options: [
                            { label: 'A', text: '通常采用金字塔形的层级结构' },
                            { label: 'B', text: '池化层有助于减少计算量' },
                            { label: 'C', text: '批归一化可以加速训练' },
                            { label: 'D', text: '跳跃连接可以缓解梯度消失' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: 'CNN的设计需要考虑多个方面，包括网络深度、特征提取和训练效率等。'
                    },
                    {
                        id: 304,
                        title: '强化学习基础算法对比',
                        status: 'solved',
                        comments: 145,
                        passRate: 75,
                        difficulty: '中等',
                        tags: ['强化学习', 'RL', '算法'],
                        type: '判断',
                        content: 'Q-learning 是一种基于值函数的强化学习算法。',
                        answer: true,
                        explanation: 'Q-learning 是一种经典的值函数方法，它通过学习动作值函数来选择最优策略。'
                    }
                ]
            },
            {
                id: 4,
                name: '移动开发',
                icon: '📱',
                count: 432,
                description: '覆盖 Android、iOS 原生开发及跨平台解决方案，包括性能优化与最佳实践',
                questions: [
                    {
                        id: 401,
                        title: 'Android生命周期详解',
                        status: 'solved',
                        comments: 267,
                        passRate: 89,
                        difficulty: '简单',
                        tags: ['Android', '生命周期', '组件'],
                        type: '单选',
                        content: '关于 Android Activity 的生命周期，以下说法正确的是：',
                        options: [
                            { label: 'A', text: 'onCreate() 在 Activity 创建时调用' },
                            { label: 'B', text: 'onStart() 在 onResume() 之后调用' },
                            { label: 'C', text: 'onPause() 在 Activity 被销毁时调用' },
                            { label: 'D', text: 'onStop() 在 Activity 对用户不可见时调用' }
                        ],
                        answer: 'D',
                        explanation: 'Activity 的生命周期方法有特定的调用顺序和时机。'
                    },
                    {
                        id: 402,
                        title: 'iOS内存管理机制',
                        status: 'attempted',
                        comments: 134,
                        passRate: 71,
                        difficulty: '中等',
                        tags: ['iOS', '内存管理', 'ARC'],
                        type: '判断',
                        content: 'ARC(自动引用计数)机制完全消除了内存泄漏的可能性。',
                        answer: false,
                        explanation: 'ARC 虽然自动管理了内存，但循环引用等问题仍可能导致内存泄漏。'
                    },
                    {
                        id: 403,
                        title: 'Flutter 状态管理方案对比',
                        status: 'solved',
                        comments: 167,
                        passRate: 78,
                        difficulty: '中等',
                        tags: ['Flutter', '状态管理', 'Provider'],
                        type: '多选',
                        content: '关于 Flutter 的状态管理，以下哪些说法是正确的？',
                        options: [
                            { label: 'A', text: 'Provider 是官方推荐的状态管理方案' },
                            { label: 'B', text: 'GetX 提供了更简洁的语法' },
                            { label: 'C', text: 'Bloc 适合大型应用' },
                            { label: 'D', text: 'Redux 的状态流是单向的' }
                        ],
                        answer: ['A', 'C', 'D'],
                        explanation: 'Flutter 提供了多种状态管理方案，需要根据项目规模和需求选择合适的方案。'
                    },
                    {
                        id: 404,
                        title: 'React Native 性能优化',
                        status: 'attempted',
                        comments: 189,
                        passRate: 70,
                        difficulty: '困难',
                        tags: ['React Native', '性能优化', '移动端'],
                        type: '简答',
                        content: '请详细说明 React Native 应用的性能优化策略。',
                        answer: 'React Native 性能优化包括：使用 FlatList 代替 ScrollView、避免不必要的渲染、使用 useCallback 和 useMemo、优化图片加载等...',
                        explanation: '性能优化是 React Native 开发中的重要主题，需要从多个层面进行考虑。'
                    }
                ]
            },
            {
                id: 5,
                name: '数据库',
                icon: '💾',
                count: 345,
                description: '从关系型到 NoSQL 数据库，深入数据库原理、优化和高可用架构设计',
                questions: [
                    {
                        id: 501,
                        title: 'MongoDB和MySQL的应用场景对比',
                        status: 'solved',
                        comments: 198,
                        passRate: 86,
                        difficulty: '中等',
                        tags: ['MongoDB', 'MySQL', '数据库选型'],
                        type: '多选',
                        content: '以下关于 MongoDB 和 MySQL 的对比，哪些说法是正确的？',
                        options: [
                            { label: 'A', text: 'MongoDB 更适合处理非结构化数据' },
                            { label: 'B', text: 'MySQL 对事务支持更好' },
                            { label: 'C', text: 'MongoDB 的水平扩展性更好' },
                            { label: 'D', text: 'MySQL 的查询语言更标准' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: '选择数据库时需要根据具体的业务场景和需求来决定。'
                    },
                    {
                        id: 502,
                        title: '数据库事务的ACID特性',
                        status: 'attempted',
                        comments: 223,
                        passRate: 75,
                        difficulty: '中等',
                        tags: ['数据库', '事务', 'ACID'],
                        type: '判断',
                        content: '数据库的原子性(Atomicity)保证了事务要么全部执行，要么全部不执行。',
                        answer: true,
                        explanation: '原子性是事务的基本特性之一，确保了事务的完整性。'
                    },
                    {
                        id: 503,
                        title: 'PostgreSQL 高级特性应用',
                        status: 'unsolved',
                        comments: 145,
                        passRate: 68,
                        difficulty: '困难',
                        tags: ['PostgreSQL', '数据库', '高级特性'],
                        type: '多选',
                        content: 'PostgreSQL 相比其他关系型数据库具有哪些独特优势？',
                        options: [
                            { label: 'A', text: '支持复杂的地理信息处理' },
                            { label: 'B', text: '内置全文搜索功能' },
                            { label: 'C', text: '支持自定义数据类型' },
                            { label: 'D', text: '支持 JSON 数据类型' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: 'PostgreSQL 提供了许多高级特性，使其在特定场景下具有独特优势。'
                    },
                    {
                        id: 504,
                        title: '分布式数据库设计原则',
                        status: 'attempted',
                        comments: 178,
                        passRate: 72,
                        difficulty: '困难',
                        tags: ['分布式', '数据库设计', '架构'],
                        type: '简答',
                        content: '请详细说明分布式数据库设计时需要考虑的关键因素和原则。',
                        answer: '分布式数据库设计需要考虑数据一致性、可用性、分区容错性(CAP)，以及数据分片策略...',
                        explanation: '分布式数据库设计是一个复杂的主题，需要在多个目标之间做出权衡。'
                    }
                ]
            },
            {
                id: 6,
                name: '运维部署',
                icon: '🛠️',
                count: 278,
                description: '包括 Docker、K8s 等容器技术，以及自动化部署、监控和运维最佳实践',
                questions: [
                    {
                        id: 601,
                        title: 'Docker容器编排与服务发现',
                        status: 'attempted',
                        comments: 167,
                        passRate: 68,
                        difficulty: '困难',
                        tags: ['Docker', '容器编排', '服务发现'],
                        type: '多选',
                        content: '关于 Docker 容器编排，以下哪些说法是正确的？',
                        options: [
                            { label: 'A', text: 'Kubernetes 是最流行的容器编排工具' },
                            { label: 'B', text: 'Docker Swarm 集成在 Docker 引擎中' },
                            { label: 'C', text: '服务发现是容器编排的核心功能' },
                            { label: 'D', text: '容器编排可以自动处理负载均衡' }
                        ],
                        answer: ['A', 'B', 'C', 'D'],
                        explanation: '容器编排工具帮助管理分布式应用的部署和扩展。'
                    },
                    {
                        id: 602,
                        title: 'Kubernetes核心概念及应用',
                        status: 'unsolved',
                        comments: 145,
                        passRate: 62,
                        difficulty: '困难',
                        tags: ['Kubernetes', 'K8s', '容器编排'],
                        type: '简答',
                        content: '请解释 Kubernetes 中 Pod、Service、Deployment 的概念及其关系。',
                        answer: 'Pod 是最小的部署单元，Service 提供稳定的服务访问点，Deployment 管理 Pod 的部署和更新...',
                        explanation: '理解这些核心概念对于使用 Kubernetes 进行容器编排至关重要。'
                    }
                ]
            }
        ],
        pagination: {
            currentPage: 1,
            pageSize: 20,
            total: 0
        }
    }),

    getters: {
        getDisplayCategories: (state): Category[] => state.categories.slice(0, 6),
        getAllCategories: (state): Category[] => state.categories,
        getAllQuestions: (state): Question[] => {
            return state.categories.flatMap(category => category.questions);
        },
        getCategoryQuestions: (state) => (categoryId: string | number): Question[] => {
            if (categoryId === 'all') {
                return state.categories.flatMap(category => category.questions);
            }
            const category = state.categories.find(c => c.id === Number(categoryId));
            return category ? category.questions : [];
        },
        getAllTags: (state): string[] => {
            const tagSet = new Set<string>();
            state.categories.forEach(category => {
                category.questions.forEach(question => {
                    question.tags?.forEach(tag => tagSet.add(tag));
                });
            });
            return Array.from(tagSet).sort();
        },
        getAllTypes: (state): string[] => {
            const typeSet = new Set<string>();
            state.categories.forEach(category => {
                category.questions.forEach(question => {
                    if (question.type) {
                        typeSet.add(question.type);
                    }
                });
            });
            return Array.from(typeSet).sort();
        },
        getPaginatedQuestions: (state) => {
            const allQuestions = state.categories.flatMap(category => category.questions);
            state.pagination.total = allQuestions.length;

            const start = (state.pagination.currentPage - 1) * state.pagination.pageSize;
            const end = start + state.pagination.pageSize;

            return allQuestions.slice(start, end);
        },
        getTotalQuestions: (state): number => {
            const allQuestions = state.categories.flatMap(category => category.questions);
            return allQuestions.length;
        }
    },

    actions: {
        setPage(page: number) {
            this.pagination.currentPage = page;
        },

        resetPagination() {
            this.pagination.currentPage = 1;
        },

        updateTotal() {
            this.pagination.total = this.getTotalQuestions;
        }
    }
}) 