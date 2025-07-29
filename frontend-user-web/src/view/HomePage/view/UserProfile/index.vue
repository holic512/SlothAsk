<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import UserInfoComponent from './components/UserInfoComponent.vue';
import AnswersComponent from './components/CommentComponent.vue';
import CollectionsComponent from './components/CollectionsComponent.vue';
import {setTitle} from "@/utils/title";
import {CommentItemInterface} from "@/view/HomePage/view/UserProfile/interface/CommentItemInterface";
import {UserInfo} from "./interface/UserInfoInterface";
import {
  getUserCommentListApi,
  getUserFavQuestionListApi,
  getUserProfileApi
} from "@/view/HomePage/view/UserProfile/api/GetProfileApi";
import {ElMessage} from "element-plus";
import {Opportunity} from "@element-plus/icons-vue";
import {FavQuestionItemInterface} from "./interface/FavQuestionItemInterface";
import router from "@/router/router";


// 模块类型
type ModuleType = 'answers' | 'collections';

// 获取路由参数
const route = useRoute();
const username = route.params.username as string;
const showError = ref(false);

// 状态变量 - 用户信息留空，等待接口调用
const userInfo = ref<UserInfo>({
  avatar: '',
  nickname: '',
  codingAge: '',
  followingCount: 0,
  followersCount: 0,
  joinDate: '',
  bio: '',
  gender: undefined,
  age: undefined,
  isFollowed: false
});

// 回答列表数据
const answers = ref<CommentItemInterface[]>([]);

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 收藏列表数据
const collections = ref<FavQuestionItemInterface[]>([]);

// 收藏分页相关
const collectionsCurrentPage = ref(1);
const collectionsPageSize = ref(10);
const collectionsTotal = ref(0);

// 当前选中的模块
const activeModule = ref<ModuleType>('answers');

// 加载状态
const isLoading = ref<boolean>(true);
const isCollectionsLoading = ref<boolean>(false);

// 切换模块
const switchModule = (module: ModuleType) => {
  activeModule.value = module;
  // 切换到收藏模块时，如果还没有加载过收藏数据，则加载
  if (module === 'collections' && collections.value.length === 0 && !isCollectionsLoading.value) {
    getFavQuestionList(collectionsCurrentPage.value);
  }
};

// 编辑资料按钮点击事件
const handleEditProfile = () => {
  router.push('/account/profile');
};

// 获取用户信息
const getUserProfile = async () => {
  const response = await getUserProfileApi(username);
  if (response.status === 200) {
    userInfo.value = response.data as UserInfo;
  } else if (response.status === 404) {
    ElMessage.error(response.message);
  }
}

// 获取用户评论列表
const getCommentList = async (page = 1) => {
  isLoading.value = true;
  try {
    const response = await getUserCommentListApi(username, page, pageSize.value);
    if (response.status === 200) {
      answers.value = response.data.records.map((item: any) => ({
        questionTUid: item.questionTUid,
        questionTitle: item.questionTitle,
        content: item.content,
        likes: item.likes,
        createdAt: item.createdAt
      }));
      total.value = response.data.total || 0;
    } else {
      ElMessage.error(response.message || '获取评论列表失败');
    }
  } catch (error) {
    ElMessage.error('获取评论列表失败');
  } finally {
    isLoading.value = false;
  }
};

// 获取用户收藏问题列表
const getFavQuestionList = async (page = 1) => {
  isCollectionsLoading.value = true;
  try {
    const response = await getUserFavQuestionListApi(username, page, collectionsPageSize.value);
    if (response.status === 200) {
      collections.value = response.data.records.map((item: any) => ({
        questionId: item.questionId,
        questionTitle: item.questionTitle,
        tags: item.tags || [],
        views: item.views,
        savedAt: item.savedAt
      }));
      collectionsTotal.value = response.data.total || 0;
    } else {
      ElMessage.error(response.message || '获取收藏列表失败');
    }
  } catch (error) {
    ElMessage.error('获取收藏列表失败');
  } finally {
    isCollectionsLoading.value = false;
  }
};
// 分页切换事件
const handlePageChange = (page: number) => {
  currentPage.value = page;
  getCommentList(page);
};

// 收藏分页切换事件
const handleCollectionsPageChange = (page: number) => {
  collectionsCurrentPage.value = page;
  getFavQuestionList(page);
};

