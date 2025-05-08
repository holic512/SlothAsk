<!-- pages/FavoritePage.vue -->
<template>
  <div style="background-color:white;margin: 0;width:100%;height:100%">
    <!-- 只有登录状态才渲染内容 -->
    <div v-if="isLoggedIn" class="favorite-page">
      <!-- 左侧卡片 -->
      <el-card class="fav-card" shadow="never">
        <div class="card-header">
          <el-icon class="star-icon">
            <StarFilled/>
          </el-icon>
          <div class="title-block">
            <h3 class="title">我的收藏</h3>
            <p class="subtitle">{{ totalCount }} 题</p>
          </div>
        </div>

        <div class="card-actions">
          <el-button class="button" size="small" type="primary">
            <el-icon size="16" style="margin-right: 4px">
              <CaretRight/>
            </el-icon>
            开始练习
          </el-button>
        </div>
        <el-divider/>

        <div>
          <el-text>进度</el-text>
        </div>
        <div class="card-progress">
          <el-progress
              :format="(p) => `${answeredCount}/${totalCount}`"
              :percentage="totalCount === 0 ? 0 : (answeredCount / totalCount * 100)"
              type="circle"
              width="120"
          />
          <p class="progress-sub">0 尝试中</p>
        </div>
      </el-card>

      <!-- 右侧内容区 -->
      <div class="fav-content">
        <div class="content-header">
          <el-input
              v-model="search"
              clearable
              placeholder="搜索题目"
              prefix-icon="Search"
              size="small"
              @clear="fetchFavorites"
              @keyup.enter="searchQuestions"
          />
        </div>

        <div class="content-body">
          <div v-if="loading" class="loading-state">
            <el-skeleton :rows="5" animated style="width: 100%"/>
          </div>

          <div v-else-if="items.length === 0" class="empty-state">
            <el-empty description="暂无收藏题目"/>
          </div>

          <!-- 有题目时显示题目列表 -->
          <div v-else class="question-list">
            <div v-for="(item, index) in items" :key="index" class="question-item">
              <div class="question-header">
                <h4 class="question-title">{{ item.title }}</h4>
                <span class="question-time">{{ formatDate(item.createTime) }}</span>
              </div>
              <div class="question-actions">
                <el-button plain size="small" type="primary" @click="goToQuestion(item.virtualId)">查看题目</el-button>
                <el-button plain size="small" type="danger" @click="unfavoriteQuestion(item.virtualId)">取消收藏
                </el-button>
              </div>
            </div>

            <!-- 分页 -->
            <div class="pagination-container">
              <el-pagination
                  :current-page="currentPage"
                  :page-size="pageSize"
                  :total="totalCount"
                  background
                  layout="prev, pager, next"
                  @current-change="handlePageChange"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 用户未登录遮罩 -->
    <div v-if="!isLoggedIn" class="login-overlay">
      <div class="login-card">
        <el-icon class="login-icon"><User /></el-icon>
        <h2>您还未登录</h2>
        <p>登录后才能查看收藏的题目</p>
        <el-button type="primary" @click="handleLogin">登录 / 注册</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {CaretRight, StarFilled, User} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import axios from '@/axios/axios.js'
import {useSessionStore} from "@/pinia/Session";

