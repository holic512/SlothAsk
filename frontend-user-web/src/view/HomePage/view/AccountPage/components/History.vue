<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('答题记录');
});
</script>

<template>
  <div class="history-container">
    <h2>答题记录</h2>
  </div>
</template>

<style scoped>
.history-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 