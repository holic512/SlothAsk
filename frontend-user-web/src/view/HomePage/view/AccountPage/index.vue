<script setup lang="ts">
import {computed, onMounted, onUnmounted, ref, watch} from 'vue';
import {setTitle} from '@/utils/title';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {
  Brush,
  Collection,
  DataLine,
  Expand,
  Fold,
  Folder,
  Setting,
  Star,
  SwitchButton,
  Timer,
  User,
  Warning
} from '@element-plus/icons-vue';
import {getUserNameAndAvatar} from "@/view/HomePage/view/TopMenu/Api/ApiUserInfo";
import {useSessionStore} from "@/pinia/Session";

const router = useRouter();
const route = useRoute();

const userInfo = ref({
  avatar: '',
  nickname: ''
});

// 响应式侧边栏控制
const collapsed = ref(false);
const sidebarWidth = 250;
const windowWidth = ref(window.innerWidth);
const breakpoint = 1000; // 抽屉模式断点

// 计算是否为抽屉模式（窗口宽度小于断点）
const isDrawerMode = computed(() => {
  return windowWidth.value < breakpoint;
});

// 监听窗口尺寸变化
const handleResize = () => {
  windowWidth.value = window.innerWidth;
  if (windowWidth.value < breakpoint) {
    collapsed.value = true;
  } else if (windowWidth.value >= breakpoint && windowWidth.value <= breakpoint + 100) {
    // 当窗口从小变大，超过断点时，自动展开侧边栏
    collapsed.value = false;
  }
};

// 侧边栏样式
const sidebarClass = computed(() => {
  return {
    'drawer-mode': isDrawerMode.value,
    'sidebar-floating': isDrawerMode.value // 只在抽屉模式下浮动
  };
});

const sidebarStyle = computed(() => {
  return {width: `${sidebarWidth}px`};
});

// 切换按钮样式
const toggleButtonStyle = computed(() => {
  if (isDrawerMode.value) {
    // 抽屉模式下，按钮固定在左侧
    return {
      left: collapsed.value ? '0px' : `${sidebarWidth}px`
    };
  } else {
    // 普通模式下，按钮跟随侧边栏
    return {
      left: collapsed.value ? '0px' : `${sidebarWidth}px`
    };
  }
});

// 主内容区域样式类
const mainContentClass = computed(() => {
  return {
    'content-blur': !collapsed.value && isDrawerMode.value, // 仅抽屉模式展开时模糊
    'full-width': collapsed.value || isDrawerMode.value,  // 收缩状态或抽屉模式占满宽度
    'with-sidebar': !collapsed.value && !isDrawerMode.value // 普通展开状态下保留侧边栏宽度
  };
});

const toggleSidebar = () => {
  collapsed.value = !collapsed.value;
};

// 学习相关
const studyMenuItems = [
  {path: '/questionbank/myFavoritesQuestion', name: '收藏夹', icon: Star, enabled: true},
  {path: '/account/wrong-questions', name: '错题本', icon: Warning, enabled: false},
  {path: '/account/history', name: '历史浏览', icon: Timer, enabled: true},
  {path: '/account/progress', name: '进展分析', icon: DataLine, enabled: false},
  {path: '/account/record', name: '答题记录', icon: Collection, enabled: false},
  {path: '/account/my-bank', name: '我的题库', icon: Folder, enabled: false},
];

// 项目相关
const projectMenuItems = [
  {path: '/account/projects', name: '显示项目', icon: Folder, enabled: false},
];

// 设置相关
const settingMenuItems = [
  {path: '/account/profile', name: '个人资料', icon: User, enabled: true},
  {path: '/account/settings', name: '账号设置', icon: Setting, enabled: true},
  {path: '/account/appearance', name: '外观设置', icon: Brush, enabled: false},
];

// 获取用户session pinia实例
const userSession = useSessionStore();

// 判断用户是否登录
const isLoggedIn = computed(() => {
  return userSession.userSession && userSession.userSession.tokenValue;
});

