<template>
  <div class="answer-discussion">
    <!-- 头部区域 -->
    <div class="discussion-header">
      <h2 class="title">评论 ({{ totalCount }})</h2>
      <div class="header-actions">
        <el-dropdown trigger="click" @command="handleSortChange">
          <span class="sort-dropdown">
            {{ sortOptions[currentSort] }}
            <el-icon><arrow-down/></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-for="(label, key) in sortOptions"
                                :key="key"
                                :command="key">
                {{ label }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 评论发布表单 -->
    <div class="comment-form">
      <div class="form-content">
        <el-input
            v-model="commentContent"
            :maxlength="1000"
            :rows="3"
            placeholder="发一条友善的评论"
            resize="none"
            show-word-limit
            type="textarea"
        />
        <div class="form-footer">
          <!-- 已登录时显示的按钮 -->
          <el-button
              v-if="isLoggedIn"
              :disabled="!commentContent.trim()"
              type="primary"
              @click="submitComment"
          >
            发布
          </el-button>
          <!-- 未登录时显示的按钮 -->
          <el-button
              v-else
              type="primary"
              @click="showLoginTip"
          >
            登录后发布
          </el-button>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div v-if="comments.length" class="comments-container">
      <!-- 每个评论项及其回复 -->
      <div v-for="comment in comments" :key="comment.id" class="comment-block">
        <!-- 评论主体 -->
        <div :id="`comment-${comment.id}`" class="comment-item">
          <div class="comment-main">
            <el-avatar :size="40" :src="comment.userInfo.avatar" class="user-avatar">
              {{ comment.userInfo.nickname.charAt(0) }}
            </el-avatar>

            <div class="comment-content">
              <!-- 用户信息和时间 -->
              <div class="comment-header">
                <div class="user-info">
                  <router-link 
                    v-if="comment.isAuthor !== 1" 
                    :class="{'is-author': comment.isAuthor === 1}"
                    :to="`/userProfile/${comment.userInfo.username}`"
                    class="username username-link"
                  >
                    {{ comment.userInfo.nickname }}
                  </router-link>
                  <span 
                    v-else 
                    :class="{'is-author': comment.isAuthor === 1}" 
                    class="username"
                  >
                    我
                  </span>
                  <el-tag v-if="comment.isAuthor === 1" class="author-tag" effect="plain" size="small" type="primary">
                    作者
                  </el-tag>
                </div>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>

              <!-- 评论文本 -->
              <p class="comment-text">{{ comment.content }}</p>

              <!-- 操作栏 -->
              <div class="comment-actions">
                <div :class="{'is-active': comment.isLike === 1}" class="action-btn" @click="handleLike(comment.id)">
                  <el-icon>
                    <thumb-up-icon :active="comment.isLike === 1"/>
                  </el-icon>
                  <span>{{ comment.likeCount > 0 ? comment.likeCount : '' }}</span>
                </div>

                <div class="action-btn" @click="handleReply(comment.id)">
                  <el-icon>
                    <chat-line-round/>
                  </el-icon>
                  <span>回复</span>
                </div>

                <el-dropdown v-if="comment.isAuthor === 1" trigger="click">
                  <div class="action-btn">
                    <el-icon>
                      <more-filled/>
                    </el-icon>
                  </div>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="handleDeleteComment(comment.id)">删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>

              <!-- 回复表单 -->
              <div v-if="replyToId === comment.id && replyToSubId === null" class="reply-form">
                <el-input
                    v-model="replyContent"
                    :maxlength="500"
                    :placeholder="`回复 @${comment.userInfo.nickname}:`"
                    :rows="2"
                    show-word-limit
                    type="textarea"
                />
                <div class="form-actions">
                  <el-button text @click="cancelReply">取消</el-button>
                  <el-button
                      :disabled="!replyContent.trim()"
                      size="small"
                      type="primary"
                      @click="submitReply(comment.id, comment.userInfo.id)"
                  >
                    回复
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 评论的回复列表 -->
        <div v-if="comment.replies && comment.replies.length" class="replies-container">
          <!-- 显示的回复 -->
          <div v-for="reply in getAllReplies(comment)" :id="`comment-${reply.id}`" :key="reply.id" class="reply-item">
            <el-avatar :size="32" :src="reply.userInfo.avatar" class="user-avatar">
              {{ reply.userInfo.nickname.charAt(0) }}
            </el-avatar>

            <div class="reply-content">
              <div class="reply-header">
                <div class="user-info">
                  <router-link 
                    v-if="reply.isAuthor !== 1" 
                    :class="{'is-author': reply.isAuthor === 1}"
                    :to="`/userProfile/${reply.userInfo.username}`"
                    class="username username-link"
                  >
                    {{ reply.userInfo.nickname }}
                  </router-link>
                  <span 
                    v-else 
                    :class="{'is-author': reply.isAuthor === 1}" 
                    class="username"
                  >
                    我
                  </span>
                  <el-tag v-if="reply.isAuthor === 1" class="author-tag" effect="plain" size="small" type="primary">
                    作者
                  </el-tag>
                </div>
                <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
              </div>

              <!-- 回复对象 -->
              <p class="reply-reference">
                回复
                <router-link 
                  v-if="reply.replyToUser && reply.replyToUser.isAuthor !== 1"
                  :class="{'is-author': reply.replyToUser?.isAuthor === 1}"
                  :to="`/userProfile/${getReplyToUsername(reply, comment)}`"
                  class="reply-target reply-target-link"
                >
                  @{{ reply.replyToUser.nickname }}
                </router-link>
                <span
                  v-else
                  :class="{'is-author': reply.replyToUser?.isAuthor === 1}"
                  class="reply-target"
                >
                  @{{ reply.replyToUser ? reply.replyToUser.nickname : getCommentOwnerName(comment) }}
                </span>
                {{ getReplyChain(reply, comment) }}
              </p>

              <!-- 回复内容 -->
              <p class="reply-text">{{ reply.content }}</p>

              <!-- 回复操作栏 -->
              <div class="reply-actions">
                <div :class="{'is-active': reply.isLike === 1}" class="action-btn" @click="handleLike(reply.id)">
                  <el-icon>
                    <thumb-up-icon :active="reply.isLike === 1"/>
                  </el-icon>
                  <span>{{ reply.likeCount > 0 ? reply.likeCount : '' }}</span>
                </div>

                <div class="action-btn" @click="handleReply(comment.id, reply.id, reply.userInfo.id)">
                  <el-icon>
                    <chat-line-round/>
                  </el-icon>
                  <span>回复</span>
                </div>

                <el-dropdown v-if="reply.isAuthor === 1">
                  <div class="action-btn">
                    <el-icon>
                      <more-filled/>
                    </el-icon>
                  </div>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="handleDeleteComment(reply.id)">删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>

              <!-- 嵌套回复表单 -->
              <div v-if="replyToId === comment.id && replyToSubId === reply.id" class="reply-form">
                <el-input
                    v-model="replyContent"
                    :maxlength="500"
                    :placeholder="`回复 @${reply.userInfo.nickname}:`"
                    :rows="2"
                    show-word-limit
                    type="textarea"
                />
                <div class="form-actions">
                  <el-button text @click="cancelReply">取消</el-button>
                  <el-button
                      :disabled="!replyContent.trim()"
                      size="small"
                      type="primary"
                      @click="submitReply(comment.id, reply.userInfo.id)"
                  >
                    回复
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 加载更多回复 -->
          <div v-if="commentHasMoreReplies(comment)" class="load-more-replies">
            <el-button link type="primary" @click="toggleRepliesExpand(comment.id)">
              {{
                expandedComments.includes(comment.id) ? '收起回复' : `查看更多回复(${comment.replies.length - initialRepliesShown})`
              }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else-if="!loading" class="empty-comments">
      <el-empty description="暂无评论，快来发表你的观点吧～"/>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-loading :fullscreen="false" text="加载中..."/>
    </div>

    <!-- 分页组件 -->
    <div v-if="comments.length > 0 && totalPages > 1" class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        :hide-on-single-page="false"
        :page-size="pageSize"
        :total="totalCount"
        background
        layout="prev, pager, next, jumper, total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, defineComponent, h, onMounted, ref, watch} from 'vue';
import {ArrowDown, ChatLineRound, MoreFilled} from '@element-plus/icons-vue';
import type {CommentType, ReplyType, SortType} from './types';
import {commentService} from './commentService';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage, ElMessageBox} from 'element-plus';
import {useSessionStore} from "@/pinia/Session";

// 自定义点赞图标组件
const ThumbUpIcon = defineComponent({
  name: 'ThumbUpIcon',
  props: {
    active: {
      type: Boolean,
      default: false
    }
  },
  setup(props) {
    return () => h('svg',
        {
          viewBox: '0 0 24 24',
          width: '1em',
          height: '1em'
        },
        [
          h('path', {
            fill: 'currentColor',
            d: 'M9 21h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-2c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.58 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2zM9 9l4.34-4.34L12 10h9v2l-3 7H9V9zM1 9h4v12H1z'
          })
        ]
    );
  }
});

// 路由
const route = useRoute();
const router = useRouter();

// 用户登录状态
const userSession = useSessionStore();

// 判断用户是否登录
const isLoggedIn = computed(() => {
  return userSession.userSession && userSession.userSession.tokenValue;
});

// 显示登录提示
const showLoginTip = () => {
  ElMessageBox.confirm('登录后才能发表评论，是否前往登录？', '提示', {
    confirmButtonText: '去登录',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    handleLogin();
  }).catch(() => {
    // 用户取消登录
  });
};

// 处理登录跳转
const handleLogin = () => {
  router.push({
    path: '/sign/email',
    query: {
      redirect: route.fullPath
    }
  });
};

// 状态管理
const comments = ref<CommentType[]>([]);
const loading = ref<boolean>(false);
const totalCount = ref<number>(0);
const currentSort = ref<SortType>('newest');
const currentPage = ref<number>(1);
const pageSize = ref<number>(10);
const initialRepliesShown = 3; // 初始展示的回复数量

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(totalCount.value / pageSize.value);
});

