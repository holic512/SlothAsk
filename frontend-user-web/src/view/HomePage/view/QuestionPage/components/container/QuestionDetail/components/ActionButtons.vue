<template>
  <div class="action-buttons">
    <!-- 收藏按钮 -->
    <button
        :class="{ active: isFavorited }"
        :disabled="loading"
        class="btn"
        @click="handleStarClick"
    >
      <!-- 非加载时显示心形图标，加载中显示旋转图标 -->
      <el-icon v-if="!loading">
        <Star/>
      </el-icon>
      <svg v-else class="icon loading-icon" viewBox="0 0 50 50">
        <circle cx="25" cy="25" fill="none" r="20" stroke-width="5"/>
      </svg>
      <span>{{ loading ? '处理中' : (isLoggedIn ? '收藏' : '登录后收藏') }}</span>
    </button>

    <!-- 分享按钮 -->
    <button class="btn" @click="share">
      <svg class="icon" fill="currentColor" viewBox="0 0 20 20">
        <path
            d="M15 8a3 3 0 10-2.83-4H9a1 1 0 000 2h3.17A3.001 3.001 0 0015 8zM5 10a1 1 0 000 2h10a1 1 0 000-2H5zm10 4a3.001 3.001 0 00-2.83 2H9a1 1 0 000 2h3.17A3 3 0 1015 14z"/>
      </svg>
      <span>分享</span>
    </button>
  </div>
</template>

<script lang="ts" setup>
import {computed, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from '@/axios/axios'
import {ElMessage, ElMessageBox} from 'element-plus'
import {useSessionStore} from "@/pinia/Session";
import {Star} from "@element-plus/icons-vue";

const isFavorited = defineModel()

const loading = ref(false)

const route = useRoute()
const router = useRouter()
const userSession = useSessionStore()

// 判断用户是否登录
const isLoggedIn = computed(() => {
  return userSession.userSession && userSession.userSession.tokenValue;
});

// 显示登录提示
const showLoginTip = () => {
  ElMessageBox.confirm('登录后才能收藏题目，是否前往登录？', '提示', {
    confirmButtonText: '去登录',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    handleLogin();
  }).catch(() => {
    // 用户取消登录
  });
};

// 处理登录跳转
const handleLogin = () => {
  router.push({
    path: '/sign/email',
    query: {
      redirect: route.fullPath
    }
  });
};

// 收藏/取消收藏
const handleStarClick = async () => {
  if (loading.value) return

  // 检查用户是否登录
  if (!isLoggedIn.value) {
    showLoginTip();
    return;
  }

  loading.value = true

  const virtualId = route.params.questionId as string
  if (!virtualId) {
    loading.value = false
    return
  }

  try {
    if (isFavorited.value) {
      // 取消收藏
      const response = await axios.post('service-question/user/favQuestion/removeFav', null, {
        params: {virtualId}
      })
      if (response.data.status === 200) {
        isFavorited.value = false
        ElMessage.success('已取消收藏')
      } else {
        ElMessage.error('取消收藏失败')
      }
    } else {
      // 添加收藏
      const response = await axios.post('service-question/user/favQuestion/addFav', null, {
        params: {virtualId}
      })
      if (response.data.status === 200) {
        isFavorited.value = true
        ElMessage.success('已添加收藏')
      } else {
        ElMessage.error('收藏失败')
      }
    }
  } catch (error) {
    console.error('收藏操作出错：', error)
    ElMessage.error('操作失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const share = () => {
  if (navigator.share) {
    navigator.share({
      title: '分享标题',
      text: '分享内容描述',
      url: window.location.href,
    }).catch(err => console.error('分享失败:', err))
  } else {
    alert('当前浏览器不支持分享 API')
  }
}
</script>

<style lang="scss" scoped>
.action-buttons {
  display: flex;
  gap: 1rem;

  .btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    background-color: #fff;
    color: #333;
    border: none;
    border-radius: 1.5rem;
    padding: 4px 12px;
    cursor: pointer;
    transition: background-color 0.2s ease;

    &:hover:enabled {
      background-color: #f2f2f2;
    }

    &:disabled {
      cursor: not-allowed;
      opacity: 0.6;
    }

    .icon {
      width: 1rem;
      height: 1rem;
    }

    .loading-icon {
      stroke: currentColor;
      animation: spin 1s linear infinite;
    }

    &.active {
      color: #e63946;

      .icon {
        fill: #e63946;
      }
    }
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
