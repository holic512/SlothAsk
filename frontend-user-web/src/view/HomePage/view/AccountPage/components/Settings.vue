<script setup lang="ts">
import { onMounted } from 'vue';
import { setTitle } from '@/utils/title';

onMounted(() => {
  setTitle('账号设置');
});
</script>

<template>
  <div class="settings-container">
    <h2>账号设置</h2>
  </div>
</template>

<style scoped>
.settings-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
</style> 