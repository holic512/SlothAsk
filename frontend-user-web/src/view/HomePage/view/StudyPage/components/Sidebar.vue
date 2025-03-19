<template>
  <div class="sidebar">
    <h2 class="sidebar-title">推荐学习</h2>
    
    <div class="sidebar-section">
      <h3 class="section-title">热门题目</h3>
      <div class="popular-questions">
        <div v-for="i in 5" :key="i" class="question-item">
          <span class="question-number">{{ i }}</span>
          <span class="question-title">Java基础知识选择题</span>
          <span class="view-count">
            <el-icon><View /></el-icon>
            {{ 100 + i * 20 }}
          </span>
        </div>
      </div>
    </div>
    
    <div class="sidebar-section">
      <h3 class="section-title">学习进度</h3>
      <div class="progress-container">
        <div class="progress-stats">
          <div class="stat-item">
            <div class="stat-value">42</div>
            <div class="stat-label">已学习</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">68%</div>
            <div class="stat-label">正确率</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">12</div>
            <div class="stat-label">连续天数</div>
          </div>
        </div>
        <div class="progress-bar-container">
          <div class="progress-label">总体进度</div>
          <div class="progress-bar">
            <div class="progress-fill" style="width: 42%;"></div>
          </div>
          <div class="progress-text">42%</div>
        </div>
      </div>
    </div>
    
    <div class="sidebar-section">
      <h3 class="section-title">学习计划</h3>
      <div class="study-plan">
        <div class="plan-item">
          <div class="plan-icon">📚</div>
          <div class="plan-content">
            <div class="plan-title">Java基础入门</div>
            <div class="plan-progress">已完成 8/20 题</div>
          </div>
        </div>
        <div class="plan-item">
          <div class="plan-icon">💻</div>
          <div class="plan-content">
            <div class="plan-title">Spring框架精通</div>
            <div class="plan-progress">已完成 5/15 题</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { View } from '@element-plus/icons-vue';
</script>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.sidebar-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 12px;
}

.sidebar-section {
  margin-bottom: 8px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  margin: 0 0 16px 0;
  color: #555;
  display: flex;
  align-items: center;
}

.section-title::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 16px;
  background-color: #1890ff;
  margin-right: 8px;
  border-radius: 2px;
}

.popular-questions {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.question-item {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  border-radius: 8px;
  transition: all 0.3s;
  background-color: #f9f9f9;
  cursor: pointer;
}

.question-item:hover {
  background-color: #f0f0f0;
  transform: translateX(2px);
}

.question-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #e6f7ff;
  color: #1890ff;
  font-weight: 600;
  font-size: 12px;
  margin-right: 12px;
  flex-shrink: 0;
}

.question-title {
  flex: 1;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #333;
}

.view-count {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  margin-left: 8px;
}

.view-count .el-icon {
  margin-right: 4px;
  font-size: 14px;
}

.progress-container {
  background-color: #f9f9f9;
  border-radius: 12px;
  padding: 16px;
}

.progress-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.stat-item {
  text-align: center;
  flex: 1;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.progress-bar-container {
  margin-top: 12px;
}

.progress-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.progress-bar {
  height: 10px;
  background-color: #eee;
  border-radius: 5px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background-color: #1890ff;
  border-radius: 5px;
}

.progress-text {
  text-align: right;
  font-size: 12px;
  color: #999;
}

.study-plan {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.plan-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border-radius: 10px;
  background-color: #f9f9f9;
  transition: all 0.3s;
  cursor: pointer;
}

.plan-item:hover {
  background-color: #f0f0f0;
  transform: translateY(-2px);
}

.plan-icon {
  font-size: 20px;
  margin-right: 12px;
  flex-shrink: 0;
}

.plan-content {
  flex: 1;
}

.plan-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.plan-progress {
  font-size: 12px;
  color: #999;
}
</style>
