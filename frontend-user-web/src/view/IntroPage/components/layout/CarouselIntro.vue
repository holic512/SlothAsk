<template>
  <div class="carousel-intro">
    <!-- 场景容器 -->
    <div 
      :style="{ transform: `translate3d(0, -${currentScene * 100}vh, 0)` }"
      class="scenes-container"
      @wheel.passive="handleWheel"
      @touchstart.passive="handleTouchStart"
      @touchmove.passive="handleTouchMove"
      @touchend.passive="handleTouchEnd"
    >
      <!-- 第一幕：原有的FirstScene -->
      <div :class="{ 'scene-active': currentScene === 0, 'scene-inactive': currentScene !== 0 }" :style="{ animationDelay: '0ms' }" class="scene scene-1" data-scene-index="0">
        <FirstScene 
          v-if="shouldLoadScene(0)" 
          ref="firstSceneRef" 
          :is-active="currentScene === 0"
          @scene-ready="onSceneReady(0)"
        />
      </div>
      
      <!-- 第二幕：翻牌动画 -->
      <div :class="{ 'scene-active': currentScene === 1, 'scene-inactive': currentScene !== 1 }" :style="{ animationDelay: '100ms' }" class="scene scene-2" data-scene-index="1">
        <SecondScene 
          v-if="shouldLoadScene(1)" 
          ref="secondSceneRef" 
          :is-active="currentScene === 1"
          @scene-ready="onSceneReady(1)"
        />
      </div>
      
      <!-- 第三幕：社区介绍 -->
      <div :class="{ 'scene-active': currentScene === 2, 'scene-inactive': currentScene !== 2 }" :style="{ animationDelay: '200ms' }" class="scene scene-3" data-scene-index="2">
        <ThirdScene 
          v-if="shouldLoadScene(2)" 
          ref="thirdSceneRef" 
          :is-active="currentScene === 2"
          @scene-ready="onSceneReady(2)"
        />
      </div>

      <!-- 第四幕：开始学习之旅 -->
      <div :class="{ 'scene-active': currentScene === 3, 'scene-inactive': currentScene !== 3 }" :style="{ animationDelay: '300ms' }" class="scene scene-4" data-scene-index="3">
        <FourthScene 
          v-if="shouldLoadScene(3)" 
          ref="fourthSceneRef" 
          :is-active="currentScene === 3"
          @scene-ready="onSceneReady(3)"
        />
      </div>
    </div>
    
    <!-- 右侧导航点 -->
    <div :data-theme="getCurrentNavColor()" class="navigation-dots">
      <div 
        v-for="(scene, index) in totalScenes" 
        :key="index"
        :class="{ active: currentScene === index }"
        class="nav-dot"
        @click="goToScene(index)"
      >
        <span class="dot-label">{{ getSceneLabel(index) }}</span>
      </div>
    </div>
    

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, defineAsyncComponent } from 'vue'
import { useRouter } from 'vue-router'
import { useFirstVisitStore } from '@/pinia/FirstVisitStore.ts'

// 懒加载组件
const FirstScene = defineAsyncComponent(() => import('../first/FirstScene.vue'))
const SecondScene = defineAsyncComponent(() => import('../secend/SecondScene.vue'))
const ThirdScene = defineAsyncComponent(() => import('../third/ThirdScene.vue'))
const FourthScene = defineAsyncComponent(() => import('../fourth/FourthScene.vue'))

const router = useRouter()
const firstVisitStore = useFirstVisitStore()

const currentScene = ref(0)
const totalScenes = 4
const isScrolling = ref(false)
const touchStartY = ref(0)
const touchEndY = ref(0)

// 性能优化相关状态
const loadedScenes = ref(new Set([0])) // 默认加载第一个场景
const sceneReadyStates = ref(new Map()) // 记录场景准备状态
const preloadDistance = 1 // 预加载距离（当前场景前后1个场景）
const unloadDistance = 2 // 卸载距离（距离当前场景超过2个的场景会被卸载）
const memoryThreshold = 3 // 内存阈值：最多同时加载3个场景
const idleCallbackId = ref(null) // 空闲时间回调ID

// 动画性能优化
const animationFrameId = ref(null)
const isReducedMotion = ref(false)
const intersectionObserver = ref(null) // 交叉观察器
const performanceMonitor = ref({ frameCount: 0, lastTime: 0 }) // 性能监控

