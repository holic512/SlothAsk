<!--
/**
 * ArtTextReveal 樱花文字填充动画组件
 * 
 * 功能说明：
 * - 樱花小球从文字起始位置上方掉落并弹跳
 * - 樱花滚动过程中逐步填充文字颜色（从左到右）
 * - 生成精致的飘落花瓣效果（心形、椭圆、水滴三种样式）
 * - 支持自定义文字、颜色、字体大小等
 * 
 * Props 属性：
 * @param {string} text - 要显示的文字内容
 * @param {string} fillColor - 文字填充颜色，默认 '#FFD700'
 * @param {string} strokeColor - 文字描边颜色，默认 '#000000'
 * @param {string} fontSize - 字体大小，默认 '80px'
 * @param {number} duration - 动画持续时间（秒），默认 2.5
 * @param {boolean} repeat - 是否循环播放，默认 false
 * @param {boolean} autoPlay - 是否自动播放，默认 true
 * 
 * Events 事件：
 * @emit animationStart - 动画开始时触发
 * @emit animationComplete - 动画完成时触发
 * 
 * Methods 方法：
 * - playAnimation() - 播放动画
 * - pauseAnimation() - 暂停动画
 * - resetAnimation() - 重置动画
 * 
 * 使用示例：
 * <ArtTextReveal 
 *   text="Hello World" 
 *   fillColor="#000000" 
 *   strokeColor="#1a1a1a" 
 *   fontSize="60px"
 *   :duration="3"
 *   :autoPlay="true"
 *   @animationStart="onStart"
 *   @animationComplete="onComplete"
 * />
 * 
 * 特色功能：
 * 1. 自适应文字宽度 - 樱花会根据文字长度自动调整起始位置
 * 2. 精致花瓣效果 - 三种不同形状的花瓣随机生成
 * 3. 物理动画效果 - 重力、空气阻力、摆动等真实物理效果
 * 4. 响应式设计 - 支持不同屏幕尺寸自适应
 */
-->

<template>
  <div ref="wrapperRef" class="art-text-wrapper">
    <!-- SVG描边文字 -->
    <svg :viewBox="`0 0 ${svgWidth} ${svgHeight}`" class="art-text-svg">
      <defs>
        <mask :id="`textMask-${uniqueId}`">
          <rect fill="black" height="100%" width="100%" />
          <text
            :style="{
              fontSize: fontSize,
              fontFamily: 'system-ui, -apple-system, sans-serif',
              fontWeight: '600',
              letterSpacing: '0.02em'
            }"
            :x="svgWidth / 2"
            :y="svgHeight / 2"
            dominant-baseline="central"
            fill="white"
            text-anchor="middle"
          >
            {{ text }}
          </text>
        </mask>
        
        <!-- 简化渐变定义 -->
        <radialGradient :id="`ballGradient-${uniqueId}`" cx="50%" cy="50%" r="50%">
          <stop offset="0%" stop-color="#FFF0F5" />
          <stop offset="100%" stop-color="#FF69B4" />
        </radialGradient>
        
        <!-- 樱花图片 -->
        <image :id="`sakuraFlower-${uniqueId}`" 
               :height="ballRadius * 2"
               :width="ballRadius * 2"
               :x="-ballRadius"
               :y="-ballRadius"
               href="/IntroPage/樱花.png"
               opacity="0.95" />
        
        <!-- 填充遮罩 -->
        <mask :id="`fillMask-${uniqueId}`">
          <rect fill="black" height="100%" width="100%" />
          <!-- 已填充区域（从左侧开始） -->
          <rect
            :height="svgHeight"
            :width="filledWidth"
            :x="0"
            :y="0"
            fill="white"
          />
        </mask>
      </defs>
      
      <!-- 描边文字 -->
      <text
        :style="{
          fontSize: fontSize,
          fontFamily: 'system-ui, -apple-system, sans-serif',
          fontWeight: '600',
          letterSpacing: '0.02em',
          fill: 'none',
          stroke: strokeColor,
          strokeWidth: '2px'
        }"
        :x="svgWidth / 2"
        :y="svgHeight / 2"
        dominant-baseline="central"
        text-anchor="middle"
      >
        {{ text }}
      </text>
      
      <!-- 填充文字 -->
      <text
        :mask="`url(#fillMask-${uniqueId})`"
        :style="{
          fontSize: fontSize,
          fontFamily: 'system-ui, -apple-system, sans-serif',
          fontWeight: '600',
          letterSpacing: '0.02em',
          fill: '#000000'
        }"
        :x="svgWidth / 2"
        :y="svgHeight / 2"
        dominant-baseline="central"
        text-anchor="middle"
      >
        {{ text }}
      </text>
      
      <!-- 樱花小球 -->
       <g ref="glowBallRef" :style="{ opacity: ballOpacity }">
         <!-- 樱花主体 -->
         <g :transform="`translate(${ballPosition.x}, ${ballPosition.y}) rotate(${ballRotation}) scale(${ballScale})`">
           <use :href="`#sakuraFlower-${uniqueId}`" />
         </g>


       </g>
    </svg>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed, nextTick } from 'vue'
import { gsap } from 'gsap'

interface Props {
  text: string
  fillColor?: string
  strokeColor?: string
  fontSize?: string
  duration?: number
  repeat?: boolean
  autoPlay?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  fillColor: '#FFD700',
  strokeColor: '#000000',
  fontSize: '80px',
  duration: 2.5,
  repeat: false,
  autoPlay: true
})

const emit = defineEmits<{
  animationComplete: []
  animationStart: []
}>()

