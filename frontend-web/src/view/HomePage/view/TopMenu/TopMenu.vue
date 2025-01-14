<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import SearchBox from './components/SearchBox/index.vue'

const router = useRouter()

const handleSelect = (key) => {
  router.push(key)
}

const handleSearch = (searchText) => {
  // 处理搜索逻辑
  console.log('搜索:', searchText)
}
</script>

<template>
  <el-menu
      mode="horizontal"
      :ellipsis="false"
      class="top-menu"
      @select="handleSelect"
  >
    <!-- Logo -->
    <el-menu-item >
      <img src="/HomePage/logo.ico" alt="Logo" class="logo"/>
      <span class="logo-text">SlothAsk</span>
    </el-menu-item>

    <!-- 导航菜单 -->
    <el-menu-item index="/">学习</el-menu-item>
    <el-menu-item index="/problemset">题库</el-menu-item>
    <el-menu-item index="/contest">竞赛</el-menu-item>
    <el-menu-item index="/discussion">讨论</el-menu-item>
    <el-menu-item index="/interview">面试分享</el-menu-item>

    <div class="flex-grow"/>

    <!-- 搜索框 -->
    <SearchBox @search="handleSearch"/>

    <!-- 用户操作区 -->
    <div class="user-actions">
      <el-button text>登录</el-button>
      <el-button type="primary" class="vip-button">
        Sloth会员
      </el-button>
    </div>
  </el-menu>
</template>

<style scoped>
.top-menu {
  height: 50px;
  padding: 0 10vw;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.logo {
  height: 24px;
  margin-right: 6px;
}

.logo-text {
  font-size: 15px;
  font-weight: 550;
  color: #2c3e50;
  letter-spacing: -0.5px;
}

.flex-grow {
  flex-grow: 1;
}

.user-actions {
  display: flex;
  align-items: center;

  gap: 4px;
}

.vip-button {
  display: flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 4px;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-menu-item) {
  font-size: 13px;
  padding: 0 12px;
  height: 45px;
}

:deep(.el-button.is-text) {
  padding: 8px;
}

</style>