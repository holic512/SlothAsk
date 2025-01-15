<script setup>
import { ref, nextTick } from 'vue'
import { Search } from '@element-plus/icons-vue'

const props = defineProps({
  placeholder: {
    type: String,
    default: '搜索题目、文章...'
  }
})

const emit = defineEmits(['search'])

const searchInput = ref('')
const isSearchExpanded = ref(false)
const inputRef = ref(null)

const expandSearch = () => {
  isSearchExpanded.value = true
  nextTick(() => {
    inputRef.value.focus()
  })
}

const collapseSearch = () => {
  if (!searchInput.value) {
    isSearchExpanded.value = false
  }
}

const handleSearch = () => {
  emit('search', searchInput.value)
}
</script>

<template>
  <div 
    class="search-container"
    :class="{ 'is-expanded': isSearchExpanded }"
  >
    <el-input
      ref="inputRef"
      v-model="searchInput"
      :placeholder="placeholder"
      :prefix-icon="Search"
      clearable
      @focus="expandSearch"
      @blur="collapseSearch"
      @keyup.enter="handleSearch"
    />
    <el-icon 
      class="search-icon" 
      @click="expandSearch"
    >
      <Search />
    </el-icon>
  </div>
</template>

<style scoped>
.search-container {
  position: relative;
  width: 32px;
  transition: all 0.3s ease;
  margin: 0 8px;
}

.search-container.is-expanded {
  width: 220px;
}

.search-icon {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 18px;
  color: #606266;
  cursor: pointer;
  z-index: 1;
}

.search-container:not(.is-expanded) .el-input {
  opacity: 0;
  pointer-events: none;
}

.search-container.is-expanded .search-icon {
  display: none;
}

.search-container:not(.is-expanded) :deep(.el-input__wrapper) {
  opacity: 0;
}

.search-container.is-expanded :deep(.el-input__wrapper) {
  transition: all 0.3s ease;
  box-shadow: 0 0 0 1px #dcdfe6;
  border-radius: 4px;
}

.search-container.is-expanded :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc;
}

.search-container.is-expanded :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff;
}

.search-container :deep(.el-input),
.search-container :deep(.el-input__wrapper) {
  transition: all 0.3s ease;
}
</style> 