<template>
  <div class="question-container" v-if="question">
    <div class="question-detail">
      <div class="title-section">
        <h1 class="question-title">{{ question?.title }}</h1>
      </div>
      <div class="question-meta">
        <div class="meta-left">
          <span :class="`difficulty difficulty-${question?.difficulty}`">
            {{ difficultyLabel(question?.difficulty ?? 0) }}
          </span>
          <span :class="`type-tag type-${question?.type}`">
            {{ typeLabel(question?.type ?? 0) }}
          </span>
          <div class="tags-container">
            <span class="tag" v-for="(tag, index) in displayedTags" :key="tag.id">
              {{ tag.name }}
            </span>
            <span class="more-tags" v-if="hasMoreTags" @click="showAllTags = !showAllTags">
              {{ showAllTags ? '收起' : `+${question?.tags.length - maxVisibleTags}` }}
            </span>
          </div>
        </div>
        <div class="actions">
          <el-button text icon="view" @click="handleShareClick">{{ question.viewCount }}</el-button>
          <el-button text icon="share" @click="handleShareClick">分享</el-button>
          <el-button text icon="star" @click="handleStarClick">收藏</el-button>
        </div>
      </div>
    </div>

    <!--    答题页面  -->
    <div class="question-content" v-if="questionComponent">
      <!--  答题页面  -->
      <component :is="questionComponent" :question="question"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, defineAsyncComponent, onMounted, ref, watch} from 'vue';
import {useRoute} from 'vue-router';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';

import {setTitle} from '@/utils/title';
import {QuestionInterface} from "@/view/HomePage/view/QuestionPage/interface/QuestionInterface";
import {ApiGetQuestionByVirtualId} from "@/view/HomePage/view/QuestionPage/service/ApiGetQuestionByVirtualId";

// 使用异步组件动态导入，只在需要时加载
const SingleChoice = defineAsyncComponent(() =>
    import('./components/SingleChoice.vue')
);
const MultipleChoice = defineAsyncComponent(() =>
    import("@/view/HomePage/view/QuestionPage/components/container/QuestionDetail/components/MultipleChoice.vue")
);
const TrueFalse = defineAsyncComponent(() =>
    import('./components/TrueFalse.vue')
);
const ShortAnswer = defineAsyncComponent(() =>
    import('./components/ShortAnswer.vue')
);

const route = useRoute();
const questionBank = useQuestionBankStore();

// 根据路由获取 题目信息
const question = ref<QuestionInterface>();

watch(() => route.params.questionId as string, async (newValue) => {
  if (newValue) {  // 确保newValue存在
    const result = await ApiGetQuestionByVirtualId(newValue);  // 使用newValue而不是questionId.value
    if (result.status === 200) {
      question.value = result.data;
    }
  }
}, {immediate: true});


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

  // 根据题目类型返回对应的异步组件
  switch (question.value.type) {
    case 1:
      return SingleChoice;
    case 2:
      return MultipleChoice;
    case 3:
      return TrueFalse;
    case 4:
      return ShortAnswer;
    default:
      return null;
  }
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

// 标签显示控制
const maxVisibleTags = 3; // 默认最多显示3个标签
const showAllTags = ref(false);

// 计算属性：要显示的标签
const displayedTags = computed(() => {
  if (!question.value?.tags) return [];
  return showAllTags.value ? question.value.tags : question.value.tags.slice(0, maxVisibleTags);
});

// 计算属性：是否有更多标签
const hasMoreTags = computed(() => {
  return question.value?.tags && question.value.tags.length > maxVisibleTags;
});
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


.question-content,
.question-detail {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 24px;
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

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  display: flex;

  background-color: #f0f0f0;
  color: #666;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
}

.more-tags {
  color: #1890ff;
  cursor: pointer;
  font-size: 12px;
  padding: 2px 8px;
}

.submit-section {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>

