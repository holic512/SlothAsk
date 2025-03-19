import { defineStore } from 'pinia'

interface Question {
    id: number; // 自增主键
    category_id: number; // 分类ID
    title: string; // 题目标题
    content: string; // 题目内容
    answer: string | string[] | boolean; // 题目答案
    difficulty: number; // 1:简单 2:中等 3:困难
    type: number; // 1:单选 2:多选 3:判断 4:简答
    tags: string; // 题目标签
    status: number; // 1:正常 0:禁用
    view_count: number; // 浏览次数
    create_time: string; // 创建时间
    update_time: string; // 更新时间
}

interface Category {
    id: number; // 自增主键
    project_id: number; // 所属项目ID
    name: string; // 分类名称
    description: string; // 分类描述
    creator_id: number; // 创建者ID
    avatar_url: string; // 分类头像URL
    sort_order: number; // 排序序号
    view_count: number; // 访问数量
    status: number; // 1:正常 0:禁用
    create_time: string; // 创建时间
    update_time: string; // 更新时间
}

interface PaginationState {
    currentPage: number;
    pageSize: number;
    total: number;
}

interface QuestionBankState {
    questions: Question[];
    categories: Category[];
    pagination: PaginationState;
}

export const useQuestionBankStore = defineStore('questionBank', {
    state: (): QuestionBankState => ({
        categories: [
            {
                "id": 1,
                "project_id": 1,
                "name": "前端开发",
                "description": "涵盖 Vue、React、TypeScript 等前端主流技术栈，从基础到高级的全面解析",
                "creator_id": 1,
                "avatar_url": "",
                "sort_order": 1,
                "view_count": 1234,
                "status": 1,
                "create_time": "2023-01-01T00:00:00Z",
                "update_time": "2023-01-01T00:00:00Z"
            },
            {
                "id": 2,
                "project_id": 1,
                "name": "后端开发",
                "description": "包含 Java、Spring、数据库等后端核心技术，系统设计到性能优化的深度探讨",
                "creator_id": 1,
                "avatar_url": "",
                "sort_order": 2,
                "view_count": 890,
                "status": 1,
                "create_time": "2023-01-02T00:00:00Z",
                "update_time": "2023-01-02T00:00:00Z"
            },
            {
                "id": 3,
                "project_id": 1,
                "name": "人工智能",
                "description": "从机器学习基础到深度学习进阶，涉及神经网络、计算机视觉等热门领域",
                "creator_id": 1,
                "avatar_url": "",
                "sort_order": 3,
                "view_count": 567,
                "status": 1,
                "create_time": "2023-01-03T00:00:00Z",
                "update_time": "2023-01-03T00:00:00Z"
            },
            {
                "id": 4,
                "project_id": 1,
                "name": "移动开发",
                "description": "覆盖 Android、iOS 原生开发及跨平台解决方案，包括性能优化与最佳实践",
                "creator_id": 1,
                "avatar_url": "",
                "sort_order": 4,
                "view_count": 432,
                "status": 1,
                "create_time": "2023-01-04T00:00:00Z",
                "update_time": "2023-01-04T00:00:00Z"
            },
            {
                "id": 5,
                "project_id": 1,
                "name": "数据库",
                "description": "从关系型到 NoSQL 数据库，深入数据库原理、优化和高可用架构设计",
                "creator_id": 1,
                "avatar_url": "",
                "sort_order": 5,
                "view_count": 345,
                "status": 1,
                "create_time": "2023-01-05T00:00:00Z",
                "update_time": "2023-01-05T00:00:00Z"
            },
            {
                "id": 6,
                "project_id": 1,
                "name": "运维部署",
                "description": "包括 Docker、K8s 等容器技术，以及自动化部署、监控和运维最佳实践",
                "creator_id": 1,
                "avatar_url": "",
                "sort_order": 6,
                "view_count": 278,
                "status": 1,
                "create_time": "2023-01-06T00:00:00Z",
                "update_time": "2023-01-06T00:00:00Z"
            }
        ],
        questions:[
                {
                    "id": 101,
                    "category_id": 1,
                    "title": "Vue3的响应式原理是什么？",
                    "content": "请详细解释 Vue3 的响应式原理，包括与 Vue2 的区别。",
                    "answer": "Vue3 使用 Proxy 代替 Vue2 的 Object.defineProperty 来实现响应式...",
                    "difficulty": 2,
                    "type": 4,
                    "tags": "Vue3,响应式,Proxy",
                    "status": 1,
                    "view_count": 125,
                    "create_time": "2023-01-01T00:00:00Z",
                    "update_time": "2023-01-01T00:00:00Z"
                },
                {
                    "id": 102,
                    "category_id": 1,
                    "title": "以下哪些是 Vue3 的新特性？",
                    "content": "关于 Vue3 的新特性，下列说法正确的有：\nA. Composition API 的引入\nB. Teleport 组件\nC. Fragments 支持\nD. 更好的 TypeScript 支持",
                    "answer": "A,B,C,D",
                    "difficulty": 1,
                    "type": 2,
                    "tags": "Vue3,基础概念",
                    "status": 1,
                    "view_count": 98,
                    "create_time": "2023-01-02T00:00:00Z",
                    "update_time": "2023-01-02T00:00:00Z"
                },
                {
                    "id": 103,
                    "category_id": 1,
                    "title": "Vue3 中 ref 和 reactive 的区别",
                    "content": "关于 Vue3 中 ref 和 reactive 的区别，下列说法正确的是：\nA. ref 用于基本类型，reactive 用于对象类型\nB. ref 和 reactive 完全相同\nC. reactive 不能用于基本类型\nD. ref 不能用于对象类型",
                    "answer": "A",
                    "difficulty": 2,
                    "type": 1,
                    "tags": "Vue3,Composition API",
                    "status": 1,
                    "view_count": 156,
                    "create_time": "2023-01-03T00:00:00Z",
                    "update_time": "2023-01-03T00:00:00Z"
                },
                {
                    "id": 201,
                    "category_id": 2,
                    "title": "Spring Boot 核心注解有哪些？",
                    "content": "以下哪些是 Spring Boot 的核心注解？\nA. @SpringBootApplication\nB. @ComponentScan\nC. @EnableAutoConfiguration\nD. @Configuration",
                    "answer": "A,B,C,D",
                    "difficulty": 1,
                    "type": 2,
                    "tags": "Spring Boot,注解,Java",
                    "status": 1,
                    "view_count": 234,
                    "create_time": "2023-01-05T00:00:00Z",
                    "update_time": "2023-01-05T00:00:00Z"
                },
                {
                    "id": 202,
                    "category_id": 2,
                    "title": "Redis 的数据类型及应用场景",
                    "content": "请详细说明 Redis 的五种基本数据类型及其适用场景。",
                    "answer": "Redis 的五种基本数据类型包括：String、List、Hash、Set、Sorted Set...",
                    "difficulty": 2,
                    "type": 4,
                    "tags": "Redis,缓存,数据类型",
                    "status": 1,
                    "view_count": 189,
                    "create_time": "2023-01-06T00:00:00Z",
                    "update_time": "2023-01-06T00:00:00Z"
                },
                {
                    "id": 203,
                    "category_id": 2,
                    "title": "MySQL 索引原理及优化策略",
                    "content": "MySQL 的 B+ 树索引在叶子节点存储了完整的数据记录。",
                    "answer": "false",
                    "difficulty": 3,
                    "type": 3,
                    "tags": "MySQL,索引,性能优化",
                    "status": 1,
                    "view_count": 167,
                    "create_time": "2023-01-07T00:00:00Z",
                    "update_time": "2023-01-07T00:00:00Z"
                },
                {
                    "id": 301,
                    "category_id": 3,
                    "title": "神经网络中反向传播算法的原理",
                    "content": "请解释神经网络中反向传播算法的原理和计算过程。",
                    "answer": "反向传播算法是通过计算损失函数对各层权重的梯度，从后向前逐层更新权重...",
                    "difficulty": 3,
                    "type": 4,
                    "tags": "神经网络,反向传播,深度学习",
                    "status": 1,
                    "view_count": 78,
                    "create_time": "2023-01-08T00:00:00Z",
                    "update_time": "2023-01-08T00:00:00Z"
                },
                {
                    "id": 302,
                    "category_id": 3,
                    "title": "常见的机器学习算法对比",
                    "content": "以下关于机器学习算法的说法，哪些是正确的？\nA. 决策树适合处理非线性数据\nB. SVM 对特征缩放敏感\nC. 随机森林不容易过拟合\nD. 神经网络需要大量训练数据",
                    "answer": "A,B,C,D",
                    "difficulty": 2,
                    "type": 2,
                    "tags": "机器学习,算法,模型对比",
                    "status": 1,
                    "view_count": 145,
                    "create_time": "2023-01-09T00:00:00Z",
                    "update_time": "2023-01-09T00:00:00Z"
                },
                {
                    "id": 303,
                    "category_id": 3,
                    "title": "卷积神经网络结构设计",
                    "content": "设计卷积神经网络时，以下哪些原则是正确的？\nA. 通常采用金字塔形的层级结构\nB. 池化层有助于减少计算量\nC. 批归一化可以加速训练\nD. 跳跃连接可以缓解梯度消失",
                    "answer": "A,B,C,D",
                    "difficulty": 3,
                    "type": 2,
                    "tags": "CNN,深度学习,神经网络",
                    "status": 1,
                    "view_count": 134,
                    "create_time": "2023-01-10T00:00:00Z",
                    "update_time": "2023-01-10T00:00:00Z"
                },{
                "id": 401,
                "category_id": 4,
                "title": "Android 生命周期详解",
                "content": "关于 Android Activity 的生命周期，以下说法正确的是：\nA. onCreate() 在 Activity 创建时调用\nB. onStart() 在 onResume() 之后调用\nC. onPause() 在 Activity 被销毁时调用\nD. onStop() 在 Activity 对用户不可见时调用",
                "answer": "A,D",
                "difficulty": 1,
                "type": 2,
                "tags": "Android,生命周期,组件",
                "status": 1,
                "view_count": 267,
                "create_time": "2023-01-08T00:00:00Z",
                "update_time": "2023-01-08T00:00:00Z"
                },
                {
                    "id": 402,
                    "category_id": 4,
                    "title": "Flutter 状态管理方案对比",
                    "content": "关于 Flutter 的状态管理，以下哪些说法是正确的？\nA. Provider 是官方推荐的状态管理方案\nB. GetX 提供了更简洁的语法\nC. Bloc 适合大型应用\nD. Redux 的状态流是单向的",
                    "answer": "A,C,D",
                    "difficulty": 2,
                    "type": 2,
                    "tags": "Flutter,状态管理,Provider",
                    "status": 1,
                    "view_count": 167,
                    "create_time": "2023-01-09T00:00:00Z",
                    "update_time": "2023-01-09T00:00:00Z"
                },
                {
                    "id": 403,
                    "category_id": 4,
                    "title": "React Native 性能优化",
                    "content": "请详细说明 React Native 应用的性能优化策略。",
                    "answer": "React Native 性能优化包括：使用 FlatList 代替 ScrollView、避免不必要的渲染、使用 useCallback 和 useMemo、优化图片加载等...",
                    "difficulty": 3,
                    "type": 4,
                    "tags": "React Native,性能优化,移动端",
                    "status": 1,
                    "view_count": 189,
                    "create_time": "2023-01-10T00:00:00Z",
                    "update_time": "2023-01-10T00:00:00Z"
                },
                {
                    "id": 501,
                    "category_id": 5,
                    "title": "MongoDB 和 MySQL 的应用场景对比",
                    "content": "以下关于 MongoDB 和 MySQL 的对比，哪些说法是正确的？\nA. MongoDB 更适合处理非结构化数据\nB. MySQL 对事务支持更好\nC. MongoDB 的水平扩展性更好\nD. MySQL 的查询语言更标准",
                    "answer": "A,B,C,D",
                    "difficulty": 2,
                    "type": 2,
                    "tags": "MongoDB,MySQL,数据库选型",
                    "status": 1,
                    "view_count": 198,
                    "create_time": "2023-01-11T00:00:00Z",
                    "update_time": "2023-01-11T00:00:00Z"
                },
                {
                    "id": 502,
                    "category_id": 5,
                    "title": "数据库事务的 ACID 特性",
                    "content": "数据库的原子性 (Atomicity) 保证了事务要么全部执行，要么全部不执行。",
                    "answer": "true",
                    "difficulty": 2,
                    "type": 3,
                    "tags": "数据库,事务,ACID",
                    "status": 1,
                    "view_count": 223,
                    "create_time": "2023-01-12T00:00:00Z",
                    "update_time": "2023-01-12T00:00:00Z"
                },
                {
                    "id": 503,
                    "category_id": 5,
                    "title": "PostgreSQL 高级特性应用",
                    "content": "PostgreSQL 相比其他关系型数据库具有哪些独特优势？\nA. 支持复杂的地理信息处理\nB. 内置全文搜索功能\nC. 支持自定义数据类型\nD. 支持 JSON 数据类型",
                    "answer": "A,B,C,D",
                    "difficulty": 3,
                    "type": 2,
                    "tags": "PostgreSQL,数据库,高级特性",
                    "status": 1,
                    "view_count": 145,
                    "create_time": "2023-01-13T00:00:00Z",
                    "update_time": "2023-01-13T00:00:00Z"
                },
                {
                    "id": 601,
                    "category_id": 6,
                    "title": "Docker 容器编排与服务发现",
                    "content": "关于 Docker 容器编排，以下哪些说法是正确的？\nA. Kubernetes 是最流行的容器编排工具\nB. Docker Swarm 集成在 Docker 引擎中\nC. 服务发现是容器编排的核心功能\nD. 容器编排可以自动处理负载均衡",
                    "answer": "A,B,C,D",
                    "difficulty": 3,
                    "type": 2,
                    "tags": "Docker,容器编排,服务发现",
                    "status": 1,
                    "view_count": 167,
                    "create_time": "2023-01-14T00:00:00Z",
                    "update_time": "2023-01-14T00:00:00Z"
                },
                {
                    "id": 602,
                    "category_id": 6,
                    "title": "Kubernetes 核心概念及应用",
                    "content": "请解释 Kubernetes 中 Pod、Service、Deployment 的概念及其关系。",
                    "answer": "Pod 是最小的部署单元，Service 提供稳定的服务访问点，Deployment 管理 Pod 的部署和更新...",
                    "difficulty": 3,
                    "type": 4,
                    "tags": "Kubernetes,K8s,容器编排",
                    "status": 1,
                    "view_count": 145,
                    "create_time": "2023-01-15T00:00:00Z",
                    "update_time": "2023-01-15T00:00:00Z"
                },
                {
                    "id": 603,
                    "category_id": 6,
                    "title": "DevOps 持续集成与部署",
                    "content": "关于 CI/CD，以下哪些说法是正确的？\nA. 持续集成 (CI) 关注代码的自动化构建和测试\nB. 持续部署 (CD) 关注将代码自动化部署到生产环境\nC. CI/CD 需要 DevOps 工具链支持\nD. CD 一定是自动化的",
                    "answer": "A,B,C",
                    "difficulty": 2,
                    "type": 2,
                    "tags": "DevOps,CI/CD,自动化部署",
                    "status": 1,
                    "view_count": 112,
                    "create_time": "2023-01-16T00:00:00Z",
                    "update_time": "2023-01-16T00:00:00Z"
                }
        ],
        pagination: {
            currentPage: 1,
            pageSize: 20,
            total: 0
        }
    }),

    getters: {
        // 获取显示的分类（前6个）
        getDisplayCategories: (state): Category[] => state.categories.slice(0, 6),

        // 获取所有分类
        getAllCategories: (state): Category[] => state.categories,

        // 获取所有问题
        getAllQuestions: (state): Question[] => state.questions,

        // 获取指定分类下的问题
        getCategoryQuestions: (state) => (categoryId: string | number): Question[] => {
            if (categoryId === 'all') {
                return state.questions;
            }
            return state.questions.filter(question => question.category_id === Number(categoryId));
        },
        // 获取指定分类下的问题数量
        getQuestionCountByCategory: (state) => (categoryId: number): number => {
            return state.questions.filter(question => question.category_id === categoryId).length;
        },
        // 获取所有问题数量
        getTotalQuestionCount(): number {
            return this.questions.filter(question => question.category_id != null).length;
        },

        // 获取所有标签
        getAllTags: (state): string[] => {
            const tagSet = new Set<string>();
            state.questions.forEach(question => {
                question.tags.split(',').forEach(tag => tagSet.add(tag.trim()));
            });
            return Array.from(tagSet).sort();
        },

        // 获取所有问题类型
        getAllTypes: (state): number[] => {
            const typeSet = new Set<number>();
            state.questions.forEach(question => {
                typeSet.add(question.type);
            });
            return Array.from(typeSet).sort();
        },

        // 获取分页后的问题
        getPaginatedQuestions: (state): Question[] => {
            state.pagination.total = state.questions.length;

            const start = (state.pagination.currentPage - 1) * state.pagination.pageSize;
            const end = start + state.pagination.pageSize;

            return state.questions.slice(start, end);
        },


    },

    actions: {
        setPage(page: number) {
            this.pagination.currentPage = page;
        },

        resetPagination() {
            this.pagination.currentPage = 1;
        },

        updateTotal() {
            this.pagination.total = this.getTotalQuestionCount;
        }
    }
}) 