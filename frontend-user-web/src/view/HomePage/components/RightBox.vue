<script lang="ts" setup>
import {computed, ref, watch} from 'vue'

// 静态数据提升到模块顶层
const qrTabs = [
  { label: '公众号', name: 'wechat', img: '/HomePage/qrcode_wechat.png' },
  { label: '抖音', name: 'douyin', img: '/HomePage/qrcode_douyin.png' },
  { label: '小红书', name: 'xiaohongshu', img: '/HomePage/qrcode_xiaohongshu.png' },
] as const

const show = ref(false)
const activeName = ref(qrTabs[0].name)

// 通过 computed 缓存当前选中项
const currentTab = computed(() =>
    qrTabs.find(t => t.name === activeName.value)!
)

// 监听 show，只在打开时注册键盘事件
watch(show, (val) => {
  if (val) {
    window.addEventListener('keydown', onKeyDown)
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

  <transition name="fade-slide">
    <!-- 改用 v-show，保留 DOM，避免频繁销毁重建 -->
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
          <!-- 直接使用 computed.currentTab -->
          <img
              :alt="`${currentTab.label} 二维码`"
              :src="currentTab.img"
          />
        </div>
      </div>
    </div>
  </transition>
</template>

<style scoped>
/* 样式保持不变 */
.qr-trigger {
  position: fixed;
  right: 40px;
  bottom: 150px;
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 50%;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  padding: 0;
  z-index: 1001;
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

/* 动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
.fade-slide-enter-to,
.fade-slide-leave-from {
  opacity: 1;
  transform: translateY(0);
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
}
.tab-content img {
  width: 120px;
  height: 120px;
  object-fit: contain;
}
</style>
