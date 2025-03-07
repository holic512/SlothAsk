<script setup>
import { onMounted } from 'vue';
import { setTitle } from '../../../../utils/title';
import CategoryClass from './components/CategoryClass.vue';

onMounted(() => {
  setTitle('题库');
});
</script>

<template>
  <div class="questionbank-page">
    <h2 class="page-title">全部题库</h2>
    <CategoryClass />
  </div>
</template>

<style scoped>
.questionbank-page {
  padding: 24px 0;
}

.page-title {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px 24px;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}
</style>