<script setup>
import { ref, computed } from 'vue'
import LeftNavigation from './LeftNavigation/LeftNavigation.vue'
import MainHeader from './Header/MainHeader.vue'

const isCollapsed = ref(false)

// 计算侧边栏宽度
const asideWidth = computed(() => {
  return isCollapsed.value ? '64px' : '200px'
})
</script>

<template>
  <el-container class="main-container">
    <el-aside :width="asideWidth" class="main-aside">
      <LeftNavigation :collapse="isCollapsed" />
    </el-aside>
    
    <el-container class="right-container">
      <el-header class="main-header" height="48px">
        <MainHeader v-model:collapsed="isCollapsed" />
      </el-header>
      
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.main-container {
  width: 100%;
  height: 100vh;
  background-color: var(--el-bg-color);
}

.main-aside {
  transition: width 0.3s;
  background-color: var(--el-bg-color);
  border-right: 1px solid var(--el-border-color-light);
  overflow: hidden;
}

.right-container {
  display: flex;
  flex-direction: column;
  background-color: var(--el-bg-color-page);
  min-height: 100vh;
}

.main-header {
  padding: 0;
  background-color: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color-light);
}

.main-content {
  padding: 20px;
  background-color: var(--el-bg-color-page);
}

/* 滚动条样式 */
.main-content::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.main-content::-webkit-scrollbar-thumb {
  background: var(--el-border-color);
  border-radius: 3px;
}

.main-content::-webkit-scrollbar-track {
  background: transparent;
}

.main-content:hover::-webkit-scrollbar-thumb {
  background: var(--el-border-color-darker);
}

/* Firefox 滚动条 */
.main-content {
  scrollbar-width: thin;
  scrollbar-color: var(--el-border-color) transparent;
}

/* 确保内容区域最小高度 */
:deep(.el-main) {
  --el-main-padding: 20px;
  min-height: calc(100vh - 48px);
}

/* 确保布局容器背景色 */
:deep(.el-container) {
  background-color: var(--el-bg-color-page);
}

:deep(.el-aside),
:deep(.el-header) {
  background-color: var(--el-bg-color);
}
</style>