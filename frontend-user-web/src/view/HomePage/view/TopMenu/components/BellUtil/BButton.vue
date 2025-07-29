<script lang="ts" setup>

</script>

<template>
  <el-badge :is-dot="false" :offset="[-10, 8]">
    <!--    操作按钮   -->
    <el-button class="navbar-right-button" text>
      <el-icon>
        <img alt="bell" src="/HomePage/TopMenu/bell-l.png" style="width: 18px; height: 18px;"/>
      </el-icon>
    </el-button>
  </el-badge>
</template>

<style scoped>
.navbar-right-button {
  width: 32px;
}
</style>