# 证据映射

## 题目与系统定位

| 论文结论 | 证据文件 | 证据说明 |
| --- | --- | --- |
| 系统为AI驱动知识问答平台 | `README.md` | 项目名称为 SlothAsk，明确描述为基于AI驱动的智能知识问答平台 |
| 项目采用 Spring Boot 微服务架构 | `README.md`、`Service-Infrastructure/pom.xml`、各服务入口类 | README 与多服务目录共同证明系统为 Spring Boot 微服务体系 |
| 系统包含用户端与管理端前端 | `frontend-user-web`、`frontend-admin-web` | 用户端和管理端为独立前端工程 |

## 需求与业务证据

| 业务能力 | 证据文件 | 可核对事实 |
| --- | --- | --- |
| 题库项目、分类与题目浏览 | `Service-Question/src/main/java/org/example/servicequestion/user/questionBank/controller/GetQuestionBankController.java` | 提供项目列表、分类列表、分类详情、题目数量与题目列表接口 |
| 题目详情查看、热门题目、同分类题目导航 | `Service-Question/src/main/java/org/example/servicequestion/user/question/controller/GetUserQuestionController.java` | 提供题目详情、答案、同分类题目分页和热门题目接口 |
| 题目筛选学习与统计 | `Service-Question/src/main/java/org/example/servicequestion/user/study/controller/GetUserStudyController.java` | 提供推荐分类、标签筛选、题目过滤、提交热力图和热门题目接口 |
| 用户保存答案与提交答案 | `Service-Question/src/main/java/org/example/servicequestion/user/answer/controller/PostAnswerQuestionController.java` | 提供保存草稿和提交答案接口 |
| 用户获取答题记录 | `Service-Question/src/main/java/org/example/servicequestion/user/answer/controller/GetAnswerQuestionController.java` | 支持读取题目答题记录 |
| AI解析请求 | `Service-Question/src/main/java/org/example/servicequestion/user/aiAnalysis/service/Impl/SendAiAnalysisServiceImpl.java` | 校验用户、校验答题状态、设置Redis锁并发送RabbitMQ消息 |
| AI解析执行 | `Service-Ai/src/main/java/org/example/serviceai/userAnswer/consumer/AiAnalysisConsumer.java` | 从队列消费消息，取题目与答案，调用大模型并保存结果 |
| 语义向量检索 | `Service-Question/src/main/java/org/example/servicequestion/user/search/controller/GetUserQuestionSearchController.java`、`QuestionVectorServiceImpl.java` | 支持批量向量化、进度查询和相似题目搜索 |
| 向量嵌入生成 | `Service-Ai/src/main/java/org/example/serviceai/userSearch/UserSearchEmbeddingController.java` | 提供单条和批量文本嵌入服务 |
| 评论与点赞 | `Service-Question/src/main/java/org/example/servicequestion/user/questionComment/service/Impl/PostQuestionCommentServiceImpl.java` | 支持发表评论、点赞、取消点赞和级联删除 |
| 学习历史 | `Service-Question/src/main/java/org/example/servicequestion/user/question/service/Impl/QuestionHistoryServiceImpl.java` | 采用队列和批量入库策略记录浏览历史 |
| 实时消息 | `Service-Notification/src/main/java/org/example/servicenotification/baseMessage/controller/ConnectSSEController.java` | 支持 SSE 建链与在线状态维护 |
| 网关统一注入用户头 | `Service-Gateway/src/main/java/org/example/servicegateway/config/Filter/UserHeaderGlobalFilter.java` | 登录用户访问时自动写入 `X-User-Id` 与 `X-Upc-Id` |

## 数据库设计证据

| 数据对象 | 证据文件 | 可核对事实 |
| --- | --- | --- |
| 题库内容表 `question` | `design/database.sql` | 包含题目标题、内容、标准答案、难度、类型、标签和浏览量字段 |
| 题库分类表 `question_category` | `design/database.sql` | 存储分类名称、描述、图标、排序、访问量与权限关联基础 |
| 项目分类表 `project_category` | `design/database.sql` | 支撑项目级题库聚合 |
| 用户答题记录表 `user_question_record` | `design/database.sql` | 包含用户、题目、用户答案、提交状态和时间字段 |
| AI分析结果表 `user_answer_ai_analysis` | `design/database.sql` | 包含准确率、AI解析内容、来源模型与时间字段 |
| 评论表 `question_comment` | `design/database.sql` | 采用父子结构支持回复链路 |
| 评论点赞表 `question_comment_like` | `design/database.sql` | 建立用户与评论的点赞关系 |
| 学习历史表 `user_question_history` | `design/database.sql` | 存储用户浏览题目历史 |
| 错题本 `wrong_question_book` | `design/database.sql` | 存储用户错题与掌握状态 |

## 前端交互证据

| 前端页面或组件 | 证据文件 | 可核对事实 |
| --- | --- | --- |
| 题库页 | `frontend-user-web/src/view/HomePage/view/QuestionBankPage/BankPage/index.vue` | 展示项目、分类和题目列表 |
| 题目详情页 | `frontend-user-web/src/view/HomePage/view/QuestionPage/index.vue` | 包含题目、回答、评论、边栏推荐 |
| 回答编辑与AI分析 | `frontend-user-web/src/view/HomePage/view/QuestionPage/components/container/QuestionDetail/components/ShortAnswer/components/MyAnswer.vue` | 支持草稿保存、提交答案、重新答题和自动触发AI分析 |
| 搜索页 | `frontend-user-web/src/view/HomePage/view/SearchPage/index.vue` | 结合搜索API展示结果 |
| 学习统计页 | `frontend-user-web/src/view/HomePage/view/StudyPage/index.vue` | 展示热力图、热门题目和筛选功能 |
| 用户中心 | `frontend-user-web/src/view/HomePage/view/AccountPage/index.vue` | 集成历史、错题、收藏、资料维护 |
| 管理端题目维护 | `frontend-admin-web/src/view/MainView/system/roles/index.vue`、`frontend-admin-web/src/view/MainView/stats/question/index.vue` 等 | 说明系统存在后台管理界面与统计视图 |

## 论文章节映射

| 章节 | 主要证据来源 |
| --- | --- |
| 第1章 绪论 | `README.md`、参考文献、项目架构说明 |
| 第2章 系统需求分析 | 题库、答题、搜索、AI分析、评论、历史等控制器与前端页面 |
| 第3章 系统设计 | 网关、配置中心、数据库脚本、向量检索服务、通知服务、AI服务 |
| 第4章 系统实现 | 题库浏览、答题提交、AI解析、语义搜索、学习统计、评论互动的实现类与组件 |
| 第5章 系统测试 | 控制器契约、数据库字段约束、前端交互流程与可推导测试场景 |
