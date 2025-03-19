<template>
  <div class="page-wrapper">
    <div class="sidebar-left">
      <SidebarLeft />
    </div>
    <div class="container">
      <QuestionDetail />
      <CommentSection />
    </div>
    <div class="sidebar-right">
      <SidebarRight />
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { setTitle } from '../../../../utils/title';
import QuestionDetail from '../QuestionPage/components/QuestionDetail.vue'
import CommentSection from '../QuestionPage/components/CommentSection.vue'
import SidebarLeft from '../QuestionPage/components/SidebarLeft.vue'
import SidebarRight from '../QuestionPage/components/SidebarRight.vue'

onMounted(() => {
  setTitle('题库');
});
</script>

<style scoped>
.page-wrapper {
  display: flex;
  height: 100vh;
}

.sidebar-left {
  width: 300px;
  flex-shrink: 0;
}

.sidebar-right {
  margin-top: 11px;
  margin-right: 15px;
}

.container {
  flex: 1;
  margin-top: 5px;
  margin-left: 30px;
  margin-right: 30px;
}
</style>