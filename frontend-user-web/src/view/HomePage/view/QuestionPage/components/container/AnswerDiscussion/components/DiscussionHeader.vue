<template>
  <div class="discussion-header">
    <h2 class="title">回答讨论 ({{ count }})</h2>
    <div class="actions">
      <el-dropdown trigger="click">
        <span class="sort-dropdown">
          {{ sortOptions[sortValue] }}
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-for="(label, key) in sortOptions" 
                             :key="key"
                             @click="handleSortChange(key as SortType)">
              {{ label }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowDown } from '@element-plus/icons-vue';
import type { SortType, SortOption } from '../types';

const props = defineProps<{
  count: number;
}>();

const emit = defineEmits<{
  (e: 'sort-change', sortType: SortType): void;
}>();

const sortOptions: SortOption = {
  newest: '最新',
  oldest: '最早',
  popular: '最热门'
};

// 使用 defineModel (Vue 3.5+ 新特性)
const sortValue = defineModel<SortType>({
  default: 'newest',
  required: false,
});

const handleSortChange = (type: SortType) => {
  sortValue.value = type;
  emit('sort-change', type);
};
</script>

<style scoped>
.discussion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.title {
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.sort-dropdown {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
}

.actions {
  display: flex;
  align-items: center;
}
</style> 