<script setup>
import { ref, onMounted } from 'vue'
import { setTitle } from '../../../../utils/title'

onMounted(() => {
  setTitle('竞赛')
})
</script>

<template>
  <div class="contest-page">
    <h2>竞赛</h2>
    <!-- 竞赛列表容器 -->
    <div class="contest-container">
      <!-- 这里后续可以添加竞赛列表内容 -->
    </div>
  </div>
</template>

<style scoped>
.contest-page {
  padding: 20px;
}

.contest-container {
  margin-top: 20px;
}
</style> 