// 排序选项
const sortOptions = {
  newest: '最新',
  oldest: '最早',
  popular: '最热门'
};

// 评论输入内容
const commentContent = ref<string>('');

// 回复相关状态
const replyToId = ref<number | null>(null);     // 当前回复的评论ID
const replyToSubId = ref<number | null>(null);  // 当前回复的子回复ID
const replyToUserId = ref<number | null>(null); // 被回复用户ID
const replyContent = ref<string>('');           // 回复内容
const expandedComments = ref<number[]>([]);     // 已展开所有回复的评论ID列表

// 防抖函数
const debounce = (fn: Function, delay: number) => {
  let timer: number | null = null;
  return function (...args: any[]) {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      fn.apply(this, args);
      timer = null;
    }, delay);
  };
};

// 点赞防抖Map，用于存储每个评论/回复的防抖状态
const likeDebounceMap = new Map<number, boolean>();

// 获取评论的所有回复（考虑是否展开）
const getAllReplies = (comment: CommentType): ReplyType[] => {
  if (!comment.replies || comment.replies.length === 0) return [];

  // 如果已展开所有回复，显示全部
  if (expandedComments.value.includes(comment.id)) {
    return comment.replies;
  }

  // 否则只显示初始数量
  return comment.replies.slice(0, initialRepliesShown);
};