// 导航栏颜色配置：根据场景背景选择合适的颜色
const navColors = {
  0: 'dark',  // 第一幕：白色背景，使用深色导航
  1: 'light', // 第二幕：深色背景，使用浅色导航
  2: 'light', // 第三幕：深色背景，使用浅色导航
  3: 'light'  // 第四幕：深色背景，使用浅色导航
}

const getCurrentNavColor = () => {
  return navColors[currentScene.value] || 'dark'
}

const getSceneLabel = (index) => {
  const labels = ['首页', '功能', '社区', '开始']
  return labels[index]
}

// 懒加载逻辑
const shouldLoadScene = (sceneIndex) => {
  return loadedScenes.value.has(sceneIndex)
}

const loadScene = (sceneIndex) => {
  if (sceneIndex >= 0 && sceneIndex < totalScenes) {
    loadedScenes.value.add(sceneIndex)
  }
}

const unloadScene = (sceneIndex) => {
  if (sceneIndex !== currentScene.value) {
    loadedScenes.value.delete(sceneIndex)
    sceneReadyStates.value.delete(sceneIndex)
  }
}

// 智能预加载和卸载管理
const manageSceneLoading = () => {
  const current = currentScene.value
  const loadedCount = loadedScenes.value.size
  
  // 智能预加载策略：根据内存使用情况调整
  const shouldPreload = loadedCount < memoryThreshold
  
  if (shouldPreload) {
    // 预加载相邻场景
    for (let i = Math.max(0, current - preloadDistance); i <= Math.min(totalScenes - 1, current + preloadDistance); i++) {
      loadScene(i)
    }
  }
  
  // 智能卸载：优先卸载最远的场景
  if (loadedCount > memoryThreshold) {
    const scenesToUnload = []
    for (let i = 0; i < totalScenes; i++) {
      if (i !== current && Math.abs(i - current) > unloadDistance) {
        scenesToUnload.push({ index: i, distance: Math.abs(i - current) })
      }
    }
    
    // 按距离排序，优先卸载最远的
    scenesToUnload.sort((a, b) => b.distance - a.distance)
    const unloadCount = loadedCount - memoryThreshold + 1
    
    for (let i = 0; i < Math.min(unloadCount, scenesToUnload.length); i++) {
      unloadScene(scenesToUnload[i].index)
    }
  }
  
  // 使用空闲时间进行预加载
  if (idleCallbackId.value) {
    cancelIdleCallback(idleCallbackId.value)
  }
  
  if (typeof requestIdleCallback !== 'undefined') {
    idleCallbackId.value = requestIdleCallback(() => {
      // 在空闲时间预加载下一个可能访问的场景
      const nextScene = current < totalScenes - 1 ? current + 1 : current - 1
      if (nextScene >= 0 && nextScene < totalScenes && !loadedScenes.value.has(nextScene)) {
        loadScene(nextScene)
      }
    }, { timeout: 2000 })
  }
}

// 场景准备状态回调
const onSceneReady = (sceneIndex) => {
  sceneReadyStates.value.set(sceneIndex, true)
  
  // 性能监控：记录场景加载时间
  if (typeof performance !== 'undefined') {
    console.debug(`Scene ${sceneIndex} ready at ${performance.now()}ms`)
  }
}

// 性能调试工具
const debugPerformance = () => {
  if (process.env.NODE_ENV === 'development') {
    console.log('Performance Monitor:', {
      loadedScenes: Array.from(loadedScenes.value),
      currentScene: currentScene.value,
      isScrolling: isScrolling.value,
      frameRate: performanceMonitor.value.frameCount,
      reducedMotion: isReducedMotion.value
    })
  }
}

// 检测用户是否偏好减少动画
const checkReducedMotion = () => {
  if (typeof window !== 'undefined' && window.matchMedia) {
    const mediaQuery = window.matchMedia('(prefers-reduced-motion: reduce)')
    isReducedMotion.value = mediaQuery.matches
    
    mediaQuery.addEventListener('change', (e) => {
      isReducedMotion.value = e.matches
    })
  }
}

