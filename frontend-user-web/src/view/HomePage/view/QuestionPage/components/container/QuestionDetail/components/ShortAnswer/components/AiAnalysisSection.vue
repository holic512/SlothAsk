<template>
  <div class="ai-analysis-section">
    <!-- AI 未分析状态 -->
    <div v-if="analysisStatus === 'not-analyzed'" class="ai-not-analyzed">
      <div class="not-analyzed-info">
        <el-icon class="analysis-icon">
          <Document/>
        </el-icon>
        <span class="not-analyzed-text">该回答尚未进行 AI 分析</span>
      </div>
      <div class="analysis-actions">
        <el-button
            :loading="isRequestingReview"
            size="small"
            type="primary"
            @click="requestAiReview"
        >
          开始分析
        </el-button>
      </div>
    </div>

    <!-- AI 分析中状态 -->
    <div v-else-if="analysisStatus === 'analyzing'" class="ai-analyzing">
      <div class="analyzing-info">
        <el-icon class="analyzing-icon">
          <Loading/>
        </el-icon>
        <span class="analyzing-text">AI 正在分析中，请稍候...</span>
      </div>
    </div>

    <!-- AI 已分析完成状态 -->
    <div v-else class="ai-analyzed">
      <div class="ai-accuracy">
        <span class="accuracy-label">AI 正确率：</span>
        <span class="accuracy-value">{{ aiAnalysis.accuracy }}%</span>
        <div class="accuracy-bar">
          <div :style="{ width: aiAnalysis.accuracy + '%' }" class="accuracy-fill"></div>
        </div>
      </div>
      <div class="analysis-actions">
        <!--        <el-button-->
        <!--            :loading="isRequestingReview"-->
        <!--            size="small"-->
        <!--            @click="requestAiReview"-->
        <!--        >-->
        <!--          重新评估-->
        <!--        </el-button>-->
        <el-button
            size="small"
            type="primary"
            @click="openAiAnalysisDialog"
        >
          查看解析
        </el-button>
      </div>
    </div>

    <!-- AI 分析弹出框 -->
    <AiAnalysisDialog
        v-model="showAiAnalysisDialog"
        :ai-analysis="aiAnalysis"
        @re-analysis="handleReAnalysis"
    />
  </div>
</template>

<script lang="ts" setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Document, Loading} from '@element-plus/icons-vue'
import AiAnalysisDialog from './AiAnalysisDialog.vue'
import {ApiSendAiAnalysis, type SendAiAnalysisRequest} from '../../../../../../service/ApiSendAiAnalysis'
import {
  type AiAnalysisRecord,
  ApiCheckAiAnalysisStatus,
  ApiGetAiAnalysis,
  type GetAiAnalysisRequest
} from '../../../../../../service/ApiGetAiAnalysis'

// TypeScript 接口定义
interface AiAnalysis {
  accuracy: number
  analysis: string
  reviewTime: string
  aiSource: string
}

const props = defineProps<{
  answerId: number | string
}>()

// 分析状态类型
type AnalysisStatus = 'not-analyzed' | 'analyzing' | 'completed'

// 响应式数据
const isRequestingReview = ref(false)
const showAiAnalysisDialog = ref(false)
const analysisStatus = ref<AnalysisStatus>('not-analyzed')
const pollingTimer = ref<NodeJS.Timeout | null>(null)

// AI分析数据
const aiAnalysis = ref<AiAnalysis>({
  accuracy: 0,
  analysis: '',
  reviewTime: '',
  aiSource: ''
})

// 方法
const openAiAnalysisDialog = () => {
  showAiAnalysisDialog.value = true
}

const handleReAnalysis = async () => {
  // 重新分析逻辑，与重新评估相同
  await requestAiReview()
}

