<template>
  <div class="intro-page">
    <!-- 走马灯介绍页面 -->
    <CarouselIntro />
  </div>
</template>

<script setup>
import CarouselIntro from './components/layout/CarouselIntro.vue'
import {onMounted} from "vue";
// 设置标题位置
import {setTitle} from '@/utils/title.js'

onMounted(() => {
  setTitle('通知中心')
})
</script>

<style scoped>
.intro-page {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
</style>