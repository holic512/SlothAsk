<script setup>
import {useRouter, useRoute} from 'vue-router'
import SearchBox from './components/SearchBox/SearchBox.vue'
import {useSessionStore} from "@/pinia/Session";
import {watch, ref, computed, onMounted, onUnmounted} from "vue";
import {useTopMenuStore} from "@/view/HomePage/view/TopMenu/pinia/topMenuStore";
import UserUtil from "@/view/HomePage/view/TopMenu/components/UserUtil/UserUtil.vue";
import {getUserNameAndAvatar} from '@/view/HomePage/view/TopMenu/Api/ApiUserInfo';
import {Bell, Menu} from "@element-plus/icons-vue";

const router = useRouter()
const route = useRoute()

// 响应式屏幕宽度检测
const windowWidth = ref(window.innerWidth);
const isMobile = computed(() => windowWidth.value < 768);
const showMobileMenu = ref(false);

const updateWindowWidth = () => {
  windowWidth.value = window.innerWidth;
  if (windowWidth.value >= 768) {
    showMobileMenu.value = false;
  }
};

onMounted(() => {
  window.addEventListener('resize', updateWindowWidth);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateWindowWidth);
});

// 用户信息
const userInfo = ref({
  nickname: '',
  avatar: ''
});

// 获取用户信息的方法
const fetchUserInfo = async () => {
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

const handleSelect = (key) => {
  router.push(key)
  showMobileMenu.value = false;
}

const handleSearch = (searchText) => {
  console.log('搜索:', searchText)
}

const handleLogoClick = () => {
  router.push({
    name: 'StudyPage'
  });
};

const handleLogin = () => {
  router.push({
    path: '/sign/email',
    query: {
      redirect: route.fullPath
    }
  });
};

// 获取用户session pinia实例
const userSession = useSessionStore();

// 获取用于 管理 topMenu的 pinia实例
const topMenuStore = useTopMenuStore();

watch(() => userSession.userSession, async (newValue) => {
  if (newValue === null) {
    topMenuStore.isLogin = false;
    return
  } else {
    topMenuStore.isLogin = true;
  }
  const status = await fetchUserInfo()
  topMenuStore.isLogin = (status === 200);
}, {immediate: true})

// 根据昵称生成头像显示文本
const getAvatarText = (nickname) => {
  if (!nickname) return '';
  return nickname.charAt(0).toUpperCase();
};

const toggleMobileMenu = () => {
  showMobileMenu.value = !showMobileMenu.value;
};
</script>

<template>
  <header class="navbar">
    <!-- 顶部导航栏 -->
    <div class="navbar-container">
      <!-- 移动端菜单按钮 -->
      <button v-if="isMobile" class="mobile-menu-button" @click="toggleMobileMenu">
        <el-icon :size="20">
          <Menu/>
        </el-icon>
      </button>

      <!-- Logo -->
      <div class="logo-container" @click="handleLogoClick">
        <img src="/HomePage/logo.jpg" alt="Logo" class="logo-img">
      </div>

      <!-- 桌面导航菜单 -->
      <nav v-if="!isMobile" class="desktop-nav">
        <ul class="nav-list">
          <li
              v-for="item in [
              { path: '/study', name: '学习' },
              { path: '/questionbank', name: '题库' },
              { path: '/contest', name: '竞赛' },
              { path: '/discussion', name: '讨论' },
              { path: '/interview', name: '面试分享' }
            ]"
              :key="item.path"
              :class="['nav-item', { 'active': route.path === item.path }]"
              @click="handleSelect(item.path)"
          >
            {{ item.name }}
          </li>
        </ul>
      </nav>

      <!-- 右侧功能区 -->
      <div class="navbar-right">
        <!-- 搜索框 - 桌面端显示 -->
        <SearchBox v-if="!isMobile" @search="handleSearch"/>

        <!-- 用户操作区 -->
        <div v-if="topMenuStore.isLogin" class="user-section">
          <!-- 消息通知 -->
          <button class="icon-button">
            <el-icon :size="isMobile ? 20 : 18">
              <Bell/>
            </el-icon>
          </button>

          <!-- 头像个人信息 -->
          <el-popover
              placement="bottom"
              trigger="click"
              popper-class="custom-avatar-popover"
              :width="isMobile ? 280 : 300"
              :hide-after="0"
          >
            <UserUtil :userInfo="userInfo"/>
            <template #reference>
              <div class="avatar-container">
                <el-avatar
                    size="small"
                    :src="userInfo.avatar"
                    v-if="userInfo.avatar"
                />
                <el-avatar
                    size="small"
                    v-else
                >{{ getAvatarText(userInfo.nickname) }}
                </el-avatar>
              </div>
            </template>
          </el-popover>

          <button class="vip-button">
            <span v-if="!isMobile">Sloth会员</span>
            <span v-else>会员</span>
          </button>
        </div>

        <div v-else class="user-actions">
          <button v-if="!isMobile" class="login-button" @click="handleLogin">登录</button>
          <button class="vip-button">
            <span v-if="!isMobile">Sloth会员</span>
            <span v-else>会员</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 移动端菜单 -->
    <transition name="slide-down">
      <div v-if="isMobile && showMobileMenu" class="mobile-menu">
        <div class="mobile-menu-content">
          <ul class="mobile-nav-list">
            <li
                v-for="item in [
                { path: '/', name: '学习' },
                { path: '/questionbank', name: '题库' },
                { path: '/contest', name: '竞赛' },
                { path: '/discussion', name: '讨论' },
                { path: '/interview', name: '面试分享' }
              ]"
                :key="item.path"
                :class="['mobile-nav-item', { 'active': route.path === item.path }]"
                @click="handleSelect(item.path)"
            >
              {{ item.name }}
            </li>
          </ul>
          <div class="mobile-search-container">
            <SearchBox @search="handleSearch"/>
          </div>
          <button v-if="!topMenuStore.isLogin" class="mobile-login-button" @click="handleLogin">登录</button>
        </div>
      </div>
    </transition>
  </header>