// 响应式数据
const wrapperRef = ref<HTMLElement>()
const glowBallRef = ref<SVGElement>()
const uniqueId = ref(Math.random().toString(36).substr(2, 9))

const svgWidth = ref(800)
const svgHeight = ref(200)
const ballPosition = ref({ x: -50, y: 100 })
const ballRadius = computed(() => {
  const fontSize = parseInt(props.fontSize)
  return Math.max(fontSize * 0.3, 30) // 球体大小随字体大小适配，最小30px
})
const ballOpacity = ref(0)
const ballRotation = ref(0)
const ballScale = ref(1.2)
const filledWidth = ref(0)



// 计算属性
const textWidth = computed(() => {
  // 估算文字宽度
  const fontSize = parseInt(props.fontSize)
  return props.text.length * fontSize * 0.6
})

// 动画时间线
let timeline: gsap.core.Timeline | null = null

// 初始化SVG尺寸
const initializeSVG = async () => {
  await nextTick()
  
  if (wrapperRef.value) {
    const fontSize = parseInt(props.fontSize)
    // 确保SVG容器足够大以容纳大字体
    svgWidth.value = Math.max(textWidth.value + 400, 1200)
    svgHeight.value = Math.max(fontSize * 2.5, 400)
    ballPosition.value = {
      x: ballRadius.value,
      y: -ballRadius.value * 2
    }
  }
}

// 创建动画
const createAnimation = () => {
  if (!glowBallRef.value) return
  
  timeline = gsap.timeline({
    repeat: props.repeat ? -1 : 0,
    repeatDelay: 1,
    onStart: () => emit('animationStart'),
    onComplete: () => {
      if (!props.repeat) {
        emit('animationComplete')
      }
    }
  })
  
  // 计算文字实际位置，樱花从文字起始位置开始
  const textStartX = (svgWidth.value - textWidth.value) / 2 // 文字居中时的起始位置
  const startX = Math.max(ballRadius.value, textStartX) // 从文字起始位置开始
  const startY = -ballRadius.value * 2
  const groundY = svgHeight.value * 0.20 // 文字上部作为地面
  const endX = Math.min(svgWidth.value + ballRadius.value, textStartX + textWidth.value + ballRadius.value)
  
  // 重置状态
  filledWidth.value = 0
  ballRotation.value = 0
  ballScale.value = 1.2
  
  timeline
    // 小球从上方出现，带有优雅的缩放效果
    .set(ballPosition.value, { x: startX, y: startY })
    .set(ballScale, { value: 0.3 })
    .to(ballOpacity, {
      value: 1,
      duration: 0.3,
      ease: 'back.out(1.7)'
    })
    .to(ballScale, {
      value: 1.4,
      duration: 0.3,
      ease: 'back.out(1.7)'
    }, '<')
    // 小球下落，带有轻微的水平摆动
    .to(ballPosition.value, {
      y: groundY,
      duration: 0.8,
      ease: 'power2.in',
      onUpdate: () => {
        // 添加轻微的水平摆动效果
        const progress = timeline!.progress()
        ballPosition.value.x = startX + Math.sin(progress * Math.PI * 2) * 3
      }
    })
    .to(ballScale, {
      value: 1.4,
      duration: 0.1,
      ease: 'power2.out'
    }, '-=0.1')
    // 简化弹跳效果
    .to(ballPosition.value, {
      y: groundY - 30,
      duration: 0.3,
      ease: 'power2.out'
    })
    .to(ballScale, {
      value: 1.2,
      duration: 0.3,
      ease: 'power2.out'
    }, '<')
    .to(ballPosition.value, {
      y: groundY,
      duration: 0.3,
      ease: 'power2.in'
    })
    .to(ballScale, {
      value: 1.3,
      duration: 0.1,
      ease: 'power2.out'
    }, '-=0.1')
    // 开始滚动并填充文字
    .to(ballPosition.value, {
      x: endX,
      duration: props.duration,
      ease: 'power1.inOut',
      onUpdate: () => {
         // 简化文字填充动画 - 直接填充
         filledWidth.value = Math.max(0, ballPosition.value.x - ballRadius.value)
         
         // 更柔和的旋转效果
         const distance = ballPosition.value.x - startX
         ballRotation.value = (distance / ballRadius.value) * 20 // 更柔和的旋转速度
       }
    }, '-=0.1')
    // 小球淡出
    .to(ballOpacity, {
      value: 0,
      duration: 0.4,
      ease: 'power2.in'
    }, '-=0.3')
}

// 播放动画
const playAnimation = () => {
  if (timeline) {
    timeline.restart()
  } else {
    createAnimation()
  }
}

// 暂停动画
const pauseAnimation = () => {
  timeline?.pause()
}

// 重置动画
const resetAnimation = () => {
  timeline?.progress(0).pause()
  ballOpacity.value = 0
  ballRotation.value = 0
  ballScale.value = 1.2
  filledWidth.value = 0
  ballPosition.value = {
    x: ballRadius.value,
    y: -ballRadius.value * 2
  }
}

// 暴露方法
defineExpose({
  playAnimation,
  pauseAnimation,
  resetAnimation
})

onMounted(async () => {
  await initializeSVG()
  
  if (props.autoPlay) {
    // 立即创建动画，无需延迟
    await nextTick(() => {
      createAnimation()
    })
  }
})
</script>

<style scoped>
.art-text-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: auto;
  position: relative;
  overflow: visible;
}

.art-text-svg {
  width: 100%;
  height: auto;
  max-width: none;
  min-height: 300px;
  display: block;
  overflow: visible;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .art-text-wrapper {
    padding: 0 1rem;
  }
}

@media (max-width: 480px) {
  .art-text-wrapper {
    padding: 0 1rem;
  }
}
</style>