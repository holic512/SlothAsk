<script setup lang="ts">
import Category from './components/LeftBox/RecommendCategory.vue'
import Question from './components/LeftBox/Question.vue'
import Sidebar from './components/Sidebar/index.vue'
import {onMounted} from 'vue';
import {setTitle} from '@/utils/title';
import RightBox from "@/view/HomePage/components/RightBox.vue";

onMounted(() => {
  setTitle('学习');
});
</script>

<template>
  <div class="page-container">
    <div class="content-section">
      <!--  左边  题库推荐 和 列表 -->
      <div class="main-content">
        <div class="category-section">
          <Category/>
        </div>
        <div class="main-container">
          <Question/>
        </div>
      </div>

      <!--  右边  -->
      <div class="side-container">
        <Sidebar/>
      </div>
    </div>
  </div>

  <RightBox/>

</template>

<style scoped>
.page-container {
  max-width: 1400px;
  padding: 0 16px;
  /* 添加底部边距，防止内容被FooterMenu覆盖 */
  margin: 30px auto 40px;
  box-sizing: border-box;

  overflow-x: hidden;

  height: auto;
}

.content-section {
  display: flex;
  justify-content: space-between;
  gap: 36px;
  flex-wrap: wrap; /* 在小屏幕上允许换行，防止溢出 */
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
  min-width: 0; /* 防止flex子项溢出 */
}

.category-section {
  flex: 1;
}

.main-container {
  flex: 1;
  min-width: 0;
}

.side-container {
  width: 320px;
  flex-shrink: 0;
}

@media (max-width: 1300px) {
  .side-container {
    display: none;
  }

  .page-container {
    max-width: 1000px;
  }
}
</style>