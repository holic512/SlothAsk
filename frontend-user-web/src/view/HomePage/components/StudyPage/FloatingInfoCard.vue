<script lang="ts" setup>
import {computed, nextTick, ref, watch} from 'vue'

// 静态数据提升到模块顶层
const qrTabs = [
  { label: '公众号', name: 'wechat', img: '/HomePage/qrcode_wechat.png' },
  { label: '抖音', name: 'douyin', img: '/HomePage/qrcode_douyin.png' },
  { label: '小红书', name: 'xiaohongshu', img: '/HomePage/qrcode_xiaohongshu.png' },
] as const

const show = ref(false)
const activeName = ref(qrTabs[0].name)
const imagesLoaded = ref(false)
const imageLoadingStates = ref<Record<string, boolean>>({})

// 通过 computed 缓存当前选中项
const currentTab = computed(() =>
    qrTabs.find(t => t.name === activeName.value)!
)

// 预加载图片函数
const preloadImages = async () => {
  if (imagesLoaded.value) return
  
  const loadPromises = qrTabs.map(tab => {
    return new Promise<void>((resolve) => {
      const img = new Image()
      img.onload = () => {
        imageLoadingStates.value[tab.name] = true
        resolve()
      }
      img.onerror = () => {
        imageLoadingStates.value[tab.name] = false
        resolve()
      }
      img.src = tab.img
    })
  })
  
  await Promise.all(loadPromises)
  imagesLoaded.value = true
}

// 监听 show，只在打开时注册键盘事件和预加载图片
watch(show, async (val) => {
  if (val) {
    window.addEventListener('keydown', onKeyDown)
    // 异步预加载图片，不阻塞UI
    nextTick(() => {
      preloadImages()
    })
  } else {
    window.removeEventListener('keydown', onKeyDown)
  }
})

function onKeyDown(e: KeyboardEvent) {
  if (e.key === 'Escape') {
    show.value = false
  }
}



function onMaskClick(e: MouseEvent) {
  // 只有点击遮罩层才关闭
  if ((e.target as HTMLElement).classList.contains('qr-overlay')) {
    show.value = false
  }
}
</script>

<template>
  <button class="qr-trigger" @click="show = !show">
    <img alt="二维码图标" src="/HomePage/erweima_compressed.png" />
  </button>

  <!-- 移除动画，直接使用 v-show -->
  <div v-show="show" class="qr-overlay" @click="onMaskClick">
    <div class="qr-popup">
      <div class="tabs">
        <button
            v-for="tab in qrTabs"
            :key="tab.name"
            :class="{ active: activeName === tab.name }"
            @click="activeName = tab.name"
        >
          {{ tab.label }}
        </button>
      </div>
      <div class="tab-content">
        <!-- 懒加载图片，只在弹窗打开时加载 -->
        <div v-if="!imageLoadingStates[currentTab.name] && show" class="loading-placeholder">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>
        <img
            v-show="imageLoadingStates[currentTab.name] || !show"
            :alt="`${currentTab.label} 二维码`"
            :src="show ? currentTab.img : ''"
            @error="imageLoadingStates[currentTab.name] = false"
            @load="imageLoadingStates[currentTab.name] = true"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 样式保持不变 */
.qr-trigger {
  position: fixed;
  right: 40px;
  bottom: 150px;
  width: 40px;
  height: 40px;
  border: 1px solid #dcdfe6;
  border-radius: 50%;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  padding: 0;
  z-index: 1001;
  transition: border-color 0.15s, box-shadow 0.15s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.qr-trigger:hover {
  border-color: #409eff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(64, 158, 255, 0.2);
}

.qr-trigger:active {
  border-color: #3a8ee6;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.12);
}
.qr-trigger img {
  width: 24px;
  height: 24px;
}

.qr-overlay {
  position: fixed;
  inset: 0;
  background: transparent;
  z-index: 1000;
}

.qr-popup {
  position: fixed;
  bottom: 80px;
  right: 95px;
  width: 165px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  font-family: sans-serif;
  z-index: 1002;
}

.tabs {
  display: flex;
  background: #f5f5f5;
}
.tabs button {
  flex: 1;
  padding: 6px 0;
  background: transparent;
  border: none;
  font-size: 12px;
  color: #555;
  cursor: pointer;
  position: relative;
  transition: color 0.2s;
}
.tabs button.active {
  color: #409eff;
}
.tabs button.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20%;
  width: 60%;
  height: 2px;
  background: #409eff;
  border-radius: 1px;
}

.tab-content {
  padding: 8px;
  text-align: center;
  min-height: 136px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.tab-content img {
  width: 120px;
  height: 120px;
  object-fit: contain;
}

.loading-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 120px;
  color: #999;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 8px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-placeholder p {
  margin: 0;
  font-size: 12px;
}
</style>
// 静态数据提升到模块顶层
const qrTabs = [
  { label: '公众号', name: 'wechat', img: '/HomePage/qrcode_wechat.png' },
  { label: '抖音', name: 'douyin', img: '/HomePage/qrcode_douyin.png' },
  { label: '小红书', name: 'xiaohongshu', img: '/HomePage/qrcode_xiaohongshu.png' },
] as const
