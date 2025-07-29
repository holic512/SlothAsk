<template>
  <div class="first-scene">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="floating-petals">
        <img
            v-for="petal in petals"
            :key="petal.id"
            :src="petal.image"
            :style="{
            left: petal.x + 'px',
            top: petal.y + 'px',
            transform: `rotate(${petal.rotation}deg)`,
            width: petal.size + 'px',
            height: petal.size + 'px',
            opacity: petal.opacity
          }"
            alt="花瓣"
            class="petal-image"
        />
      </div>
      <div class="gradient-orbs">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
        <div class="orb orb-3"></div>
      </div>
    </div>

    <div class="scene-container">
      <!-- 主标题动画区域 -->
      <div class="title-section">
        <ArtTextReveal
            ref="artTextRef"
            :autoPlay="false"
            :duration="1.5"
            :fontSize="'320px'"
            fillColor="#2c3e50"
            strokeColor="#34495e"
            text="SlothAsk"
            @animationComplete="onAnimationComplete"
            @animationStart="onAnimationStart"
        />
      </div>

      <!-- 内容区域 -->
      <div :class="{ 'show': showContent }" class="intro-content">
        <div class="subtitle">
          <h2>您的智能学习伙伴</h2>
          <p class="description">
            探索知识的海洋，让学习变得更加轻松愉快<br>
            与AI一起，开启您的学习新旅程
          </p>
        </div>

        <div class="action-buttons">
          <el-button class="enter-btn" size="large" type="primary" @click="enterApp">
            <span>开始学习之旅</span>
            <i class="arrow">→</i>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, nextTick, onUnmounted} from 'vue'
import {useRouter} from 'vue-router'
import {useFirstVisitStore} from '@/pinia/FirstVisitStore.js'
import ArtTextReveal from "@/view/IntroPage/components/first/ArtTextReveal.vue";

const router = useRouter()
const firstVisitStore = useFirstVisitStore()
const artTextRef = ref()
const showContent = ref(false)
const petals = ref([])
const mousePosition = ref({x: 0, y: 0})
const animationId = ref(null)
const lastMouseMoveTime = ref(0)

const enterApp = () => {
  // 标记用户已经访问过
  firstVisitStore.markAsVisited()
  // 跳转到主页
  router.push('/study')
}

// 移除重播动画逻辑，优化初始化机制

const onAnimationStart = () => {
  console.log('第一幕动画开始')
  showContent.value = false
}

const onAnimationComplete = () => {
  console.log('第一幕动画完成')
  setTimeout(() => {
    showContent.value = true
  }, 200) // 减少延迟时间，让内容更快显示
}

// 花瓣图片数组
const petalImages = ['/IntroPage/花瓣红.png', '/IntroPage/花瓣蓝.png', '/IntroPage/花瓣黄.png']

// 初始化花瓣 - 响应式数量控制
const initPetals = () => {
  petals.value = []
  const screenWidth = window.innerWidth
  let petalCount = 36 // 默认大屏数量

  if (screenWidth <= 480) {
    petalCount = 12 // 小屏最多3片
  } else if (screenWidth <= 768) {
    petalCount = 6 // 中屏最多6片
  }

  for (let i = 0; i < petalCount; i++) {
    createPetal()
  }
}

// 创建单个花瓣 - 响应式尺寸
const createPetal = () => {
  const screenWidth = window.innerWidth
  let sizeRange = {min: 15, max: 25} // 默认大屏尺寸

  if (screenWidth <= 480) {
    sizeRange = {min: 8, max: 15} // 小屏尺寸
  } else if (screenWidth <= 768) {
    sizeRange = {min: 10, max: 20} // 中屏尺寸
  }

  const petal = {
    id: Math.random(),
    x: Math.random() * window.innerWidth,
    y: -50,
    vx: (Math.random() - 0.5) * 2,
    vy: Math.random() * 2 + 1,
    rotation: Math.random() * 360,
    rotationSpeed: (Math.random() - 0.5) * 4,
    size: Math.random() * (sizeRange.max - sizeRange.min) + sizeRange.min,
    opacity: Math.random() * 0.6 + 0.4,
    image: petalImages[Math.floor(Math.random() * petalImages.length)],
    swayAmplitude: Math.random() * 30 + 10,
    swaySpeed: Math.random() * 0.02 + 0.01
  }
  petals.value.push(petal)
}

