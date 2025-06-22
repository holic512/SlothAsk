<template>
  <el-dialog
    v-model="visible"
    :before-close="handleClose"
    class="ai-analysis-dialog"
    title="AI 详细解析"
    width="45%"
  >
    <div class="dialog-content">
      <div class="analysis-header">
        <div class="header-info">
          <i class="el-icon-cpu"></i>
          <span class="analysis-title">智能分析报告</span>
        </div>
        <div class="accuracy-display">
          <span class="accuracy-label">正确率</span>
          <span class="accuracy-value">{{ aiAnalysis.accuracy }}%</span>
          <div class="accuracy-bar">
            <div :style="{ width: aiAnalysis.accuracy + '%' }" class="accuracy-fill"></div>
          </div>
        </div>
      </div>
      
      <div class="analysis-content">
        <div class="analysis-text" v-html="sanitizedAnalysis"></div>
      </div>
      
      <div class="analysis-footer">
        <div class="footer-info">
          <div class="review-time">
            <i class="el-icon-time"></i>
            <span>分析时间：{{ formatTime(aiAnalysis.reviewTime) }}</span>
          </div>
          <div class="ai-source">
            <i class="el-icon-cpu"></i>
            <span>来源模型：{{ aiAnalysis.aiSource }}</span>
          </div>
        </div>
      </div>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleClose">
          关闭
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {computed, ref} from 'vue'
import DOMPurify from 'dompurify'

// TypeScript 接口定义
interface AiAnalysis {
  accuracy: number
  analysis: string
  reviewTime: string
  aiSource: string
}

const props = defineProps<{
  modelValue: boolean
  aiAnalysis: AiAnalysis
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'reAnalysis': []
}>()

// 响应式数据
const isReAnalyzing = ref(false)

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const sanitizedAnalysis = computed(() => 
  DOMPurify.sanitize(props.aiAnalysis.analysis || '')
)

// 方法
const handleClose = () => {
  visible.value = false
}

const formatTime = (timeStr: string) => {
  return new Date(timeStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
:deep(.ai-analysis-dialog) {
  .el-dialog__header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 1.5rem;
    border-radius: 8px 8px 0 0;
  }
  
  .el-dialog__title {
    color: white;
    font-weight: 600;
    font-size: 1.2rem;
  }
  
  .el-dialog__body {
    padding: 0;
  }
  
  .el-dialog__footer {
    background: #f8f9fa;
    border-radius: 0 0 8px 8px;
  }
}

.dialog-content {
  padding: 1.5rem;
}

.analysis-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f0f0f0;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.header-info i {
  font-size: 1.5rem;
  color: #409eff;
}

.analysis-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
}

.accuracy-display {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: #f8f9fa;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.accuracy-label {
  font-weight: 500;
  color: #606266;
  font-size: 0.9rem;
}

.accuracy-value {
  font-weight: 700;
  color: #67c23a;
  font-size: 1.2rem;
}

.accuracy-bar {
  width: 100px;
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.accuracy-fill {
  height: 100%;
  background: linear-gradient(90deg, #67c23a, #85ce61);
  transition: width 0.3s ease;
}

.analysis-content {
  margin-bottom: 1.5rem;
}

:deep(.analysis-text) {
  font-size: 1rem;
  line-height: 1.8;
  color: #2c3e50;
  
  h4, h5 {
    margin-top: 1.5em;
    margin-bottom: 0.8em;
    font-weight: 600;
    color: #2c3e50;
    border-left: 4px solid #409eff;
    padding-left: 0.75rem;
  }
  
  h4 {
    font-size: 1.1rem;
    color: #409eff;
  }
  
  h5 {
    font-size: 1rem;
    color: #606266;
  }
  
  p {
    margin-bottom: 1.2em;
    text-align: justify;
  }
  
  ul {
    padding-left: 1.5em;
    margin-bottom: 1.2em;
  }
  
  li {
    margin-bottom: 0.6em;
    position: relative;
  }
  
  li::marker {
    color: #409eff;
  }
  
  /* 代码块样式 */
  pre, code {
    background-color: #f5f7fa;
    border-radius: 4px;
    font-family: 'Fira Code', 'Consolas', monospace;
    padding: 0.2em 0.4em;
    font-size: 0.9em;
  }
  
  pre {
    padding: 1em;
    overflow-x: auto;
    margin: 1.2em 0;
    border: 1px solid #e4e7ed;
  }
  
  /* 引用样式 */
  blockquote {
    border-left: 4px solid #409eff;
    padding-left: 1em;
    margin: 1.2em 0;
    color: #555;
    font-style: italic;
    background: #f8f9fa;
    padding: 1rem;
    border-radius: 0 4px 4px 0;
  }
}

.analysis-footer {
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
}

.footer-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.review-time,
.ai-source {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #909399;
  font-size: 0.9rem;
}

.review-time i,
.ai-source i {
  color: #c0c4cc;
}

.ai-source {
  color: #606266;
}

.ai-source i {
  color: #409eff;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  :deep(.ai-analysis-dialog) {
    .el-dialog {
      width: 95% !important;
      margin: 5vh auto;
    }
  }
  
  .analysis-header {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }
  
  .accuracy-display {
    width: 100%;
    justify-content: space-between;
  }
  
  .footer-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
  
  .dialog-footer {
    flex-direction: column;
  }
  
  .dialog-footer .el-button {
    width: 100%;
  }
}
</style>