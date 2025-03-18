<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('收藏夹');
});
</script>

<template>
  <div class="favorites-container">
    <h2>收藏夹</h2>
  </div>
</template>

<style scoped>
.favorites-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 