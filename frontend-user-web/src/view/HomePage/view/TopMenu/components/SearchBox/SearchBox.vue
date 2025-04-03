<script setup>
import { ref, nextTick } from 'vue'
import {Search} from "@element-plus/icons-vue";


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

const expandSearch = async () => {
  isSearchExpanded.value = true
  await nextTick()
  inputRef.value?.focus()
}

const collapseSearch = () => {
  if (!searchInput.value) {
    isSearchExpanded.value = false
  }
}

const handleSearch = () => {
  emit('search', searchInput.value.trim())
}
</script>

<template>
  <div class="search-container" :class="{ 'is-expanded': isSearchExpanded }">
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
    <el-button v-if="!isSearchExpanded" @click="expandSearch" class="search-icon" text>
      <el-icon><Search /></el-icon>
    </el-button>
  </div>
</template>

<style scoped>
.search-container {
  position: relative;
  width: 32px;
  transition: width 0.3s ease;
  margin: 0 8px;

  &.is-expanded {
    width: 220px;

    .search-icon {
      display: none;
    }

    :deep(.el-input__wrapper) {
      box-shadow: 0 0 0 1px #dcdfe6;
      border-radius: 4px;

      &:hover {
        box-shadow: 0 0 0 1px #c0c4cc;
      }

      &.is-focus {
        box-shadow: 0 0 0 1px #409eff;
      }
    }
  }

  &:not(.is-expanded) {
    :deep(.el-input),
    :deep(.el-input__wrapper) {
      opacity: 0;
      pointer-events: none;
    }
  }
}

.search-icon {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  font-size: 18px;
  color: #606266;
  cursor: pointer;
  z-index: 1;
}
</style>