<script setup>
import { ref, onMounted } from 'vue'
import { setTitle } from '../../../../utils/title'

onMounted(() => {
  setTitle('讨论')
})
</script>

<template>
  <div class="discussion-page">
    <h2>讨论</h2>
    <!-- 讨论区容器 -->
    <div class="discussion-container">
      <!-- 这里后续可以添加讨论区内容 -->
    </div>
  </div>
</template>

<style scoped>
.discussion-page {
  padding: 20px;
}

.discussion-container {
  margin-top: 20px;
}
</style> 