<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { setTitle } from '@/utils/title';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import {
  User,
  List,
  Star,
  Warning,
  DataLine,
  Timer,
  Collection,
  Folder,
  Setting,
  Brush,
  SwitchButton
} from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();

const userInfo = ref({
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  nickname: '测试用户'
});

// 学习相关
const studyMenuItems = [
  { path: '/account/problem-list', name: '题单', icon: List },
  { path: '/account/favorites', name: '收藏夹', icon: Star },
  { path: '/account/wrong-questions', name: '错题本', icon: Warning },
  { path: '/account/progress', name: '进展分析', icon: DataLine },
  { path: '/account/history', name: '答题记录', icon: Timer },
  { path: '/account/my-bank', name: '我的题库', icon: Collection },
];

// 项目相关
const projectMenuItems = [
  { path: '/account/projects', name: '显示项目', icon: Folder },
];

// 设置相关
const settingMenuItems = [
  { path: '/account', name: '个人资料', icon: User },
  { path: '/account/settings', name: '账号设置', icon: Setting },
  { path: '/account/appearance', name: '外观设置', icon: Brush },
];

const handleLogout = () => {
  ElMessage.success('退出登录成功');
  // 这里添加退出登录的逻辑
};

onMounted(() => {
  setTitle('账户');
});
</script>

<template>
  <div class="account-contain">
    <div class="account-layout">
      <!-- 左侧导航 -->
      <div class="account-sidebar">
        <div class="user-info">
          <el-avatar :size="64" :src="userInfo.avatar" />
          <div class="nickname">{{ userInfo.nickname }}</div>
        </div>
        <el-menu
          :default-active="route.path"
          class="account-menu"
          router
        >
          <!-- 学习相关 -->
          <div class="menu-group">
            <div class="menu-group-title">学习相关</div>
            <el-menu-item v-for="item in studyMenuItems" :key="item.path" :index="item.path">
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>

          <el-divider />

          <!-- 项目相关 -->
          <div class="menu-group">
            <div class="menu-group-title">项目相关</div>
            <el-menu-item v-for="item in projectMenuItems" :key="item.path" :index="item.path">
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>

          <el-divider />

          <!-- 设置相关 -->
          <div class="menu-group">
            <div class="menu-group-title">设置相关</div>
            <el-menu-item v-for="item in settingMenuItems" :key="item.path" :index="item.path">
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>
        </el-menu>
        <div class="logout-btn" @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </div>
      </div>
      
      <!-- 右侧内容区 -->
      <div class="account-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<style scoped>
.account-contain {
  width: 100%;
  min-height: calc(100vh - 60px);
  background-color: #F7F8FA;
  padding: 20px;
}

.account-layout {
  display: flex;
  gap: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.account-sidebar {
  width: 240px;
  background: transparent;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px 0;
}

.nickname {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.account-menu {
  border: none;
  background: transparent;
}

.menu-group {
  margin-bottom: 8px;
}

.menu-group-title {
  padding: 0 20px;
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.account-menu :deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  color: #666;
  border-radius: 8px;
  margin-bottom: 4px;
}

.account-menu :deep(.el-menu-item.is-active) {
  background-color: #ecf5ff;
  color: #409eff;
}

.account-menu :deep(.el-menu-item:hover) {
  background-color: #f5f7fa;
}

.account-menu :deep(.el-divider) {
  margin: 12px 0;
}

.logout-btn {
  margin-top: auto;
  padding: 12px 20px;
  color: #666;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  transition: all 0.3s;
}

.logout-btn:hover {
  background-color: #f5f7fa;
  color: #f56c6c;
}

.account-content {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
</style>