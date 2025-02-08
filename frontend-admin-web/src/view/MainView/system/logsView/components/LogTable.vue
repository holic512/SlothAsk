<template>
  <div class="table-container">
    <el-table :data="logs" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="login_time" label="登录时间" />
      <el-table-column prop="login_ip" label="登录IP" />
      <el-table-column prop="login_status" label="状态">
        <template #default="{ row }">
          <el-tag :type="row.login_status === 1 ? 'success' : 'danger'">
            {{ row.login_status === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="user_agent" label="浏览器信息" />
    </el-table>
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current_page"
        :page-size="pagination.page_size"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import type { AdminLoginLog } from '../../types/log'

const props = defineProps<{
  logs: AdminLoginLog[]
  pagination: { current_page: number; page_size: number }
  total: number
}>()

const handlePageChange = (page: number) => {
  props.pagination.current_page = page
}

const handleSizeChange = (size: number) => {
  props.pagination.page_size = size
}
</script>

<style scoped>
.table-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  margin-bottom: 20px;
}

.el-table {
  margin-top: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style> 