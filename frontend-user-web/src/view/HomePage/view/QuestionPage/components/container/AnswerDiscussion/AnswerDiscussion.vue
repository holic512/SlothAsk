<template>
  <div class="answer-discussion">
    <discussion-header :count="comments.length" @sort-change="handleSortChange"/>

    <comment-form @submit="addComment"/>

    <comment-list
        v-if="comments.length"
        :comments="comments"
        @reply="handleReply"
        @like="handleLike"
    />

    <div v-else class="empty-comments">
      <el-empty description="暂无讨论"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, shallowRef, watch} from 'vue';
import DiscussionHeader from './components/DiscussionHeader.vue';
import CommentForm from './components/CommentForm.vue';
import CommentList from './components/CommentList.vue';
import type {CommentType, ReplyType, SortType} from './types';

// 使用 shallowRef 提高性能
const comments = shallowRef<CommentType[]>([
  {
    id: 1,
    userId: 101,
    nickname: '张三',
    avatar: 'https://placekitten.com/100/100',
    content: '这个问题解释得很详细，学到了很多！',
    createTime: '2023-10-15T10:30:00',
    likeCount: 5,
    isLiked: false,
    replies: [
      {
        id: 101,
        userId: 102,
        nickname: '李四',
        avatar: 'https://placekitten.com/101/101',
        content: '我也觉得讲解很透彻',
        createTime: '2023-10-15T11:20:00',
        parentId: 1
      }
    ]
  },
  {
    id: 2,
    userId: 103,
    nickname: '王五',
    avatar: 'https://placekitten.com/102/102',
    content: '我有个问题，这个算法的时间复杂度是多少？',
    createTime: '2023-10-16T09:15:00',
    likeCount: 2,
    isLiked: false,
    replies: []
  }
]);

// 当前排序方式
const currentSort = ref<SortType>('newest');

// 添加评论
const addComment = (content: string): void => {
  const newComment: CommentType = {
    id: Date.now(),
    userId: 999, // 模拟当前用户
    nickname: '当前用户',
    avatar: 'https://placekitten.com/110/110',
    content: content,
    createTime: new Date().toISOString(),
    likeCount: 0,
    isLiked: false,
    replies: []
  };

  comments.value = [newComment, ...comments.value];
};

// 处理回复
const handleReply = ({commentId, content}: { commentId: number, content: string }): void => {
  const updatedComments = [...comments.value];
  const commentIndex = updatedComments.findIndex(c => c.id === commentId);

  if (commentIndex !== -1) {
    const comment = updatedComments[commentIndex];
    const newReply: ReplyType = {
      id: Date.now(),
      userId: 999, // 模拟当前用户
      nickname: '当前用户',
      avatar: 'https://placekitten.com/110/110',
      content: content,
      createTime: new Date().toISOString(),
      parentId: commentId
    };

    updatedComments[commentIndex] = {
      ...comment,
      replies: [...(comment.replies || []), newReply]
    };

    comments.value = updatedComments;
  }
};

// 处理点赞
const handleLike = (commentId: number): void => {
  const updatedComments = [...comments.value];
  const commentIndex = updatedComments.findIndex(c => c.id === commentId);

  if (commentIndex !== -1) {
    const comment = updatedComments[commentIndex];
    if (!comment.isLiked) {
      updatedComments[commentIndex] = {
        ...comment,
        likeCount: comment.likeCount + 1,
        isLiked: true
      };

      comments.value = updatedComments;
    }
  }
};

// 处理排序变更
const handleSortChange = (sortType: SortType): void => {
  currentSort.value = sortType;

  // 根据排序方式对评论进行排序
  const sortedComments = [...comments.value];

  switch (sortType) {
    case 'newest':
      sortedComments.sort((a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime());
      break;
    case 'oldest':
      sortedComments.sort((a, b) => new Date(a.createTime).getTime() - new Date(b.createTime).getTime());
      break;
    case 'popular':
      sortedComments.sort((a, b) => b.likeCount - a.likeCount);
      break;
  }

  comments.value = sortedComments;
};

// 监听排序变化
watch(currentSort, (newValue) => {
  handleSortChange(newValue);
}, {immediate: true});
</script>

<style scoped>
.answer-discussion {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin: 16px 16px 0;
  overflow: hidden;
  padding: 4px;
}

.empty-comments {
  padding: 40px 0;
}
</style> 