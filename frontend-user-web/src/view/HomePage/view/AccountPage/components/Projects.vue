<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('显示项目');
});
</script>

<template>
  <div class="projects-container">
    <h2>显示项目</h2>
  </div>
</template>

<style scoped>
.projects-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 