<script lang="ts" setup>
import {ref, onMounted, computed, reactive, watch} from 'vue'
import {ElMessage, ElPagination, ElDatePicker} from 'element-plus'
import {Refresh, Check, Filter, Search, Delete, ArrowDown, ArrowUp} from '@element-plus/icons-vue'
import DynamicMessageComponent from '@/view/HomePage/components/Message/DynamicMessageComponent.vue'
import {MessageDisplayMode, MessageType, ReadStatus} from '@/view/HomePage/components/Message/MessageInterface'
import type {BaseMessage} from '@/view/HomePage/components/Message/MessageInterface'
import {
  getMessagesByPage,
  markAllMessagesAsRead,
  checkHasUnreadMessages,
  type MessageQueryFilters
} from './service/ApiMessagePage'

// 设置标题位置
import {setTitle} from '@/utils/title.js'

onMounted(() => {
  setTitle('通知中心')
})


// 响应式数据
const messages = ref<BaseMessage[]>([])
const loading = ref(false)
const hasUnreadMessages = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showFilters = ref(false)

// 过滤条件
const filters = reactive<MessageQueryFilters>({
  pageNum: 1,
  pageSize: 10,
  type: undefined,
  readStatus: undefined,
  keyword: '',
  orderBy: 'created_time',
  orderDirection: 'desc'
})

// 日期范围选择器
const dateRange = ref<[Date, Date] | null>(null)

// 监听日期范围变化
watch(dateRange, (newRange) => {
  if (newRange) {
    filters.createTimeStart = newRange[0].toISOString()
    filters.createTimeEnd = newRange[1].toISOString()
  } else {
    filters.createTimeStart = undefined
    filters.createTimeEnd = undefined
  }
})

// 计算属性
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

// 消息类型选项
const messageTypeOptions = [
  {label: '全部类型', value: undefined},
  {label: 'AI解析', value: MessageType.AI_RECOGNITION},
  {label: '用户关注', value: MessageType.USER_FOLLOW},
  {label: '评论回复', value: MessageType.COMMENT_REPLY}
]

// 阅读状态选项
const readStatusOptions = [
  {label: '全部状态', value: undefined},
  {label: '未读', value: ReadStatus.UNREAD},
  {label: '已读', value: ReadStatus.READ}
]

// 排序选项
const sortOptions = [
  {label: '创建时间', value: 'created_time'},
  {label: '消息类型', value: 'type'},
  {label: '阅读状态', value: 'read_status'}
]

// 切换排序方向
const toggleSortDirection = () => {
  filters.orderDirection = filters.orderDirection === 'desc' ? 'asc' : 'desc'
  applyFilters()
}

// 获取分页消息
const fetchMessages = async () => {
  try {
    loading.value = true
    // 更新过滤条件中的分页参数
    filters.pageNum = currentPage.value
    filters.pageSize = pageSize.value

    const response = await getMessagesByPage(filters)
    if (response.status === 200) {
      messages.value = response.data.records || []
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取消息失败')
    }
  } catch (error) {
    console.error('获取消息失败:', error)
    ElMessage.error('获取消息失败')
  } finally {
    loading.value = false
  }
}

// 应用过滤条件
const applyFilters = () => {
  currentPage.value = 1 // 重置到第一页
  fetchMessages()
}

// 重置过滤条件
const resetFilters = () => {
  Object.assign(filters, {
    type: undefined,
    readStatus: undefined,
    keyword: '',
    orderBy: 'created_time',
    orderDirection: 'desc'
  })
  dateRange.value = null
  applyFilters()
}

// 检查是否有未读消息
const checkUnreadMessages = async () => {
  try {
    const response = await checkHasUnreadMessages()
    if (response.status === 200) {
      hasUnreadMessages.value = response.data || false
    }
  } catch (error) {
    console.error('检查未读消息失败:', error)
  }
}

// 一键已读所有消息
const markAllAsRead = async () => {
  try {
    const response = await markAllMessagesAsRead()
    if (response.status === 200) {
      ElMessage.success(`已标记 ${response.data} 条消息为已读`)
      // 刷新消息列表和未读状态
      await Promise.all([fetchMessages(), checkUnreadMessages()])
    } else {
      ElMessage.error(response.message || '标记已读失败')
    }
  } catch (error) {
    console.error('标记已读失败:', error)
    ElMessage.error('标记已读失败')
  }
}

// 刷新消息列表
const refreshMessages = async () => {
  await Promise.all([fetchMessages(), checkUnreadMessages()])
}

// 页码变化处理
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchMessages()
}

// 页面大小变化处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchMessages()
}


// 组件挂载时获取数据
onMounted(() => {
  refreshMessages()
})
</script>

