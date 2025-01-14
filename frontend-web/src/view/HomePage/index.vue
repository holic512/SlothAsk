<!--Home页面的主视图-->
<script setup>

import TopMenu from "./view/TopMenu/TopMenu.vue";
</script>

<template>
  <div style="height: 100vh">
    <el-scrollbar>
      <TopMenu/>
      <router-view></router-view>
    </el-scrollbar>
  </div>
</template>

<style scoped>

</style>