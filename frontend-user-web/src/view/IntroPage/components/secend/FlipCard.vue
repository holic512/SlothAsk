<template>
  <div
      :class="{ 'clicked': isClicked }"
      :style="{ '--front-bg': frontBg }"
      class="flip-card"
      @click="handleClick"
  >
    <div class="flip-card-inner">
      <!-- 只保留正面 -->
      <div class="flip-card-front">
        <img v-if="frontIcon" :src="frontIcon" alt="front icon" class="card-icon"/>
        <div v-else class="default-content">{{ frontText || '?' }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, defineProps, defineEmits, watch, nextTick} from 'vue'

/**
 * 卡片组件
 * @param {string} frontBg - 正面背景色，默认 '#3498db'
 * @param {string} frontIcon - 正面图标路径
 * @param {string} frontText - 正面文字（无图标时显示）
 * @param {string} label - 悬浮显示的标签文字
 */
const props = defineProps({
  frontBg: {
    type: String,
    default: '#3498db'
  },
  frontIcon: {
    type: String,
    default: ''
  },
  frontText: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['click'])

const isClicked = ref(false)
const isAnimating = ref(false)

// 点击处理（防抖）
let clickTimeout = null
const handleClick = (event) => {
  if (clickTimeout || isAnimating.value) return

  clickTimeout = setTimeout(() => {
    clickTimeout = null
  }, 100)

  // 触发点击动画
  isAnimating.value = true
  isClicked.value = true
  
  // 发送点击事件给父组件
  emit('click', event)
  
  // 动画完成后重置状态并移除will-change
  setTimeout(() => {
    isClicked.value = false
    isAnimating.value = false
  }, 300)
}
</script>

<style scoped>
.flip-card {
  width: 64px;
  height: 64px;
  perspective: 1000px;
  cursor: pointer;
  user-select: none;
  /* 移除will-change，只在需要时动态添加 */
  /* 添加containment优化 */
  contain: layout style;
}

.flip-card-inner {
  position: relative;
  width: 100%;
  height: 100%;
  text-align: center;
  transition: transform 0.3s ease-out;
  border-radius: 8px;
  /* 简化阴影效果 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  /* 移除will-change */
}

.flip-card.clicked .flip-card-inner {
  transform: scale(1.1);
  transition: transform 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
  /* 动画时临时添加will-change */
  will-change: transform;
}

.flip-card-front {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--front-bg);
  color: white;
  /* 移除will-change */
}

.card-icon {
  width: 36px;
  height: 36px;
  object-fit: contain;
  /* 简化阴影效果 */
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.15));
}

.default-content {
  font-size: 24px;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* 悬停效果 - 简化阴影 */
.flip-card:hover .flip-card-inner {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.12);
  transform: translateY(-1px);
  /* 动画时临时添加will-change */
  will-change: transform;
}

/* 悬停结束时移除will-change */
.flip-card:not(:hover) .flip-card-inner {
  will-change: auto;
}

/* 点击效果 - 避免与clicked动画冲突 */
.flip-card:active:not(.clicked) .flip-card-inner {
  transform: scale(0.95);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .flip-card {
    width: 48px;
    height: 48px;
  }

  .card-icon {
    width: 28px;
    height: 28px;
  }

  .default-content {
    font-size: 18px;
  }
}

@media (max-width: 480px) {
  .flip-card {
    width: 40px;
    height: 40px;
  }

  .card-icon {
    width: 20px;
    height: 20px;
  }

  .default-content {
    font-size: 16px;
  }
}


/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  .flip-card-inner {
    transition: none;
  }

  .flip-card:hover .flip-card-inner {
    transform: none;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  .flip-card.clicked .flip-card-inner {
    transform: scale(1.05);
  }
}


</style>