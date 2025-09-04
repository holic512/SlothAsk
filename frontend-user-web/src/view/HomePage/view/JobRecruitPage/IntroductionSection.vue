<template>
  <div class="introduction-section">
    <div class="container">
      <div class="intro-content">
        <!-- 主标题 -->
        <h1 class="main-title">找工作，更轻松</h1>
        
        <!-- 副标题 -->
        <p class="subtitle">汇聚优质岗位信息，让求职变得简单高效</p>
        
        <!-- 功能亮点 -->
        <div class="features">
          <div class="feature-item">
            <el-icon class="feature-icon"><Refresh /></el-icon>
            <span>实时更新</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Management /></el-icon>
            <span>进度跟踪</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Monitor /></el-icon>
            <span>便捷管理</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { Refresh, Management, Monitor } from '@element-plus/icons-vue'
// 介绍组件 - 展示公司招聘的总体介绍和亮点
</script>

<style scoped>
.introduction-section {
  background: white;
  padding: 30px 0 0 0;
  text-align: left;
}

.container {
  max-width: 1200px;
  margin: 0 auto ;
  padding-bottom: 24px;
}

.intro-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

/* 主标题样式 */
.main-title {
  font-size: 2.5rem;
  color: #2c3e50;
  margin: 0 0 16px 0;
  font-weight: 700;
  line-height: 1.2;
}

/* 副标题样式 */
.subtitle {
  font-size: 1.1rem;
  color: #666;
  margin: 0 0 16px 0;
  line-height: 1.5;
  font-weight: 400;
}

/* 功能特点容器 */
.features {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

/* 单个功能项 */
.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: #f8f9fa;
  border-radius: 25px;
  color: #495057;
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.3s ease;
  border: 1px solid #e9ecef;
}

.feature-item:hover {
  background: #e3f2fd;
  border-color: #2196f3;
  color: #1976d2;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.15);
}

.feature-icon {
  font-size: 16px;
  color: inherit;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .introduction-section {
    padding: 25px 0;
    text-align: center;
  }
  
  .intro-content {
    gap: 20px;
    align-items: center;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 1rem;
    margin-bottom: 24px;
  }
  
  .features {
    justify-content: center;
    gap: 16px;
  }
  
  .feature-item {
    font-size: 0.9rem;
    padding: 10px 16px;
  }
}

@media (max-width: 480px) {
  .main-title {
    font-size: 1.8rem;
  }
  
  .subtitle {
    font-size: 0.95rem;
  }
  
  .features {
    flex-direction: column;
    gap: 12px;
    width: 100%;
  }
  
  .feature-item {
    width: 100%;
    justify-content: center;
    max-width: 200px;
  }
}
</style>