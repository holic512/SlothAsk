<template>
  <div class="comment-container">
    <div class="comment-section">
      <!-- 发布新评论 -->
      <div class="comment-form">
        <el-input
            v-model="newComment"
            type="textarea"
            placeholder="写下你的评论..."
            :rows="3"
            clearable
        />
        <div class="form-action">
          <el-button type="primary" size="small" @click="submitComment">发表评论</el-button>
        </div>
      </div>

      <!-- 评论列表 -->
      <div v-if="comments.length" class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-main">
            <el-avatar :src="comment.avatar" size="small" />
            <div class="comment-body">
              <div class="nickname">{{ comment.nickname }}</div>
              <div class="content">{{ comment.content }}</div>
              <div class="meta">
                {{ formatDate(comment.createTime) }}
                <el-button text size="small" @click="toggleReply(comment.id)">
                  {{ replyId === comment.id ? '取消' : '回复' }}
                </el-button>
                <!-- 点赞按钮 -->
                <el-button
                    size="small"
                    @click="toggleLike(comment.id)"
                    class="like-button">
                  <img
                      :src="likeIcon"
                      alt="点赞"
                      style="width: 14px; height: 14px;"
                  />
                  {{ comment.likeCount }}
                </el-button>
              </div>


              <!-- 回复框 -->
              <div v-if="replyId === comment.id" class="reply-form">
                <el-input
                    v-model="replyContent"
                    type="textarea"
                    placeholder="写下你的回复..."
                    :rows="2"
                    clearable
                />
                <div class="form-action">
                  <el-button size="small" type="primary" @click="submitReply(comment.id)">回复</el-button>
                </div>
              </div>

              <!-- 子评论 -->
              <div v-if="comment.replies?.length" class="sub-comment-list">
                <div v-for="reply in comment.replies" :key="reply.id" class="sub-comment-item">
                  <el-avatar :src="reply.avatar" size="small" />
                  <div class="sub-comment-body">
                    <div class="nickname">{{ reply.nickname }}</div>
                    <div class="content">{{ reply.content }}</div>
                    <div class="meta">{{ formatDate(reply.createTime) }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="empty-text">暂无评论</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useSessionStore } from '@/pinia/Session'
import {ApiPostCommentLike} from "@/view/HomePage/view/QuestionPage/service/ApiPostCommentLike";
import { ApiPostQuestionComment} from "../../service/ApiPostQuestionComment";
import { ApiGetQuestionCommentByVirtualId } from '@/view/HomePage/view/QuestionPage/service/ApiGetQuestionCommentByVirtualId'
import { CommentInterface } from '@/view/HomePage/view/QuestionPage/interface/CommentInterface'
import likeIcon from '@/view/HomePage/view/QuestionPage/image/like.png';

// 获取路由参数
const route = useRoute()
const questionId = route.params.questionId as string

// 获取当前登录用户信息
const userSession = useSessionStore()

// 评论列表
const comments = ref<CommentInterface[]>([])

// 发布评论和回复相关
const newComment = ref<string>('')
const replyContent = ref<string>('')
const replyId = ref<number | null>(null)

// 获取评论数据
const fetchComments = async () => {
  const res = await ApiGetQuestionCommentByVirtualId(questionId)
  if (res.status === 200) {
    console.log('获取评论数据成功', res.data)
    comments.value = res.data.map(comment => ({
      ...comment,
      like: false,
      replies: comment.replies?.map(reply => ({
        ...reply,
        like: false
      })) || []
    }))
  }
}

// 监听路由参数变化，重新获取评论数据
watch(() => route.params.questionId as string, async (newValue) => {
  if (newValue) {  // 确保newValue存在
    const result = await ApiGetQuestionCommentByVirtualId(newValue);
    if (result.status === 200) {
      comments.value = result.data;
    }
  }
}, {immediate: true});

//发布评论
const submitComment = async () => {

  if (!userSession.userSession.userId) {
    ElMessage.warning('请先登录！');
    return;
  }
  // 判断评论内容是否为空
  if (!newComment.value.trim()) {
    ElMessage.warning('评论不能为空');
    return;
  }

  const res = await ApiPostQuestionComment({
    questionId: questionId,
    userId:userSession.userSession.userId , // 当前登录用户
    content: newComment.value
  });

  if (res.status === 200) {
    ElMessage.success("评论成功！");
    newComment.value = '';
    await fetchComments();
  }
};

// 展开/关闭回复框
const toggleReply = (id: number) => {
  replyId.value = replyId.value === id ? null : id
  replyContent.value = ''
}

// 提交回复
const submitReply = async (parentId: number) => {

  if (!userSession.userSession.userId) {
    ElMessage.warning('请先登录！');
    return;
  }

  if (!replyContent.value.trim()){
    ElMessage.warning('评论不能为空');
    return;
  }


  const res = await ApiPostQuestionComment({
    questionId: questionId,
    userId: userSession.userSession.userId,
    content: replyContent.value,
    parentId: parentId
  });

  if (res.status === 200) {
    ElMessage.success("回复成功！");
    replyContent.value = '';
    replyId.value = null;
    await fetchComments();
  }
};


// 点赞功能
const toggleLike = async (id: number, isReply: boolean = false) => {


  // 找到对应的评论或回复
  const target = isReply
      ? comments.value.flatMap(comment => comment.replies || []).find(reply => reply.id === id)
      : comments.value.find(comment => comment.id === id);

  if (!target) return;

  try {
    await ApiPostCommentLike(target.id);
    target.likeCount += 1;
    ElMessage.success("点赞成功");
  } catch (error) {
    ElMessage.error("点赞失败");
    console.error("点赞失败：", error);
  }
};


// 时间格式化工具函数
const formatDate = (dateStr: string): string => {
  const date = new Date(dateStr)
  return date.toLocaleString()
}
</script>



<style scoped>

.comment-container{
  width: 100%;
  margin: 0 auto;
  padding: 6px 18px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.comment-section {
  padding:16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}
.comment-form {
  margin-bottom: 20px;
}
.form-action {
  margin-top: 8px;
  text-align: right;
}
.comment-list {
  border-top: 1px solid #eee;
}
.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}
.comment-main {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}
.comment-body {
  flex: 1;
}
.nickname {
  font-weight: bold;
  font-size: 14px;
}
.content {
  margin: 6px 0;
  font-size: 14px;
  color: #333;
  white-space: pre-wrap;
}
.meta {
  font-size: 12px;
  color: #999;
}
.reply-form {
  margin-top: 8px;
}
.sub-comment-list {
  margin-top: 10px;
  padding-left: 20px;
  border-left: 2px solid #f0f0f0;
}
.sub-comment-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-top: 10px;
}
.sub-comment-body {
  flex: 1;
}
.empty-text {
  text-align: center;
  color: #999;
  font-size: 14px;
  padding: 24px 0;
}

.like-button{
  border: none;
}
</style>
