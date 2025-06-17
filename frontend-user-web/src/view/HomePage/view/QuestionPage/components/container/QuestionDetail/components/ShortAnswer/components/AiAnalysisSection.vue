<template>
  <div class="ai-analysis-section">
    <div class="ai-accuracy">
      <span class="accuracy-label">AI 正确率：</span>
      <span class="accuracy-value">{{ aiAnalysis.accuracy }}%</span>
      <div class="accuracy-bar">
        <div :style="{ width: aiAnalysis.accuracy + '%' }" class="accuracy-fill"></div>
      </div>
    </div>
    <div class="analysis-actions">
      <el-button
          :loading="isRequestingReview"
          size="small"
          @click="requestAiReview"
      >
        重新评估
      </el-button>
      <el-button
          size="small"
          type="primary"
          @click="openAiAnalysisDialog"
      >
        查看解析
      </el-button>
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
import {onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import AiAnalysisDialog from './AiAnalysisDialog.vue'

// TypeScript 接口定义
interface AiAnalysis {
  accuracy: number
  analysis: string
  reviewTime: string
}

const props = defineProps<{
  answerId: number | string
}>()

// 响应式数据
const isRequestingReview = ref(false)
const showAiAnalysisDialog = ref(false)

// AI分析数据
const aiAnalysis = ref<AiAnalysis>({
  accuracy: 85,
  analysis: `
    <h4>分析总结</h4>
    <p>您的回答整体思路清晰，涵盖了问题的主要方面。以下是详细分析：</p>
    <h5>优点：</h5>
    <ul>
      <li>逻辑结构清晰，条理分明</li>
      <li>关键概念理解准确</li>
      <li>举例恰当，有助于理解</li>
    </ul>
    <h5>改进建议：</h5>
    <ul>
      <li>可以进一步深入某些细节</li>
      <li>建议补充相关的理论支撑</li>
      <li>表达可以更加简洁明了</li>
    </ul>
  `,
  reviewTime: '2024-01-15 14:30:00'
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
    await ElMessageBox.confirm('重新进行 AI 鉴别将覆盖当前结果，是否继续？', '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })

    isRequestingReview.value = true

    // TODO: 调用后端API进行AI重新评估
    // const response = await ApiRequestAiReview(props.answerId)
    
    // 模拟AI重新评估
    await new Promise(resolve => setTimeout(resolve, 2000))

    // 模拟新的评估结果
    aiAnalysis.value.accuracy = Math.floor(Math.random() * 20) + 80 // 80-99
    aiAnalysis.value.reviewTime = new Date().toISOString()

    ElMessage.success('AI 重新鉴别完成')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('AI 鉴别失败，请重试')
    }
  } finally {
    isRequestingReview.value = false
  }
}

// 加载AI分析数据
const loadAiAnalysis = async () => {
  try {
    // TODO: 调用后端API获取AI分析数据
    // const response = await ApiGetAiAnalysis(props.answerId)
    // if (response.status === 200 && response.data) {
    //   aiAnalysis.value = response.data
    // }
    
    console.log('加载AI分析数据，回答ID:', props.answerId)
  } catch (error) {
    console.error('加载AI分析数据失败:', error)
  }
}

// 生命周期
onMounted(() => {
  if (props.answerId) {
    loadAiAnalysis()
  }
})
</script>

<style scoped>
.ai-analysis-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1rem 0;
  border-bottom: 1px solid #f0f0f0;
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
  .ai-analysis-section {
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
}
</style>