const requestAiReview = async () => {
  try {
    // 如果已有分析结果，需要确认重新分析
    if (analysisStatus.value === 'completed') {
      await ElMessageBox.confirm('重新进行 AI 鉴别将覆盖当前结果，是否继续？', '确认操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      })
    }

    isRequestingReview.value = true

    // 调用后端API进行AI评估
    const request: SendAiAnalysisRequest = {
      answerId: Number(props.answerId)
    }

    const response = await ApiSendAiAnalysis(request)

    if (response.status === 200) {
      analysisStatus.value = 'analyzing'
      startPolling()
      ElMessage.success('AI 分析请求已发送，正在处理中...')
    } else {
      // 处理不同的错误状态
      switch (response.status) {
        case 400:
          ElMessage.error('请求参数错误')
          break
        case 404:
          ElMessage.error('回答记录不存在')
          break
        case 403:
          ElMessage.error('您没有权限对此回答进行AI分析')
          break
        case 409:
          ElMessage.warning('该回答正在进行AI分析中，请稍后再试')
          analysisStatus.value = 'analyzing'
          startPolling()
          break
        case 410:
          ElMessage.error('该回答尚未提交，无法进行AI分析')
          break
        case 411:
          ElMessage.warning('该问题已存在AI解析记录')
          // 重新加载分析数据
          await loadAiAnalysis()
          break
        default:
          ElMessage.error(response.message || 'AI 分析请求失败，请重试')
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('AI分析请求失败:', error)
      ElMessage.error('AI 分析失败，请重试')
      analysisStatus.value = 'not-analyzed'
    }
    stopPolling()
  } finally {
    isRequestingReview.value = false
  }
}

// 检查分析状态
const checkAnalysisStatus = async () => {
  try {
    // 调用后端API检查分析状态
    const response = await ApiCheckAiAnalysisStatus(Number(props.answerId))

    if (response.status === 200 && response.data) {
      // 分析完成，更新数据
      const analysisRecord: AiAnalysisRecord = response.data
      aiAnalysis.value = {
        accuracy: analysisRecord.accuracyRate,
        analysis: analysisRecord.aiExplanation,
        reviewTime: new Date(analysisRecord.updateTime).toLocaleString('zh-CN'),
        aiSource: analysisRecord.aiSource
      }
      analysisStatus.value = 'completed'
      stopPolling()
    } else if (response.status === 408 || response.status === 409) {
      // 正在解析中或无解析记录，继续轮询
      // 不显示消息，静默轮询
    } else {
      // 其他错误状态，停止轮询
      stopPolling()
      console.error('检查分析状态失败:', response.message)
    }
  } catch (error) {
    console.error('检查分析状态失败:', error)
    // 网络错误等，继续轮询
  }
}

// 开始轮询 - 轮询检查 ai是否解析完成
const startPolling = () => {
  if (pollingTimer.value) {
    clearInterval(pollingTimer.value)
  }

  pollingTimer.value = setInterval(() => {
    checkAnalysisStatus()
  }, 4000) // 每4秒轮询一次
}

// 停止轮询
const stopPolling = () => {
  if (pollingTimer.value) {
    clearInterval(pollingTimer.value)
    pollingTimer.value = null
  }
}

// 加载AI分析数据
const loadAiAnalysis = async () => {
  try {
    // 调用后端API获取AI分析数据
    const request: GetAiAnalysisRequest = {
      answerId: Number(props.answerId)
    }

    const response = await ApiGetAiAnalysis(request)

    if (response.status === 200 && response.data) {
      // 已有分析结果
      const analysisRecord: AiAnalysisRecord = response.data
      aiAnalysis.value = {
        accuracy: analysisRecord.accuracyRate,
        analysis: analysisRecord.aiExplanation,
        reviewTime: new Date(analysisRecord.updateTime).toLocaleString('zh-CN'),
        aiSource: analysisRecord.aiSource
      }
      analysisStatus.value = 'completed'
    } else if (response.status === 408) {
      // 正在解析中
      analysisStatus.value = 'analyzing'
      startPolling()
    } else if (response.status === 409) {
      // 无解析记录
      analysisStatus.value = 'not-analyzed'
    } else {
      // 其他错误状态，默认为未分析
      analysisStatus.value = 'not-analyzed'
      if (response.status === 404) {
        console.warn('回答记录不存在')
      } else if (response.status === 403) {
        console.warn('无权限访问此回答的AI分析')
      }
    }
  } catch (error) {
    console.error('加载AI分析数据失败:', error)
    analysisStatus.value = 'not-analyzed'
  }
}

// 生命周期
onMounted(() => {
  if (props.answerId) {
    loadAiAnalysis()
  }
})

// 监听answerId变化
watch(
    () => props.answerId,
    (newAnswerId) => {
      if (newAnswerId) {
        // 重置状态
        analysisStatus.value = 'not-analyzed'
        aiAnalysis.value = {
          accuracy: 0,
          analysis: '',
          reviewTime: '',
          aiSource: ''
        }
        stopPolling()

        // 重新加载分析数据
        loadAiAnalysis()
      }
    }
)

// 对外暴露的方法
const setAnalyzingStatus = () => {
  analysisStatus.value = 'analyzing'
}

const setNotAnalyzedStatus = () => {
  analysisStatus.value = 'not-analyzed'
  stopPolling()
}

// 暴露方法给父组件
defineExpose({
  setAnalyzingStatus,
  setNotAnalyzedStatus,
  startPolling
})

onUnmounted(() => {
  stopPolling()
})
</script>

<style scoped>
.ai-analysis-section {
  padding: 1rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.ai-not-analyzed,
.ai-analyzing,
.ai-analyzed {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.ai-analyzing {
  align-items: center;
}

.not-analyzed-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #909399;
  font-size: 0.9rem;
}

.analysis-icon {
  font-size: 1.2rem;
  color: #c0c4cc;
}

.not-analyzed-text {
  font-weight: 500;
}

.analyzing-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #409eff;
  font-size: 0.9rem;
}

.analyzing-icon {
  font-size: 1.2rem;
  color: #409eff;
  animation: rotate 2s linear infinite;
}

.analyzing-text {
  font-weight: 500;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.ai-accuracy {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
}

.accuracy-label {
  color: #606266;
  font-weight: 500;
}

.accuracy-value {
  font-weight: 600;
  color: #67c23a;
  font-size: 1rem;
}

.accuracy-bar {
  width: 80px;
  height: 4px;
  background: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
}

.accuracy-fill {
  height: 100%;
  background: linear-gradient(90deg, #67c23a, #85ce61);
  transition: width 0.3s ease;
}

.analysis-actions {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-not-analyzed,
  .ai-analyzing,
  .ai-analyzed {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .analysis-actions {
    justify-content: flex-start;
  }

  .ai-accuracy {
    flex-wrap: wrap;
  }

  .accuracy-bar {
    width: 60px;
  }

  .not-analyzed-info,
  .analyzing-info {
    justify-content: center;
  }
}
</style>