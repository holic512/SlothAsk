<template>
  <div class="second-scene">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="floating-shapes">
        <!-- 减少背景装饰元素数量，从4个减少到2个 -->
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-area">
      <!-- 标题区域 -->
      <div class="title-section">
        <h2 class="main-title">探索无限可能</h2>
        <p class="subtitle">点击卡片，发现SlothAsk的强大功能</p>
      </div>

      <!-- 卡片网格 -->
      <div class="cards-grid">
        <FlipCard
          v-for="(card, index) in cards"
          :key="card.id"
          :front-bg="card.frontBg"
          :front-icon="card.frontIcon"
          :front-text="card.frontText"
          :label="card.label"
          @click="onCardClick(index, $event)"
        />
      </div>

      <!-- 漂浮气泡 - 支持多个 -->
      <div 
        v-for="tooltip in tooltips" 
        :key="tooltip.id"
        :style="{
          left: tooltip.x + 'px',
          top: tooltip.y + 'px'
        }"
        class="floating-tooltip"
      >
        {{ tooltip.content }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import FlipCard from './FlipCard.vue'
import { getCardConfigs } from './cardConfig.js'

// 网格配置
const GRID_SIZE = 5
const TOTAL_CARDS = GRID_SIZE * GRID_SIZE

// 响应式数据
const cards = ref([])
const tooltips = ref([]) // 改为数组支持多个气泡
let tooltipIdCounter = 0 // 气泡ID计数器

// 初始化卡片数据
const initCards = () => {
  cards.value = getCardConfigs(TOTAL_CARDS)
}



// 卡片点击事件
const onCardClick = (index, event) => {
  const card = cards.value[index]
  
  // 在鼠标位置附近随机生成气泡位置
  const randomOffsetX = (Math.random() - 0.5) * 250 // -50px 到 50px 的随机偏移
  const randomOffsetY = (Math.random() - 0.5) * 250  // -30px 到 30px 的随机偏移
  
  // 创建新的气泡对象
  const newTooltip = {
    id: tooltipIdCounter++,
    content: card.label,
    x: event.clientX + randomOffsetX,
    y: event.clientY + randomOffsetY,
    show: true
  }
  
  // 添加到气泡数组
  tooltips.value.push(newTooltip)
  
  // 3秒后移除这个气泡
  setTimeout(() => {
    const index = tooltips.value.findIndex(t => t.id === newTooltip.id)
    if (index > -1) {
      tooltips.value.splice(index, 1)
    }
  }, 3000)
}

onMounted(() => {
  initCards()
})
</script>

<style scoped>
.second-scene {
  position: relative;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 20px;
  box-sizing: border-box;
  color: white;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.floating-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  border-radius: 50%;
  /* 减少模糊强度 */
  filter: blur(40px);
  opacity: 0.08;
  /* 简化动画，减少复杂度 */
  animation: simpleFloat 15s ease-in-out infinite;
}

.shape-1 {
  width: 280px;
  height: 280px;
  background: #ff9a9e;
  top: 15%;
  left: 15%;
  animation-delay: 0s;
}

.shape-2 {
  width: 220px;
  height: 220px;
  background: #a8edea;
  top: 55%;
  right: 15%;
  animation-delay: -7s;
}

.content-area {
  text-align: center;
  position: relative;
  z-index: 1;
  max-width: 1200px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.title-section {
  margin-bottom: 1rem;
}

.main-title {
  font-size: 3rem;
  font-weight: 300;
  margin: 0 0 0.5rem 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  opacity: 0;
  animation: fadeInUp 1s ease 0.3s forwards;
}

.subtitle {
  font-size: 1.2rem;
  margin: 0;
  opacity: 0.9;
  text-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
  opacity: 0;
  animation: fadeInUp 1s ease 0.6s forwards;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
  max-width: 500px;
  margin: 20px auto;
  padding: 15px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  opacity: 0;
  animation: fadeInUp 1s ease 0.9s forwards;
  /* 添加CSS containment优化性能 */
  contain: layout style paint;
}

/* 漂浮气泡样式 - 简化阴影效果 */
.floating-tooltip {
  position: fixed;
  background: rgba(255, 255, 255, 0.95);
  color: #333;
  padding: 12px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
  z-index: 1000;
  pointer-events: none;
  transform: translate(-50%, -50%);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  animation: cloudFloat 3s ease-out forwards;
  /* 动画时添加will-change */
  will-change: transform, opacity;
}

/* 云朵形状的伪元素 */
.floating-tooltip::before {
  content: '';
  position: absolute;
  top: -8px;
  left: 20%;
  width: 16px;
  height: 16px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  box-shadow: 
    -8px 0 0 -2px rgba(255, 255, 255, 0.8),
    8px 0 0 -2px rgba(255, 255, 255, 0.8);
}

.floating-tooltip::after {
  content: '';
  position: absolute;
  top: -6px;
  right: 25%;
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.85);
  border-radius: 50%;
  box-shadow: 
    -6px 0 0 -1px rgba(255, 255, 255, 0.7),
    6px 0 0 -1px rgba(255, 255, 255, 0.7);
}

@keyframes cloudFloat {
  0% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(0.5) translateY(20px);
  }
  20% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1) translateY(0px);
  }
  100% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(1.2) translateY(-80px);
  }
}

/* 简化的背景动画，移除旋转减少计算复杂度 */
@keyframes simpleFloat {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}



@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .flip-grid {
    gap: 6px;
    padding: 0.8rem;
  }
  
  .scene-title {
    font-size: 2.5rem;
  }
}

@media (max-width: 768px) {
  .second-scene {
    padding: 1rem;
  }
  
  .flip-grid {
    gap: 4px;
    padding: 0.6rem;
  }
  
  .scene-title {
    font-size: 2rem;
  }
  
  .scene-subtitle {
    font-size: 1rem;
  }
  

}

@media (max-width: 480px) {
  .flip-grid {
    gap: 3px;
    padding: 0.5rem;
  }
  
  .scene-title {
    font-size: 1.8rem;
  }
  
  .orb-2, .orb-3 {
    display: none;
  }
}

/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  .orb {
    animation: none;
  }
  
  .main-title,
  .subtitle,
  .cards-grid {
    animation: none;
    opacity: 1;
  }
}
</style>