// 判断评论是否有更多回复可以加载
const commentHasMoreReplies = (comment: CommentType): boolean => {
  return comment.replies && comment.replies.length > initialRepliesShown;
};

// 获取回复链文本（显示回复层级关系）
const getReplyChain = (reply: ReplyType, parentComment: CommentType): string => {
  if (!reply.replyToUser) return '';

  // 构建回复链
  return '';
};

// 获取评论所有者的名称
const getCommentOwnerName = (comment: CommentType): string => {
  return comment.isAuthor === 1 ? '我' : comment.userInfo.nickname;
};

// 获取被回复用户的username
const getReplyToUsername = (reply: ReplyType, parentComment: CommentType): string => {
  if (reply.replyToUser) {
    // 如果有replyToUser信息，需要从评论或回复中找到对应的username
    // 先在父评论中查找
    if (parentComment.userInfo.id === reply.replyToUser.id) {
      return parentComment.userInfo.username;
    }
    // 再在回复列表中查找
    const targetReply = parentComment.replies?.find(r => r.userInfo.id === reply.replyToUser!.id);
    if (targetReply) {
      return targetReply.userInfo.username;
    }
  }
  // 如果找不到，返回父评论的username
  return parentComment.userInfo.username;
};

// 获取问题ID
const getQuestionId = (): string => {
  return route.params.questionId as string;
};