<template>
  <div class="message-page">
    <div class="message-card">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-left">
          <h2 class="page-title">全部消息</h2>
          <span class="message-count">{{ total }} 条</span>
        </div>
        <div class="header-actions">
          <el-button
              :icon="Refresh"
              :loading="loading"
              size="small"
              text
              @click="refreshMessages"
          >
            刷新
          </el-button>
          <el-button
              :disabled="!hasUnreadMessages || loading"
              :icon="Check"
              size="small"
              text
              @click="markAllAsRead"
          >
            全部已读
          </el-button>
          <el-button
              :icon="Filter"
              size="small"
              text
              @click="showFilters = !showFilters"
          >
            筛选
          </el-button>
        </div>
      </div>

      <!-- 过滤条件面板 -->
      <div v-if="showFilters" class="filter-panel">
        <div class="filter-row">
          <div class="filter-item">
            <el-select
                v-model="filters.type"
                clearable
                placeholder="消息类型"
                size="small"
                @change="applyFilters"
            >
              <el-option
                  v-for="option in messageTypeOptions"
                  :key="option.value || 'all'"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </div>
          <div class="filter-item">
            <el-select
                v-model="filters.readStatus"
                clearable
                placeholder="阅读状态"
                size="small"
                @change="applyFilters"
            >
              <el-option
                  v-for="option in readStatusOptions"
                  :key="option.value || 'all'"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </div>
          <div class="filter-item filter-search">
            <el-input
                v-model="filters.keyword"
                clearable
                placeholder="搜索关键词"
                size="small"
                @keyup.enter="applyFilters"
            >
              <template #suffix>
                <el-icon class="search-icon" @click="applyFilters">
                  <Search/>
                </el-icon>
              </template>
            </el-input>
          </div>
          <div class="filter-item">
            <el-date-picker
                v-model="dateRange"
                end-placeholder="结束"
                format="MM-DD"
                range-separator="-"
                size="small"
                start-placeholder="开始"
                type="daterange"
                value-format="YYYY-MM-DD"
                @change="applyFilters"
            />
          </div>
          <el-button
              size="small"
              text
              @click="toggleSortDirection"
          >
            {{ filters.orderDirection === 'desc' ? '↓' : '↑' }}
          </el-button>
          <el-button
              size="small"
              text
              @click="resetFilters"
          >
            重置
          </el-button>
        </div>
      </div>

      <!-- 消息列表 -->
      <div v-loading="loading" class="message-content">
        <div v-if="messages.length === 0 && !loading" class="empty-state">
          <div class="empty-text">暂无消息</div>
        </div>

        <div v-else class="message-list">
          <DynamicMessageComponent
              v-for="message in messages"
              :key="message.messageId"
              :display-mode="MessageDisplayMode.FULL_PAGE"
              :message="message"
          />
        </div>
      </div>

      <!-- 分页组件 -->
      <div v-if="total > 0" class="pagination-wrapper">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50]"
            :total="total"
            layout="prev, pager, next, sizes"
            small
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
        />
      </div>

    </div>


  </div>

</template>

<style scoped>
.message-page {
  padding: 16px;
  background: #fafafa;
  min-height: 100vh;
}

.message-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-title {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.message-count {
  font-size: 12px;
  color: #999;
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 4px;
}

.header-actions {
  display: flex;
  gap: 4px;
}

.filter-panel {
  padding: 12px 16px;
  background: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
}

.filter-row {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
}

.filter-item {
  min-width: 120px;
}

.filter-item.filter-search {
  flex: 1;
  min-width: 160px;
}

.search-icon {
  cursor: pointer;
  color: #999;
}

.search-icon:hover {
  color: #666;
}

.message-content {
  min-height: 400px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #999;
}

.empty-text {
  font-size: 14px;
  margin-bottom: 4px;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.pagination-wrapper {
  padding: 12px 16px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
  display: flex;
  justify-content: center;
}


/* Element Plus 样式调整 */
:deep(.el-button--text) {
  color: #666;
  padding: 4px 8px;
}

:deep(.el-button--text:hover) {
  color: #409eff;
  background: #f0f9ff;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-input--small .el-input__wrapper) {
  border-radius: 4px;
}

:deep(.el-pagination--small) {
  font-size: 12px;
}

:deep(.el-pagination--small .el-pager li) {
  min-width: 24px;
  height: 24px;
  line-height: 24px;
}

:deep(.el-pagination--small .btn-prev),
:deep(.el-pagination--small .btn-next) {
  width: 24px;
  height: 24px;
  line-height: 24px;
}

/* 响应式 */
@media (max-width: 768px) {
  .message-page {
    padding: 8px;
  }

  .page-header {
    flex-direction: column;
    gap: 8px;
    padding: 8px 12px;
  }

  .filter-row {
    flex-direction: column;
    gap: 6px;
  }

  .filter-item {
    min-width: 100%;
  }

  .message-list {
    padding: 12px;
  }
}
</style>