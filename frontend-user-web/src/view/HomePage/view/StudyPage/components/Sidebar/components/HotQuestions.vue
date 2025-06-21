<script lang="ts" setup>
import {onMounted, ref} from 'vue';

interface Question {
  id: number;
  title: string;
  views: number;
}

// Props 接收后端数据
const props = defineProps<{
  backendData?: Question[];
}>();

const hotQuestions = ref<Question[]>([]);

// 生成模拟数据
const generateMockData = (): Question[] => {
  const mockTitles = [
    "Vue3 组合式 API 的最佳实践",
    "JavaScript 异步编程详解",
    "TypeScript 类型系统深入理解",
    "React Hooks 使用技巧",
    "CSS Grid 布局完全指南",
    "Node.js 性能优化策略",
    "前端工程化配置详解",
    "数据结构与算法基础",
    "HTTP 协议原理分析",
    "微前端架构设计思路"
  ];
  
  return mockTitles.map((title, index) => ({
    id: index + 1,
    title,
    views: Math.floor(Math.random() * 5000) + 100
  }));
};

// 处理题目点击事件
const handleQuestionClick = (question: Question) => {
  console.log('跳转到题目详情页:', question.id);
  // 这里可以添加路由跳转逻辑
  // router.push(`/question/${question.id}`);
};

// 格式化访问量显示
const formatViews = (views: number): string => {
  if (views >= 1000) {
    return (views / 1000).toFixed(1) + 'k';
  }
  return views.toString();
};

onMounted(() => {
  // 如果有后端数据则使用，否则使用模拟数据
  hotQuestions.value = props.backendData || generateMockData();
});
</script>

<template>
  <div class="hot-questions-card">
    <div class="card-header">
      <h3 class="title">
        <svg class="title-icon" height="16" viewBox="0 0 16 16" width="16">
          <path d="M5.05.31c.81 2.17.41 3.38-.52 4.31C3.55 5.67 1.98 6.45.9 7.98c-1.45 2.05-1.7 6.53 3.53 7.7-2.2-1.16-2.67-4.52-.3-6.61-.61 2.03.53 3.33 1.94 2.86 1.39-.47 2.3.53 2.27 1.67-.02.78-.31 1.44-1.13 1.81 3.42-.59 4.78-3.42 4.78-5.56 0-2.84-2.53-3.22-1.25-5.61-1.52.13-2.03 1.13-1.89 2.75.09 1.08-1.02 1.8-1.86 1.33-.67-.41-.66-1.19-.06-1.78C8.18 5.31 8.68 2.45 5.05.32L5.05.31z" fill="#ff6b35"/>
        </svg>
        热门题目
      </h3>
    </div>
    
    <div class="questions-list">
      <div 
        v-for="(question, index) in hotQuestions" 
        :key="question.id"
        class="question-item"
        @click="handleQuestionClick(question)"
      >
        <div class="question-rank">{{ index + 1 }}</div>
        <div class="question-content">
          <div class="question-title">{{ question.title }}</div>
          <div class="question-meta">
            <svg class="view-icon" height="12" viewBox="0 0 16 16" width="12">
              <path d="M8 2C4.5 2 1.5 4.5 0 8c1.5 3.5 4.5 6 8 6s6.5-2.5 8-6c-1.5-3.5-4.5-6-8-6zm0 10c-2.2 0-4-1.8-4-4s1.8-4 4-4 4 1.8 4 4-1.8 4-4 4zm0-6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z" fill="#6c757d"/>
            </svg>
            <span class="view-count">{{ formatViews(question.views) }} 次浏览</span>
          </div>
        </div>
        <div class="question-arrow">
          <svg height="12" viewBox="0 0 16 16" width="12">
            <path d="M6 3l5 5-5 5V3z" fill="#adb5bd"/>
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.hot-questions-card {
  padding: 16px 20px;
  border-radius: 12px;
  background-color: #ffffff;
  border: 1px solid #e9ecef;
  font-family: 'ZCOOL XiaoWei', sans-serif;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.hot-questions-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  border-color: #ff6b35;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f1f3f4;
}

.title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.title-icon {
  flex-shrink: 0;
}

.question-count {
  background-color: #fff5f0;
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid #ffdbcc;
}

.count-text {
  font-size: 0.8rem;
  color: #ff6b35;
  font-weight: 500;
}

.questions-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.question-item {
  display: flex;
  align-items: center;
  padding: 12px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.15s ease;
  position: relative;
  overflow: hidden;
}

.question-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 107, 53, 0.05), transparent);
  transition: left 0.2s ease;
  z-index: 1;
}

.question-item:hover::before {
  left: 100%;
}

.question-item:hover {
  background-color: #fafbfc;
  transform: translateX(4px);
}

.question-rank {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff6b35, #ff8c42);
  color: white;
  font-size: 0.8rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
  position: relative;
  z-index: 2;
}

.question-content {
  flex: 1;
  min-width: 0;
  position: relative;
  z-index: 2;
}

.question-title {
  font-size: 0.9rem;
  color: #2c3e50;
  font-weight: 500;
  line-height: 1.4;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.15s ease;
}

.question-item:hover .question-title {
  color: #ff6b35;
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 4px;
}

.view-icon {
  flex-shrink: 0;
  opacity: 0.7;
}

.view-count {
  font-size: 0.75rem;
  color: #6c757d;
  opacity: 0.8;
}

.question-arrow {
  margin-left: 8px;
  opacity: 0;
  transform: translateX(-4px);
  transition: all 0.15s ease;
  position: relative;
  z-index: 2;
}

.question-item:hover .question-arrow {
  opacity: 1;
  transform: translateX(0);
}

/* 特殊样式：前三名题目 */
.question-item:nth-child(1) .question-rank {
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  box-shadow: 0 2px 8px rgba(255, 215, 0, 0.3);
}

.question-item:nth-child(2) .question-rank {
  background: linear-gradient(135deg, #c0c0c0, #e8e8e8);
  box-shadow: 0 2px 8px rgba(192, 192, 192, 0.3);
}

.question-item:nth-child(3) .question-rank {
  background: linear-gradient(135deg, #cd7f32, #daa520);
  box-shadow: 0 2px 8px rgba(205, 127, 50, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hot-questions-card {
    padding: 12px 16px;
  }
  
  .title {
    font-size: 1.1rem;
  }
  
  .question-item {
    padding: 10px 6px;
  }
  
  .question-title {
    font-size: 0.85rem;
  }
  
  .question-rank {
    width: 20px;
    height: 20px;
    font-size: 0.7rem;
    margin-right: 10px;
  }
}

@media (max-width: 480px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .question-title {
    font-size: 0.8rem;
  }
  
  .view-count {
    font-size: 0.7rem;
  }
}
</style>