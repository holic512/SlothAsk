<template>
  <div class="page-wrapper">
    <div class="sidebar-left" :class="{ 'sidebar-hidden': isMobileView }">
      <el-button
          v-if="isMobileView"
          class="sidebar-toggle"
          icon="Menu"
          circle
          @click="toggleLeftSidebar"
      />
      <SidebarLeft :class="{ 'sidebar-mobile': isMobileView && showLeftSidebar }"/>
    </div>
    <div class="content-wrapper">
      <div class="container">
        <QuestionDetail/>
        <CommentSection/>
      </div>
    </div>
    <div class="sidebar-right" :class="{ 'sidebar-hidden': isTabletView }">
      <SidebarRight/>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, onBeforeUnmount, computed} from 'vue';
import {setTitle} from '@/utils/title';
import QuestionDetail from './components/container/QuestionDetail.vue'
import CommentSection from './components/container/CommentSection.vue'
import SidebarLeft from './components/sidebar-left/SidebarLeft.vue'
import SidebarRight from './components/sidebar-right/SidebarRight.vue'

// 响应式状态
const windowWidth = ref(window.innerWidth);
const showLeftSidebar = ref(false);

// 计算属性：判断当前视图类型 - 调整阈值
const isMobileView = computed(() => windowWidth.value < 1350);
const isTabletView = computed(() => windowWidth.value < 1600);

// 监听窗口大小变化
const handleResize = () => {
  windowWidth.value = window.innerWidth;
  // 如果从小屏幕变为大屏幕，关闭移动端侧边栏
  if (!isMobileView.value) {
    showLeftSidebar.value = false;
  }
};

// 切换左侧边栏显示状态
const toggleLeftSidebar = () => {
  showLeftSidebar.value = !showLeftSidebar.value;
};

onMounted(() => {
  setTitle('题库');
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.page-wrapper {
  display: flex;
  min-height: 100vh;
  position: relative;
  background-color: #f9fafb;
}

.sidebar-left {
  width: 300px; /* 稍微减小宽度 */
  flex-shrink: 0;
  position: relative;
  transition: all 0.2s ease; /* 将动画时间从0.3s减少到0.2s，让切换更加利索 */
  background-color: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  padding: 16px;
  overflow: hidden; /* 防止内容溢出 */
}

.sidebar-right {
  width: 240px;
  flex-shrink: 0;
  margin-top: 16px;
  margin-right: 20px; /* 增加右侧留白 */
  margin-left: 20px;
  transition: all 0.3s ease;
}

/* 新增内容包装器，用于居中固定宽度的容器 */
.content-wrapper {
  flex: 1;
  display: flex;
  justify-content: center;
  transition: all 0.3s ease;
}

.container {
  width: 950px; /* 固定最大宽度 */
  margin: 16px 0; /* 只设置上下边距 */
  padding: 0 12px; /* 内部左右留白 */
  transition: all 0.3s ease;
}

/* 平板视图样式 */
@media (max-width: 1600px) {
  /* 调整阈值 */
  .sidebar-hidden {
    width: 0;
    overflow: hidden;
    margin: 0;
    padding: 0;
  }

  .container {
    margin: 16px;
    padding: 0 24px; /* 减小内部留白 */
  }
}

/* 中等屏幕视图样式 */
@media (max-width: 1550px) {
  .container {
    padding: 0 20px; /* 进一步减小内部留白 */
  }
}

/* 移动端视图样式 */
@media (max-width: 1350px) {
  /* 调整阈值 */
  .sidebar-hidden {
    width: 0;
    padding: 0;
    margin: 0;
    overflow: hidden;
  }

  .content-wrapper {
    padding: 0 12px;
  }

  .container {
    margin: 12px 0;
    padding: 0 16px;
    width: 100%; /* 确保在移动视图中占满宽度 */
  }

  .sidebar-toggle {
    position: fixed;
    top: 64px;
    left: 16px;
    z-index: 1000;
    background-color: #ffffff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  }

  .sidebar-mobile {
    position: fixed;
    top: 0;
    left: 0;
    height: 99vh;
    width: 280px;
    background: white;
    z-index: 999;
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
    overflow-y: auto;
    padding: 16px; /* 恢复内边距 */
    transition: all 0.2s ease; /* 为移动端侧边栏也添加更快的过渡效果 */
  }
}

/* 小屏幕手机视图 */
@media (max-width: 480px) {
  .content-wrapper {
    padding: 0 8px; /* 减小内容包装器的内边距 */
  }

  .container {
    margin: 8px 0;
    padding: 0 12px; /* 小屏幕手机进一步减小内部留白，但仍保持可读性 */
  }
}
</style>