const goToScene = (index) => {
  if (isScrolling.value) return
  
  // 取消之前的动画帧
  if (animationFrameId.value) {
    cancelAnimationFrame(animationFrameId.value)
  }
  
  currentScene.value = index
  isScrolling.value = true
  
  // 预先管理场景加载
  manageSceneLoading()
  
  // 性能监控
  const startTime = performance.now()
  
  // 使用requestAnimationFrame优化动画性能
  const animateTransition = () => {
    const currentTime = performance.now()
    const elapsed = currentTime - startTime
    const duration = isReducedMotion.value ? 300 : 800
    
    // 监控帧率
    performanceMonitor.value.frameCount++
    if (currentTime - performanceMonitor.value.lastTime > 1000) {
      const fps = performanceMonitor.value.frameCount
      performanceMonitor.value.frameCount = 0
      performanceMonitor.value.lastTime = currentTime
      
      // 如果帧率过低，启用性能模式
      if (fps < 30) {
        isReducedMotion.value = true
      }
    }
    
    if (elapsed < duration) {
      animationFrameId.value = requestAnimationFrame(animateTransition)
    } else {
      isScrolling.value = false
      animationFrameId.value = null
    }
  }
  
  animationFrameId.value = requestAnimationFrame(animateTransition)
}

const nextScene = () => {
  if (currentScene.value < totalScenes - 1) {
    goToScene(currentScene.value + 1)
  }
}

const prevScene = () => {
  if (currentScene.value > 0) {
    goToScene(currentScene.value - 1)
  }
}

// 防抖和节流优化
let wheelTimeout = null
let lastWheelTime = 0
const wheelThrottleDelay = 100

const handleWheel = (event) => {
  if (isScrolling.value) return
  
  // 注意：由于使用了.passive修饰符，不能调用preventDefault()
  
  const now = Date.now()
  if (now - lastWheelTime < wheelThrottleDelay) {
    return
  }
  lastWheelTime = now
  
  // 清除之前的防抖定时器
  if (wheelTimeout) {
    clearTimeout(wheelTimeout)
  }
  
  // 使用requestAnimationFrame优化滚轮响应
  wheelTimeout = requestAnimationFrame(() => {
    if (Math.abs(event.deltaY) > 10) { // 增加阈值过滤微小滚动
      if (event.deltaY > 0) {
        nextScene()
      } else {
        prevScene()
      }
    }
    wheelTimeout = null
  })
}

// 触摸优化变量
let touchMoveCount = 0
const minTouchDistance = 50
const maxTouchTime = 500
let touchStartTime = 0

const handleTouchStart = (event) => {
  touchStartY.value = event.touches[0].clientY
  touchStartTime = Date.now()
  touchMoveCount = 0
}

const handleTouchMove = (event) => {
  touchMoveCount++
  // 注意：由于使用了.passive修饰符，不能调用preventDefault()
  // 这样可以提高滚动性能，让浏览器并行处理滚动
}

const handleTouchEnd = (event) => {
  if (isScrolling.value) return
  
  const touchEndTime = Date.now()
  const touchDuration = touchEndTime - touchStartTime
  
  // 忽略过长的触摸（可能是意外触摸）
  if (touchDuration > maxTouchTime) {
    return
  }
  
  touchEndY.value = event.changedTouches[0].clientY
  const deltaY = touchStartY.value - touchEndY.value
  
  // 动态调整最小滑动距离（快速滑动时降低阈值）
  const adjustedMinDistance = touchDuration < 200 ? minTouchDistance * 0.7 : minTouchDistance
  
  if (Math.abs(deltaY) > adjustedMinDistance) {
    if (deltaY > 0) {
      nextScene()
    } else {
      prevScene()
    }
  }
}

const handleKeydown = (event) => {
  if (isScrolling.value) return
  
  switch (event.key) {
    case 'ArrowDown':
    case 'PageDown':
      event.preventDefault()
      nextScene()
      break
    case 'ArrowUp':
    case 'PageUp':
      event.preventDefault()
      prevScene()
      break
  }
}

