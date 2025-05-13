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
          <span class="tag" style="display: flex; align-items: center;gap: 8px">
            <el-icon><View/></el-icon>
            {{ question.viewCount }}
          </span>
        </div>

        <ActionButtons v-model="isFavorited"/>
      </div>
    </div>

    <!--    答题页面  -->
    <div class="question-content" v-if="questionComponent">
      <!--  答题页面  -->
      <component :is="questionComponent" :question="question"/>
    </div>
    
    <!-- 导航区域 -->
    <div class="question-navigation">
      <el-button 
        :loading="loadingNext"
        class="next-question-btn" 
        type="primary"
        @click="goToNextQuestion"
      >
        <el-icon class="next-icon"><ArrowRight /></el-icon>
        下一题
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, defineAsyncComponent, onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import axios from '@/axios/axios';
import {ElMessage} from 'element-plus';
import {useScrollbarStore} from '@/pinia/ScrollbarStore';

import {setTitle} from '@/utils/title';
import {QuestionInterface} from "@/view/HomePage/view/QuestionPage/interface/QuestionInterface";
import {ApiGetQuestionByVirtualId} from "@/view/HomePage/view/QuestionPage/service/ApiGetQuestionByVirtualId";
import ActionButtons
  from "@/view/HomePage/view/QuestionPage/components/container/QuestionDetail/components/ActionButtons.vue";
import {ArrowRight, View} from "@element-plus/icons-vue";

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
const router = useRouter();
const questionBank = useQuestionBankStore();

// 根据路由获取 题目信息
const question = ref<QuestionInterface>();
// 收藏状态
const isFavorited = ref(false);
// 加载下一题状态
const loadingNext = ref(false);

// 检查题目是否已收藏
const checkFavoriteStatus = async (virtualId: string) => {
  try {
    // 这里需要调用检查收藏状态的API
    const response = await axios.get('service-question/user/favQuestion/checkFav', {
      params: {
        virtualId
      },
    });

    if (response.data.status === 200) {
      isFavorited.value = response.data.data;
    }
  } catch (error) {
    console.error('检查收藏状态出错：', error);
  }
};

watch(() => route.params.questionId as string, async (newValue) => {
  if (newValue) {  // 确保newValue存在
    const result = await ApiGetQuestionByVirtualId(newValue);  // 使用newValue而不是questionId.value
    if (result.status === 200) {
      question.value = result.data;
      // 获取题目信息后检查收藏状态
      checkFavoriteStatus(newValue);
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

// 获取下一题并跳转
const goToNextQuestion = async () => {
  if (!question.value) return;
  
  const currentVid = route.params.questionId as string;
  if (!currentVid) return;
  
  try {
    loadingNext.value = true;
    // 调用获取下一题API
    const response = await axios.get(`service-question/user/study/nextQuestion/${currentVid}`);
    
    if (response.data.status === 200 && response.data.data) {
      const nextQuestionVid = response.data.data;
      // 先获取scrollbarStore
      const scrollbarStore = useScrollbarStore();
      
      // 使用路径导航
      router.push(`/question/${nextQuestionVid}`).then(() => {
        // 导航成功后等待DOM更新完成再滚动
        setTimeout(() => {
          console.log('Attempting to scroll to top, scrollbar instance:', scrollbarStore.scrollbarRef);
          scrollbarStore.scrollToTop();
          
          // 备用方案：如果store中的scrollbar引用失效，尝试直接使用DOM API
          if (!scrollbarStore.scrollbarRef) {
            const scrollWrap = document.querySelector('.el-scrollbar__wrap');
            if (scrollWrap) {
              scrollWrap.scrollTop = 0;
              console.log('Used DOM API to scroll to top');
            }
          }
        }, 100);
      });
    } else {
      // 处理没有下一题的情况
      console.warn('没有更多题目了');
      ElMessage.warning('已经是最后一题了');
    }
  } catch (error) {
    console.error('获取下一题失败:', error);
    ElMessage.error('获取下一题失败，请稍后重试');
  } finally {
    loadingNext.value = false;
  }
};
</script>


<style scoped>
.question-container {
  width: 100%;
  margin: 0 auto;

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
  margin-bottom: 12px;
}


.question-title {
  font-size: 24px;
  font-weight: 600;
  margin: 8px 0;
  line-height: 1;
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

.actions {
  display: flex;
  align-items: center;
  gap: 16px;
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
  border-radius: 12px;
  font-size: 12px;
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
  padding: 4px 12px;
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

/* 导航区域样式 */
.question-navigation {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 16px;
  display: flex;
  justify-content: flex-end;
}

.next-question-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
}

.next-icon {
  font-size: 14px;
}
</style>