const router = useRouter()
const route = useRoute()
const userSession = useSessionStore()
const totalCount = ref(0)
const answeredCount = ref(0)
const search = ref('')
const items = ref<any[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

// 侧边栏状态 - 获取父组件的状态
// 通过父级查找QuestionBankPage组件中的isDrawerMode和collapsed状态
const parentSidebarCollapsed = ref(false);
const parentIsDrawerMode = ref(false);

// 尝试获取父组件状态
const updateParentSidebarState = () => {
  // 在这里可以添加获取父组件状态的逻辑
  // 由于Vue组件之间的隔离性，这里我们使用一种简化的方法：观察DOM状态
  const sidebarEl = document.querySelector('.sidebar');
  if (sidebarEl) {
    parentSidebarCollapsed.value = !sidebarEl.classList.contains('drawer-mode') || 
                                  window.getComputedStyle(sidebarEl).display === 'none';
    parentIsDrawerMode.value = sidebarEl.classList.contains('drawer-mode');
  }
};

// 监听路由变化时更新侧边栏状态
watch(() => route.path, () => {
  setTimeout(updateParentSidebarState, 100);
});

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

// 获取收藏列表
const fetchFavorites = async (page = 1) => {
  if (!isLoggedIn.value) {
    return;
  }
  
  loading.value = true
  try {
    const response = await axios.get('service-question/user/favQuestion/list', {
      params: {
        page: page,
        pageSize: pageSize.value,
        title: search.value || null
      },
      headers: {
        'X-User-Id': localStorage.getItem('userId') || '0'
      }
    })

    if (response.data.status === 200) {
      items.value = response.data.data.list || []
      totalCount.value = response.data.data.totalCount || 0
      currentPage.value = page
    } else {
      ElMessage.error('获取收藏列表失败')
    }
  } catch (error) {
    console.error('获取收藏列表出错：', error)
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索题目
const searchQuestions = () => {
  if (!isLoggedIn.value) {
    return;
  }
  currentPage.value = 1
  fetchFavorites(1)
}

// 处理分页变化
const handlePageChange = (page: number) => {
  fetchFavorites(page)
}

// 跳转到题目详情
const goToQuestion = (virtualId: string) => {
  router.push(`/question/${virtualId}`)
}

// 取消收藏
const unfavoriteQuestion = async (virtualId: string) => {
  try {
    const response = await axios.post('service-question/user/favQuestion/removeFav', null, {
      params: {
        virtualId: virtualId
      },
      headers: {
        'X-User-Id': localStorage.getItem('userId') || '0'
      }
    })

    if (response.data.status === 200) {
      ElMessage.success('已取消收藏')
      await fetchFavorites(currentPage.value) // 刷新当前页面
    } else {
      ElMessage.error('取消收藏失败')
    }
  } catch (error) {
    console.error('取消收藏出错：', error)
    ElMessage.error('取消收藏失败')
  }
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 页面加载时获取收藏列表并更新侧边栏状态
onMounted(() => {
  if (isLoggedIn.value) {
    fetchFavorites();
  }
  updateParentSidebarState();
  
  // 添加窗口大小变化监听，更新侧边栏状态
  window.addEventListener('resize', updateParentSidebarState);
  // 添加一个MutationObserver来监听DOM变化
  const observer = new MutationObserver(updateParentSidebarState);
  const targetNode = document.querySelector('.question-bank-container');
  if (targetNode) {
    observer.observe(targetNode, { childList: true, subtree: true, attributes: true });
  }
  
  return () => {
    window.removeEventListener('resize', updateParentSidebarState);
    observer.disconnect();
  };
})
</script>

<style lang="scss" scoped>
.favorite-page {
  display: flex;
  gap: 24px;
  padding: 36px;
  position: relative;

  .fav-card {
    width: 360px;
    display: flex;
    flex-direction: column;
    border-radius: 8px;

    .card-header {
      display: flex;
      flex-direction: column;
      margin-bottom: 16px;

      .star-icon {
        background-color: rgb(245, 245, 245);
        padding: 8px;
        border-radius: 8px;
        font-size: 64px;
        color: rgb(255, 172, 51);
        margin-right: 12px;
      }

      .title-block {
        .title {
          font-size: 32px;
          font-weight: 600;
          margin: 8px 0;
        }

        .subtitle {
          margin: 4px 0 0;
          color: #888;
          font-size: 12px;
        }
      }
    }

    .card-actions {
      width: 100%;
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 24px;

      .button {
        width: 120px;
        height: 36px;
        border-radius: 24px;
        font-size: 14px;
      }
    }

    .card-progress {
      display: flex;
      flex-direction: column;
      align-items: center;

      .el-progress {
        margin-bottom: 8px;
      }

      .progress-sub {
        margin: 0;
        font-size: 12px;
        color: #888;
      }
    }
  }

  .fav-content {
    flex: 1;
    display: flex;
    flex-direction: column;

    .content-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 16px;
    }

    .content-body {
      flex: 1;
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      padding: 20px;

      .loading-state, .empty-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        min-height: 300px;
      }

      .question-list {
        display: flex;
        flex-direction: column;
        gap: 16px;

        .question-item {
          padding: 16px;
          border: 1px solid #eaeaea;
          border-radius: 8px;
          background-color: #f9f9f9;

          .question-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;

            .question-title {
              font-size: 16px;
              margin: 0;
            }

            .question-time {
              font-size: 12px;
              color: #999;
            }
          }

          .question-actions {
            display: flex;
            gap: 12px;
            justify-content: flex-end;
          }
        }

        .pagination-container {
          margin-top: 20px;
          display: flex;
          justify-content: center;
        }
      }
    }
  }
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
</style>