// 简化动画循环
const animatePetals = () => {
  petals.value.forEach((petal) => {
    // 基础下落
    petal.y += petal.vy
    petal.x += petal.vx

    // 简化摆动效果
    petal.x += Math.sin(Date.now() * petal.swaySpeed) * 0.3

    // 旋转
    petal.rotation += petal.rotationSpeed

    // 简化鼠标交互效果
    const dx = mousePosition.value.x - petal.x
    const dy = mousePosition.value.y - petal.y
    const distanceSquared = dx * dx + dy * dy

    if (distanceSquared < 10000) { // 100*100，避免开方运算
      const distance = Math.sqrt(distanceSquared)
      const force = (100 - distance) / 100
      petal.vx += (dx / distance) * force * 0.3
      petal.vy += (dy / distance) * force * 0.2
    }

    // 重置超出屏幕的花瓣
    if (petal.y > window.innerHeight + 50 || petal.x < -50 || petal.x > window.innerWidth + 50) {
      petal.x = Math.random() * window.innerWidth
      petal.y = -50
      petal.vx = (Math.random() - 0.5) * 2
      petal.vy = Math.random() * 2 + 1
      petal.rotationSpeed = (Math.random() - 0.5) * 3
    }
  })

  animationId.value = requestAnimationFrame(animatePetals)
}

// 检测是否为移动设备
const isMobile = () => {
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ||
      ('ontouchstart' in window) ||
      (navigator.maxTouchPoints > 0)
}

// 节流优化的鼠标/触摸移动事件
const handleMouseMove = (event) => {
  const now = Date.now()
  const throttleLimit = isMobile() ? 33 : 16 // 移动端30fps，桌面端60fps

  if (now - lastMouseMoveTime.value < throttleLimit) return
  lastMouseMoveTime.value = now

  mousePosition.value = {
    x: event.clientX || (event.touches && event.touches[0] ? event.touches[0].clientX : 0),
    y: event.clientY || (event.touches && event.touches[0] ? event.touches[0].clientY : 0)
  }

  // 小屏幕不生成额外花瓣，减少性能消耗
  if (window.innerWidth <= 480) return

  // 减少鼠标附近生成花瓣的频率
  const spawnChance = isMobile() ? 0.01 : 0.03
  if (Math.random() < spawnChance) {
    const screenWidth = window.innerWidth
    let sizeRange = {min: 6, max: 10}

    if (screenWidth > 768) {
      sizeRange = {min: 6, max: 12}
    }

    const extraPetal = {
      id: Math.random(),
      x: mousePosition.value.x + (Math.random() - 0.5) * 60,
      y: mousePosition.value.y + (Math.random() - 0.5) * 60,
      vx: (Math.random() - 0.5) * 2.5,
      vy: Math.random() * 1.5 + 1,
      rotation: Math.random() * 360,
      rotationSpeed: (Math.random() - 0.5) * 3,
      size: Math.random() * (sizeRange.max - sizeRange.min) + sizeRange.min,
      opacity: Math.random() * 0.5 + 0.3,
      image: petalImages[Math.floor(Math.random() * petalImages.length)],
      swayAmplitude: Math.random() * 10 + 5,
      swaySpeed: Math.random() * 0.015 + 0.008
    }
    petals.value.push(extraPetal)

    // 动态调整花瓣总数限制
    const maxPetals = screenWidth <= 768 ? 15 : 25
    if (petals.value.length > maxPetals) {
      petals.value.shift()
    }
  }
}

// 触摸移动事件处理
const handleTouchMove = (event) => {
  event.preventDefault() // 防止页面滚动
  handleMouseMove(event)
}

// 暴露方法给父组件
defineExpose({})

onMounted(() => {
  // 初始化花瓣
  initPetals()

  // 启动花瓣动画
  animatePetals()

  // 根据设备类型添加相应的事件监听
  if (isMobile()) {
    window.addEventListener('touchmove', handleTouchMove, {passive: false})
    window.addEventListener('touchstart', handleMouseMove, {passive: true})
  } else {
    window.addEventListener('mousemove', handleMouseMove)
  }

  // 窗口大小变化时重新初始化花瓣
  const handleResize = () => {
    initPetals()
  }
  window.addEventListener('resize', handleResize)

  // 确保DOM完全渲染后再启动动画，避免初始化卡顿
  nextTick(() => {
    // 等待一个渲染周期确保所有元素都已准备就绪
    requestAnimationFrame(() => {
      // 手动触发动画播放，确保时机正确
      if (artTextRef.value) {
        artTextRef.value.playAnimation()
      }
    })
  })
})

