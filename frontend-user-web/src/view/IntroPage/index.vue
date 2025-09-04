<template>
  <div class="intro-page">
    <!-- 走马灯介绍页面 -->
    <CarouselIntro />
  </div>
</template>

<script setup>
import CarouselIntro from './components/layout/CarouselIntro.vue'
</script>

<style scoped>
.intro-page {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
</style>