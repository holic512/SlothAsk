<template>
  <div class="sidebar">
    <!--  签到页面  -->
    <SignIn/>

    <!--  回答热力图   -->
    <Heatmap/>

    <!--  热门题目   -->
    <HotQuestions/>

  </div>
</template>

<script setup>

import SignIn from "@/view/HomePage/view/StudyPage/components/Sidebar/components/SignIn.vue";
import Heatmap from "@/view/HomePage/view/StudyPage/components/Sidebar/components/Heatmap.vue";
import HotQuestions from "@/view/HomePage/view/StudyPage/components/Sidebar/components/HotQuestions.vue";
</script>


<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 24px;
}

</style>
