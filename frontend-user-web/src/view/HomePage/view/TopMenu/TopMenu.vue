<script setup>
import {useRoute, useRouter} from 'vue-router';
import {computed, onMounted, onUnmounted, ref, watch} from 'vue';
import {useTopMenuStore} from "@/view/HomePage/view/TopMenu/pinia/topMenuStore";
import {getUserBasicInfo} from "@/view/HomePage/view/TopMenu/Api/ApiUserInfo";
import {useSessionStore} from "@/pinia/Session";
import {useUserProfileStore} from "@/pinia/UserProfile";
import UserUtil from "@/view/HomePage/view/TopMenu/components/UserUtil/UserUtil.vue";
import BellUtil from "@/view/HomePage/view/TopMenu/components/BellUtil/index.vue";
import SearchUtil from "@/view/HomePage/view/TopMenu/components/SearchUtil/index.vue";
import VipModal from "@/view/HomePage/view/TopMenu/components/VipModal/VipModal.vue";
import {ArrowRight, Menu, QuestionFilled, Reading, User} from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();

// 控制用户工具弹出窗口的可见性
const isPopoverVisible = ref(false);

// 控制VIP弹窗的可见性
const isVipModalVisible = ref(false);

// 当前用户的VIP等级 (0: 免费, 1: 月付, 2: 永久)
const currentVipLevel = ref(0);

