<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('题单');
});
</script>

<template>
  <div class="problem-list-container">
    <h2>题单</h2>
  </div>
</template>

<style scoped>
.problem-list-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 