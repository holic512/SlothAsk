<!--Home页面的主视图-->
<script setup>
import TopMenu from "./view/TopMenu/TopMenu.vue";
import FooterMenu from "./view/FootMenu/FootMenu.vue"
import { useRoute } from 'vue-router';
import { computed } from 'vue';

const route = useRoute();
const showFooter = computed(() => !route.path.includes('questionbank'));
</script>

<template>
  <div class="page-container">
    <el-scrollbar>
      <TopMenu/>
      <router-view></router-view>
    </el-scrollbar>
    <FooterMenu v-if="showFooter" />
  </div>
</template>

<style scoped>

</style>