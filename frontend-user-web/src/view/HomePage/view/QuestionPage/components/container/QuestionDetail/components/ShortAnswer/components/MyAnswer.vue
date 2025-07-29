<template>
  <div class="my-answer">
    <!-- 已提交答案状态 -->
    <div v-if="hasSubmittedAnswer" class="submitted-answer">
      <div class="answer-header">
        <h3 class="section-title">我的回答</h3>


        <div class="submit-time">
          <span>提交时间：{{ formatTime(userAnswer.submitTime) }}</span>
        </div>

        <el-button
            :loading="isReAnswering"
            size="small"
            type="warning"
            @click="reAnswerQuestion"
        >
          重新回答
        </el-button>
      </div>

      <!-- AI 分析组件 -->
      <AiAnalysisSection ref="aiAnalysisRef" :answer-id="userAnswer.id" />

      <div class="answer-content" v-html="sanitizedUserAnswer"></div>
    </div>

    <!-- 未提交答案状态 -->
    <div v-else class="answer-editor">
      <div class="editor-header">
        <h3 class="section-title">我的回答</h3>
        <div :class="{ 'unsaved': hasUnsavedChanges }" class="save-status">

          <span>{{ hasUnsavedChanges ? '有未保存的更改' : '已保存' }}</span>
        </div>
      </div>

      <div class="editor-container">
        <el-input
            v-model="currentAnswer"
            :rows="12"
            class="answer-textarea"
            placeholder="请输入您的答案..."
            resize="vertical"
            type="textarea"
            @input="onAnswerChange"
        />
      </div>

      <div class="editor-actions">
        <div class="action-left">
          <span class="word-count">{{ currentAnswer.length }} 字</span>
        </div>
        <div class="action-right">
          <el-button
              :disabled="!hasUnsavedChanges"
              :loading="isSaving"
              @click="saveToLocal"
          >
            保存草稿
          </el-button>
          <el-button
              :disabled="!currentAnswer.trim()"
              :loading="isSubmitting"
              type="primary"
              @click="submitAnswer"
          >
            提交答案
          </el-button>
        </div>
      </div>
    </div>


  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, watch} from 'vue'
import DOMPurify from 'dompurify'
import {ElMessage, ElMessageBox} from 'element-plus'
import AiAnalysisSection from './AiAnalysisSection.vue'
import {type AnswerRecordResponse, ApiGetAnswerRecord} from '../../../../../../service/ApiGetAnswerRecord'
import {ApiSaveAnswer, type SaveAnswerRequest} from '../../../../../../service/ApiSaveAnswer'
import {ApiSubmitAnswer, type SubmitAnswerRequest} from '../../../../../../service/ApiSubmitAnswer'
import {ApiRetryAnswer, type RetryAnswerRequest} from '../../../../../../service/ApiRetryAnswer'
import {ApiSendAiAnalysis, type SendAiAnalysisRequest} from '../../../../../../service/ApiSendAiAnalysis'

// TypeScript 接口定义
interface UserAnswer {
  content: string
  submitTime: string
  id: string
}



interface ApiResponse<T> {
  status: number
  data: T
  message: string
}

const props = defineProps<{
  question: {
    content: string
    answer: string | null
    virtualId: string
  }
}>()

// 响应式数据
const currentAnswer = ref('')
const hasUnsavedChanges = ref(false)
const isSaving = ref(false)
const isSubmitting = ref(false)
const isReAnswering = ref(false)
const currentAnswerId = ref<number | null>(null)
const aiAnalysisRef = ref<InstanceType<typeof AiAnalysisSection> | null>(null)

// 模拟数据
const userAnswer = ref<UserAnswer | null>(null)

// 计算属性
const hasSubmittedAnswer = computed(() => !!userAnswer.value)
const sanitizedUserAnswer = computed(() => {
  const content = userAnswer.value?.content || ''
  // 将换行符转换为HTML的<br>标签
  const contentWithBreaks = content.replace(/\n/g, '<br>')
  return DOMPurify.sanitize(contentWithBreaks)
})


// 方法
const onAnswerChange = () => {
  hasUnsavedChanges.value = true
}

