<template>
  <div class="page-wrapper">
    <!-- 左侧栏固定 -->
    <div class="sidebar-left-container" :class="{ 'sidebar-hidden': isMobileView }">
      <el-button
          v-if="isMobileView"
          class="sidebar-toggle"
          icon="Menu"
          circle
          @click="toggleLeftSidebar"
      />
      <transition name="slide-fade">
        <SidebarLeft v-if="!isMobileView || showLeftSidebar" :class="{ 'sidebar-mobile': isMobileView && showLeftSidebar }"/>
      </transition>
    </div>

    <!-- 遮罩层 - 点击关闭侧边栏 -->
    <transition name="fade">
      <div 
        v-if="isMobileView && showLeftSidebar" 
        class="sidebar-overlay" 
        @click="closeSidebar"
      ></div>
    </transition>

    <!-- 中间内容区 -->
    <div class="main-content">
      <div class="content-wrapper" :class="{ 'content-full': isMobileView }">
        <div class="container">
          <QuestionDetail/>
          <AnswerDiscussion/>
        </div>
      </div>

      <!-- 右侧栏 -->
      <div class="sidebar-right-container" :class="{ 'sidebar-hidden': isTabletView }">
        <SidebarRight/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onBeforeUnmount, onMounted, provide, ref} from 'vue';
import {setTitle} from '@/utils/title';
import QuestionDetail from './components/container/QuestionDetail/QuestionDetail.vue'
import AnswerDiscussion from './components/container/AnswerDiscussion/AnswerDiscussion.vue'
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

// 监听ESC键关闭侧边栏
const handleKeyDown = (e) => {
  if (e.key === 'Escape' && showLeftSidebar.value) {
    closeSidebar();
  }
};

// 切换左侧边栏显示状态
const toggleLeftSidebar = () => {
  showLeftSidebar.value = !showLeftSidebar.value;
};

// 关闭侧边栏
const closeSidebar = () => {
  showLeftSidebar.value = false;
};

// 提供closeSidebar函数给子组件
provide('closeSidebar', closeSidebar);

onMounted(() => {
  setTitle('题库');
  window.addEventListener('resize', handleResize);
  window.addEventListener('keydown', handleKeyDown);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  window.removeEventListener('keydown', handleKeyDown);
});
</script>

<style scoped>
.page-wrapper {
  display: flex;
  min-height: 100vh;
  position: relative;
  background-color: #f9fafb;
  width: 100%;
  max-width: 100%;
  justify-content: flex-start; /* 左对齐 */
}

/* 左侧边栏容器 - 固定在左侧 */
.sidebar-left-container {
  width: 300px;
  height: 100%;
  flex-shrink: 0;
  position: relative;
  transition: width 0.2s ease, margin 0.2s ease;
  background-color: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  padding: 16px;
}

/* 主内容区域包含内容和右侧边栏 */
.main-content {
  flex: 1;
  display: flex;
  justify-content: center; /* 主内容区内部居中 */
}

/* 内容区包装器 */
.content-wrapper {
  width: 950px; /* 固定主内容宽度 */
  transition: width 0.3s ease;
  display: flex;
  justify-content: center;
}

.container {
  width: 100%;
  margin: 16px 0;
  padding: 0 16px;
  transition: all 0.3s ease;
}

/* 右侧边栏容器 */
.sidebar-right-container {
  width: 280px;
  flex-shrink: 0;
  margin: 16px 16px 0 24px;
  transition: width 0.2s cubic-bezier(0.4, 0, 0.2, 1), margin 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  position: relative;
}


/* 平板视图样式 */
@media (max-width: 1600px) {
  .sidebar-hidden {
    width: 0;
    margin: 16px 16px 0 0; /* 保持右侧、顶部和底部的margin，只将左侧margin设为0 */
    padding: 0;
    overflow: hidden;
  }

  .content-wrapper {
    width: 950px; /* 保持固定宽度 */
  }
}

/* 中等屏幕视图样式 */
@media (max-width: 1550px) {
  .content-wrapper {
    width: 900px; /* 稍微缩小一点 */
  }
}

/* 小屏视图样式 */
@media (max-width: 1350px) {
  .sidebar-left-container.sidebar-hidden {
    width: 0;
    padding: 0;
    margin: 0;
    overflow: hidden;
  }

  .content-wrapper {
    width: 850px; /* 左侧边栏隐藏时保持主内容固定宽度 */
  }

  .content-full {
    padding: 0 16px;
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
    height: 100vh;
    width: 280px;
    background: white;
    z-index: 999;
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
    overflow-y: auto;
    padding: 8px;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    will-change: transform;
    transform: translateZ(0);
    backface-visibility: hidden;
    perspective: 1000px;
  }
}

/* 平板视图样式 */
@media (max-width: 900px) {
  .content-wrapper {
    width: 700px; /* 再缩小 */
  }
}

/* 移动设备视图样式 */
@media (max-width: 768px) {
  .main-content {
    justify-content: flex-start; /* 小屏幕下内容靠左 */
  }

  .content-wrapper {
    width: 100%; /* 小屏幕下占满 */
    padding: 0 16px;
  }

  .container {
    margin: 12px 0;
    padding: 0;
  }
}

/* 小屏幕手机视图 */
@media (max-width: 480px) {
  .content-wrapper {
    padding: 0;
  }

  .container {
    margin: 8px 0;
  }
}

/* 遮罩层样式 */
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.25);
  z-index: 998; /* 确保在侧边栏下方，但在其他内容上方 */
  cursor: pointer;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  will-change: opacity;
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

/* 侧边栏动画 */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform, opacity;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}
</style>