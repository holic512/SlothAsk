<script setup lang="ts">
import {computed, onMounted, onUnmounted, ref} from 'vue';
import {setTitle} from '@/utils/title';
import {useRoute, useRouter} from 'vue-router';
import {
  ElBacktop,
  ElButton,
  ElDivider,
  ElEmpty,
  ElMessage,
  ElMessageBox,
  ElScrollbar,
  ElSkeleton,
  ElSkeletonItem,
  ElTag,
  ElTooltip
} from 'element-plus';
import {ArrowDown, Clock, Delete, Refresh, User} from '@element-plus/icons-vue';
import {getUserHistory, HistoryGroup} from './service';
import {clearDayHistoryRecords, deleteHistoryRecord} from './service/deleteService';
import {debounce} from 'lodash-es';
import {useSessionStore} from "@/pinia/Session";

// 难度映射为常量
const DIFFICULTY_TYPE_MAP: Record<string, string> = {
  简单: 'success',
  中等: 'warning',
  困难: 'danger',
  入门: 'info',
  专家: 'danger',
};

// 日期优先级常量
const DATE_PRIORITY = ['今天', '昨天', '前天'];

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

// 页面标题设置
onMounted(() => {
  setTitle('历史浏览');
  if (isLoggedIn.value) {
    loadHistoryData();
  }
  window.addEventListener('resize', handleResize);
});

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});

// 响应式状态
const loading = ref(false);
const hasMore = ref(true);
const selectedFilter = ref('全部');
const isMobileView = ref(window.innerWidth < 768);
const currentPage = ref(1);

// 处理窗口大小变化 - 添加防抖
const handleResize = debounce(() => {
  isMobileView.value = window.innerWidth < 768;
}, 200);

// 历史记录数据
const historyList = ref<HistoryGroup[]>([]);

// 过滤并排序后的历史列表，只在 historyList 或 selectedFilter 变化时重新计算
const filteredHistoryList = computed<HistoryGroup[]>(() => {
  // 先过滤
  const filtered = selectedFilter.value === '全部'
    ? historyList.value
    : historyList.value.map(day => ({
        date: day.date,
        records: day.records.filter(r => r.category === selectedFilter.value)
      })).filter(day => day.records.length > 0);

  // 再排优先级和时间
  return filtered
    .sort((a, b) => {
      const aIndex = DATE_PRIORITY.indexOf(a.date);
      const bIndex = DATE_PRIORITY.indexOf(b.date);
      
      // 如果两者都在优先日期中
      if (aIndex !== -1 && bIndex !== -1) {
        return aIndex - bIndex;
      }
      // 如果只有一个在优先日期中
      else if (aIndex !== -1) {
        return -1;
      }
      else if (bIndex !== -1) {
        return 1;
      }
      
      // 其他日期按日期排序
      return b.date.localeCompare(a.date);
    });
});

// 是否存在任何记录
const hasAnyRecord = computed(() =>
  filteredHistoryList.value.some(day => day.records.length > 0)
);

