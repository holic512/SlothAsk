<template>
  <div class="third-scene">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="grid-pattern"></div>
      <div class="floating-particles">
        <div v-for="i in 20" :key="i" :style="getParticleStyle(i)" class="particle"></div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-area">
      <!-- 标题区域 -->
      <div class="title-section">
        <h2 class="main-title">平台数据统计</h2>
        <p class="subtitle">实时展示平台核心数据指标</p>
      </div>

      <!-- 统计卡片区域 -->
      <div class="statistics-container">
        <div v-for="(stat, index) in statistics" :key="index" class="statistic-card">
          <div class="card-header">
            <div :class="stat.iconClass" class="icon-wrapper">
              <i :class="stat.icon"></i>
            </div>
            <div :class="stat.trend" class="trend-indicator">
              <i :class="stat.trendIcon"></i>
              <span>{{ stat.trendValue }}</span>
            </div>
          </div>
          <div class="card-content">
            <div class="statistic-value">
              <span class="number">{{ formatNumber(stat.value) }}</span>
              <span class="unit">{{ stat.unit }}</span>
            </div>
            <div class="statistic-label">{{ stat.label }}</div>
            <div class="statistic-description">{{ stat.description }}</div>
          </div>
          <div class="card-footer">
            <div class="progress-bar">
              <div :style="{ width: stat.progress + '%', backgroundColor: stat.color }" class="progress-fill"></div>
            </div>
            <span class="progress-text">{{ stat.progressText }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// 定义props
const props = defineProps({
  isActive: {
    type: Boolean,
    default: false
  }
})

// 定义emits
const emit = defineEmits(['scene-ready'])

// 统计数据
const statistics = ref([
  {
    label: '注册人数',
    value: 128456,
    unit: '人',
    description: '累计注册用户',
    icon: 'el-icon-user',
    iconClass: 'user-icon',
    color: '#409EFF',
    trend: 'up',
    trendIcon: 'el-icon-top',
    trendValue: '+12.5%',
    progress: 85,
    progressText: '本月新增 15,234 人'
  },
  {
    label: '在线人数',
    value: 3247,
    unit: '人',
    description: '当前在线用户',
    icon: 'el-icon-connection',
    iconClass: 'online-icon',
    color: '#67C23A',
    trend: 'up',
    trendIcon: 'el-icon-top',
    trendValue: '+8.3%',
    progress: 65,
    progressText: '较昨日增长 8.3%'
  },
  {
    label: '题目数量',
    value: 45678,
    unit: '道',
    description: '平台题目总数',
    icon: 'el-icon-document',
    iconClass: 'question-icon',
    color: '#E6A23C',
    trend: 'up',
    trendIcon: 'el-icon-top',
    trendValue: '+5.2%',
    progress: 92,
    progressText: '本周新增 1,234 道'
  },
  {
    label: '答题次数',
    value: 2567890,
    unit: '次',
    description: '累计答题次数',
    icon: 'el-icon-edit',
    iconClass: 'answer-icon',
    color: '#F56C6C',
    trend: 'up',
    trendIcon: 'el-icon-top',
    trendValue: '+15.7%',
    progress: 78,
    progressText: '今日答题 45,678 次'
  }
])

// 格式化数字显示
const formatNumber = (num) => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

// 生成粒子样式
const getParticleStyle = (index) => {
  const size = Math.random() * 4 + 2
  const left = Math.random() * 100
  const animationDelay = Math.random() * 10
  const animationDuration = Math.random() * 20 + 10
  
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    animationDelay: `${animationDelay}s`,
    animationDuration: `${animationDuration}s`
  }
}

onMounted(() => {
  // 通知父组件场景已准备就绪
  emit('scene-ready')
})
</script>

<style scoped>
.third-scene {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 2rem;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.grid-pattern {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(64, 158, 255, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(64, 158, 255, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
}

.floating-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.particle {
  position: absolute;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  border-radius: 50%;
  opacity: 0.6;
  animation: particleFloat 15s infinite linear;
}

.content-area {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 80rem;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.title-section {
  margin-bottom: 4rem;
  max-width: 50rem;
}

.main-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
}

.subtitle {
  font-size: 1rem;
  font-weight: 400;
  color: #606266;
  line-height: 1.6;
  max-width: 40rem;
  margin: 0 auto;
}

.statistics-container {
  display: grid;
  gap: 1rem;
  max-width: 60rem;
  width: 100%;
  padding: 0 1rem;
  grid-template-columns: repeat(4, 1fr);
}

.statistic-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #EBEEF5;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  min-height: 160px;
}

.statistic-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--card-color, #409EFF) 0%, rgba(255, 255, 255, 0) 100%);
}

.statistic-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  border-color: #C6E2FF;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.icon-wrapper {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  color: #ffffff;
  position: relative;
}

.user-icon {
  background: linear-gradient(135deg, #409EFF, #66B1FF);
}

.online-icon {
  background: linear-gradient(135deg, #67C23A, #85CE61);
}

.question-icon {
  background: linear-gradient(135deg, #E6A23C, #EEBC6C);
}

.answer-icon {
  background: linear-gradient(135deg, #F56C6C, #F78989);
}

.trend-indicator {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.5rem;
  border-radius: 1rem;
  background: rgba(103, 194, 58, 0.1);
  color: #67C23A;
}

.card-content {
  margin-bottom: 1rem;
}

.statistic-value {
  display: flex;
  align-items: baseline;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #303133;
  line-height: 1;
}

.unit {
  font-size: 0.875rem;
  color: #909399;
  font-weight: 500;
}

.statistic-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: #606266;
  margin-bottom: 0.25rem;
}

.statistic-description {
  font-size: 0.75rem;
  color: #909399;
  line-height: 1.4;
}

.card-footer {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.progress-bar {
  width: 100%;
  height: 4px;
  background: #F5F7FA;
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 2px;
  transition: width 0.3s ease;
  background: linear-gradient(90deg, currentColor 0%, rgba(255, 255, 255, 0.3) 100%);
}

.progress-text {
  font-size: 0.75rem;
  color: #909399;
  text-align: left;
}

@keyframes gridMove {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(50px, 50px);
  }
}

@keyframes particleFloat {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 0.6;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
    opacity: 0;
  }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .statistics-container {
    grid-template-columns: repeat(2, 1fr);
    max-width: 45rem;
    gap: 1.5rem;
  }
  
  .statistic-card {
    padding: 1.25rem;
    min-height: 140px;
  }
  
  .number {
    font-size: 1.25rem;
  }
}

@media (max-width: 768px) {
  .third-scene {
    padding: 1rem;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .title-section {
    margin-bottom: 2rem;
  }
  
  .statistics-container {
    grid-template-columns: 1fr;
    max-width: 25rem;
    gap: 1rem;
  }
  
  .statistic-card {
    padding: 1rem;
    min-height: 120px;
  }
  
  .number {
    font-size: 1.125rem;
  }
  
  .icon-wrapper {
    width: 2rem;
    height: 2rem;
    font-size: 0.875rem;
  }
}

@media (max-width: 480px) {
  .main-title {
    font-size: 1.5rem;
  }
  
  .statistic-card {
    padding: 0.875rem;
    min-height: 110px;
  }
  
  .number {
    font-size: 1rem;
  }
  
  .statistic-label {
    font-size: 0.75rem;
  }
  
  .unit {
    font-size: 0.75rem;
  }
}
</style>