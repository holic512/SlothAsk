<script setup lang="ts">
import Category from './components/Category.vue'
import Question from './components/Question.vue'
import Sidebar from './components/Sidebar.vue'
import { onMounted } from 'vue';
import { setTitle } from '../../../../utils/title';

onMounted(() => {
  setTitle('学习');
});
</script>

<template>
  <div class="page-container">
    <div class="content-section">
      <div class="main-content">
        <div class="category-section">
          <Category/>
        </div>
        <div class="main-container">
          <Question/>
        </div>
      </div>
      <div class="side-container">
        <div class="side-content">
          <Sidebar/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 1300px;
  margin: 0 auto;
  margin-top: 20px;
  padding: 0 16px;
}

.content-section {
  display: flex;
  justify-content: space-between;
  gap: 24px;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.category-section {
  flex: 1;
}

.main-container {
  flex: 1;
  min-width: 0;
}

.side-container {
  width: 240px;
  flex-shrink: 0;
}

.side-content {
  position: sticky;
  top: 20px;
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #eee;
}
</style>