<template>
  <div class="question-container" v-if="question">
    <div class="question-detail">
      <div class="title-section">
        <h1 class="question-title">{{ question?.id }}.{{ question?.title }}</h1>
      </div>
      <div class="question-meta">
        <div class="meta-left">
          <span :class="`difficulty difficulty-${question?.difficulty}`">
            {{ difficultyLabel(question?.difficulty ?? 0) }}
          </span>
          <span :class="`type-tag type-${question?.type}`">
            {{ typeLabel(question?.type ?? 0) }}
          </span>
          <div class="tags">
            <span v-for="(tag, index) in question?.tags.split(',').map(tag => tag.trim())" :key="index" class="tag">
              {{ tag }}
            </span>
          </div>
        </div>
        <div class="actions">
          <el-button text icon="view" @click="handleShareClick">{{ question.view_count }}</el-button>
          <el-button text icon="share" @click="handleShareClick">分享</el-button>
          <el-button text icon="star" @click="handleStarClick">收藏</el-button>
        </div>
      </div>
    </div>

    <div class="question-content" v-if="questionComponent">
      <component :is="questionComponent" :question="question" />
      <div class="submit-section" v-if="question?.type !== 4">
        <el-button type="primary" size="large" @click="handleSubmit">提交答案</el-button>
      </div>
    </div>

    <div class="question-answer">
      <div class="section-header">
        <h2 class="section-title">答案解析</h2>
        <el-button type="primary" plain>查看答案</el-button>
      </div>
      <div class="answer-content">
        <div class="answer-text">
          {{ question.answer }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useQuestionBankStore } from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import SingleChoice from './SingleChoice.vue';
import MultipleChoice from './MultipleChoice.vue';
import TrueFalse from './TrueFalse.vue';
import ShortAnswer from './ShortAnswer.vue';
import { setTitle } from '../../../../../utils/title';

const route = useRoute();
const questionBank = useQuestionBankStore();

const questionId = computed(() => Number(route.params.questionId));

const question = computed(() => {
  return questionBank.getAllQuestions.find(q => q.id === questionId.value);
});

// 获取难度对应的文本标签
const difficultyLabel = (difficulty: number) => {
  switch (difficulty) {
    case 1:
      return '简单';
    case 2:
      return '中等';
    case 3:
      return '困难';
    default:
      return '';
  }
};

// 获取题目类型对应的文本标签
const typeLabel = (type: number) => {
  switch (type) {
    case 1:
      return '单选';
    case 2:
      return '多选';
    case 3:
      return '判断';
    case 4:
      return '简答';
    default:
      return '';
  }
};

// 根据问题类型返回对应的组件
const questionComponent = computed(() => {
  if (!question.value) return null;

  const componentMap = {
    1: SingleChoice, // 单选
    2: MultipleChoice, // 多选
    3: TrueFalse, // 判断
    4: ShortAnswer, // 简答
  };

  return componentMap[question.value.type] || null;
});

const handleShareClick = () => {
  console.log('分享点击');
};
const handleStarClick = () => {
  console.log('收藏点击');
};
const handlePassClick = () => {
  console.log('查看点击');
};

onMounted(() => {
  setTitle(`${question.value?.title || '题目详情'}`);
});

// 当题目变化时更新标题
watch(() => question.value?.title, (newTitle) => {
  if (newTitle) {
    setTitle(newTitle);
  }
});

const handleSubmit = () => {
  if (!question.value) return;

  // 获取当前题目组件的答案
  const currentComponent = questionComponent.value;
  if (!currentComponent) return;

  // 提交答案逻辑
  try {
    const answer = currentComponent.getAnswer?.();
    if (answer !== undefined) {
      console.log('提交答案:', answer);
      // TODO: 调用API提交答案
    }
  } catch (error) {
    console.error('获取答案失败:', error);
  }
};
</script>


<style scoped>
.question-container {
  width: 100%;
  margin: 0 auto;
  padding: 6px 18px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}


.question-answer,
.question-content,
.question-detail{
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 32px;
}

.title-section {
  margin-bottom: 16px;
}



.question-title {
  font-size: 24px;
  font-weight: 600;
  margin: 8px 0;
  line-height: 1.4;
  color: #1a1a1a;
}

.question-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
}

.meta-left,
.meta-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  color: #1a1a1a;
}



.difficulty,
.type-tag {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 14px;
}

.difficulty-1 {
  color: #52c41a;
  background: #f6ffed;
}

.difficulty-2 {
  color: #faad14;
  background: #fff7e6;
}

.difficulty-3 {
  color: #ff4d4f;
  background: #fff1f0;
}

.type-1 {
  color: #1890ff;
  background: #e6f7ff;
}

.type-2 {
  color: #722ed1;
  background: #f9f0ff;
}

.type-3 {
  color: #52c41a;
  background: #f6ffed;
}

.type-4 {
  color: #fa8c16;
  background: #fff7e6;
}

.tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  padding: 4px 12px;
  background: #f5f7fa;
  border-radius: 16px;
  font-size: 13px;
  color: #666;
}

.submit-section {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