// 处理回复操作
const handleReply = (commentId: number, replyId?: number, userId?: number): void => {
  // 检查用户是否登录
  if (!isLoggedIn.value) {
    ElMessage.warning('登录后才能回复评论');
    return;
  }

  toggleReplyForm(commentId, replyId, userId);
};

// 切换回复表单显示状态
const toggleReplyForm = (commentId: number, replyId?: number, userId?: number): void => {
  if (replyToId.value === commentId && replyToSubId.value === replyId) {
    // 如果点击的是当前已打开的回复表单，则关闭它
    cancelReply();
  } else {
    // 否则打开新的回复表单
    replyToId.value = commentId;
    replyToSubId.value = replyId || null;
    replyToUserId.value = userId || null;
    replyContent.value = '';
  }
};

// 取消回复
const cancelReply = (): void => {
  replyToId.value = null;
  replyToSubId.value = null;
  replyToUserId.value = null;
  replyContent.value = '';
};

// 提交评论
const submitComment = async (): Promise<void> => {
  if (!commentContent.value.trim()) return;

  // 检查用户是否登录
  if (!isLoggedIn.value) {
    ElMessage.warning('登录后才能发表评论');
    return;
  }

  const questionId = getQuestionId();
  loading.value = true;

  try {
    const success = await commentService.addComment({
      targetVirtualId: questionId,
      content: commentContent.value
    });

    if (success) {
      commentContent.value = '';
      await loadComments(true);
      ElMessage.success('评论发布成功');
    } else {
      ElMessage.error('评论发布失败，请稍后重试');
    }
  } catch (error) {
    console.error('添加评论失败', error);
    ElMessage.error('评论发布失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 提交回复
const submitReply = async (commentId: number, userId: number): Promise<void> => {
  if (!replyContent.value.trim()) return;

  // 检查用户是否登录
  if (!isLoggedIn.value) {
    ElMessage.warning('登录后才能回复评论');
    return;
  }

  const questionId = getQuestionId();
  loading.value = true;

  try {
    const success = await commentService.addComment({
      targetVirtualId: questionId,
      content: replyContent.value,
      parentId: commentId,
      replyToUserId: userId
    });

    if (success) {
      cancelReply();
      await loadComments(true);
      ElMessage.success('回复发布成功');
    } else {
      ElMessage.error('回复发布失败，请稍后重试');
    }
  } catch (error) {
    console.error('回复评论失败', error);
    ElMessage.error('回复发布失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 时间格式化
const formatTime = (timeStr: string): string => {
  const date = new Date(timeStr);
  const now = new Date();
  const diff = now.getTime() - date.getTime();

  // 小于1分钟
  if (diff < 60 * 1000) {
    return '刚刚';
  }
  // 小于1小时
  else if (diff < 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 1000))}分钟前`;
  }
  // 小于24小时
  else if (diff < 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 60 * 1000))}小时前`;
  }
  // 小于7天
  else if (diff < 7 * 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`;
  }
  // 其他情况显示完整日期
  else {
    return date.toLocaleDateString();
  }
};

// 切换回复展开状态
const toggleRepliesExpand = (commentId: number): void => {
  const index = expandedComments.value.indexOf(commentId);
  if (index === -1) {
    // 添加到展开列表
    expandedComments.value.push(commentId);
  } else {
    // 从展开列表中移除
    expandedComments.value.splice(index, 1);
  }
};

// 将平面回复列表转换为嵌套树结构
const buildCommentTree = (commentList: any[]): CommentType[] => {
  // 找出所有顶级评论（parentId为null的评论）
  const rootComments = commentList.filter(comment => comment.parentId === null);

  // 找出所有回复（parentId不为null的评论）
  const repliesComments = commentList.filter(comment => comment.parentId !== null);

  // 为每个顶级评论添加replies数组
  rootComments.forEach(rootComment => {
    // 确保replies属性初始化
    rootComment.replies = [];

    // 添加属于当前顶级评论的回复
    rootComment.replies = repliesComments.filter(reply =>
        reply.parentId === rootComment.id
    );
  });

  return rootComments;
};

// 加载评论列表
const loadComments = async (refresh: boolean = false): Promise<void> => {
  const questionId = getQuestionId();

  if (!questionId) return;

  if (refresh) {
    currentPage.value = 1;
  }

  loading.value = true;

  try {
    const response = await commentService.getComments({
      targetVirtualId: questionId,
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      sort: currentSort.value
    });

    // 将评论列表转换为树形结构
    const commentTree = buildCommentTree(response.comments);

    if (refresh) {
      comments.value = commentTree;
    } else {
      // 对于分页，直接替换评论列表，不追加
      comments.value = commentTree;
    }

    // 更新总数
    totalCount.value = response.total;
  } catch (error) {
    console.error('加载评论失败', error);
    ElMessage.error('加载评论失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 处理排序变更
const handleSortChange = (sortType: SortType): void => {
  currentSort.value = sortType;
  loadComments(true);
};

// 处理分页变更
const handlePageChange = (page: number): void => {
  currentPage.value = page;
  loadComments(false); // 不要重置页码，使用false
  // 滚动到评论区域顶部
  const discussionElement = document.querySelector('.answer-discussion');
  if (discussionElement) {
    discussionElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
};

// 处理点赞
const handleLikeWithoutDebounce = async (id: number): Promise<void> => {
  // 检查是否正在处理中
  if (likeDebounceMap.get(id)) {
    return;
  }

  // 检查用户是否登录
  if (!isLoggedIn.value) {
    ElMessage.warning('登录后才能点赞');
    return;
  }

  // 标记为处理中
  likeDebounceMap.set(id, true);

  // 查找对应的评论或回复
  let target: any = null;
  let isReply = false;

  // 查找在评论中
  for (const comment of comments.value) {
    if (comment.id === id) {
      target = comment;
      break;
    }

    // 查找在回复中
    if (comment.replies) {
      for (const reply of comment.replies) {
        if (reply.id === id) {
          target = reply;
          isReply = true;
          break;
        }
      }
      if (target) break;
    }
  }

  if (!target) {
    likeDebounceMap.set(id, false);
    return;
  }

  // 更新本地状态
  if (target.isLike === 1) {
    // 已点赞，执行取消点赞
    target.isLike = 0;
    target.likeCount = Math.max(0, target.likeCount - 1);

    try {
      await commentService.unlikeComment(id);
    } catch (error) {
      console.error('取消点赞失败', error);
      ElMessage.error('取消点赞失败');

      // 失败时恢复状态
      target.isLike = 1;
      target.likeCount += 1;
    }
  } else {
    // 未点赞，执行点赞
    target.isLike = 1;
    target.likeCount += 1;

    try {
      await commentService.likeComment(id);
    } catch (error) {
      console.error('点赞失败', error);
      ElMessage.error('点赞失败');

      // 失败时恢复状态
      target.isLike = 0;
      target.likeCount = Math.max(0, target.likeCount - 1);
    }
  }

  // 处理完成，移除标记
  likeDebounceMap.set(id, false);
};

// 使用防抖包装点赞函数
const handleLike = debounce(handleLikeWithoutDebounce, 300);

// 处理删除评论
const handleDeleteComment = async (commentId: number): Promise<void> => {
  try {
    // 弹出确认框
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    // 执行删除操作
    const success = await commentService.deleteComment(commentId);

    if (success) {
      // 刷新评论列表
      await loadComments(true);
      ElMessage.success('删除成功');
    } else {
      ElMessage.error('删除失败，请稍后重试');
    }
  } catch (error) {
    // 用户取消操作或发生其他错误
    if (error !== 'cancel') {
      console.error('删除评论失败', error);
      ElMessage.error('删除失败，请稍后重试');
    }
  }
};

// 组件挂载时加载评论
onMounted(() => {
  loadComments();
});

// 监听排序变化
watch(currentSort, (newValue) => {
  if (newValue) {
    loadComments(true);
  }
});
</script>

<style scoped>
.answer-discussion {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin: 12px 0;
  overflow: hidden;
  border: 1px solid #ebeef5;
  position: relative;
}

/* 头部样式 */
.discussion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #f5f7fa;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.sort-dropdown {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.sort-dropdown:hover {
  background-color: #ecf5ff;
}

/* 评论表单样式 */
.comment-form {
  padding: 16px;
  display: flex;
  gap: 12px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fafafa;
}

.form-content {
  flex: 1;
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

/* 评论列表容器 - 移除滚动条 */
.comments-container {
  padding: 0;
}

/* 评论块 - 包含评论和它的所有回复 */
.comment-block {
  border-bottom: 1px solid #f0f0f0;
}

.comment-block:last-child {
  border-bottom: none;
}

/* 单个评论项 */
.comment-item {
  padding: 16px;
  scroll-margin-top: 60px;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #6d757a; /* B站用户名默认颜色 */
}

.username.is-author {
  color: #23ade5; /* B站作者颜色 */
}

.username-link {
  text-decoration: none;
  transition: color 0.3s ease;
}

.username-link:hover {
  color: #409eff;
  text-decoration: underline;
}

.reply-target-link {
  text-decoration: none;
  transition: color 0.3s ease;
}

.reply-target-link:hover {
  color: #409eff;
  text-decoration: underline;
}

.author-tag {
  font-size: 12px;
  height: 18px;
  line-height: 16px;
  padding: 0 4px;
}

.comment-time {
  font-size: 12px;
  color: #9499a0; /* B站时间颜色 */
}

.comment-text {
  font-size: 14px;
  line-height: 1.6;
  margin: 8px 0;
  color: #18191c; /* B站评论文本颜色 */
  white-space: pre-wrap;
  word-break: break-word;
}

.comment-actions {
  display: flex;
  gap: 16px;
  margin-top: 8px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #9499a0; /* B站按钮默认颜色 */
  cursor: pointer;
  padding: 4px 6px;
  border-radius: 4px;
  transition: background-color 0.15s;
}

.action-btn:hover {
  background-color: #f4f4f4;
  color: #6d757a;
}

.action-btn.is-active {
  color: #00aeec; /* B站点赞激活颜色 */
}

/* 回复容器 */
.replies-container {
  padding: 0 16px 12px 56px; /* 增加左侧内边距对齐 */
  background-color: #f7f8fa;
  margin-top: -8px;
}

/* 回复项 */
.reply-item {
  display: flex;
  gap: 8px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.reply-time {
  font-size: 12px;
  color: #9499a0;
}

.reply-reference {
  font-size: 13px;
  color: #9499a0;
  margin: 4px 0;
}

.reply-target {
  color: #00aeec;
  font-weight: 500;
  margin: 0 2px;
}

.reply-target.is-author {
  color: #23ade5;
}

.reply-text {
  font-size: 14px;
  line-height: 1.5;
  margin: 6px 0;
  color: #18191c;
  white-space: pre-wrap;
  word-break: break-word;
}

.reply-actions {
  display: flex;
  gap: 12px;
  margin-top: 6px;
}

/* 回复表单 */
.reply-form {
  margin-top: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 12px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
  gap: 8px;
}

.load-more-replies {
  padding: 8px 0;
  display: flex;
  justify-content: center;
  border-top: 1px solid #f0f0f0;
  margin-top: 4px;
}

/* 空状态和加载状态 */
.empty-comments {
  padding: 32px 0;
  text-align: center;
}

.loading-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding: 16px 0;
  border-top: 1px solid #ebeef5;
}

.pagination-container .el-pagination {
  --el-pagination-font-size: 14px;
  --el-pagination-bg-color: #f5f7fa;
  --el-pagination-text-color: #606266;
  --el-pagination-border-radius: 4px;
}
</style>