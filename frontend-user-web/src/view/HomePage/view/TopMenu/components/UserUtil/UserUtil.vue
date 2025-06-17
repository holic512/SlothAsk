<script setup lang="ts">
import {computed, defineEmits, defineProps} from 'vue';
import {useRouter} from 'vue-router';
// 引入Element Plus图标组件
import {
  Collection,
  DataAnalysis,
  Document,
  House,
  Setting,
  Star,
  SwitchButton,
  Timer,
  User,
  Warning
} from '@element-plus/icons-vue';
import {useSessionStore} from "@/pinia/Session";
import {useUserProfileStore} from "@/pinia/UserProfile";

const router = useRouter();
const userSession = useSessionStore();
const userProfileStore = useUserProfileStore();

// 定义事件
const emit = defineEmits(['closePopover']);

// 接收父组件传递的用户信息
const props = defineProps({
  userInfo: {
    type: Object,
    default: () => ({
      nickname: '用户名称',
      avatar: ''
    })
  }
});

// 根据昵称生成头像显示文本
const getAvatarText = computed(() => {
  if (!props.userInfo.nickname) return '';
  return props.userInfo.nickname.charAt(0).toUpperCase();
});

// 工具栏项目 - 使用导入的Element Plus图标组件
const toolItems = [
  { name: '历史记录', icon: Document, color: '#67C23A', path: '/account/history' },  // 绿色
  { name: '收藏夹', icon: Star, color: '#E6A23C', path: '/questionbank/myFavoritesQuestion' },    // 黄色
  { name: '错题本', icon: Warning, color: '#F56C6C', path: '/account/wrong-questions' }, // 红色
  { name: '进展分析', icon: DataAnalysis, color: '#409EFF', path: '/account/progress' }, // 蓝色
  { name: '答题记录', icon: Timer, color: '#9370DB', path: '/account/record' },  // 紫色
  { name: '我的题库', icon: Collection, color: '#20B2AA', path: '/account/my-bank' } // 青绿色
];

// 设置项目  功能未开发 先隐藏
const settingItems = [
  { name: '我的主页', icon: House, path: 'myProfile' },
  { name: '个人资料', icon: User, path: '/account/profile' },
  // { name: '显示项目', icon: Link, path: '/account/projects' },
  { name: '账号设置', icon: Setting, path: '/account/settings' },
  // { name: '外观', icon: Brush, path: '/account/appearance' },
  { name: '退出登录', icon: SwitchButton, path: 'logout' }
];

// 点击工具项的处理函数
const handleToolClick = (path: string) => {
  router.push(path);
  emit('closePopover'); // 触发关闭弹窗事件
};

// 点击设置项的处理函数
const handleSettingClick = (path: string) => {
  if (path === 'logout') {
    // 处理退出登录逻辑
    userSession.clearSession();
    console.log('退出登录');
    // 刷新页面
    window.location.reload();
  } else if (path === 'myProfile') {
    // 跳转到我的主页
    const username = userProfileStore.getUserProfile().username;
    if (username) {
      router.push(`/userProfile/${username}`);
    }
  } else {
    router.push(path);
  }
  emit('closePopover'); // 触发关闭弹窗事件
};
</script>

<template>
  <div class="user-util-container">
    <!-- 第一个div：用户信息 -->
    <div class="user-info">
      <div class="avatar-container">
        <img v-if="props.userInfo.avatar" :src="props.userInfo.avatar" alt="用户头像" class="avatar">
        <div v-else class="avatar-text">{{ getAvatarText }}</div>
      </div>
      <div class="user-details">
        <el-text size="large">{{ props.userInfo.nickname }}</el-text>
        <div class="vip-level">VIP 1</div>
      </div>
    </div>

    <!-- 第二个div：工具栏 -->
    <div class="tools-grid">
      <div 
        v-for="tool in toolItems" 
        :key="tool.name"
        class="tool-item"
        :style="{ '--tool-color': tool.color }"
        @click="handleToolClick(tool.path)"
      >
        <div class="icon-container">
          <el-icon :size="24" :color="tool.color"><component :is="tool.icon" /></el-icon>
        </div>
        <span>{{ tool.name }}</span>
      </div>
    </div>

    <!-- 第三个div：设置菜单 -->
    <div class="settings-menu">
      <div 
        v-for="setting in settingItems" 
        :key="setting.name"
        class="setting-item"
        @click="handleSettingClick(setting.path)"
      >
        <el-icon :size="16"><component :is="setting.icon" /></el-icon>
        <span>{{ setting.name }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-util-container {
  width: 100%;
  background-color: #ffffff;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 用户信息样式 */
.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.avatar-container {
  margin-right: 12px;
  position: relative;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f2f6fc;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.avatar-text {
  width: 50px;
  height: 46px;
  border-radius: 50%;
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  border: 2px solid #f2f6fc;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.vip-level {
  font-size: 13px;
  font-weight: 500;
  background: linear-gradient(45deg, #FFD700, #DAA520);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  padding: 2px 0;
  text-shadow: 0 0 2px rgba(218, 165, 32, 0.2);
}

/* 工具栏样式 */
.tools-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 12px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.tool-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 8px;
  aspect-ratio: 1 / 1; /* 确保宽高相等 */
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
  padding: 16px 8px;
}

.tool-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background-color: var(--tool-color, #409EFF);
  opacity: 0.7;
  transition: all 0.3s;
}

.tool-item:hover {
  background-color: #edf2fc;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.tool-item:hover::before {
  height: 5px;
  opacity: 1;
}

.icon-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  margin-bottom: 8px;
}

/* 设置菜单样式 */
.settings-menu {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.setting-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background-color: transparent;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.setting-item:hover {
  background-color: #f5f7fa;
}

.setting-item span {
  font-size: 13px;
  color: #606266;
  margin-left: 8px;
}
</style>