const saveToLocal = async () => {
  isSaving.value = true
  try {
    // 调用API保存答案
    const saveRequest: SaveAnswerRequest = {
      virtualId: props.question.virtualId,
      answer: currentAnswer.value
    }

    const response = await ApiSaveAnswer(saveRequest)

    if (response.status === 200) {
      hasUnsavedChanges.value = false
      ElMessage.success('草稿已保存')
    } else {
      ElMessage.error(response.message || '保存失败，请重试')
    }
  } catch (error: any) {
    console.error('保存答案失败:', error)
    ElMessage.error('保存失败，请重试')
  } finally {
    isSaving.value = false
  }
}

const submitAnswer = async () => {
  if (!currentAnswer.value.trim()) {
    ElMessage.warning('请输入答案内容')
    return
  }

  try {
    await ElMessageBox.confirm('确定要提交答案吗？', '确认提交', {
      confirmButtonText: '确定提交',
      cancelButtonText: '取消',
      type: 'warning'
    })

    isSubmitting.value = true

    // 1. 先保存答案
    const saveRequest: SaveAnswerRequest = {
      virtualId: props.question.virtualId,
      answer: currentAnswer.value
    }

    const saveResponse = await ApiSaveAnswer(saveRequest)
    if (saveResponse.status !== 200) {
      ElMessage.error(saveResponse.message || '保存失败，无法提交')
      return
    }

    // 2. 如果没有答题记录ID，需要重新获取
    if (!currentAnswerId.value) {
      const recordResponse = await ApiGetAnswerRecord(props.question.virtualId)
      if (recordResponse.status === 200 && recordResponse.data) {
        currentAnswerId.value = recordResponse.data.id
      } else {
        ElMessage.error('获取答题记录失败，无法提交')
        return
      }
    }

    // 3. 调用提交接口
    const submitRequest: SubmitAnswerRequest = {
      answerId: currentAnswerId.value!
    }

    const submitResponse = await ApiSubmitAnswer(submitRequest)

    if (submitResponse.status === 200) {
      // 提交成功，设置为已提交状态
      userAnswer.value = {
        content: currentAnswer.value,
        submitTime: new Date().toISOString(),
        id: currentAnswerId.value!.toString()
      }

      const submittedAnswerId = currentAnswerId.value!
      currentAnswer.value = ''
      hasUnsavedChanges.value = false
      currentAnswerId.value = null

      ElMessage.success('答案提交成功！')

      // 自动发起AI分析请求
      setTimeout(async () => {
        try {
          // 先设置AI分析状态为正在分析中
          if (aiAnalysisRef.value) {
            aiAnalysisRef.value.setAnalyzingStatus()
          }

          const aiRequest: SendAiAnalysisRequest = {
            answerId: submittedAnswerId
          }
          const response = await ApiSendAiAnalysis(aiRequest)

          if (response.status === 200) {
            // 开始轮询检查状态
            if (aiAnalysisRef.value) {
              aiAnalysisRef.value.startPolling()
            }
          } else {
            // 如果请求失败，重置状态为未分析
            if (aiAnalysisRef.value) {
              aiAnalysisRef.value.setNotAnalyzedStatus()
            }
          }
        } catch (error) {
          console.error('自动发起AI分析失败:', error)
          // 如果请求失败，重置状态为未分析
          if (aiAnalysisRef.value) {
            aiAnalysisRef.value.setNotAnalyzedStatus()
          }
        }
      }, 500)

    } else {
      ElMessage.error(submitResponse.message || '提交失败，请重试')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('提交答案失败:', error)
      ElMessage.error('提交失败，请重试')
    }
  } finally {
    isSubmitting.value = false
  }
}



const formatTime = (timeStr: string) => {
  return new Date(timeStr).toLocaleString('zh-CN')
}

