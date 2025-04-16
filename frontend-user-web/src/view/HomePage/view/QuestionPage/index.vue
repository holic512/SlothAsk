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
      <SidebarLeft :class="{ 'sidebar-mobile': isMobileView && showLeftSidebar }"/>
    </div>

    <!-- 中间内容区 -->
    <div class="main-content">
      <div class="content-wrapper" :class="{ 'content-full': isMobileView }">
        <div class="container">
          <QuestionDetail/>
          <CommentSection/>
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
  width: 240px;
  flex-shrink: 0;
  margin: 24px 16px 0 24px;
  transition: width 0.2s ease, margin 0.2s ease;
}


/* 平板视图样式 */
@media (max-width: 1600px) {
  .sidebar-hidden {
    width: 0;
    margin: 0;
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
    padding: 16px;
    border-radius: 0;
    transition: all 0.2s ease;
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
</style>