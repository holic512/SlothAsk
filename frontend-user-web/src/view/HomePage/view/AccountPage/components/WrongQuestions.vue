<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('错题本');
});
</script>

<template>
  <div class="wrong-questions-container">
    <h2>错题本</h2>
  </div>
</template>

<style scoped>
.wrong-questions-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 