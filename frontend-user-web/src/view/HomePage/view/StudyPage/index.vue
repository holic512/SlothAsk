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
  <title>SlothAsk - 树懒问答(SlothAsk),碎片知识整理的高效神器</title>
  <div class="page-container">
    <div class="category-section">
      <Category/>
    </div>

    <div class="content-section">
      <div class="main-container">
        <Question/>
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

.category-section {
  margin-bottom: 24px;
}

.content-section {
  display: flex;
  gap: 24px;
}

.main-container {
  flex: 1;
  min-width: 0; /* 防止内容溢出 */
  max-width: calc(100% - 260px); /* 确保主内容区域有足够空间 */
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