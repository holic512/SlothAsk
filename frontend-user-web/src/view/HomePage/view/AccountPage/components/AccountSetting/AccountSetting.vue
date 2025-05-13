<script lang="ts" setup>
/**
 * 账户设置组件
 * 已集成后端API：
 * - 获取用户账户信息（用户名和邮箱）
 * - 更新用户名
 * - 更新邮箱（包含验证码验证）
 *
 * 待后端实现的API：
 * - 修改密码相关接口
 */
import {computed, onMounted, ref} from 'vue';
import {ElMessage} from 'element-plus';
import {useRoute, useRouter} from 'vue-router';
import {getUserAccount} from './service/ApiGetUserAccount';
import {updateUsername} from './service/ApiUpdateUserAccount';
import {useSessionStore} from "@/pinia/Session";
import {User} from '@element-plus/icons-vue';

// 导入子组件
import UsernameEditor from './components/UsernameEditor.vue';
import EmailEditor from './components/EmailEditor.vue';
import PasswordEditor from './components/PasswordEditor.vue';
import ThirdPartyBinder from './components/ThirdPartyBinder.vue';

const router = useRouter();
const route = useRoute();
const userSession = useSessionStore();

// 判断用户是否登录
const isLoggedIn = computed(() => {
  return userSession.userSession && userSession.userSession.tokenValue;
});

// 处理登录跳转
const handleLogin = () => {
  router.push({
    path: '/sign/email',
    query: {
      redirect: route.fullPath
    }
  });
};

// 用户账户信息
const username = ref('');
const email = ref('');
const remainingUsernameChanges = ref(3);

// 获取用户账户信息
const fetchUserAccount = async () => {
  if (!isLoggedIn.value) return;
  
  try {
    const response = await getUserAccount();
    if (response.status === 200) {
      username.value = response.data.username;
      email.value = response.data.email;
      // 获取剩余修改次数
      remainingUsernameChanges.value = response.data.remainingUsernameChanges || 0;
    } else {
      ElMessage.error('获取用户信息失败');
    }
  } catch (error) {
    console.error('获取用户信息出错:', error);
    ElMessage.error('获取用户信息出错');
  }
};

// 组件挂载时获取用户信息
onMounted(() => {
  if (isLoggedIn.value) {
    fetchUserAccount();
  }
});

// 处理用户名更新
const handleUsernameUpdate = async (newUsername) => {
  if (!newUsername) {
    ElMessage.warning('用户名不能为空');
    return;
  }
  
  if (remainingUsernameChanges.value <= 0) {
    ElMessage.warning('本月修改次数已用完');
    return;
  }
  
  try {
    const response = await updateUsername({ username: newUsername });
    if (response.status === 200 && response.data) {
      ElMessage.success('用户名修改成功');
      username.value = newUsername;
      // 更新剩余修改次数
      remainingUsernameChanges.value -= 1;
    } else {
      ElMessage.error(response.message || '用户名修改失败');
    }
  } catch (error) {
    console.error('更新用户名出错:', error);
    ElMessage.error('更新用户名出错');
  }
};
</script>

<template>
  <div class="account-settings">
    <!-- 基本信息区域 -->
    <div class="section">
      <div class="section-title">基本信息</div>
      
      <!-- 用户名组件 -->
      <UsernameEditor 
        v-model:remaining-changes="remainingUsernameChanges"
        v-model:username="username"
        @update-username="handleUsernameUpdate"
      />
      
      <!-- 邮箱组件 -->
      <EmailEditor 
        v-model:email="email"
      />
    </div>
    
    <el-divider />
    
    <!-- 密码修改区域 -->
    <div class="section">
      <div class="section-title">密码修改</div>
      <PasswordEditor 
        :email="email"
      />
    </div>
    
    <el-divider />
    
    <!-- 第三方绑定区域 -->
    <div class="section">
      <div class="section-title">第三方绑定</div>
      <ThirdPartyBinder />
    </div>
    
    <!-- 用户未登录遮罩 -->
    <div v-if="!isLoggedIn" class="login-overlay">
      <div class="login-card">
        <el-icon class="login-icon"><User /></el-icon>
        <h2>您还未登录</h2>
        <p>登录后才能访问账户设置</p>
        <el-button type="primary" @click="handleLogin">登录 / 注册</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.account-settings {
  max-width: 700px;
  margin: 0 auto;
  padding: 24px;
  background-color: white;
  position: relative;
}

.section {
  margin-bottom: 20px;
  padding: 4px 0;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 24px;
}

:deep(.el-divider--horizontal) {
  margin: 24px 0;
}

/* 登录遮罩样式 */
.login-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(8px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
  height: 100%;
  width: 100%;
  border-radius: 12px;
}

.login-card {
  background: white;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 320px;
  animation: fadeIn 0.3s ease-out;
  margin-bottom: 60px; /* 稍微向上偏移，视觉上更居中 */
}

.login-icon {
  font-size: 48px;
  color: var(--el-color-primary);
  background-color: var(--el-color-primary-light-9);
  padding: 16px;
  border-radius: 50%;
  margin-bottom: 16px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .account-settings {
    padding: 20px;
    margin: 0;
    width: 100%;
    border-radius: 0;
    box-shadow: none;
  }

  .login-overlay {
    border-radius: 0;
  }
}
</style>
