<template>
  <el-container class="question-bank-container">
    <!-- 左侧菜单：根据抽屉模式选择不同的渲染位置 -->
    <transition mode="out-in" name="slide">
      <div
          v-show="!collapsed"
          :class="sidebarClass"
          :style="sidebarStyle"
          class="sidebar"
      >
        <LeftSidebar/>
      </div>
    </transition>

    <!-- 模糊遮罩层 -->
    <transition name="fade">
      <div
          v-show="!collapsed && isDrawerMode"
          class="overlay"
          @click="toggleSidebar"
      ></div>
    </transition>

    <!-- 悬浮按钮 -->
    <div
        :style="toggleButtonStyle"
        class="toggle-button"
        @click="toggleSidebar"
    >
      <el-icon>
        <component :is="collapsed ? Expand : Fold"/>
      </el-icon>
    </div>

    <!-- 主内容区 -->
    <el-main
        :class="mainContentClass"
        class="main-content"
    >
      <router-view/>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import {computed, onMounted, onUnmounted, ref} from 'vue'
import {Expand, Fold} from '@element-plus/icons-vue'
import LeftSidebar from "@/view/HomePage/view/QuestionBankPage/LeftSidebar/LeftSidebar.vue";

const collapsed = ref(false)
const sidebarWidth = 250
const windowWidth = ref(window.innerWidth)
const breakpoint = 1000 // 抽屉模式断点

// 监听窗口尺寸变化
const handleResize = () => {
  const prevIsDrawerMode = isDrawerMode.value;
  windowWidth.value = window.innerWidth;
  const newIsDrawerMode = windowWidth.value < breakpoint;

  // 如果进入抽屉模式（变小屏），自动折叠
  if (!prevIsDrawerMode && newIsDrawerMode) {
    collapsed.value = true;
  }
}

// 计算是否为抽屉模式（窗口宽度小于断点）
const isDrawerMode = computed(() => {
  return windowWidth.value < breakpoint
})

// 侧边栏样式
const sidebarClass = computed(() => {
  return {'drawer-mode': isDrawerMode.value}
})

const sidebarStyle = computed(() => {
  return {width: `${sidebarWidth}px`}
})

// 切换按钮样式
const toggleButtonStyle = computed(() => {
  if (isDrawerMode.value) {
    // 抽屉模式下，按钮固定在左侧
    return {
      left: collapsed.value ? '0px' : `${sidebarWidth}px`
    }
  } else {
    // 普通模式下，按钮跟随侧边栏
    return {
      left: collapsed.value ? '0px' : `${sidebarWidth}px`
    }
  }
})

// 主内容区域样式类
const mainContentClass = computed(() => {
  return {
    'content-blur': !collapsed.value && isDrawerMode.value, // 仅抽屉模式展开时模糊
    'full-width': collapsed.value,  // 收缩状态时占满宽度
    'with-sidebar': !collapsed.value && !isDrawerMode.value // 普通展开状态
  }
})

onMounted(() => {
  handleResize() // 初始检查窗口尺寸
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

const toggleSidebar = () => {
  collapsed.value = !collapsed.value
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.question-bank-container {
  height: calc(100vh - 54px);
  position: relative;
  display: flex;
  overflow: hidden; /* 防止内容溢出 */
  background-color: #f5f5f5;
}

.sidebar {
  border-right: 1px solid #e0e0e0;
  height: 100%;
  overflow: auto;
  transition: all 0.2s ease;
  background-color: #fff;
  z-index: 1001;
  position: relative;
  flex-shrink: 0;
  width: 250px; /* 确保宽度固定 */

}

.drawer-mode {
  position: fixed;
  top: 0;
  left: 0;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
  height: 100vh;
  z-index: 1001;
}

.main-content {
  background-color: #f5f5f5;
  overflow: auto;
  transition: all 0.2s ease;
  width: 100%; /* 默认占满容器 */
  padding: 0;
}

/* 带侧边栏的主内容区 - 普通模式展开状态 */
.with-sidebar {
  width: calc(100% - 250px);
}

/* 收缩状态下主内容占满宽度 */
.full-width {
  width: 100%;
}

.content-blur {
  filter: blur(3px);
}

/* 模糊遮罩 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  z-index: 999; /* 低于侧边栏 */
  transition: opacity 0.2s ease;
}

/* 悬浮按钮样式 */
.toggle-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 24px;
  height: 60px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-left: none;
  border-radius: 0 6px 6px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1002;
  transition: left 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 展开/收起动画 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.2s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateX(-100%);
}

/* 遮罩层动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式断点 */
@media (max-width: 1000px) {
  .sidebar:not(.drawer-mode) {
    display: none;
  }
}
</style>
