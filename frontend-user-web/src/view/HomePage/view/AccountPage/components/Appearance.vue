<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('外观设置');
});
</script>

<template>
  <div class="appearance-container">
    <h2>外观设置</h2>
  </div>
</template>

<style scoped>
.appearance-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 