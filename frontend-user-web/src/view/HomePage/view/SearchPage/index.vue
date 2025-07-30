<script setup>
import {onMounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {searchSimilarQuestions} from './Api/ApiSearchQuestions'
import {ArrowRight, Loading, Search} from '@element-plus/icons-vue'
import {setTitle} from "@/utils/title.js";

onMounted(() => {
  setTitle('搜索结果');
});


const route = useRoute()
const router = useRouter()

// 响应式数据
const searchQuery = ref('')
const searchResults = ref([])
const isLoading = ref(false)
const hasSearched = ref(false)
const totalResults = ref(0)

// 从路由参数获取搜索关键词
const getSearchQuery = () => {
  const query = route.query.q
  if (query) {
    searchQuery.value = query
    performSearch(query)
  }
}

// 执行搜索
const performSearch = async (query) => {
  if (!query || !query.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  isLoading.value = true
  hasSearched.value = true

  try {
    const response = await searchSimilarQuestions(query.trim())

    if (response.status === 200) {
      searchResults.value = response.data || []
      totalResults.value = searchResults.value.length

      if (searchResults.value.length === 0) {
        ElMessage.info('未找到相关题目，请尝试其他关键词')
      }
    } else {
      ElMessage.error(response.message || '搜索失败，请稍后重试')
      searchResults.value = []
      totalResults.value = 0
    }
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索服务暂时不可用，请稍后重试')
    searchResults.value = []
    totalResults.value = 0
  } finally {
    isLoading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    // 更新URL参数
    router.push({
      path: '/search',
      query: {q: searchQuery.value.trim()}
    })
  }
}

// 处理回车搜索
const handleKeyup = (event) => {
  if (event.key === 'Enter') {
    handleSearch()
  }
}

// 跳转到题目详情
const goToQuestion = (questionId) => {
  router.push(`/question/${questionId}`)
}

// 格式化相似度分数
const formatScore = (score) => {
  return (score * 100).toFixed(1)
}

// 监听路由变化
watch(() => route.query.q, (newQuery) => {
  if (newQuery && newQuery !== searchQuery.value) {
    searchQuery.value = newQuery
    performSearch(newQuery)
  }
})

// 组件挂载时获取搜索参数
onMounted(() => {
  getSearchQuery()
})
</script>

<template>
  <div class="search-page">


    <!-- 搜索结果区域 -->
    <div class="search-content">
      <div class="content-container">
        <!-- 搜索状态提示 -->
        <div v-if="!hasSearched" class="search-tips">
          <div class="tips-content">
            <el-icon class="tips-icon">
              <Search/>
            </el-icon>
            <h3>智能题目搜索</h3>
            <p>输入关键词，我们将为您找到最相关的题目</p>
            <div class="tips-examples">
              <span class="tip-tag">算法</span>
              <span class="tip-tag">数据结构</span>
              <span class="tip-tag">Java</span>
              <span class="tip-tag">前端开发</span>
            </div>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-else-if="isLoading" class="loading-state">
          <el-icon class="loading-icon">
            <Loading/>
          </el-icon>
          <p>正在搜索相关题目...</p>
        </div>

        <!-- 搜索结果 -->
        <div v-else-if="searchResults.length > 0" class="search-results">
          <!-- 结果统计 -->
          <div class="results-header">
            <h3>搜索结果</h3>
            <span class="results-count">找到 {{ totalResults }} 个相关题目</span>
          </div>

          <!-- 结果列表 -->
          <div class="results-list">
            <div
                v-for="(result, index) in searchResults"
                :key="result.id || index"
                class="result-item"
                @click="goToQuestion(result.id)"
            >
              <div class="result-content">
                <div class="result-header">
                  <h4 class="result-title">{{ result.title }}</h4>
                  <div class="result-score">
                    <span class="score-label">相似度</span>
                    <span class="score-value">{{ formatScore(result.score) }}%</span>
                  </div>
                </div>

              </div>
              <div class="result-arrow">
                <el-icon>
                  <ArrowRight/>
                </el-icon>
              </div>
            </div>
          </div>
        </div>

        <!-- 无结果状态 -->
        <div v-else class="no-results">
          <div class="no-results-content">
            <el-icon class="no-results-icon">
              <Search/>
            </el-icon>
            <h3>未找到相关题目</h3>
            <p>尝试使用不同的关键词或检查拼写</p>
            <div class="search-suggestions">
              <p>搜索建议：</p>
              <ul>
                <li>使用更通用的关键词</li>
                <li>检查关键词拼写</li>
                <li>尝试相关的技术术语</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-page {
  min-height: 100vh;
  background: #f8f9fa;
}



/* 内容区域 */
.search-content {
  padding: 32px 0;
}

.content-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 搜索提示 */
.search-tips {
  text-align: center;
  padding: 60px 20px;
}

.tips-content {
  max-width: 400px;
  margin: 0 auto;
}

.tips-icon {
  font-size: 48px;
  color: #1890ff;
  margin-bottom: 16px;
}

.tips-content h3 {
  color: #333;
  margin-bottom: 8px;
  font-size: 24px;
}

.tips-content p {
  color: #666;
  margin-bottom: 24px;
  font-size: 16px;
}

.tips-examples {
  display: flex;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
}

.tip-tag {
  background: #f0f0f0;
  color: #666;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.tip-tag:hover {
  background: #e6f3ff;
  color: #1890ff;
}

/* 加载状态 */
.loading-state {
  text-align: center;
  padding: 60px 20px;
}

.loading-icon {
  font-size: 32px;
  color: #1890ff;
  margin-bottom: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.loading-state p {
  color: #666;
  font-size: 16px;
}

/* 搜索结果 */
.search-results {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.results-header {
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.results-header h3 {
  color: #333;
  margin: 0;
  font-size: 18px;
}

.results-count {
  color: #666;
  font-size: 14px;
}

.results-list {
  padding: 0;
}

.result-item {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.result-item:hover {
  background: #f8f9fa;
}

.result-item:last-child {
  border-bottom: none;
}

.result-content {
  flex: 1;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.result-title {
  color: #333;
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  line-height: 1.4;
  flex: 1;
  margin-right: 16px;
}

.result-score {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
}

.score-label {
  color: #999;
  font-size: 12px;
}

.score-value {
  color: #1890ff;
  font-weight: 600;
  font-size: 14px;
}

.result-meta {
  display: flex;
  gap: 16px;
}

.result-id {
  color: #999;
  font-size: 13px;
}

.result-arrow {
  color: #ccc;
  margin-left: 16px;
}

/* 无结果状态 */
.no-results {
  text-align: center;
  padding: 60px 20px;
}

.no-results-content {
  max-width: 400px;
  margin: 0 auto;
}

.no-results-icon {
  font-size: 48px;
  color: #ccc;
  margin-bottom: 16px;
}

.no-results-content h3 {
  color: #333;
  margin-bottom: 8px;
  font-size: 20px;
}

.no-results-content p {
  color: #666;
  margin-bottom: 24px;
  font-size: 16px;
}

.search-suggestions {
  text-align: left;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.search-suggestions p {
  color: #333;
  font-weight: 500;
  margin-bottom: 12px;
}

.search-suggestions ul {
  color: #666;
  margin: 0;
  padding-left: 20px;
}

.search-suggestions li {
  margin-bottom: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-container {
    padding: 0 16px;
  }

  .search-content {
    padding: 20px 0;
  }

  .result-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .result-title {
    margin-right: 0;
  }

  .tips-examples {
    justify-content: center;
  }
}
</style>