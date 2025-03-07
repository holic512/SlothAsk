<template>
  <div class="question-container">
    <div class="question-detail">
      <div class="question-header">
        <div class="title-section">
          <span class="question-id">#{{ question?.id }}</span>
          <h1 class="question-title">{{ question?.title }}</h1>
        </div>
        <div class="question-meta">
          <div class="meta-left">
            <span class="difficulty" :class="question?.difficulty">
              {{ question?.difficulty }}
            </span>
            <span class="type-tag" :class="question?.type">
              {{ question?.type }}
            </span>
            <div class="tags">
              <span v-for="tag in question?.tags" 
                :key="tag" 
                class="tag"
              >
                {{ tag }}
              </span>
            </div>
          </div>
          <div class="meta-right">
            <div class="stat-item">
              <el-icon><ChatLineRound /></el-icon>
              <span>{{ question?.comments || 0 }} 评论</span>
            </div>
            <div class="stat-item">
              <el-icon><Position /></el-icon>
              <span>{{ question?.passRate }}% 通过率</span>
            </div>
          </div>
        </div>
      </div>

      <div class="question-content">
        <component 
          :is="questionComponent" 
          v-if="question"
          :question="question"
        />
      </div>
    </div>

    <div class="question-answer">
      <div class="section-header">
        <h2 class="section-title">答案解析</h2>
        <el-button type="primary" plain>查看答案</el-button>
      </div>
      <div class="answer-content">
        <div class="answer-text">
          这里是答案解析内容...
        </div>
      </div>
    </div>

    <div class="question-comments">
      <div class="section-header">
        <h2 class="section-title">评论区 ({{ question?.comments || 0 }})</h2>
        <el-button type="primary" @click="showCommentInput = true">
          写评论
          <el-icon class="el-icon--right"><ChatLineRound /></el-icon>
        </el-button>
      </div>

      <div v-if="showCommentInput" class="comment-input">
        <el-input
          v-model="commentText"
          type="textarea"
          :rows="3"
          placeholder="写下你的评论..."
          resize="none"
        />
        <div class="comment-actions">
          <el-button @click="showCommentInput = false">取消</el-button>
          <el-button type="primary" @click="submitComment">发布评论</el-button>
        </div>
      </div>

      <div class="comments-list">
        <div class="comment-item" v-for="i in 3" :key="i">
          <div class="comment-user">
            <el-avatar :size="32">用户</el-avatar>
            <div class="user-info">
              <span class="username">用户名称</span>
              <span class="comment-time">2小时前</span>
            </div>
          </div>
          <div class="comment-content">
            这是一条评论内容...
          </div>
          <div class="comment-actions">
            <span class="action-item">
              <el-icon><StarFilled /></el-icon>
              <span>12</span>
            </span>
            <span class="action-item">
              <el-icon><ChatLineRound /></el-icon>
              <span>回复</span>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useQuestionBankStore } from '../../pinia/QuestionBank';
import { 
  ChatLineRound, 
  View, 
  Position,
  StarFilled
} from '@element-plus/icons-vue';
import SingleChoice from './SingleChoice.vue';
import MultipleChoice from './MultipleChoice.vue';
import TrueFalse from './TrueFalse.vue';
import ShortAnswer from './ShortAnswer.vue';
import { setTitle } from '../../../../../utils/title';

const route = useRoute();
const questionBank = useQuestionBankStore();
const showCommentInput = ref(false);
const commentText = ref('');

const questionId = computed(() => Number(route.params.questionId));

const question = computed(() => {
  return questionBank.getAllQuestions.find(q => q.id === questionId.value);
});

// 根据问题类型返回对应的组件
const questionComponent = computed(() => {
  if (!question.value) return null;
  
  const componentMap = {
    '单选': SingleChoice,
    '多选': MultipleChoice,
    '判断': TrueFalse,
    '简答': ShortAnswer
  };
  
  return componentMap[question.value.type] || null;
});

const submitComment = () => {
  // 处理评论提交
  console.log('提交评论:', commentText.value);
  commentText.value = '';
  showCommentInput.value = false;
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
</script>

<style scoped>
.question-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.question-detail, .question-answer, .question-comments {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 32px;
}

.title-section {
  margin-bottom: 16px;
}

.question-id {
  font-size: 16px;
  color: #999;
  font-family: Monaco, monospace;
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
  border-bottom: 1px solid #f0f0f0;
}

.meta-left, .meta-right {
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

.question-content {
  margin-top: 24px;
}

.comment-item {
  padding: 20px;
  border-radius: 12px;
  background: #f9f9f9;
  transition: background-color 0.2s;
}

.comment-item:hover {
  background: #f5f5f5;
}

.comment-user {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: 500;
  color: #1a1a1a;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  color: #333;
  line-height: 1.6;
  margin-left: 44px;
  margin-bottom: 12px;
}

.comment-actions {
  display: flex;
  gap: 16px;
  margin-left: 44px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 13px;
  cursor: pointer;
  transition: color 0.2s;
}

.action-item:hover {
  color: #1890ff;
}

.difficulty, .type-tag {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 14px;
}

.difficulty.简单 {
  color: #52c41a;
  background: #f6ffed;
}

.difficulty.中等 {
  color: #faad14;
  background: #fff7e6;
}

.difficulty.困难 {
  color: #ff4d4f;
  background: #fff1f0;
}

.type-tag.单选 {
  color: #1890ff;
  background: #e6f7ff;
}

.type-tag.多选 {
  color: #722ed1;
  background: #f9f0ff;
}

.type-tag.判断 {
  color: #52c41a;
  background: #f6ffed;
}

.type-tag.简答 {
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

.content-text, .answer-text {
  font-size: 15px;
  line-height: 1.8;
  color: #333;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.comment-input {
  margin-bottom: 24px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 12px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  padding: 16px;
  border-radius: 8px;
  background: #f9f9f9;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.username {
  font-weight: 500;
  color: #333;
}

.comment-time {
  color: #999;
  font-size: 13px;
}

.comment-content {
  color: #333;
  line-height: 1.6;
  margin-left: 44px;
}

.question-type {
  margin-top: 24px;
  padding: 24px;
  background: #f9f9f9;
  border-radius: 8px;
}
</style>