</template>

<style scoped>
/* 基础样式 */
.navbar {
  width: 100%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.navbar-container {
  display: flex;
  align-items: center;
  height: 52px;
  max-width: 1380px;
  margin: 0 auto;
  box-sizing: border-box;
}

/* Logo 样式 */
.logo-container {
  display: flex;
  align-items: center;

  cursor: pointer;
  margin-right: 32px;
}

.logo-img {
  height: 24px;
  object-fit: contain;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  letter-spacing: -0.5px;
}

/* 桌面导航菜单 */
.desktop-nav {
  flex-grow: 1;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 2px;
}

.nav-item {
  padding: 6px 14px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s ease;
  font-weight: 500;
}

.nav-item:hover {
  background-color: #f5f5f5;
  color: #000;
}

.nav-item.active {
  color: #000;
  font-weight: 600;
  background-color: #f0f0f0;
}

/* 右侧功能区 */
.navbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-left: auto;
}

.user-section, .user-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 按钮样式 */
.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.icon-button:hover {
  background-color: #f5f5f5;
}

.login-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px 12px;
  color: #333;
  font-size: 14px;
  font-weight: 500;
  border-radius: 4px;
  transition: all 0.2s;
}

.login-button:hover {
  color: #000;
  background-color: #f5f5f5;
}

.vip-button {
  background-color: #1a1a1a;
  color: white;
  border: none;
  cursor: pointer;
  padding: 6px 14px;
  font-size: 13px;
  font-weight: 500;
  border-radius: 4px;
  transition: all 0.2s;
  white-space: nowrap;
}

.vip-button:hover {
  background-color: #333;
}

/* 头像容器 */
.avatar-container {
  cursor: pointer;
  transition: transform 0.2s;
}

.avatar-container:hover {
  transform: scale(1.05);
}

/* 移动端菜单按钮 */
.mobile-menu-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333;
}

/* 移动端菜单 */
.mobile-menu {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 999;
}

.mobile-menu-content {
  padding: 16px 0;
}

.mobile-nav-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.mobile-nav-item {
  padding: 14px 24px;
  font-size: 15px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 500;
}

.mobile-nav-item:hover {
  background-color: #f5f5f5;
}

.mobile-nav-item.active {
  color: #000;
  font-weight: 600;
  background-color: #f0f0f0;
}

.mobile-search-container {
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.mobile-login-button {
  width: 100%;
  padding: 14px 24px;
  text-align: left;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 15px;
  color: #333;
  font-weight: 500;
  transition: all 0.2s;
}

.mobile-login-button:hover {
  background-color: #f5f5f5;
}

/* 动画效果 */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.3s ease;
}

.slide-down-enter-from,
.slide-down-leave-to {
  transform: translateY(-20px);
  opacity: 0;
}

/* 响应式调整 */
/* 调整移动端样式 */
@media (max-width: 768px) {
  .navbar-container {
    height: 48px; /* 从56px缩小到48px */
    padding: 0 12px; /* 从16px缩小到12px */
  }

  .logo-text {
    font-size: 15px; /* 从16px缩小到15px */
  }

  .vip-button {
    padding: 5px 10px; /* 从8px 12px缩小 */
    font-size: 12px; /* 从13px缩小 */
  }
}


@media (max-width: 480px) {
  .logo-text {
    display: none;
  }

  .mobile-menu-button {
    margin-right: 8px;
  }
}
</style>

<style>
/* 全局样式 */
.custom-avatar-popover {
  border-radius: 12px !important;
  padding: 16px !important;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1) !important;
  border: 1px solid #f0f0f0 !important;
}

.custom-avatar-popover .el-popper__arrow {
  display: none !important;
}
</style>