<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import {setTitle} from '@/utils/title';
import {useRouter, useRoute} from 'vue-router';
import {ElMessage} from 'element-plus';
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
import {getUserNameAndAvatar} from "@/view/HomePage/view/TopMenu/Api/ApiUserInfo";
import {useSessionStore} from "@/pinia/Session";

const router = useRouter();
const route = useRoute();

const userInfo = ref({
  avatar: '',
  nickname: ''
});

// 学习相关
const studyMenuItems = [
  {path: '/account/problem-list', name: '题单', icon: List},
  {path: '/account/favorites', name: '收藏夹', icon: Star},
  {path: '/account/wrong-questions', name: '错题本', icon: Warning},
  {path: '/account/history', name: '历史浏览', icon: Timer},
  {path: '/account/progress', name: '进展分析', icon: DataLine},
  {path: '/account/record', name: '答题记录', icon: Collection},
  {path: '/account/my-bank', name: '我的题库', icon: Folder},
];

// 项目相关
const projectMenuItems = [
  {path: '/account/projects', name: '显示项目', icon: Folder},
];

// 设置相关
const settingMenuItems = [
  {path: '/account/profile', name: '个人资料', icon: User},
  {path: '/account/settings', name: '账号设置', icon: Setting},
  {path: '/account/appearance', name: '外观设置', icon: Brush},
];

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

// 获取用户session pinia实例
const userSession = useSessionStore();

watch(() => userSession.userSession, async (newValue) => {
  // 监听 用户session是否发生改变,并修改 topMenu状态  根据这个 token 获取个人信息
  // 存在使用这个token 到后端验证是否有效  目前是 获取 用户姓名和头像
  if (newValue === null) {
    await router.push("/")
    return
  }

  await fetchUserInfo()
}, {immediate: true})


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
          <el-avatar :size="64" :src="userInfo.avatar" v-if="userInfo.avatar"/>
          <el-avatar :size="64" v-else>{{ userInfo.nickname[0] }}  </el-avatar>
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
              <el-icon>
                <component :is="item.icon"/>
              </el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>

          <el-divider/>

          <!-- 项目相关 -->
          <div class="menu-group">
            <div class="menu-group-title">项目相关</div>
            <el-menu-item v-for="item in projectMenuItems" :key="item.path" :index="item.path">
              <el-icon>
                <component :is="item.icon"/>
              </el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>

          <el-divider/>

          <!-- 设置相关 -->
          <div class="menu-group">
            <div class="menu-group-title">设置相关</div>
            <el-menu-item v-for="item in settingMenuItems" :key="item.path" :index="item.path">
              <el-icon>
                <component :is="item.icon"/>
              </el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>
        </el-menu>
        <div class="logout-btn" @click="handleLogout">
          <el-icon>
            <SwitchButton/>
          </el-icon>
          <span>退出登录</span>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="account-content">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.account-contain {
  width: 100%;
  min-height: calc(100vh - 430px);
  height: 100%; /* 确保高度继承 */
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 12px 0;
  display: flex;
  justify-content: center;
}

.account-layout {
  width: 1100px;
  display: flex;
  margin: 0 auto;
  gap: 25px;
  height: 100%; /* 继承父容器高度 */
}

.account-content {
  flex: 1;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  height: 100%; /* 继承父容器高度 */
}

.account-sidebar {
  width: 250px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 12px 8px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.25s ease;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  padding: 12px 0;
}



.nickname {
  font-size: 18px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  letter-spacing: 0.5px;
}

.account-menu {
  border: none;
  background: transparent;
}

.menu-group {
  margin-bottom: 14px;
}

.menu-group-title {
  padding: 0 20px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
  margin-bottom: 10px;
  font-weight: 500;
  letter-spacing: 0.5px;
}

.account-menu :deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  color: var(--el-text-color-regular);
  border-radius: 10px;
  margin-bottom: 2px;
  transition: all 0.25s ease;
  font-weight: 400;
}

.account-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-8) 100%);
  color: var(--el-color-primary);
  font-weight: 500;
}

.account-menu :deep(.el-menu-item:hover) {
  background-color: #f0e9e2;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.account-menu :deep(.el-divider) {
  margin: 18px 0;
  border-color: var(--el-border-color-lighter);
}

.logout-btn {
  margin-top: auto;
  padding: 12px 20px;
  color: var(--el-text-color-regular);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  border-radius: 10px;
  transition: all 0.25s ease;
}

.logout-btn:hover {
  background-color: #f0e0dd;
  color: var(--el-color-danger);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.1);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.2;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.3;
  }
  100% {
    transform: scale(1);
    opacity: 0.2;
  }
}
</style>