onMounted(() => {
  // 检测减少动画偏好
  checkReducedMotion()
  
  // 初始化场景加载管理
  manageSceneLoading()
  
  // 添加事件监听器（使用被动监听提高性能）
  window.addEventListener('keydown', handleKeydown, { passive: false })
  
  // 初始化交叉观察器用于性能监控
  if ('IntersectionObserver' in window) {
    intersectionObserver.value = new IntersectionObserver(
      (entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            // 场景进入视口时的优化处理
            const sceneIndex = parseInt(entry.target.dataset.sceneIndex)
            if (!isNaN(sceneIndex)) {
              loadScene(sceneIndex)
            }
          }
        })
      },
      { threshold: 0.1, rootMargin: '50px' }
    )
    
    // 观察所有场景元素
    nextTick(() => {
      const sceneElements = document.querySelectorAll('.scene[data-scene-index]')
      sceneElements.forEach(element => {
        intersectionObserver.value?.observe(element)
      })
    })
  }
  
  // 开发环境下启用性能调试
  if (process.env.NODE_ENV === 'development') {
    window.debugCarouselPerformance = debugPerformance
    console.log('Carousel performance debugging enabled. Use window.debugCarouselPerformance() to check status.')
  }
  
  // 预加载下一个场景（性能优化）
  nextTick(() => {
    if (currentScene.value < totalScenes - 1) {
      loadScene(currentScene.value + 1)
    }
    
    // 初始化性能监控
    performanceMonitor.value.lastTime = performance.now()
  })
})

onUnmounted(() => {
  // 清理事件监听器
  window.removeEventListener('keydown', handleKeydown)
  
  // 清理定时器和动画帧
  if (wheelTimeout) {
    if (typeof wheelTimeout === 'number') {
      cancelAnimationFrame(wheelTimeout)
    } else {
      clearTimeout(wheelTimeout)
    }
  }
  
  if (animationFrameId.value) {
    cancelAnimationFrame(animationFrameId.value)
  }
  
  if (idleCallbackId.value && typeof cancelIdleCallback !== 'undefined') {
    cancelIdleCallback(idleCallbackId.value)
  }
  
  // 清理交叉观察器
  if (intersectionObserver.value) {
    intersectionObserver.value.disconnect()
  }
  
  // 清理状态
  loadedScenes.value.clear()
  sceneReadyStates.value.clear()
})
</script>

<style scoped>
.carousel-intro {
  height: 100vh;
  overflow: hidden;
  position: relative;
  background: #ffffff;
  /* 启用硬件加速和图层优化 */
  transform: translateZ(0);
  contain: layout style paint;
}

.scenes-container {
  height: 400vh;
  /* 使用更流畅的缓动函数 */
  transition: transform 0.8s cubic-bezier(0.16, 1, 0.3, 1);
  will-change: transform;
  /* 强制GPU加速 */
  transform: translate3d(0, 0, 0);
  contain: layout style paint;
  /* 优化合成层 */
  isolation: isolate;
}

.scene {
  height: 100vh;
  width: 100%;
  position: relative;
  /* 性能优化：强制GPU加速 */
  transform: translate3d(0, 0, 0);
  backface-visibility: hidden;
  perspective: 1000px;
  /* 图层管理优化 */
  contain: layout style paint;
  /* 优化渲染性能 */
  isolation: isolate;
}

/* 场景状态优化 */
.scene-active {
  /* 激活状态：启用所有动画和交互 */
  pointer-events: auto;
  visibility: visible;
  will-change: transform, opacity;
}

.scene-inactive {
  /* 非激活状态：禁用交互，保持可见性用于过渡 */
  pointer-events: none;
  will-change: auto;
}

/* 懒加载过渡效果 - 复合动画优化 */
.scene > * {
  opacity: 0;
  transform: translate3d(0, 20px, 0);
  transition: opacity 0.6s cubic-bezier(0.16, 1, 0.3, 1), transform 0.6s cubic-bezier(0.16, 1, 0.3, 1);
  will-change: transform, opacity;
}

.scene-active > * {
  opacity: 1;
  transform: translate3d(0, 0, 0);
  /* 错开子元素动画时间 */
  animation-delay: var(--animation-delay, 0ms);
}

/* 为子元素设置错开的动画延迟 */
.scene-active > *:nth-child(1) { --animation-delay: 0ms; }
.scene-active > *:nth-child(2) { --animation-delay: 100ms; }
.scene-active > *:nth-child(3) { --animation-delay: 200ms; }
.scene-active > *:nth-child(4) { --animation-delay: 300ms; }
.scene-active > *:nth-child(5) { --animation-delay: 400ms; }

