<template>
  <div class="sidebar">
    <!--  签到页面  -->
    <SignIn/>

    <!--  回答热力图   -->
    <Heatmap :backendData="heatmapData"/>

    <!--  热门题目   -->
    <HotQuestions/>

  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import SignIn from "@/view/HomePage/view/StudyPage/components/Sidebar/components/SignIn.vue";
import Heatmap from "@/view/HomePage/view/StudyPage/components/Sidebar/components/Heatmap.vue";
import HotQuestions from "@/view/HomePage/view/StudyPage/components/Sidebar/components/HotQuestions.vue";
import {apiGetUserSubmitCountStats} from "@/view/HomePage/view/StudyPage/service/ApiGetUserSubmitCountStats";
import {isUserLoggedIn} from "@/utils/useIsLoggedIn.js";

// 热力图数据
const heatmapData = ref([]);

// 获取用户提交次数统计数据
const fetchHeatmapData = async () => {

  if (!isUserLoggedIn()) {
    // console.log('未登录，不查询');
    return;
  }

  try {
    const response = await apiGetUserSubmitCountStats();
    if (response.status === 200) {
      // 转换数据格式以匹配Heatmap组件的期望格式
      heatmapData.value = response.data.map(item => ({
        date: item.date,
        answerTimes: item.count
      }));
    }
  } catch (error) {
    console.error('获取用户提交次数统计失败:', error);
    // 如果获取失败，保持空数组，Heatmap组件会使用模拟数据
    heatmapData.value = [];
  }
};

// 组件挂载时获取数据
onMounted(() => {
  fetchHeatmapData();
});
</script>


<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 24px;
}

</style>
