<script setup lang="ts">
import { useLogList } from './hooks/useLogList'
import LogTable from './components/LogTable.vue'
import SearchForm from './components/SearchForm.vue'

const { logs, searchQuery, pagination } = useLogList()

const handleReset = () => {
  searchQuery.value = ''
}
</script>

<template>
  <div class="logs-container">
    <h2>登录日志</h2>
    <SearchForm v-model:searchQuery="searchQuery" @reset="handleReset" />
    <LogTable :logs="logs" :pagination="pagination" :total="logs.length" />
  </div>
</template>

<style scoped>
.logs-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 600;
  color: #1f2f3d;
}
</style> 