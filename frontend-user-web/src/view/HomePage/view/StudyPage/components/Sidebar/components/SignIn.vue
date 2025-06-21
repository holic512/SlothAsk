<script lang="ts" setup>
import {computed, onMounted, ref} from 'vue';
import {format} from 'date-fns';
import {zhCN} from 'date-fns/locale';
import {apiGetSignInStatus} from '../../../service/ApiGetSignInStatus';
import {apiPostSignIn} from '../../../service/ApiPostSignIn';
import {ElMessage} from 'element-plus';

const signedIn = ref(false);
const loading = ref(false);
const today = new Date();

const day = computed(() => {
  return format(today, 'd');
});

const date = computed(() => {
  return format(today, 'yyyy年MM月');
});

const weekday = computed(() => {
  return format(today, 'EEEE', { locale: zhCN });
});

// 查询签到状态
const checkSignInStatus = async () => {
  try {
    const response = await apiGetSignInStatus();
    if (response.status === 200) {
      signedIn.value = response.data;
    }
  } catch (error) {
    console.error('查询签到状态失败:', error);
  }
};

// 执行签到
const handleSignIn = async () => {
  if (signedIn.value || loading.value) {
    return;
  }
  
  loading.value = true;
  try {
    const response = await apiPostSignIn();
    if (response.status === 200) {
      signedIn.value = true;
      ElMessage.success(response.message || '签到成功！');
      // 添加签到成功的动画效果
      setTimeout(() => {
        // 可以在这里添加额外的成功反馈
      }, 300);
    } else {
      ElMessage.warning(response.message || '签到失败');
    }
  } catch (error) {
    console.error('签到失败:', error);
    ElMessage.error('签到失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 组件挂载时查询签到状态
onMounted(() => {
  checkSignInStatus();
});
</script>

<template>
  <div :class="{ 'signed-in': signedIn, 'loading': loading }" class="signin-card" @click="handleSignIn">
    <div class="left-content">
      <div class="date-info">
        <div class="day">{{ day }}</div>
        <div class="date">{{ date }}</div>
      </div>
      <div class="weekday">{{ weekday }}</div>
      <div :class="{ 'signed': signedIn }" class="status-text">
        {{ loading ? '签到中...' : (signedIn ? '今日已签到' : '今天还未签到') }}
      </div>
    </div>
    <div class="right-content">
      <div v-if="loading" class="loading-spinner"></div>
      <img v-else-if="signedIn" alt="Signed In" class="status-gif" src="/HomePage/StudyPage/happy.gif" />
      <img v-else alt="Not Signed In" class="status-gif" src="/HomePage/StudyPage/wait.gif" />
    </div>
  </div>
</template>

<style scoped>
.signin-card {
  height: 150px;
  padding: 12px 20px;
  border-radius: 12px;
  background: linear-gradient(135deg, #fefcfa 0%, #fdf8f3 100%);
  border: 1px solid #f0dcc9;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  font-family: 'ZCOOL XiaoWei', sans-serif;
  display: flex;
  justify-content: space-between;
  align-items: center;

  position: relative;
  overflow: hidden;
}

.signin-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(248, 203, 170, 0.15), transparent);
  transition: left 0.6s ease;
  z-index: 1;
  pointer-events: none;
}

.signin-card:not(.signed-in):hover::before {
  left: 100%;
}

.signin-card:not(.signed-in):hover {
  box-shadow: 0 8px 25px rgba(248, 203, 170, 0.2);
  border-color: #F8CBAA;
  background: linear-gradient(135deg, #fefcfa 0%, #fcf4ed 100%);
}

.signin-card.signed-in {
  background: linear-gradient(135deg, #fdf6f0 0%, #F8CBAA 100%);
  border-color: #e6b896;
  cursor: default;
  box-shadow: 0 4px 15px rgba(248, 203, 170, 0.3);
  animation: signInSuccess 0.6s ease-out;
}

@keyframes signInSuccess {
  0% {
    transform: scale(1);
    box-shadow: 0 4px 15px rgba(248, 203, 170, 0.3);
  }
  50% {
    transform: scale(1.02);
    box-shadow: 0 8px 30px rgba(248, 203, 170, 0.5);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 4px 15px rgba(248, 203, 170, 0.3);
  }
}

.left-content {
  flex: 1;
  position: relative;
  z-index: 2;
}

.date-info {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 8px;
}

.day {
  font-size: 3rem;
  font-weight: bold;
  color: #2c3e50;
}

.date {
  font-size: 1rem;
  color: #7f8c8d;
}

.weekday {
  font-size: 1.2rem;
  color: #868e96;
  margin-bottom: 8px;
}

.status-text {
  font-size: 0.9rem;
  color: #6c757d;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0.8;
  transform: translateY(0);
}

.status-text.signed {
  color: #d4965a;
  font-weight: 500;
  opacity: 1;
  animation: statusChange 0.5s ease-out;
}

@keyframes statusChange {
  0% {
    opacity: 0;
    transform: translateY(10px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.right-content {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 2;
}

.status-gif {
  width: 100%;
  height: 100%;
  object-fit: contain;
  opacity: 0.8;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.signin-card.signed-in .status-gif {
  opacity: 1;
  filter: brightness(1.1);
}

.signin-card.loading {
  cursor: wait;
  pointer-events: none;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #F8CBAA;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>