onUnmounted(() => {
  // 清理动画和事件监听
  if (animationId.value) {
    cancelAnimationFrame(animationId.value)
  }

  // 清理所有事件监听器
  window.removeEventListener('mousemove', handleMouseMove)
  window.removeEventListener('touchmove', handleTouchMove)
  window.removeEventListener('touchstart', handleMouseMove)
  window.removeEventListener('resize', initPetals)
})
</script>

<style scoped>
.first-scene {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffffff;
  color: #2c3e50;
  position: relative;
  overflow: hidden;
  padding: 1rem;
  box-sizing: border-box;
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

.floating-petals {
  position: absolute;
  width: 100%;
  height: 100%;
}

.petal-image {
  position: absolute;
  pointer-events: none;
  transform-origin: center;
  will-change: transform, opacity;
  transition: none;
  user-select: none;
  z-index: 1;
}

.gradient-orbs {
  position: absolute;
  width: 100%;
  height: 100%;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.08;
  animation: float 25s ease-in-out infinite;
  will-change: transform;
}

.orb-1 {
  width: 200px;
  height: 200px;
  background: #ff9a9e;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.orb-2 {
  width: 150px;
  height: 150px;
  background: #a8edea;
  top: 60%;
  right: 10%;
  animation-delay: -8s;
}

.orb-3 {
  width: 180px;
  height: 180px;
  background: #d299c2;
  bottom: 20%;
  left: 20%;
  animation-delay: -16s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

.scene-container {
  text-align: center;
  position: relative;
  z-index: 1;
  max-width: 1000px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  box-sizing: border-box;
}

.title-section {
  flex-shrink: 0;
}

.intro-content {
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.4s ease, transform 0.4s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  max-width: 800px;
  width: 100%;
  padding: 1rem;
}

.intro-content.show {
  opacity: 1;
  transform: translateY(0);
}

.subtitle {
  text-align: center;
}

.subtitle h2 {
  font-size: 2.5rem;
  font-weight: 300;
  color: #34495e;
  margin: 0 0 1rem 0;
  line-height: 1.3;
}

.description {
  font-size: 1.2rem;
  line-height: 1.7;
  color: #7f8c8d;
  margin: 0;
  font-weight: 300;
  max-width: 600px;
  text-align: center;
}

.action-buttons {
  display: flex;
  justify-content: center;
  width: 100%;
}

.enter-btn {
  padding: 1rem 2rem;
  font-size: 1.1rem;
  border-radius: 50px;
  background: linear-gradient(45deg, #667eea, #764ba2);
  border: none;
  color: white;
  display: flex;
  align-items: center;
  gap: 0.8rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
  min-height: 44px;
  cursor: pointer;
  font-weight: 500;
  letter-spacing: 0.02em;
}

.enter-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(102, 126, 234, 0.4);
  background: linear-gradient(45deg, #5a6fd8, #6a42a0);
}

.arrow {
  font-size: 1.1rem;
  transition: transform 0.3s ease;
}

.enter-btn:hover .arrow {
  transform: translateX(4px);
}

/* 简化的响应式设计 */
@media (max-width: 768px) {
  .subtitle h2 {
    font-size: 2rem;
  }

  .description {
    font-size: 1rem;
  }

  .enter-btn {
    padding: 0.8rem 1.5rem;
    font-size: 1rem;
    width: 100%;
    max-width: 280px;
  }

  .orb-2, .orb-3 {
    display: none;
  }
}

@media (max-width: 480px) {
  .first-scene {
    padding: 1rem;
  }

  .subtitle h2 {
    font-size: 1.8rem;
  }

  .description {
    font-size: 0.9rem;
  }

  .floating-petals {
    display: none;
  }

  .gradient-orbs {
    display: none;
  }
}

/* 减少动画的用户偏好 */
@media (prefers-reduced-motion: reduce) {
  .orb {
    animation: none;
  }

  .intro-content {
    transition: opacity 0.2s ease;
    transform: none;
  }

  .intro-content.show {
    transform: none;
  }

  .enter-btn {
    transition: background 0.2s ease;
  }

  .arrow {
    transition: none;
  }
}
</style>