// 模拟接口加载数据
onMounted(() => {
  // 设置title
  setTitle('用户主页');

  // 检查username参数是否存在
  if (!username) {
    showError.value = true;
    isLoading.value = false;
    return;
  }

  // 获取用户信息
  getUserProfile();

  // 获取评论列表
  getCommentList(currentPage.value);

  // 初始化时不加载收藏数据，等用户切换到收藏模块时再加载
});
</script>

<template>
  <div class="user-profile-page">
    <!-- 毛玻璃错误提示层 -->
    <div v-if="showError" class="glass-overlay">
      <div class="glass-message">
        <div class="glass-icon">
          <svg fill="none" height="64" viewBox="0 0 24 24" width="64" xmlns="http://www.w3.org/2000/svg">
            <path
                d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM13 17H11V15H13V17ZM13 13H11V7H13V13Z"
                fill="white"/>
          </svg>
        </div>
        <h2>缺少用户信息</h2>
        <p>无法加载用户资料，请检查请求参数是否正确</p>
      </div>
    </div>

    <div class="container">
      <!-- 主要内容卡片 -->
      <div class="main-card">
        <!-- 用户信息组件 -->
        <UserInfoComponent
            :user-info="userInfo"
            @edit-profile="handleEditProfile"
        />

        <!-- 内容模块切换 -->
        <div class="module-tabs-container">
          <div class="tabs-wrapper">
            <button
                :class="{ active: activeModule === 'answers' }"
                class="tab-btn"
                @click="switchModule('answers')"
            >
              回答
            </button>
            <button
                :class="{ active: activeModule === 'collections' }"
                class="tab-btn"
                @click="switchModule('collections')"
            >
              收藏
            </button>
          </div>
          <el-tooltip
              :show-after="500"
              content="该内容由缓存机制处理,更新可能会存在延迟"
              placement="bottom"
          >
            <el-icon class="tip-icon" size="small">
              <Opportunity/>
            </el-icon>
          </el-tooltip>
        </div>

        <!-- 模块内容区域 -->
        <div class="module-content">
          <AnswersComponent
              v-if="activeModule === 'answers'"
              :answers="answers"
              :current-page="currentPage"
              :is-loading="isLoading"
              :page-size="pageSize"
              :total="total"
              @page-change="handlePageChange"
          />
          <CollectionsComponent
              v-else
              :collections="collections"
              :current-page="collectionsCurrentPage"
              :is-loading="isCollectionsLoading"
              :page-size="collectionsPageSize"
              :total="collectionsTotal"
              @page-change="handleCollectionsPageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-profile-page {
  background-color: #f9f9f9;
  min-height: 100vh;
  padding: 24px 0;
}

.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 16px;
}

.main-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  padding: 20px;
}

.module-tabs-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 24px 0 16px 0;
  padding: 0;
  border-bottom: 1px solid #e5e7eb;
}

.tabs-wrapper {
  display: flex;
  gap: 0;
  position: relative;
}

.tab-btn {
  padding: 16px 24px;
  background: transparent;
  border: none;
  font-size: 15px;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: #111827;
  border-bottom-color: #2563eb;
  font-weight: 600;
}

.tab-btn:hover:not(.active) {
  color: #374151;
  background: rgba(0, 0, 0, 0.02);
}

.tab-btn::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: #2563eb;
  transform: scaleX(0);
  transition: transform 0.2s ease;
}

.tab-btn.active::after {
  transform: scaleX(1);
}

.tip-icon {
  color: #b8a082;
  transition: color 0.2s;
}

.tip-icon:hover {
  color: #8b7355;
}

.module-content {
  overflow: hidden;
  margin-top: 8px;
}

.tab-badge {
  background: rgba(245, 242, 239, 0.8);
  color: #8b7355;
  border-radius: 10px;
  padding: 0 6px;
  font-size: 12px;
  font-weight: normal;
}

/* 毛玻璃提示层 */
.glass-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.glass-message {
  text-align: center;
  color: white;
  padding: 30px;
  border-radius: 12px;
  background-color: rgba(0, 0, 0, 0.3);
  max-width: 400px;
}

.glass-icon {
  margin-bottom: 20px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .user-profile-page {
    padding: 10px 0;
  }

  .container {
    padding: 0 10px;
  }

  .tab-btn {
    padding: 12px 16px;
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  .tab-btn {
    padding: 12px 10px;
    font-size: 13px;
  }

  .tab-badge {
    display: none;
  }
}
</style>