<script setup>
import {useRouter, useRoute} from 'vue-router'
import SearchBox from './components/SearchBox/SearchBox.vue'
import {useSessionStore} from "@/pinia/Session";
import {watch} from "vue";
import {useTopMenuStore} from "@/view/HomePage/view/TopMenu/pinia/topMenuStore";

const router = useRouter()
const route = useRoute()

const handleSelect = (key) => {
  router.push(key)
}

const handleSearch = (searchText) => {
  // 处理搜索逻辑
  console.log('搜索:', searchText)
}

const handleLogoClick = () => {
  router.push({
    name: 'StudyPage'
  });
};

const handleLogin = () => {
  router.push({
    path: '/sign/email',
    query: {
      redirect: route.fullPath
    }
  });
};

// 获取用户session pinia实例
const userSession = useSessionStore();

// 获取用于 管理 topMenu的 pinia实例
const topMenuStore = useTopMenuStore();

watch(() => userSession.userSession, () => {
  // 监听 用户session是否发生改变,并修改 topMenu状态
  topMenuStore.isLogin = userSession.userSession;

})

</script>

<template>
  <div style="display: flex;justify-content: center;  border-bottom: 1px solid #ebeef5;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);">

    <el-menu
        mode="horizontal"
        :ellipsis="false"
        class="top-menu"
        @select="handleSelect"
        :default-active="route.path"
    >
      <!-- Logo -->
      <el-menu-item>
        <div class="logo-container" @click="handleLogoClick">
          <img src="/HomePage/logo.ico" alt="Logo" class="logo-img">
          <span class="logo-text">SlothAsk</span>
        </div>
      </el-menu-item>

      <!-- 导航菜单 -->
      <el-menu-item index="/">学习</el-menu-item>
      <el-menu-item index="/questionbank">题库</el-menu-item>
      <el-menu-item index="/contest">竞赛</el-menu-item>
      <el-menu-item index="/discussion">讨论</el-menu-item>
      <el-menu-item index="/interview">面试分享</el-menu-item>

      <div class="flex-grow"/>

      <!-- 搜索框 -->
      <SearchBox @search="handleSearch"/>

      <!-- 用户操作区 -->
      <div class="user-actions" v-if="topMenuStore.isLogin">
        <el-button text @click="handleLogin">登录</el-button>
        <el-button type="primary" class="vip-button">
          Sloth会员
        </el-button>
      </div>
      <div v-else style="display: flex;align-items: center;gap: 16px">
        <!--  消息页面  -->
        <el-button text>
          <el-icon size="18">
            <Bell/>
          </el-icon>
        </el-button>


        <!--  头像个人信息  -->
        <div>
          <el-avatar
              size='small'
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          />
        </div>


      </div>
    </el-menu>

  </div>


</template>

<style scoped>
.top-menu {
  height: 50px;
  width: 1300px;
  display: flex;
  align-items: center;

}

.logo-container {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px;
  border-radius: 6px;
  transition: all 0.2s ease;
}


.logo-img {
  height: 24px;
  width: 24px;
  object-fit: contain;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
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
  border-bottom: none !important;
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