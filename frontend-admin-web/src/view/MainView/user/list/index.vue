<script setup lang="ts">
import {onMounted, computed} from 'vue'
import SearchForm from './components/SearchForm.vue'
import UserTable from './components/UserTable.vue'
import UserDetail from './components/UserDetail.vue'
import UserForm from './components/UserForm.vue'
import PasswordForm from './components/PasswordForm.vue'
import {useUserListStore} from "./pinia/userListStore";
import {apiFetchUserList} from "./service/ApiFetchUserList";
import {handleCurrentNumChange} from "./service/handleCurrentNumChange";
import {handleSizeChange} from "./service/handleSizeChange";

// 定义变量属性类
const userListStore = useUserListStore()

// 添加计算属性确保 total 为数字类型
const totalCount = computed((): number => {
  return Number(userListStore.total) || 0
})

// 钩子函数 当页面渲染时获取基础数据
onMounted(async (): Promise<void> => {
  const userListData = await apiFetchUserList({
    keyword: userListStore.searchKeyword,
    status: userListStore.searchStatus,
    pageNum: userListStore.pageNum,
    pageSize: userListStore.pageSize
  })

  if (userListData.status === 200) {
    userListStore.tableData = userListData.data.list
    userListStore.total = userListData.data.total
  }
})

</script>

<template>
  <div class="user-list-container">
    <!-- 搜索表单 -->
    <SearchForm/>

    <!-- 用户表格 -->
    <UserTable/>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="userListStore.total"
          :page-sizes="[10, 20, 50, 100]"
          :current-page="userListStore.pageNum"
          :page-size="userListStore.pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentNumChange"
      />
    </div>

    <!-- 用户详情抽屉 -->
    <UserDetail/>

    <!-- 用户表单 -->
    <UserForm/>

    <!-- 密码表单 -->
    <PasswordForm/>

  </div>
</template>

<style scoped>
.user-list-container {
  padding: 16px;
  background-color: var(--el-bg-color);
  border-radius: 4px;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: end;
}
</style> 