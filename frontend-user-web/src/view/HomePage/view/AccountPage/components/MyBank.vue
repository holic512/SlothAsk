<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('我的题库');
});
</script>

<template>
  <div class="my-bank-container">
    <h2>我的题库</h2>
  </div>
</template>

<style scoped>
.my-bank-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 