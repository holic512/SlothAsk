<template>
  <div class="fourth-scene">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="floating-particles">
        <div 
          v-for="particle in particles" 
          :key="particle.id"
          :style="{
            left: particle.x + 'px',
            top: particle.y + 'px',
            width: particle.size + 'px',
            height: particle.size + 'px',
            opacity: particle.opacity,
            animationDelay: particle.delay + 's'
          }"
          class="particle"
        ></div>
      </div>
      <div class="gradient-orbs">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
        <div class="orb orb-3"></div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-area">
      <!-- 标题区域 -->
      <div class="title-section">
        <h2 class="main-title">开始您的学习之旅</h2>
        <p class="subtitle">
          准备好了吗？让我们一起探索知识的海洋<br>
          您的智能学习伙伴正在等待您的到来
        </p>
      </div>

      <!-- 行动按钮区域 -->
      <div class="action-section">
        <el-button class="start-btn" size="large" type="primary" @click="enterApp">
          <span>立即开始</span>
          <i class="arrow">→</i>
        </el-button>
        
        <!-- 额外的引导信息 -->
        <div class="guide-info">
          <div class="info-item">
            <span class="info-icon">🚀</span>
            <span>即刻开启智能学习</span>
          </div>
          <div class="info-item">
            <span class="info-icon">💡</span>
            <span>个性化学习路径</span>
          </div>
          <div class="info-item">
            <span class="info-icon">🎯</span>
            <span>高效达成学习目标</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useFirstVisitStore } from '@/pinia/FirstVisitStore.ts'

// 定义props
const props = defineProps({
  isActive: {
    type: Boolean,
    default: false
  }
})

// 定义emits
const emit = defineEmits(['scene-ready'])

const router = useRouter()
const firstVisitStore = useFirstVisitStore()

// 粒子系统
const particles = ref([])
const animationId = ref(null)

// 进入应用
const enterApp = () => {
  firstVisitStore.markAsVisited()
  router.push('/study')
}

// 初始化粒子
const initParticles = () => {
  particles.value = []
  const screenWidth = window.innerWidth
  let particleCount = 30
  
  if (screenWidth <= 480) {
    particleCount = 15
  } else if (screenWidth <= 768) {
    particleCount = 20
  }
  
  for (let i = 0; i < particleCount; i++) {
    createParticle()
  }
}

// 创建单个粒子
const createParticle = () => {
  const particle = {
    id: Math.random(),
    x: Math.random() * window.innerWidth,
    y: Math.random() * window.innerHeight,
    size: Math.random() * 4 + 2,
    opacity: Math.random() * 0.5 + 0.3,
    vx: (Math.random() - 0.5) * 1,
    vy: (Math.random() - 0.5) * 1,
    delay: Math.random() * 2
  }
  particles.value.push(particle)
}

// 动画粒子
const animateParticles = () => {
  particles.value.forEach((particle) => {
    particle.x += particle.vx
    particle.y += particle.vy
    
    // 边界检测和重置
    if (particle.x < 0 || particle.x > window.innerWidth) {
      particle.vx *= -1
    }
    if (particle.y < 0 || particle.y > window.innerHeight) {
      particle.vy *= -1
    }
    
    // 保持在屏幕内
    particle.x = Math.max(0, Math.min(window.innerWidth, particle.x))
    particle.y = Math.max(0, Math.min(window.innerHeight, particle.y))
  })
  
  animationId.value = requestAnimationFrame(animateParticles)
}

// 窗口大小变化处理
const handleResize = () => {
  initParticles()
}

onMounted(() => {
  // 初始化粒子系统
  initParticles()
  animateParticles()
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
  
  // 通知父组件场景已准备就绪
  emit('scene-ready')
})

onUnmounted(() => {
  // 清理动画和事件监听
  if (animationId.value) {
    cancelAnimationFrame(animationId.value)
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.fourth-scene {
  position: relative;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

.floating-particles {
  position: absolute;
  width: 100%;
  height: 100%;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: sparkle 3s ease-in-out infinite;
  will-change: transform, opacity;
}

.gradient-orbs {
  position: absolute;
  width: 100%;
  height: 100%;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(50px);
  opacity: 0.1;
  animation: floatOrb 25s ease-in-out infinite;
}

.orb-1 {
  width: 250px;
  height: 250px;
  background: #ff9a9e;
  top: 15%;
  left: 15%;
  animation-delay: 0s;
}

.orb-2 {
  width: 200px;
  height: 200px;
  background: #a8edea;
  top: 65%;
  right: 20%;
  animation-delay: -8s;
}

.orb-3 {
  width: 180px;
  height: 180px;
  background: #ffecd2;
  bottom: 25%;
  left: 25%;
  animation-delay: -16s;
}

.content-area {
  text-align: center;
  position: relative;
  z-index: 1;
  max-width: 800px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3rem;
}

.title-section {
  margin-bottom: 1rem;
}

.main-title {
  font-size: 3.5rem;
  font-weight: 300;
  margin: 0 0 1.5rem 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  opacity: 0;
  animation: fadeInUp 1s ease 0.3s forwards;
}

.subtitle {
  font-size: 1.3rem;
  line-height: 1.8;
  margin: 0;
  opacity: 0.9;
  text-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
  opacity: 0;
  animation: fadeInUp 1s ease 0.6s forwards;
}

.action-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  opacity: 0;
  animation: fadeInUp 1s ease 0.9s forwards;
}

.start-btn {
  padding: 1.2rem 3rem;
  font-size: 1.2rem;
  border-radius: 50px;
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  display: inline-flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  font-weight: 500;
  letter-spacing: 0.02em;
}

.start-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-3px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.arrow {
  font-size: 1.2rem;
  transition: transform 0.3s ease;
}

.start-btn:hover .arrow {
  transform: translateX(5px);
}

.guide-info {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 2rem;
  margin-top: 1rem;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  opacity: 0.9;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.8rem 1.2rem;
  border-radius: 25px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.info-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.info-icon {
  font-size: 1.2rem;
}

@keyframes sparkle {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2);
  }
}

@keyframes floatOrb {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  33% {
    transform: translateY(-30px) rotate(120deg);
  }
  66% {
    transform: translateY(15px) rotate(240deg);
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
  .main-title {
    font-size: 3rem;
  }
  
  .guide-info {
    gap: 1.5rem;
  }
}

@media (max-width: 768px) {
  .fourth-scene {
    padding: 1rem;
  }
  
  .main-title {
    font-size: 2.5rem;
  }
  
  .subtitle {
    font-size: 1.1rem;
  }
  
  .start-btn {
    padding: 1rem 2rem;
    font-size: 1.1rem;
  }
  
  .guide-info {
    flex-direction: column;
    gap: 1rem;
  }
  
  .info-item {
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .content-area {
    gap: 2rem;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
  
  .start-btn {
    width: 100%;
    max-width: 280px;
  }
  
  .orb-2, .orb-3 {
    display: none;
  }
  
  .floating-particles {
    display: none;
  }
}

/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  .particle {
    animation: none;
  }
  
  .orb {
    animation: none;
  }
  
  .main-title,
  .subtitle,
  .action-section {
    animation: none;
    opacity: 1;
  }
  
  .start-btn {
    transition: background 0.2s ease;
  }
  
  .arrow {
    transition: none;
  }
}

/* 性能优化 */
.fourth-scene * {
  box-sizing: border-box;
}

.particle,
.orb,
.start-btn,
.info-item {
  will-change: transform;
}
</style>