// 加载历史记录数据
const loadHistoryData = async () => {
  if (loading.value || !isLoggedIn.value) return;

  loading.value = true;

  try {
    const data = await getUserHistory(currentPage.value);

    // 如果没有新数据，表示已加载完全部
    if (data.length === 0) {
      hasMore.value = false;
    } else {
      // 合并新数据到列表中
      mergeHistoryData(data);
      currentPage.value++;
    }
  } catch (error) {
    console.error('加载历史记录失败:', error);
    ElMessage.error('加载历史记录失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 合并历史数据，避免重复日期
const mergeHistoryData = (newData: HistoryGroup[]) => {
  newData.forEach(newGroup => {
    // 检查是否已存在相同日期的组
    const existingGroupIndex = historyList.value.findIndex(group => group.date === newGroup.date);

    if (existingGroupIndex >= 0) {
      // 如果存在，合并记录
      const existingGroup = historyList.value[existingGroupIndex];

      // 找出新记录（避免重复）
      const uniqueRecords = newGroup.records.filter(newRecord =>
          !existingGroup.records.some(record => record.id === newRecord.id)
      );

      // 合并记录并按访问时间排序
      existingGroup.records = [...existingGroup.records, ...uniqueRecords]
          .sort((a, b) => a.visitTime > b.visitTime ? -1 : 1);
    } else {
      // 如果不存在，添加新组
      historyList.value.push(newGroup);
    }
  });

  // 按日期排序 - 这里不需要重新实现排序逻辑，因为我们已经有了计算属性
  historyList.value.sort((a, b) => {
    const aIndex = DATE_PRIORITY.indexOf(a.date);
    const bIndex = DATE_PRIORITY.indexOf(b.date);
    
    // 如果两者都在优先日期中
    if (aIndex !== -1 && bIndex !== -1) {
      return aIndex - bIndex;
    }
    // 如果只有一个在优先日期中
    else if (aIndex !== -1) {
      return -1;
    }
    else if (bIndex !== -1) {
      return 1;
    }
    
    // 其他日期按日期排序
    return b.date.localeCompare(a.date);
  });
};

// 加载更多数据
const loadMore = () => {
  if (!hasMore.value || loading.value || !isLoggedIn.value) return;
  loadHistoryData();
};


// 删除记录
const deleteHistory = (dayIndex: number, recordIndex: number) => {
  if (!isLoggedIn.value) return;
  
  const list = selectedFilter.value === '全部' ? historyList.value : filteredHistoryList.value;
  const day = list[dayIndex];
  const record = day.records[recordIndex];

  ElMessageBox.confirm(
      '确定要删除该历史记录吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(async () => {
    try {
      // 调用后端接口
      const success = await deleteHistoryRecord(record.id);
      
      if (success) {
        // 前端数据更新
        if (selectedFilter.value === '全部') {
          historyList.value[dayIndex].records.splice(recordIndex, 1);

          // 如果某一天的记录全部被删除，则删除这一天
          if (historyList.value[dayIndex].records.length === 0) {
            historyList.value.splice(dayIndex, 1);
          }
        } else {
          // 在筛选模式下，需要找到原始数据中对应的记录
          const originalDayIndex = historyList.value.findIndex(d => d.date === day.date);

          if (originalDayIndex !== -1) {
            const originalRecordIndex = historyList.value[originalDayIndex].records.findIndex(r => r.id === record.id);
            if (originalRecordIndex !== -1) {
              historyList.value[originalDayIndex].records.splice(originalRecordIndex, 1);

              // 如果某一天的记录全部被删除，则删除这一天
              if (historyList.value[originalDayIndex].records.length === 0) {
                historyList.value.splice(originalDayIndex, 1);
              }
            }
          }
        }
        ElMessage.success('历史记录已删除');
      } else {
        ElMessage.error('删除失败，请稍后重试');
      }
    } catch (error) {
      console.error('删除历史记录失败:', error);
      ElMessage.error('删除失败，请稍后重试');
    }
  }).catch(() => {
    // 用户取消删除操作
    ElMessage.info('已取消删除');
  });
};

// 清空某一天的历史
const clearDayHistory = (dayIndex: number) => {
  if (!isLoggedIn.value) return;
  
  const list = selectedFilter.value === '全部' ? historyList.value : filteredHistoryList.value;
  const day = list[dayIndex];

  ElMessageBox.confirm(
      `确定要清空${day.date}的所有${selectedFilter.value === '全部' ? '' : selectedFilter.value}历史记录吗？`,
      '清空确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(async () => {
    try {
      // 调用后端接口
      const success = await clearDayHistoryRecords(day.date);
      
      if (success) {
        // 前端数据更新
        if (selectedFilter.value === '全部') {
          historyList.value.splice(dayIndex, 1);
        } else {
          // 在筛选模式下，需要找到原始数据中对应的记录
          const originalDayIndex = historyList.value.findIndex(d => d.date === day.date);

          if (originalDayIndex !== -1) {
            const recordsToKeep = historyList.value[originalDayIndex].records.filter(r =>
                r.category !== selectedFilter.value
            );

            if (recordsToKeep.length === 0) {
              historyList.value.splice(originalDayIndex, 1);
            } else {
              historyList.value[originalDayIndex].records = recordsToKeep;
            }
          }
        }
        ElMessage.success(`${day.date}的历史记录已清空`);
      } else {
        ElMessage.error('清空失败，请稍后重试');
      }
    } catch (error) {
      console.error('清空历史记录失败:', error);
      ElMessage.error('清空失败，请稍后重试');
    }
  }).catch(() => {
    // 用户取消清空操作
    ElMessage.info('已取消清空操作');
  });
};

// 刷新数据
const refreshHistory = () => {
  if (!isLoggedIn.value) return;
  
  historyList.value = [];
  currentPage.value = 1;
  hasMore.value = true;
  loadHistoryData();
};

// 查看题目
const viewQuestion = (id: string) => {
  if (!isLoggedIn.value) {
    handleLogin();
    return;
  }
  router.push(`/question/${id}`);
};

// 根据难度获取对应的Element Plus类型 - 简化为使用映射表
const getDifficultyType = (difficulty: string) => DIFFICULTY_TYPE_MAP[difficulty] || 'info';


</script>

<template>
  <div class="history-container">
    <!-- 标题和操作区 -->
    <div class="history-header">
      <div class="title-area">
        <h2 class="main-title">历史浏览</h2>
        <p class="subtitle">您的每一步足迹都被记录</p>
      </div>

      <div class="actions-area">
        <el-tooltip content="刷新" placement="top">
          <el-button
              type="primary"
              :icon="Refresh"
              circle
              @click="refreshHistory"
              class="action-btn"
          />
        </el-tooltip>
      </div>
    </div>

    <!-- 主内容区 -->
    <el-scrollbar height="calc(100vh - 200px)" class="history-scrollbar">
      <div class="history-content">
        <!-- 无数据展示 -->
        <el-empty
            v-if="!loading && !hasAnyRecord && isLoggedIn"
            description="暂无历史记录"
            class="empty-history"
        >
          <template #image>
            <div class="empty-image"></div>
          </template>
          <template #default>
            <p class="empty-text">您的学习历史将在这里记录</p>
            <el-button type="primary" @click="refreshHistory">刷新试试</el-button>
          </template>
        </el-empty>

        <!-- 历史记录列表 -->
        <template v-for="(day, dayIndex) in filteredHistoryList" :key="day.date">
          <div class="history-day" v-if="day.records.length > 0">
            <div class="day-header">
              <div class="day-title">
                <el-tag size="small" effect="plain" class="date-tag">
                  <el-icon>
                    <Clock/>
                  </el-icon>
                  <span>{{ day.date }}</span>
                </el-tag>
                <span class="record-count">{{ day.records.length }} 条记录</span>
              </div>
              <el-button
                  type="danger"
                  size="small"
                  text
                  @click="clearDayHistory(dayIndex)"
                  v-if="day.records.length > 0"
                  class="clear-button"
              >
                清空
              </el-button>
            </div>

            <!-- 历史卡片列表 -->
            <div class="history-list" :class="{ 'mobile-view': isMobileView }">
              <div
                  v-for="(record, recordIndex) in day.records"
                  :key="record.id"
                  class="history-item"
              >
                <div class="history-card" @click="viewQuestion(record.id)">
                  <div class="card-background"></div>
                  <div class="card-content">
                    <div class="info-wrapper">
                      <h3 class="title">{{ record.title }}</h3>

                      <!-- 分类标签区 - 使用info样式 -->
                      <div class="category-tags">
                        <el-tag
                            size="small"
                            effect="plain"
                            class="category-tag"
                        >
                          {{ record.category }}
                        </el-tag>

                        <el-divider direction="vertical"/>

                        <el-tag
                            v-for="(tag, idx) in record.tags"
                            :key="idx"
                            size="small"
                            type="info"
                            effect="plain"
                            class="content-tag"
                            v-if="record.tags && record.tags.length > 0"
                        >
                          {{ tag }}
                        </el-tag>
                      </div>


                      <!-- 元信息区 -->
                      <div class="meta">
                        <!-- 难度标签 - 使用扁平化 -->
                        <el-tag
                            size="small"
                            :type="getDifficultyType(record.difficulty)"
                            effect="plain"
                            class="difficulty-tag"
                        >
                          {{ record.difficulty }}
                        </el-tag>

                        <!-- 访问时间 -->
                        <span class="time">
                          <el-icon><Clock/></el-icon>
                          {{ record.visitTime }}
                        </span>
                      </div>
                    </div>
                    <div>
                      <el-tooltip content="删除记录" placement="top">
                        <el-button
                            type="danger"
                            circle
                            size="small"
                            @click.stop="deleteHistory(dayIndex, recordIndex)"
                        >
                          <el-icon>
                            <Delete/>
                          </el-icon>
                        </el-button>
                      </el-tooltip>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <el-divider v-if="dayIndex < filteredHistoryList.length - 1 && day.records.length > 0"/>
        </template>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading-skeleton">
          <div v-for="i in 3" :key="i" class="skeleton-day">
            <div class="skeleton-header">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item variant="text" style="width: 30%;"/>
                </template>
              </el-skeleton>
            </div>
            <div class="skeleton-items">
              <div v-for="j in 2" :key="j" class="skeleton-card">
                <el-skeleton animated>
                  <template #template>
                    <div class="skeleton-layout">
                      <div class="skeleton-content">
                        <el-skeleton-item variant="h3" style="width: 50%;"/>
                        <el-skeleton-item variant="text" style="width: 80%;"/>
                        <el-skeleton-item variant="text" style="width: 60%;"/>
                      </div>
                    </div>
                  </template>
                </el-skeleton>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="hasMore && !loading && hasAnyRecord" class="load-more">
          <el-button type="primary" plain @click="loadMore" class="load-more-button">
            加载更多
            <el-icon class="el-icon--right">
              <ArrowDown/>
            </el-icon>
          </el-button>
        </div>

        <!-- 没有更多数据 -->
        <div v-if="!hasMore && hasAnyRecord" class="no-more">
          没有更多历史记录了 ~
        </div>
      </div>
    </el-scrollbar>

    <!-- 回到顶部 -->
    <el-backtop target=".history-scrollbar .el-scrollbar__wrap" :right="30" :bottom="30"/>
    
    <!-- 用户未登录遮罩 -->
    <div v-if="!isLoggedIn" class="login-overlay">
      <div class="login-card">
        <el-icon class="login-icon"><User /></el-icon>
        <h2>您还未登录</h2>
        <p>登录后才能查看历史记录</p>
        <el-button type="primary" @click="handleLogin">登录 / 注册</el-button>
      </div>
    </div>
  </div>
</template>


<style lang="scss" scoped>
.history-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;

  .history-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 8px;
    padding-bottom: 12px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.05);

    .title-area {
      .main-title {
        font-size: 24px;
        font-weight: 600;
        color: var(--el-text-color-primary);
        margin: 0 0 4px 0;
        background: linear-gradient(90deg, var(--el-color-primary), #409eff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        letter-spacing: 1px;
      }

      .subtitle {
        font-size: 14px;
        color: var(--el-text-color-secondary);
        margin: 0;
        letter-spacing: 0.5px;
      }
    }

    .actions-area {
      display: flex;
      align-items: center;
      gap: 12px;

      .action-btn {
        transition: all 0.3s;

        &:hover {
          transform: rotate(180deg);
        }
      }
    }
  }

  .history-scrollbar {
    flex: 1;
    overflow: auto;
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

  .history-content {
    padding: 4px 4px 20px;

    .empty-history {
      padding: 60px 0;
      margin: 0 auto;
      max-width: 400px;

      .empty-image {
        height: 160px;
        background: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyMDAgMjAwIj48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9ImEiIHgxPSIwJSIgeTE9IjAlIiB4Mj0iMTAwJSIgeTI9IjEwMCUiPjxzdG9wIG9mZnNldD0iMCUiIHN0b3AtY29sb3I9IiM0MDllZmYiIHN0b3Atb3BhY2l0eT0iLjQiLz48c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiM0MDllZmYiIHN0b3Atb3BhY2l0eT0iLjEiLz48L2xpbmVhckdyYWRpZW50PjwvZGVmcz48cGF0aCBmaWxsPSJ1cmwoI2EpIiBkPSJNMTAwIDBDNDQuOCAwIDAgNDQuOCAwIDEwMHM0NC44IDEwMCAxMDAgMTAwIDEwMC00NC44IDEwMC0xMDBTMTU1LjIgMCAxMDAgMHptMCAxOTBjLTQ5LjcgMC05MC00MC4zLTkwLTkwUzUwLjMgMTAgMTAwIDEwczg5LjkgNDAuMyA5MCA5MGMwIDQ5LjctNDAuMyA5MC05MCA5MHoiLz48cGF0aCBmaWxsPSIjNDA5ZWZmIiBkPSJNMTE1LjcgOTYuOWwtMTcuNS0yOS45Yy0yLjEtMy42LTYuOC00LjgtMTAuNC0yLjctMy42IDIuMS00LjggNi44LTIuNyAxMC40TDk0LjEgOTAgODMuOCAxMTdhLjEuMSAwIDAgMC0uMi4xbC0uNC42Yy0yLjEgMy42LS45IDguMyAyLjcgMTAuNCAzLjYgMi4xIDguMy45IDEwLjQtMi43bC4zLS41LjEtLjIgMTEtMTkuMSA2LjQgMTAuOWMxLjQgMi40IDQuMSAzLjkgNi45IDMuOSAxLjMgMCAyLjYtLjMgMy44LTEgMy43LTIuMSA0LjktNi43IDIuOS0xMC41eiIvPjwvc3ZnPg==') center center no-repeat;
        background-size: contain;
      }

      .empty-text {
        color: var(--el-text-color-secondary);
        font-size: 14px;
        margin-bottom: 16px;
      }
    }

    .history-day {
      margin-bottom: 24px;

      .day-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;

        .day-title {
          display: flex;
          align-items: center;
          gap: 12px;

          .date-tag {
            display: flex;
            align-items: center;
            gap: 6px;
            background: var(--el-color-primary-light-9);
            color: var(--el-color-primary);
            border-color: var(--el-color-primary-light-7);
            border-radius: 16px;
            padding: 4px 12px;
            font-weight: 500;
          }

          .record-count {
            font-size: 13px;
            color: var(--el-text-color-secondary);
          }
        }

        .clear-button {
          font-size: 13px;
          margin-right: 12px;

          &:hover {
            color: var(--el-color-danger-dark-2);
          }
        }
      }

      .history-list {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(440px, 1fr));
        gap: 12px;
        padding-right: 12px;

        &.mobile-view {
          grid-template-columns: 1fr;
        }

        .history-item {
          .history-card {
            position: relative;
            border-radius: 12px;
            overflow: hidden;
            background: var(--el-bg-color);
            transition: transform 0.25s ease, box-shadow 0.25s ease;
            cursor: pointer;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
            border: 1px solid rgba(0, 0, 0, 0.03);

            &:hover {
              transform: translateY(-1px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            }

            .card-background {
              position: absolute;
              top: 0;
              left: 0;
              right: 0;
              bottom: 0;
              background: linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(245, 247, 250, 0.9) 100%);
              z-index: 0;
            }

            .card-content {
              position: relative;
              z-index: 1;
              display: flex;
              padding: 12px;

              .info-wrapper {
                flex: 1;
                padding: 0 16px;
                display: flex;
                flex-direction: column;

                .title {
                  margin: 0 0 12px 0;
                  font-size: 15px;
                  line-height: 1.4;
                  color: var(--el-text-color-primary);
                  display: -webkit-box;
                  -webkit-line-clamp: 2;
                  -webkit-box-orient: vertical;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  font-weight: 500;
                }

                .category-tags {
                  display: flex;
                  gap: 6px;
                  margin-bottom: 8px;

                  .category-tag {
                    border-radius: 12px;
                    font-size: 12px;
                  }
                }

                .meta {
                  display: flex;
                  align-items: center;
                  justify-content: space-between;
                  margin-top: auto;

                  .difficulty-tag {
                    border-radius: 12px;
                    padding: 0 8px;
                    height: 20px;
                    line-height: 20px;
                    font-size: 12px;
                  }

                  .time {
                    display: flex;
                    align-items: center;
                    gap: 4px;
                    font-size: 12px;
                    color: var(--el-text-color-secondary);
                  }
                }
              }
            }
          }
        }
      }
    }

    .loading-skeleton {
      .skeleton-day {
        margin-bottom: 30px;

        .skeleton-header {
          margin-bottom: 16px;
        }

        .skeleton-items {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(440px, 1fr));
          gap: 16px;

          .skeleton-card {
            border-radius: 12px;
            overflow: hidden;
            background: var(--el-bg-color-page);

            .skeleton-layout {
              display: flex;
              padding: 12px;
              gap: 16px;

              .skeleton-content {
                flex: 1;
                display: flex;
                flex-direction: column;
                gap: 8px;
              }
            }
          }
        }
      }
    }

    .load-more, .no-more {
      text-align: center;
      margin-top: 30px;

      .load-more-button {
        border-radius: 20px;
        padding: 8px 24px;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
      }
    }

    .no-more {
      color: var(--el-text-color-secondary);
      font-size: 14px;
      padding: 16px;
    }
  }
}
</style>