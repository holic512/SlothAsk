<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('进展分析');
});
</script>

<template>
  <div class="progress-container">
    <h2>进展分析</h2>
  </div>
</template>

<style scoped>
.progress-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 