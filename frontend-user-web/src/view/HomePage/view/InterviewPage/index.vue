<script setup>
import { ref, onMounted } from 'vue'
import { setTitle } from '../../../../utils/title'

onMounted(() => {
  setTitle('面试分享')
})
</script>

<template>
  <div class="interview-page">
    <h2>面试分享</h2>
    <!-- 面试经验分享容器 -->
    <div class="interview-container">
      <!-- 这里后续可以添加面试经验分享内容 -->
    </div>
  </div>
</template>

<style scoped>
.interview-page {
  padding: 20px;
}

.interview-container {
  margin-top: 20px;
}
</style> 