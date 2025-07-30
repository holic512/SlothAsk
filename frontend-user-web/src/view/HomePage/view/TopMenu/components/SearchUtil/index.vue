<script setup>
import {onMounted, onUnmounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {Search} from '@element-plus/icons-vue'

const router = useRouter()

// 搜索相关状态
const searchQuery = ref('')
const isExpanded = ref(false)
const isSearchFocused = ref(false)
const searchInputRef = ref(null)
const isSearching = ref(false) // 标记是否正在执行搜索操作

// 展开搜索框
const expandSearch = () => {
  isExpanded.value = true
  // 延迟聚焦，确保动画完成后再聚焦
  setTimeout(() => {
    if (searchInputRef.value) {
      searchInputRef.value.focus()
    }
  }, 200)
}

// 收缩搜索框
const collapseSearch = () => {
  if (!searchQuery.value.trim()) {
    isExpanded.value = false
    isSearchFocused.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    isSearching.value = true
    router.push({
      path: '/search',
      query: { q: searchQuery.value.trim() }
    })
    // 搜索后收缩
    searchQuery.value = ''
    isExpanded.value = false
    isSearchFocused.value = false
    // 确保输入框失去焦点
    if (searchInputRef.value) {
      searchInputRef.value.blur()
    }
    // 重置搜索标志
    setTimeout(() => {
      isSearching.value = false
    }, 200)
  }
}

// 搜索框聚焦和失焦
const handleSearchFocus = () => {
  isSearchFocused.value = true
}

const handleSearchBlur = () => {
  isSearchFocused.value = false
  // 如果正在执行搜索操作，不执行延迟收缩
  if (!isSearching.value) {
    // 延迟收缩，给用户时间点击清除按钮
    setTimeout(() => {
      collapseSearch()
    }, 150)
  }
}

// 处理搜索框回车
const handleSearchKeyup = (event) => {
  if (event.key === 'Enter') {
    handleSearch()
  } else if (event.key === 'Escape') {
    searchQuery.value = ''
    collapseSearch()
  }
}

// 清除搜索内容
const clearSearch = () => {
  searchQuery.value = ''
  if (searchInputRef.value) {
    searchInputRef.value.focus()
  }
}

// 点击外部收缩
const handleClickOutside = (event) => {
  const searchContainer = event.target.closest('.search-util-container')
  if (!searchContainer && isExpanded.value && !searchQuery.value.trim()) {
    collapseSearch()
  }
}

// 阻止输入框点击事件冒泡
const handleInputClick = (event) => {
  event.stopPropagation()
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
  <div class="search-util-container">
    <div 
      :class="{ 'expanded': isExpanded, 'focused': isSearchFocused }"
      class="search-wrapper"
      @click="!isExpanded && expandSearch()"
    >
      <el-icon class="search-icon" color="#00000">
        <Search />
      </el-icon>
      <input
        ref="searchInputRef"
        v-model="searchQuery"
        :class="{ 'visible': isExpanded }"
        class="search-input"
        placeholder="搜索题目..."
        type="text"
        @blur="handleSearchBlur"
        @click="handleInputClick"
        @focus="handleSearchFocus"
        @keyup="handleSearchKeyup"
      />
      <button
        v-if="searchQuery && isExpanded"
        class="search-clear"
        @click="clearSearch"
      >
        ×
      </button>
    </div>
  </div>
</template>

<style scoped>
.search-util-container {
  position: relative;
  display: flex;
  align-items: center;
}

/* 搜索包装器样式 */
.search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: transparent;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.search-wrapper:hover {
  background: #F5F7FA;
}

.search-wrapper.expanded {
  justify-content: flex-start;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
}

.search-wrapper.expanded {
  width: 280px;
  cursor: default;
}

.search-wrapper.expanded.focused {
  background: white;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
}

.search-icon {
  margin-left: 10px;
  font-size: 18px;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.search-wrapper.expanded .search-icon {
  margin-left: 10px;
  margin-right: 6px;
  color: #999;
  font-size: 14px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 13px;
  color: #333;
  padding: 0;
  margin-right: 8px;
  width: 0;
  opacity: 0;
  transition: all 0.3s ease 0.1s;
}

.search-input.visible {
  width: auto;
  opacity: 1;
}

.search-input::placeholder {
  color: #999;
}

.search-clear {
  position: absolute;
  right: 6px;
  width: 18px;
  height: 18px;
  border: none;
  background: #ccc;
  color: white;
  border-radius: 50%;
  font-size: 11px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
  flex-shrink: 0;
}

.search-clear:hover {
  background: #999;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .search-wrapper.expanded {
    width: 240px;
  }
}

@media (max-width: 900px) {
  .search-wrapper.expanded {
    width: 200px;
  }
}
</style>