// 菜单配置项
const menuItems = ref([
  {path: '/intro', name: '主页', icon: Reading},
  {path: '/study', name: '学习', icon: Reading},
  {path: '/questionbank/questionbank', name: '题库', icon: QuestionFilled},
  {path: '/jobrecruit', name: '招聘', icon: User},
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

// 获取用户信息的方法
const fetchUserInfo = async () => {
  try {
    const response = await getUserBasicInfo();
    if (response.status === 200) {
      // 将用户信息保存到UserProfile store中
      userProfileStore.updateBasicInfo(
          response.data.username,
          response.data.nickname,
          response.data.avatar
      );
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

// 这个方法保留用于兼容性，实际搜索功能已移到新的handleSearch方法中
const handleSearchLegacy = (searchText) => {
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

// 获取用户个人信息 pinia实例
const userProfileStore = useUserProfileStore();

// 获取用于 管理 topMenu的 pinia实例
const topMenuStore = useTopMenuStore();

// 计算属性：从UserProfile store获取用户信息用于显示
const userInfo = computed(() => ({
  nickname: userProfileStore.userProfile.nickname || '',
  avatar: userProfileStore.userProfile.avatarUrl || ''
}));

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

// 处理VIP按钮点击
const handleVipClick = () => {
  isVipModalVisible.value = true;
};

// 关闭VIP弹窗
const handleCloseVipModal = () => {
  isVipModalVisible.value = false;
};

// 处理套餐选择
const handleSelectPlan = (planId) => {
  console.log('选择套餐:', planId);
  // 这里可以添加实际的套餐购买逻辑
  // 暂时关闭弹窗
  isVipModalVisible.value = false;
};


</script>

<template>
  <header class="navbar">
    <!-- 桌面端导航栏 -->
    <div v-if="!isMobile" class="desktop-navbar">
      <div class="desktop-navbar-container">
        <!-- 第一部分：Logo和导航菜单 -->
        <div class="navbar-left-section">
          <div class="logo-container" @click="handleLogoClick">
            <img alt="Logo" class="logo-img" src="/HomePage/logo.jpg">
          </div>
          
          <nav class="desktop-nav">
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
        </div>

        <!-- 第二部分：留白区域（自动伸缩） -->
        <div class="navbar-spacer"></div>

        <!-- 第三部分：用户功能区域 -->
        <div class="navbar-right-section">
          <!-- 搜索组件 -->
          <SearchUtil />
          <!-- 用户已登录状态 -->
          <div v-if="topMenuStore.isLogin" class="user-section">
            <!-- 消息服务 -->
            <BellUtil/>

            <!-- 头像个人信息 -->
            <el-popover
                v-model:visible="isPopoverVisible"
                :hide-after="50"
                :width="300"
                placement="bottom"
                popper-class="custom-avatar-popover"
                trigger="click"
            >
              <UserUtil :userInfo="userInfo" @closePopover="isPopoverVisible = false"/>
              <template #reference>
                <div class="avatar-container">
                  <el-avatar
                      v-if="userInfo.avatar"
                      :src="userInfo.avatar"
                      size="small"
                      style="display: flex; align-items: center; justify-content: center;"
                  />
                  <el-avatar
                      v-else
                      size="small"
                      style="display: flex; align-items: center; justify-content: center;"
                  >{{ getAvatarText(userInfo.nickname) }}
                  </el-avatar>
                </div>
              </template>
            </el-popover>

            <button class="vip-button" @click="handleVipClick">
              <span>Sloth会员</span>
            </button>
          </div>

          <!-- 用户未登录状态 -->
          <div v-else class="user-actions">
            <button class="login-button" @click="handleLogin">登录</button>
            <button class="vip-button" @click="handleVipClick">
              <span>Sloth会员</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 移动端导航栏 -->
    <div v-else class="mobile-navbar">
      <div class="mobile-navbar-container">
        <!-- 移动端菜单按钮 -->
        <button class="mobile-menu-button" @click="toggleMobileMenu">
          <el-icon :size="20">
            <Menu/>
          </el-icon>
        </button>

        <!-- Logo -->
        <div class="logo-container" @click="handleLogoClick">
          <img alt="Logo" class="logo-img" src="/HomePage/logo.jpg">
        </div>

        <!-- 移动端搜索组件 -->
        <div class="mobile-search-section">
          <SearchUtil />
        </div>

        <!-- 移动端右侧功能区 -->
        <div class="mobile-navbar-right">
          <!-- 用户已登录状态 -->
          <div v-if="topMenuStore.isLogin" class="mobile-user-section">
            <BellUtil/>
            
            <el-popover
                v-model:visible="isPopoverVisible"
                :hide-after="50"
                :width="280"
                placement="bottom"
                popper-class="custom-avatar-popover"
                trigger="click"
            >
              <UserUtil :userInfo="userInfo" @closePopover="isPopoverVisible = false"/>
              <template #reference>
                <div class="avatar-container">
                  <el-avatar
                      v-if="userInfo.avatar"
                      :src="userInfo.avatar"
                      size="small"
                      style="display: flex; align-items: center; justify-content: center;"
                  />
                  <el-avatar
                      v-else
                      size="small"
                      style="display: flex; align-items: center; justify-content: center;"
                  >{{ getAvatarText(userInfo.nickname) }}
                  </el-avatar>
                </div>
              </template>
            </el-popover>

            <button class="vip-button" @click="handleVipClick">
              <span>会员</span>
            </button>
          </div>

          <!-- 用户未登录状态 -->
          <div v-else class="mobile-user-actions">
            <button class="vip-button" @click="handleVipClick">
              <span>会员</span>
            </button>
          </div>
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
                    <component :is="item.icon"/>
                  </el-icon>
                </div>
                <span class="mobile-nav-item-text">{{ item.name }}</span>
                <el-icon class="mobile-nav-item-arrow">
                  <ArrowRight/>
                </el-icon>
              </div>
            </li>
          </ul>

          <!-- 登录按钮 -->
          <div v-if="!topMenuStore.isLogin" class="mobile-login-section">
            <button class="mobile-login-button" @click="handleLogin">
              <div class="mobile-login-content">
                <el-icon :size="18">
                  <User/>
                </el-icon>
                <span>登录 / 注册账号</span>
              </div>
            </button>
          </div>

          <!-- 会员按钮 -->
          <div class="mobile-vip-section">
            <button class="mobile-vip-button" @click="handleVipClick">
              <span>开通Sloth会员，解锁全部功能</span>
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- VIP会员弹窗 -->
    <VipModal 
      v-if="isVipModalVisible"
      :currentVipLevel="currentVipLevel"
      @close="handleCloseVipModal"
      @selectPlan="handleSelectPlan"
    />
  </header>
</template>

<style scoped>
/* ==================== 基础样式 ==================== */
.navbar {
  width: 100%;
  border-bottom: 1px solid #e0e0e0;
  position: relative;
  background: white;
}

/* ==================== 桌面端导航样式 ==================== */
.desktop-navbar {
  width: 100%;
}

.desktop-navbar-container {
  display: flex;
  align-items: center;
  height: 52px;
  max-width: 1400px; /* 最大宽度限制 */
  margin: 0 auto; /* 居中显示 */
  padding: 0 12px;
  width: 100%;
  box-sizing: border-box;
}

/* 第一部分：Logo和导航菜单 */
.navbar-left-section {
  display: flex;
  align-items: center;
  flex-shrink: 0; /* 防止压缩 */
}

.logo-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-right: 48px;
  flex-shrink: 0;
}

.logo-img {
  height: 24px;
  object-fit: contain;
}

.desktop-nav {
  display: flex;
  align-items: center;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 12px;
}

.nav-item {
  padding: 12px 12px;
  font-size: 15px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
  position: relative;
  border-bottom: 2px solid transparent;
  white-space: nowrap;
}

.nav-item:hover {
  color: #333;
}

.nav-item.active {
  color: #1a1a1a;
  font-weight: 600;
  border-bottom-color: #1a1a1a;
}

/* 第二部分：留白区域（自动伸缩） */
.navbar-spacer {
  flex: 1; /* 占据剩余空间 */
  min-width: 20px; /* 最小宽度确保不会完全消失 */
}

/* 第三部分：用户功能区域 */
.navbar-right-section {
  display: flex;
  align-items: center;
  flex-shrink: 0; /* 防止压缩 */
  gap: 16px;
}

.user-section, .user-actions {
  display: flex;
  align-items: center;
  gap: 18px;
}

/* ==================== 移动端导航样式 ==================== */
.mobile-navbar {
  width: 100%;
}

.mobile-navbar-container {
  display: flex;
  align-items: center;
  height: 52px;
  padding: 0 16px;
  width: 100%;
  box-sizing: border-box;
}

.mobile-navbar-right {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.mobile-user-section, .mobile-user-actions {
  display: flex;
  align-items: center;
  gap: 12px;
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
  flex-shrink: 0;
}

.mobile-menu-button:active {
  transform: scale(0.92);
}

/* ==================== 通用组件样式 ==================== */
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
  white-space: nowrap;
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
  flex-shrink: 0;
}

.vip-button:hover {
  background-color: #333;
}

/* 头像容器 */
.avatar-container {
  cursor: pointer;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.avatar-container:hover {
  transform: scale(1.05);
}

/* ==================== 响应式调整 ==================== */

@media (max-width: 1200px) {

  
  .logo-container {
    margin-right: 32px;
  }
  
  .nav-item {
    padding: 12px 8px;
  }
}

@media (max-width: 900px) {
  .logo-container {
    margin-right: 24px;
  }
  
  .nav-list {
    gap: 8px;
  }
  
  .user-section, .user-actions {
    gap: 12px;
  }
}

/* ==================== 移动端下拉菜单样式 ==================== */
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

/* ==================== 搜索组件样式 ==================== */
/* 移动端搜索区域 */
.mobile-search-section {
  flex: 1;
  margin: 0 12px;
  display: flex;
  justify-content: center;
}

/* ==================== 动画效果 ==================== */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.3s ease;
}

.slide-down-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.slide-down-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* ==================== 自定义Popover样式 ==================== */
.custom-avatar-popover .el-popper__arrow {
  display: none;
}
</style>