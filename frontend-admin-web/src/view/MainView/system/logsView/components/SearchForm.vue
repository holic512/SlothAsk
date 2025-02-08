<template>
  <el-form :inline="true" @submit.native.prevent class="search-form">
    <el-form-item>
      <el-input
        :model-value="searchQuery"
        placeholder="搜索用户名"
        clearable
        @input="handleSearch"
        class="search-input"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleSearchClick">搜索</el-button>
    </el-form-item>
    <el-form-item>
      <el-button @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const props = defineProps<{
  searchQuery: string
}>()

const emit = defineEmits(['update:searchQuery', 'reset'])

const handleSearch = (value: string) => {
  emit('update:searchQuery', value)
}

const handleSearchClick = () => {
  emit('update:searchQuery', props.searchQuery)
}

const handleReset = () => {
  emit('reset')
}
</script>

<style scoped>
.search-form {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.search-input {
  width: 300px;
}

.el-form-item {
  margin-right: 10px;
}
</style> 