<script setup>
import { ref } from 'vue'
import {
  DataLine,
  User,
  Files,
  Medal,
  Setting,
  Tickets,
  Management,
  UserFilled,
  List,
  Tools,
  Calendar,
  ChatDotSquare
} from '@element-plus/icons-vue'

const props = defineProps({
  collapse: {
    type: Boolean,
    default: false,
    required: true
  }
})

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>

<template>
  <div class="navigation-container">
    <el-menu
      default-active="/main/dashboard"
      class="el-menu-vertical"
      :collapse="collapse"
      @open="handleOpen"
      @close="handleClose"
      router
      :background-color="'var(--el-bg-color)'"
      :text-color="'var(--el-text-color-primary)'"
      :active-text-color="'var(--el-color-primary)'"
    >
      <!-- 仪表盘 -->
      <el-menu-item index="/main/dashboard">
        <el-icon><DataLine /></el-icon>
        <template #title>仪表盘</template>
      </el-menu-item>

      <!-- 用户管理 -->
      <el-sub-menu index="/main/user">
        <template #title>
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </template>
        <el-menu-item index="/main/user/list">用户列表</el-menu-item>
        <el-menu-item index="/main/user/achievements">成就管理</el-menu-item>
        <el-menu-item index="/main/user/sign">签到记录</el-menu-item>
        <el-menu-item index="/main/user/follow">关注管理</el-menu-item>
      </el-sub-menu>

      <!-- 题库管理 -->
      <el-sub-menu index="/main/question">
        <template #title>
          <el-icon><Files /></el-icon>
          <span>题库管理</span>
        </template>
        <el-menu-item index="/main/question/project">项目分类</el-menu-item>
        <el-menu-item index="/main/question/category">题库分类</el-menu-item>
        <el-menu-item index="/main/question/list">题目列表</el-menu-item>
      </el-sub-menu>

      <!-- 评论管理 -->
      <el-sub-menu index="/main/comments">
        <template #title>
          <el-icon><ChatDotSquare /></el-icon>
          <span>评论管理</span>
        </template>
        <el-menu-item index="/main/comments/question">题目评论</el-menu-item>
        <el-menu-item index="/main/comments/category">题库评论</el-menu-item>
        <el-menu-item index="/main/comments/rating">评分管理</el-menu-item>
      </el-sub-menu>

      <!-- 会员管理 -->
      <el-sub-menu index="/main/member">
        <template #title>
          <el-icon><Medal /></el-icon>
          <span>会员管理</span>
        </template>
        <el-menu-item index="/main/member/level">会员等级</el-menu-item>
        <el-menu-item index="/main/member/list">会员列表</el-menu-item>
        <el-menu-item index="/main/member/points">积分管理</el-menu-item>
      </el-sub-menu>

      <!-- 统计分析 -->
      <el-sub-menu index="/main/stats">
        <template #title>
          <el-icon><DataLine /></el-icon>
          <span>统计分析</span>
        </template>
        <el-menu-item index="/main/stats/question">刷题统计</el-menu-item>
        <el-menu-item index="/main/stats/sign">签到统计</el-menu-item>
        <el-menu-item index="/main/stats/wrong">错题分析</el-menu-item>
      </el-sub-menu>

      <!-- 系统管理 -->
      <el-sub-menu index="/main/system">
        <template #title>
          <el-icon><Setting /></el-icon>
          <span>系统管理</span>
        </template>
        <el-menu-item index="/main/system/admin">管理员管理</el-menu-item>
        <el-menu-item index="/main/system/roles">角色权限</el-menu-item>
        <el-menu-item index="/main/system/logs">登录日志</el-menu-item>
        <el-menu-item index="/main/system/config">系统配置</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<style scoped>
.navigation-container {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: var(--el-bg-color);
  border-right: 1px solid var(--el-border-color-light);
  user-select: none; /* 防止文字被选中 */
}

/* 自定义滚动条样式 */
.navigation-container::-webkit-scrollbar {
  width: 4px;
  height: 4px;
  display: none;
}

.navigation-container:hover::-webkit-scrollbar {
  display: block;
}

.navigation-container::-webkit-scrollbar-thumb {
  background: var(--el-border-color);
  border-radius: 2px;
}

.navigation-container::-webkit-scrollbar-track {
  background: transparent;
}

.navigation-container:hover::-webkit-scrollbar-thumb {
  background: var(--el-border-color-darker);
}

.navigation-container {
  scrollbar-width: none;
}

.navigation-container:hover {
  scrollbar-width: thin;
}

.el-menu-vertical {
  border-right: none;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: 100%;
}

.el-menu-vertical.el-menu--collapse {
  width: 64px;
  min-height: 100%;
}

:deep(.el-menu-item), :deep(.el-sub-menu__title) {
  height: 48px;
  line-height: 48px;
}

:deep(.el-sub-menu .el-menu-item) {
  min-width: 200px;
  padding-left: 40px !important;
}

:deep(.el-menu-item.is-active) {
  background-color: var(--el-color-primary-light-9) !important;
}

:deep(.el-menu-item:hover), :deep(.el-sub-menu__title:hover) {
  background-color: var(--el-fill-color-light) !important;
}

:deep(.el-sub-menu.is-active .el-sub-menu__title) {
  color: var(--el-color-primary) !important;
}

:deep(.el-menu--collapse .el-sub-menu.is-active .el-sub-menu__title) {
  color: var(--el-color-primary) !important;
}

:deep(.el-menu-item .el-icon), 
:deep(.el-sub-menu__title .el-icon) {
  color: var(--el-text-color-primary);
}

:deep(.el-menu-item.is-active .el-icon), 
:deep(.el-sub-menu.is-active > .el-sub-menu__title .el-icon) {
  color: var(--el-color-primary);
}

:deep(.el-menu--popup) {
  background-color: var(--el-bg-color) !important;
  border: 1px solid var(--el-border-color-light);
}

:deep(.el-menu--popup .el-menu-item) {
  background-color: var(--el-bg-color) !important;
}

:deep(.el-menu--popup .el-menu-item:hover) {
  background-color: var(--el-fill-color-light) !important;
}

:deep(.el-menu--popup .el-menu-item.is-active) {
  background-color: var(--el-color-primary-light-9) !important;
  color: var(--el-color-primary) !important;
}

:deep(.el-menu-item), 
:deep(.el-sub-menu__title),
:deep(.el-menu--popup) {
  user-select: none; /* 确保所有菜单项文字都不可选中 */
}
</style>