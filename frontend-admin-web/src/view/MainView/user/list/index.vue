<script setup>
import { ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import SearchForm from './components/SearchForm.vue'
import UserTable from './components/UserTable.vue'
import UserDetail from './components/UserDetail.vue'
import UserForm from './components/UserForm.vue'
import PasswordForm from './components/PasswordForm.vue'

// 模拟数据
const tableData = ref([
  {
    id: 1,
    username: 'admin',
    nickname: '管理员',
    email: 'admin@example.com',
    phone: '13800138000',
    status: 1,
    gender: 1,
    age: 28,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '系统管理员',
    createTime: '2024-01-01 12:00:00'
  },
  {
    id: 2,
    username: 'zhangsan',
    nickname: '张三',
    email: 'zhangsan@example.com',
    phone: '13800138001',
    status: 1,
    gender: 1,
    age: 25,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '我是张三，一名普通用户',
    createTime: '2024-01-02 13:00:00'
  },
  {
    id: 3,
    username: 'lisi',
    nickname: '李四',
    email: 'lisi@example.com',
    phone: '13800138002',
    status: 0,
    gender: 2,
    age: 30,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '已被禁用的用户',
    createTime: '2024-01-03 14:00:00'
  },
  {
    id: 4,
    username: 'wangwu',
    nickname: '王五',
    email: 'wangwu@example.com',
    phone: '13800138003',
    status: 1,
    gender: 1,
    age: 22,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '热爱编程的新手',
    createTime: '2024-01-04 15:00:00'
  },
  {
    id: 5,
    username: 'zhaoliu',
    nickname: '赵六',
    email: 'zhaoliu@example.com',
    phone: '13800138004',
    status: 1,
    gender: 2,
    age: 27,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '产品经理一枚',
    createTime: '2024-01-05 16:00:00'
  },
  {
    id: 6,
    username: 'qianqi',
    nickname: '钱七',
    email: 'qianqi@example.com',
    phone: '13800138005',
    status: 1,
    gender: 1,
    age: 32,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '资深开发工程师',
    createTime: '2024-01-06 17:00:00'
  },
  {
    id: 7,
    username: 'sunba',
    nickname: '孙八',
    email: 'sunba@example.com',
    phone: '13800138006',
    status: 0,
    gender: 2,
    age: 24,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '已禁用的测试账号',
    createTime: '2024-01-07 18:00:00'
  },
  {
    id: 8,
    username: 'zhoujiu',
    nickname: '周九',
    email: 'zhoujiu@example.com',
    phone: '13800138007',
    status: 1,
    gender: 1,
    age: 29,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    bio: '设计师一枚',
    createTime: '2024-01-08 19:00:00'
  }
])

const loading = ref(false)

// 抽屉相关
const drawerVisible = ref(false)
const currentUser = ref(null)

// 选中行
const selectedRows = ref([])

// 表单相关
const formVisible = ref(false)
const formType = ref('add')
const currentEditUser = ref(null)

// 密码表单相关
const passwordFormVisible = ref(false)
const currentPasswordUser = ref(null)

// 用户详情相关
const detailVisible = ref(false)
const currentDetailUser = ref(null)

const searchKeyword = ref('')

// 处理搜索
const handleSearch = (keyword) => {
  console.log('搜索关键词：', keyword)
  // TODO: 实现搜索逻辑
}

// 处理新增
const handleAdd = () => {
  formType.value = 'add'
  currentEditUser.value = null
  formVisible.value = true
}

// 查看用户详情
const handleView = (row) => {
  currentUser.value = row
  drawerVisible.value = true
}

// 处理编辑
const handleEdit = (row) => {
  formType.value = 'edit'
  currentEditUser.value = row
  formVisible.value = true
}

// 处理删除
const handleDelete = (row) => {
  // TODO: 调用删除 API
  console.log('删除用户：', row.id)
  ElMessage.success('删除成功')
}

// 处理修改密码
const handlePassword = (row) => {
  currentPasswordUser.value = row
  passwordFormVisible.value = true
}

// 处理密码修改提交
const handlePasswordSubmit = (formData) => {
  console.log('修改密码：', formData)
  // TODO: 调用API修改密码
  passwordFormVisible.value = false
}

// 处理选择变化
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 处理表单提交
const handleFormSubmit = (formData) => {
  console.log('表单数据：', formData)
  // TODO: 调用API保存数据
  formVisible.value = false
}

// 处理批量删除
const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) return
  
  ElMessageBox.confirm(
    `确认删除选中的 ${selectedRows.value.length} 个用户吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // TODO: 调用批量删除 API
    console.log('批量删除：', selectedRows.value.map(row => row.id))
    ElMessage.success('删除成功')
  }).catch(() => {})
}
</script>

<template>
  <div class="user-list-container">
    <!-- 搜索表单 -->
    <SearchForm
      v-model:selectedCount="selectedRows.length"
      v-model:keyword="searchKeyword"
      @search="handleSearch"
      @add="handleAdd"
      @batch-delete="handleBatchDelete"
    />

    <!-- 用户表格 -->
    <UserTable
      v-model:data="tableData"
      v-model:loading="loading"
      @view="handleView"
      @edit="handleEdit"
      @delete="handleDelete"
      @password="handlePassword"
      @selection-change="handleSelectionChange"
    />

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="100"
        :page-sizes="[10, 20, 50, 100]"
      />
    </div>

    <!-- 用户详情抽屉 -->
    <UserDetail
      v-model:visible="drawerVisible"
      v-model:userData="currentUser"
    />

    <!-- 用户表单 -->
    <UserForm
      v-model:visible="formVisible"
      v-model:type="formType"
      v-model:userData="currentEditUser"
      @submit="handleFormSubmit"
    />

    <!-- 密码表单 -->
    <PasswordForm
      v-model:visible="passwordFormVisible"
      :user-data="currentPasswordUser"
      @submit="handlePasswordSubmit"
    />

    <!-- 用户详情 -->
    <UserDetail
      v-model:visible="detailVisible"
      v-model:userData="currentDetailUser"
    />
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
  justify-content: flex-end;
}
</style> 