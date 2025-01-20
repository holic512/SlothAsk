<script setup>
import { ref } from 'vue'
import { Refresh, Download } from '@element-plus/icons-vue'

const activities = ref([
  {
    id: 1,
    user: '张三',
    action: '完成题目',
    target: 'LeetCode 75',
    time: '10分钟前',
    status: 'success'
  },
  {
    id: 2,
    user: '李四',
    action: '发表评论',
    target: '动态规划专题',
    time: '25分钟前',
    status: 'info'
  },
  {
    id: 3,
    user: '王五',
    action: '购买会员',
    target: '年度会员',
    time: '42分钟前',
    status: 'warning'
  },
  {
    id: 4,
    user: '赵六',
    action: '提交题解',
    target: '二叉树遍历',
    time: '1小时前',
    status: 'success'
  }
])
</script>

<template>
  <el-card class="recent-activities">
    <template #header>
      <div class="card-header">
        <span>最近活动</span>
        <el-button-group>
          <el-button :icon="Refresh" circle size="small" />
          <el-button :icon="Download" circle size="small" />
        </el-button-group>
      </div>
    </template>

    <el-timeline>
      <el-timeline-item
        v-for="activity in activities"
        :key="activity.id"
        :type="activity.status"
        size="large"
        :timestamp="activity.time"
      >
        <div class="activity-content">
          <span class="user">{{ activity.user }}</span>
          <span class="action">{{ activity.action }}</span>
          <el-tag size="small" :type="activity.status">{{ activity.target }}</el-tag>
        </div>
      </el-timeline-item>
    </el-timeline>
  </el-card>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-content {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .user {
    font-weight: bold;
  }
  
  .action {
    color: var(--el-text-color-secondary);
  }
}

:deep(.el-timeline-item__node) {
  left: -2px;
}

:deep(.el-timeline-item__wrapper) {
  padding-left: 26px;
}

:deep(.el-timeline-item__timestamp) {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}
</style> 