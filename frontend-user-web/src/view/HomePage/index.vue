<!--Home页面的主视图-->
<script setup>
import TopMenu from "./view/TopMenu/TopMenu.vue";
import FooterMenu from "./view/FootMenu/FootMenu.vue"
import {useRoute} from 'vue-router';
import {computed} from 'vue';

const route = useRoute();
const showFooter = computed(() => !route.path.includes('questionbank'));
</script>

<template>
  <div class="index-page-container">
    <el-scrollbar>
      <TopMenu/>


      <router-view></router-view>


      <FooterMenu v-if="showFooter"/>
    </el-scrollbar>
  </div>
</template>

<style scoped>
.index-page-container {
  height: 100vh;
  overflow-x: hidden; /* 防止横向滚动条 */
}

</style>