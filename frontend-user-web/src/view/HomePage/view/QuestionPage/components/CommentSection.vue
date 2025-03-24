<template>
  <div class="question-comments">
    <div class="comment-container">
      <div class="section-header">
        <h2 class="section-title">回答讨论 ({{ comments.length }})</h2>
      </div>

      <div class="comment-input">
        <el-avatar :size="40">用户</el-avatar>
        <el-input class="commentText" v-model="commentText" type="textarea" :rows="3" placeholder="写下你的评论..."
                  resize="none" />
        <div class="comment-header-actions">
          <el-button class="comment-button" @click="submitComment">发布评论</el-button>
        </div>
      </div>
      <div class="comments-list">
        <div class="comment-item" v-for="comment in comments" :key="comment.id">
          <div class="comment-user">
            <el-avatar :size="48">{{ comment.username.charAt(0) }}</el-avatar>

            <div class="comment-right">
              <span class="username">{{ comment.username }}</span>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-actions">
                <div class="comment-time">{{ comment.time }}</div>
                <span class="action-item">
                  <el-button text icon="star" class="action-button">
                    {{ comment.star }}
                  </el-button>
                </span>
                <span class="action-item">
                  <el-button text icon="chatlineround" class="action-button">
                    回复
                  </el-button>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">

import { computed, ref } from "vue";
import { useRoute } from 'vue-router';
import { ElMessage } from "element-plus";
import { useQuestionBankStore } from '@/view/HomePage/view/StudyPage/store/QuestionBank';
interface Comment {
  id: number;
  username: string;
  content: string;
  time: string;
  star: number;
}
const comments = ref<Comment[]>([]); // 存储评论列表
const commentText = ref(''); // 绑定输入框内容
const route = useRoute();


const submitComment = () => {
  if (!commentText.value.trim()) {
    ElMessage.warning('评论内容不能为空');
    return;
  }

  const newComment: Comment = {
    id: Date.now(), // 使用时间戳作为唯一 ID
    username: '当前用户', // 可以根据实际需求替换为真实用户名
    content: commentText.value,
    time: new Date().toLocaleString(), // 格式化时间
    star: 0
  };

  comments.value.unshift(newComment); // 将新评论添加到列表顶部
  commentText.value = ''; // 清空输入框

  ElMessage.success('评论发布成功');
};
</script>

<style scoped>
.question-comments {
  width: 100%;
  padding: 12px 16px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}

.comment-container {
  padding: 12px 16px;
  height: auto;
  display: flex;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.section-header {
  font-size: 14px;
  font-weight: 200;
  color: #1a1a1a;
}

.comment-button {
  border-radius: 4px;
  background-color: #1677ff;
  color: white;
  margin: 5px;
}

.comment-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
  background: #fff;
  border-radius: 4px;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.comment-user .el-avatar {
  flex-shrink: 0;
  width: 48px !important;
  height: 48px !important;
  border-radius: 50% !important;
}

.comment-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.username {
  font-weight: bold;
  color: #262626;
}

.comment-time {
  font-size: 16px;
  color: #8c8c8c;
}


.comment-header-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin: 15px;
}

.comment-actions {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-top: 8px;
}

.comment-container {
  padding: 32px;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px 8px 0 0;
}

.comment-input {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 8px;
}

.comment-input .el-avatar {
  flex-shrink: 0;
  width: 40px !important;
  height: 40px !important;
  border-radius: 50% !important;
}

.comment-actions {
  display: flex;
  justify-content: flex-start;
  gap: 12px;
  margin-top: 2px;
}

.comments-list {
  padding: 0 5px;
  display: flex;
  flex-direction: column;
}

.comment-item {
  padding: 16px;
  border-radius: 8px;
  background: #fff;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.username {
  font-weight: bold;
  color: #333;
}

.comment-time {
  color: #999;
  font-size: 16px;
}

.comment-content {
  color: #333;
  line-height: 1.6;
}
</style>