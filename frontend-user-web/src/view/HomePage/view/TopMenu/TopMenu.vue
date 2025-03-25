<script setup>
import {useRouter, useRoute} from 'vue-router'
import SearchBox from './components/SearchBox/SearchBox.vue'
import {useSessionStore} from "@/pinia/Session";
import {watch, ref} from "vue";
import {useTopMenuStore} from "@/view/HomePage/view/TopMenu/pinia/topMenuStore";
import UserUtil from "@/view/HomePage/view/TopMenu/components/UserUtil/UserUtil.vue";
import {getUserNameAndAvatar} from '@/view/HomePage/view/TopMenu/Api/ApiUserInfo';
import {Bell} from "@element-plus/icons-vue";

const router = useRouter()
const route = useRoute()

// 用户信息
const userInfo = ref({
  nickname: '',
  avatar: ''
});

// 获取用户信息的方法
const fetchUserInfo = async () => {
  try {
    const response = await getUserNameAndAvatar();
    if (response.status === 200) {
      userInfo.value = response.data;

      return 200;
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
};

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

watch(() => userSession.userSession, async (newValue) => {
  // 监听 用户session是否发生改变,并修改 topMenu状态  根据这个 token 获取个人信息
  // 存在使用这个token 到后端验证是否有效  目前是 获取 用户姓名和头像


  if (newValue === null) {
    topMenuStore.isLogin = false;
    return
  } else {
    // 先更新状态
    topMenuStore.isLogin = true;
  }
  const status = await fetchUserInfo()
  // 如果用户已登录，则获取用户信息
  if (status === 200) {
    topMenuStore.isLogin = true;
  }
}, {immediate: true})

// 根据昵称生成头像显示文本
const getAvatarText = (nickname) => {
  if (!nickname) return '';
  return nickname.charAt(0).toUpperCase();
};

</script>

<template>
  <div style="display: flex;justify-content: center;  border-bottom: 1px solid #ebeef5;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); width: 100vw">

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
      <div v-if="topMenuStore.isLogin" style="display: flex;align-items: center;gap: 16px">
        <!--  消息页面  -->
        <el-button text>
          <el-icon size="18">
            <Bell/>
          </el-icon>
        </el-button>


        <!-- 头像个人信息 -->
        <el-popover
            placement="bottom"
            trigger="click"
            popper-class="custom-avatar-popover"
            width="300"
            :hide-after="0"
        >

          <!--  用户信息工具  -->
          <UserUtil :userInfo="userInfo"/>

          <!-- 头像作为 reference -->
          <template #reference>
            <div>
              <el-avatar
                  size="small"
                  :src="userInfo.avatar"
                  v-if="userInfo.avatar"
              />
              <el-avatar
                  size="small"
                  v-else
              >{{ getAvatarText(userInfo.nickname) }}
              </el-avatar>
            </div>
          </template>
        </el-popover>


        <el-button type="primary" class="vip-button">
          Sloth会员
        </el-button>

      </div>

      <div class="user-actions" v-else>
        <el-button text @click="handleLogin">登录</el-button>
        <el-button type="primary" class="vip-button">
          Sloth会员
        </el-button>
      </div>

    </el-menu>

  </div>


</template>

<style>
/* 全局样式，不使用scoped，确保能够应用到body下的元素 */
.custom-avatar-popover {
  border-radius: 12px !important;
  padding: 16px !important;
}

.custom-avatar-popover .el-popper__arrow {
  display: none !important; /* 隐藏小箭头 */
}
</style>

<style scoped>
.top-menu {
  height: 50px;
  width: 1380px;
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