const reAnswerQuestion = async () => {
  try {
    await ElMessageBox.confirm('重新回答将清除当前答案和AI评估结果，是否确定继续？', '确认重新回答', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    if (!userAnswer.value?.id) {
      ElMessage.error('无法获取答案ID，请刷新页面重试')
      return
    }

    isReAnswering.value = true

    // 调用重新回答API
    const retryRequest: RetryAnswerRequest = {
      answerId: parseInt(userAnswer.value.id)
    }

    const response = await ApiRetryAnswer(retryRequest)

    if (response.status === 200) {
      // 保存原答案内容
      const originalAnswer = userAnswer.value.content

      // 重置到未提交状态，但保留答案内容
      userAnswer.value = null
      currentAnswer.value = originalAnswer
      hasUnsavedChanges.value = false
      currentAnswerId.value = null

      ElMessage.success('已重置，可以重新回答')
    } else {
      ElMessage.error(response.message || '重置失败，请重试')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('重新回答失败:', error)
      ElMessage.error('重置失败，请重试')
    }
  } finally {
    isReAnswering.value = false
  }
}

// 加载答题记录
const loadAnswerRecord = async () => {
  try {
    const response = await ApiGetAnswerRecord(props.question.virtualId)

    if (response.status === 200 && response.data) {
      const answerRecord: AnswerRecordResponse = response.data
      currentAnswerId.value = answerRecord.id

      if (answerRecord.isSubmitted) {
        // 已提交状态
        userAnswer.value = {
          content: answerRecord.answer,
          submitTime: answerRecord.updateTime,
          id: answerRecord.id.toString()
        }
      } else {
        // 未提交状态，加载到编辑器
        currentAnswer.value = answerRecord.answer
        hasUnsavedChanges.value = false
      }
    } else {
      // 没有答题记录，初始化为空
      currentAnswer.value = ''
      hasUnsavedChanges.value = false
      currentAnswerId.value = null
    }
  } catch (error) {
    console.error('加载答题记录失败:', error)
    // 加载失败时，初始化为空
    currentAnswer.value = ''
    hasUnsavedChanges.value = false
    currentAnswerId.value = null
  }
}

// 生命周期
onMounted(() => {
  loadAnswerRecord()
})

// 监听问题变化
watch(
    () => props.question.virtualId,
    () => {
      // 重置状态
      currentAnswer.value = ''
      hasUnsavedChanges.value = false
      userAnswer.value = null
      currentAnswerId.value = null

      // 重新加载答题记录
      loadAnswerRecord()
    }
)
</script>

<style scoped>
.my-answer {
  font-family: 'Inter', sans-serif;
  color: #2c3e50;
}

/* 通用样式 */
.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

/* 已提交答案状态 */
.submitted-answer {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.answer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #e4e7ed;
}



.submit-time {
  color: #909399;
  font-size: 0.9rem;
}




:deep(.answer-content) {
  font-size: 1.05rem;
  line-height: 1.8;
  color: #2c3e50;
  padding: 1rem 0;
  white-space: pre-wrap;
  word-wrap: break-word;

  p {
    margin-bottom: 1.2em;
  }

  h4, h5 {
    margin-top: 1.5em;
    margin-bottom: 0.8em;
    font-weight: 600;
    color: #2c3e50;
  }

  ul, ol {
    padding-left: 1.5em;
    margin-bottom: 1.2em;
  }

  li {
    margin-bottom: 0.5em;
  }
}


/* 未提交答案状态 */
.answer-editor {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #e4e7ed;
}

.save-status {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.9rem;
  color: #67c23a;
  transition: color 0.3s;
}

.save-status.unsaved {
  color: #f56c6c;
}

.editor-container {
  position: relative;
}

:deep(.answer-textarea) {
  .el-textarea__inner {
    font-family: inherit;
    font-size: 1rem;
    line-height: 1.6;
    border-radius: 6px;
    border: 1px solid #dcdfe6;
    transition: border-color 0.3s;
  }

  .el-textarea__inner:focus {
    border-color: #409eff;
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
  }
}

.editor-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 0.75rem;
  border-top: 1px solid #f0f0f0;
}

.action-left {
  color: #909399;
  font-size: 0.9rem;
}

.word-count {
  font-weight: 500;
}

.action-right {
  display: flex;
  gap: 0.75rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .answer-header,
  .editor-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }



  .editor-actions {
    flex-direction: column;
    gap: 0.75rem;
    align-items: stretch;
  }

  .action-right {
    justify-content: stretch;
  }

  .action-right .el-button {
    flex: 1;
  }
}
</style>