/* 第二幕样式由SecondScene组件内部处理 */
/* 第三幕样式由ThirdScene组件内部处理 */
/* 第四幕样式由FourthScene组件内部处理 */

/* 导航点样式 */
.navigation-dots {
  position: fixed;
  right: 2rem;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  display: flex;
  flex-direction: column;
  gap: 1rem;
  z-index: 1000;
  /* 优化导航点的合成层 */
  contain: layout style paint;
  will-change: transform;
}

.nav-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  position: relative;
  /* 优化导航点动画 */
  transform: translate3d(0, 0, 0);
  will-change: transform, background-color;
}

/* 深色导航栏样式（用于浅色背景） */
.nav-dot {
  background: rgba(44, 62, 80, 0.4);
  border: 1px solid rgba(44, 62, 80, 0.6);
}

.nav-dot:hover {
  background: rgba(44, 62, 80, 0.7);
  transform: translate3d(0, 0, 0) scale(1.1);
}

.nav-dot.active {
  background: #2c3e50;
  transform: translate3d(0, 0, 0) scale(1.2);
}

/* 浅色导航栏样式（用于深色背景） */
.navigation-dots[data-theme="light"] .nav-dot {
  background: rgba(255, 255, 255, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.6);
}

.navigation-dots[data-theme="light"] .nav-dot:hover {
  background: rgba(255, 255, 255, 0.7);
}

.navigation-dots[data-theme="light"] .nav-dot.active {
  background: #ffffff;
}

.dot-label {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  padding: 0.2rem 0.6rem;
  border-radius: 12px;
  font-size: 0.75rem;
  white-space: nowrap;
  opacity: 0;
  transition: opacity 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  pointer-events: none;
  will-change: opacity;
}

/* 深色主题标签样式 */
.dot-label {
  background: rgba(44, 62, 80, 0.9);
  color: white;
}

/* 浅色主题标签样式 */
.navigation-dots[data-theme="light"] .dot-label {
  background: rgba(255, 255, 255, 0.9);
  color: #2c3e50;
}

.nav-dot:hover .dot-label {
  opacity: 1;
}



@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translate3d(0, 30px, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navigation-dots {
    right: 1rem;
    gap: 1rem;
  }
  
  .nav-dot {
    width: 10px;
    height: 10px;
  }
  
  /* 第三幕和第四幕的响应式样式由各自组件处理 */
}

@media (max-width: 480px) {
  /* 第三幕和第四幕的移动端样式由各自组件处理 */
  
  .navigation-dots {
    right: 0.5rem;
  }
  
  .dot-label {
    display: none;
  }
}

/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  .scenes-container {
    transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  }
  
  .scene > * {
    transition: opacity 0.3s cubic-bezier(0.16, 1, 0.3, 1), transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  }
  
  /* 第三幕和第四幕的减少动画样式由各自组件处理 */
  
  .nav-dot {
    transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
  }
  
  .dot-label {
    transition: opacity 0.2s cubic-bezier(0.16, 1, 0.3, 1);
  }
  
  /* 禁用复杂动画 */
  .scene-active > *:nth-child(n) {
    --animation-delay: 0ms;
  }
  
  /* 第二幕动画由SecondScene组件内部处理 */
}

/* 性能优化：减少重绘和回流 */
.carousel-intro * {
  box-sizing: border-box;
}

/* 优化GPU层合成 */
.scene,
.navigation-dots {
  will-change: transform;
}

/* 在动画完成后移除will-change */
.scene:not(.scene-active) {
  will-change: auto;
}

/* 高性能模式：为低端设备优化 */
@media (max-resolution: 1dppx) {
  .scenes-container {
    transition-duration: 0.5s;
  }
  
  .scene > * {
    transition-duration: 0.4s;
  }
}

/* 为高刷新率屏幕优化 */
@media (min-resolution: 2dppx) {
  .scenes-container {
    transition-duration: 1s;
  }
}

/* 内存优化：限制同时存在的合成层 */
.scene:not(.scene-active):not(.scene-active + .scene):not(.scene-active - .scene) {
  transform: none;
  will-change: auto;
}
</style>