// 获取用户信息的方法
const fetchUserInfo = async () => {
  // 如果用户未登录，不获取用户信息
  if (!isLoggedIn.value) return;

  try {
    const response = await getUserNameAndAvatar();
    if (response.status === 200) {
      userInfo.value = response.data;
      return 200;
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
};

// 监听用户session变化
watch(() => userSession.userSession, async (newValue) => {
  // 只有在用户登录时才获取用户信息
  if (newValue && newValue.tokenValue) {
    await fetchUserInfo();
  } else {
    // 用户未登录时，清空用户信息
    userInfo.value = {
      avatar: '',
      nickname: ''
    };
  }
}, {immediate: true});

const handleLogout = () => {
  // 清空token
  userSession.clearSession();
  ElMessage.success('退出登录成功');
  // 刷新页面
  window.location.reload();
};

onMounted(() => {
  setTitle('账户');
  // 初始化时检测屏幕尺寸
  handleResize();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<template>
  <div class="account-contain">
    <el-container class="account-layout">
      <!-- 左侧菜单：根据抽屉模式选择不同的渲染位置 -->
      <transition mode="out-in" name="slide">
        <div
            v-show="!collapsed"
            :class="sidebarClass"
            :style="sidebarStyle"
            class="account-sidebar"
        >
          <!-- 用户信息 -->
          <div class="user-info">
            <!-- 只有登录时才显示用户头像和昵称 -->
            <template v-if="isLoggedIn && userInfo.nickname">
              <el-avatar v-if="userInfo.avatar" :size="64" :src="userInfo.avatar"/>
              <el-avatar v-else :size="64">{{ userInfo.nickname[0] }}</el-avatar>
              <div class="nickname">{{ userInfo.nickname }}</div>
            </template>
            <!-- 未登录时显示默认图标 -->
            <template v-else>
              <el-avatar :size="64" icon="el-icon-user"/>
              <div class="nickname">未登录用户</div>
            </template>
          </div>

          <!-- 侧边栏菜单 -->
          <el-menu
              :default-active="route.path"
              class="account-menu"
              router
          >
            <!-- 学习相关 -->
            <div class="menu-group">
              <div class="menu-group-title">学习相关</div>
              <el-menu-item 
                v-for="item in studyMenuItems.filter(item => item.enabled)" 
                :key="item.path" 
                :index="item.path"
              >
                <el-icon>
                  <component :is="item.icon"/>
                </el-icon>
                <span>{{ item.name }}</span>
              </el-menu-item>
            </div>

            <!-- 项目相关 -->
            <div v-if="projectMenuItems.some(item => item.enabled)" class="menu-group">
              <div class="menu-group-title">项目相关</div>
              <el-menu-item 
                v-for="item in projectMenuItems.filter(item => item.enabled)" 
                :key="item.path" 
                :index="item.path"
              >
                <el-icon>
                  <component :is="item.icon"/>
                </el-icon>
                <span>{{ item.name }}</span>
              </el-menu-item>
            </div>

            <!-- 设置相关 -->
            <div class="menu-group">
              <div class="menu-group-title">设置相关</div>
              <el-menu-item 
                v-for="item in settingMenuItems.filter(item => item.enabled)" 
                :key="item.path" 
                :index="item.path"
              >
                <el-icon>
                  <component :is="item.icon"/>
                </el-icon>
                <span>{{ item.name }}</span>
              </el-menu-item>
            </div>
          </el-menu>

          <!-- 退出登录按钮 -->
          <div v-if="isLoggedIn" class="logout-btn" @click="handleLogout">
            <el-icon>
              <SwitchButton/>
            </el-icon>
            <span>退出登录</span>
          </div>
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
          class="account-content"
      >
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

.account-contain {
  width: 100%;
  min-height: calc(100vh - 430px);
  padding: 0;
  display: flex;
  justify-content: center;
  background-color: white;
}

.account-layout {
  width: 1100px;
  margin: 0;
  position: relative;
  display: flex;
  overflow: hidden;
}

.account-sidebar {
  border-right: 1px solid #e0e0e0;
  height: 100%;
  min-height: 780px;
  overflow: auto;
  transition: all 0.2s ease;
  background-color: white;
  z-index: 1001;
  position: relative; /* 默认为相对定位 */
  flex-shrink: 0;
  width: 250px;
  display: flex;
  flex-direction: column;
  padding: 12px;
}

/* 抽屉模式下的浮动侧边栏 */
.sidebar-floating {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.account-content {
  background-color: white;
  border-radius: 12px;
  padding: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.2s ease;
  overflow: auto;
  width: 100%;
}

/* 收缩状态下主内容占满宽度 */
.full-width {
  width: 100%;
}

/* 抽屉模式展开时内容模糊 */
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
  z-index: 999;
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

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  margin-bottom: 12px;
}

.nickname {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.account-menu {
  flex: 1;
  background: transparent;
  border: none;
  overflow-y: auto;
}

/* 分组标题 */
.menu-group-title {
  font-size: 12px;
  color: #888;
  text-transform: uppercase;
  margin: 4px 8px;
  font-weight: 500;
}

/* 菜单项 */
.account-menu :deep(.el-menu-item) {
  height: 42px;
  line-height: 44px;
  color: #555;
  border-radius: 8px;
  margin-bottom: 4px;
  padding: 0 16px;
  transition: background-color 0.2s ease, color 0.2s ease;
}

/* 激活态：左侧高亮条 */
.account-menu :deep(.el-menu-item.is-active) {
  background: transparent;
  color: var(--el-color-primary);
  font-weight: 600;
}

.account-menu :deep(.el-menu-item.is-active)::before {
  content: "";
  position: absolute;
  left: 0;
  top: 8px;
  bottom: 8px;
  width: 4px;
  background: var(--el-color-primary);
  border-radius: 2px;
}

/* 悬停态 */
.account-menu :deep(.el-menu-item:hover) {
  background-color: rgba(0, 123, 255, 0.08);
  color: var(--el-color-primary);
  will-change: background-color;
}

/* 退出登录按钮 */
.logout-btn {
  padding: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #555;
  border-radius: 8px;
  transition: all 0.25s ease;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: rgba(245, 108, 108, 0.1);
  color: var(--el-color-danger);
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.1);
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
  .account-layout {
    width: 100%;
    padding: 0 12px;
  }

  .account-sidebar:not(.drawer-mode) {
    display: none;
  }
}

@media (max-width: 768px) {
  .account-layout {
    flex-direction: column;
    gap: 12px;
  }
}

/* 带侧边栏的主内容区 - 普通模式展开状态 */
.with-sidebar {
  width: calc(100% - 250px);
}
</style>