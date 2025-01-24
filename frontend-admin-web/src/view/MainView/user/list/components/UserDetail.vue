<script setup lang="ts">
import { computed } from 'vue'
import { useUserListStore } from "../pinia/userListStore";
import type { IUser } from "../types/user"

const userListStore = useUserListStore()

// 使用计算属性处理 drawer 显示状态
const visible = computed({
    get: () => userListStore.drawerVisible,
    set: (value) => userListStore.drawerVisible = value
})

// 使用计算属性获取用户数据
const userData = computed<IUser | null>(() => userListStore.currentUser)

// 性别格式化
const formatGender = (gender) => {
  const genderMap = {
    1: '男',
    2: '女',
    0: '未知'
  }
  return genderMap[gender] || '未知'
}

// 状态格式化
const formatStatus = (status) => {
  const statusMap = {
    1: { text: '正常', type: 'success', effect: 'plain' },
    0: { text: '禁用', type: 'danger', effect: 'plain' }
  }
  return statusMap[status] || { text: '未知', type: 'info', effect: 'plain' }
}

</script>

<template>
  <el-drawer
    v-model="visible"
    title="用户详情"
    size="400px"
    destroy-on-close
  >
    <template v-if="userData">
      <div class="user-detail">
        <div class="profile-header">
          <el-avatar
            :size="80"
            :src="userData.avatar"
            class="user-avatar"
          />
          <div class="user-basic-info">
            <h3 class="username">{{ userData.username }}</h3>
            <p class="nickname">{{ userData.nickname }}</p>
            <el-tag 
              :type="formatStatus(userData.status).type"
              :effect="formatStatus(userData.status).effect"
              size="small"
              class="status-tag"
            >
              {{ formatStatus(userData.status).text }}
            </el-tag>
          </div>
        </div>

        <div class="info-section">
          <div class="section-title">基本信息</div>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">ID</span>
              <span class="value">{{ userData.id }}</span>
            </div>
            <div class="info-item">
              <span class="label">性别</span>
              <span class="value">{{ formatGender(userData.gender) }}</span>
            </div>
            <div class="info-item">
              <span class="label">年龄</span>
              <span class="value">{{ userData.age ? userData.age + '岁' : '保密' }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <div class="section-title">联系方式</div>
          <div class="info-grid">
            <div class="info-item full-width">
              <span class="label">邮箱</span>
              <span class="value">{{ userData.email }}</span>
            </div>
            <div class="info-item full-width">
              <span class="label">手机</span>
              <span class="value">{{ userData.phone }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <div class="section-title">其他信息</div>
          <div class="info-grid">
            <div class="info-item full-width">
              <span class="label">创建时间</span>
              <span class="value">{{ userData.createTime }}</span>
            </div>
            <div class="info-item full-width">
              <span class="label">个人简介</span>
              <span class="value bio">{{ userData.bio || '暂无简介' }}</span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </el-drawer>
</template>

<style scoped>
.user-detail {
  padding: 0 20px;
}

.profile-header {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
  margin-bottom: 24px;
}

.user-avatar {
  border: 2px solid var(--el-border-color-lighter);
  padding: 2px;
  background-color: var(--el-bg-color);
}

.user-basic-info {
  margin-left: 20px;
}

.username {
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 4px 0;
}

.nickname {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin: 0 0 8px 0;
}

.status-tag {
  font-size: 12px;
}

.info-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  margin-bottom: 16px;
  padding-left: 10px;
  border-left: 3px solid var(--el-color-primary);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item.full-width {
  grid-column: span 2;
}

.label {
  font-size: 13px;
  color: var(--el-text-color-secondary);
  margin-bottom: 4px;
}

.value {
  font-size: 14px;
  color: var(--el-text-color-primary);
}

.bio {
  line-height: 1.5;
  white-space: pre-wrap;
}

:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

:deep(.el-drawer__body) {
  padding: 0;
}
</style> 