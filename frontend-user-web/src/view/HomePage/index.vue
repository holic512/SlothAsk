<!--Home页面的主视图-->
<script lang="ts" setup>
import TopMenu from "./view/TopMenu/TopMenu.vue";
import FooterMenu from "./view/FootMenu/FootMenu.vue"
import MessageNotificationContainer from "./components/Message/MessageNotificationContainer.vue";
import {OnlineCountDisplay} from "./components/OnlineCount";
import {useRoute} from 'vue-router';
import {computed, onMounted, ref, watch} from 'vue';
import {useScrollbarStore} from "@/pinia/ScrollbarStore";
import type {ScrollbarInstance} from 'element-plus';

const route = useRoute();
const showFooter = computed(() => !route.path.includes('questionbank'));

// 使用store保存scrollbar引用
const scrollbarStore = useScrollbarStore();
const scrollbarRef = ref<ScrollbarInstance | null>(null); // 正确定义类型

onMounted(() => {
  // 确保组件已完全挂载后再保存引用
  setTimeout(() => {
    // 将组件中的scrollbar引用保存到store中
    scrollbarStore.scrollbarRef = scrollbarRef.value;
  }, 100);
});

// 确保在路由变化时scrollbar引用始终有效
watch(() => route.path, () => {
  // 当路由变化时，可能需要重新设置scrollbar引用
  if (scrollbarRef.value && scrollbarStore.scrollbarRef !== scrollbarRef.value) {
    scrollbarStore.scrollbarRef = scrollbarRef.value;
    console.log('Scrollbar reference updated after route change');
  }
});
</script>

<template>
  <div class="index-page-container">
    <el-scrollbar ref="scrollbarRef">
      <TopMenu/>

      <router-view></router-view>

      
      <FooterMenu v-if="showFooter"/>

      <!-- 返回顶部按钮 -->
      <el-backtop :bottom="100" :right="40" target=".el-scrollbar__wrap"/>

    </el-scrollbar>
    
    <!-- 消息通知容器 -->
    <MessageNotificationContainer />
    
    <!-- 在线人数显示 -->
    <OnlineCountDisplay />
  </div>
</template>

<style scoped>
.index-page-container {
  height: 100vh;
  overflow-x: hidden; /* 防止横向滚动条 */
}


</style>