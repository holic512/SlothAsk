<script setup>
import {useRoute, useRouter} from 'vue-router';
import {computed, onMounted, onUnmounted, ref, watch} from 'vue';
import {useTopMenuStore} from "@/view/HomePage/view/TopMenu/pinia/topMenuStore";
import {getUserNameAndAvatar} from "@/view/HomePage/view/TopMenu/Api/ApiUserInfo";
import {useSessionStore} from "@/pinia/Session";
import UserUtil from "@/view/HomePage/view/TopMenu/components/UserUtil/UserUtil.vue";
import SearchBox from "@/view/HomePage/view/TopMenu/components/SearchBox/SearchBox.vue";
import {ArrowRight, Bell, Menu, QuestionFilled, Reading, User} from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();

// 菜单配置项
const menuItems = ref([
  {path: '/study', name: '学习', icon: Reading},
  {path: '/questionbank', name: '题库', icon: QuestionFilled},
  // { path: '/contest', name: '竞赛' },
  // { path: '/discussion', name: '讨论' },
  // { path: '/interview', name: '面试分享' }
]);

// 响应式屏幕宽度检测
const windowWidth = ref(window.innerWidth);
const isMobile = computed(() => windowWidth.value < 800);
const showMobileMenu = ref(false);

const updateWindowWidth = () => {
  windowWidth.value = window.innerWidth;
  if (windowWidth.value >= 800) {
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
  router.push(key);
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
          <Menu />
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
              v-for="item in menuItems"
              :key="item.path"
              :class="{ 'nav-item': true, 'active': route.path === item.path }"
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
              <Bell />
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
                    style="display: flex; align-items: center; justify-content: center;margin-right: 12px"
                    :src="userInfo.avatar"
                    v-if="userInfo.avatar"
                />
                <el-avatar
                    size="small"
                    style="display: flex; align-items: center; justify-content: center;margin-right: 12px"
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
          <!-- 菜单导航项 -->
          <ul class="mobile-nav-list">
            <li
                v-for="item in menuItems"
                :key="item.path"
                :class="{ 'mobile-nav-item': true, 'active': route.path === item.path }"
                @click="handleSelect(item.path)"
            >
              <div class="mobile-nav-item-content">
                <div class="mobile-nav-item-icon">
                  <el-icon :size="18">
                    <component :is="item.icon" />
                  </el-icon>
                </div>
                <span class="mobile-nav-item-text">{{ item.name }}</span>
                <el-icon class="mobile-nav-item-arrow">
                  <ArrowRight />
                </el-icon>
              </div>
            </li>
          </ul>

          <!-- 搜索框 -->
          <div class="mobile-search-container">
            <SearchBox @search="handleSearch"/>
          </div>

          <!-- 登录按钮 -->
          <div v-if="!topMenuStore.isLogin" class="mobile-login-section">
            <button class="mobile-login-button" @click="handleLogin">
              <div class="mobile-login-content">
                <el-icon :size="18">
                  <User />
                </el-icon>
                <span>登录 / 注册账号</span>
              </div>
            </button>
          </div>

          <!-- 会员按钮 -->
          <div class="mobile-vip-section">
            <button class="mobile-vip-button">
              <span>开通Sloth会员，解锁全部功能</span>
            </button>
          </div>
        </div>
      </div>
    </transition>
  </header>
</template>

<style scoped>
/* 基础样式 */
.navbar {
  width: 100%;
  border-bottom: 1px solid #e0e0e0;
  position: relative;
}

.navbar-container {
  display: flex;
  align-items: center;
  height: 52px;
  padding: 0 48px; /* 添加左右内边距保持间距 */
  width: 100%;
  box-sizing: border-box;
}

/* Logo 样式 */
.logo-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-right: 48px;
}

.logo-img {
  height: 24px;
  object-fit: contain;
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
  transition: transform 0.2s ease;
}

.mobile-menu-button:active {
  transform: scale(0.92);
}

/* 移动端菜单 */
.mobile-menu {
  position: absolute;
  top: 52px;
  left: 0;
  right: 0;
  background: white;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  z-index: 999;
  width: 100%;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
  overflow: hidden;
}

.mobile-menu-content {
  padding: 8px 0 16px;
}

.mobile-nav-list {
  list-style: none;
  margin: 0;
  padding: 8px 0;
}

.mobile-nav-item {
  padding: 0;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.15s;
  font-weight: 500;
  position: relative;
}

.mobile-nav-item-content {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  transition: background-color 0.15s;
}

.mobile-nav-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #555;
}

.mobile-nav-item-text {
  flex: 1;
  color: #333;
}

.mobile-nav-item-arrow {
  font-size: 14px;
  color: #999;
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.2s ease;
}

.mobile-nav-item:hover .mobile-nav-item-content {
  background-color: #f8f8f8;
}

.mobile-nav-item:hover .mobile-nav-item-arrow {
  opacity: 1;
  transform: translateX(0);
}

.mobile-nav-item.active .mobile-nav-item-content {
  background-color: #f0f0f0;
}

.mobile-nav-item.active .mobile-nav-item-text {
  color: #000;
  font-weight: 600;
}

.mobile-nav-item.active .mobile-nav-item-icon {
  color: #333;
}

.mobile-search-container {
  padding: 16px 20px;
  margin: 8px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.mobile-login-section {
  padding: 8px 16px;
}

.mobile-login-button {
  width: 100%;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  transition: all 0.15s;
}

.mobile-login-content {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  border-radius: 8px;
  background-color: #f5f5f5;
  transition: background-color 0.15s;
}

.mobile-login-content span {
  margin-left: 10px;
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

.mobile-login-button:hover .mobile-login-content {
  background-color: #eee;
}

.mobile-vip-section {
  padding: 8px 16px;
  margin-top: 8px;
}

.mobile-vip-button {
  width: 100%;
  background-color: #1a1a1a;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  padding: 14px 16px;
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  transition: all 0.2s;
}

.mobile-vip-button:hover {
  background-color: #333;
}

/* 动画效果 */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  max-height: 800px;
}

.slide-down-enter-from,
.slide-down-leave-to {
  max-height: 0;
  opacity: 0;
  transform: translateY(-8px);
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