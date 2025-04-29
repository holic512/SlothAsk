<template>
  <div class="question-list">
    <!-- header -->
    <div class="ql-header">
      <span class="ql-title">我的题单</span>
      <div class="ql-actions">
        <!-- 新增按钮图标也可配置颜色 / 大小 -->
        <el-icon
            :style="{
            color: addIcon.color || 'inherit',
            fontSize: addIcon.size || '16px'
          }"
            class="action-icon"
            @click.stop="onAdd"
        >
          <Plus/>
        </el-icon>
        <el-icon
            :style="{
            color: toggleIcon.color || 'inherit',
            fontSize: toggleIcon.size || '16px'
          }"
            class="action-icon"
            @click.stop="toggleOpen"
        >
          <component :is="isOpen ? ArrowUp : ArrowDown"/>
        </el-icon>
      </div>
    </div>

    <!-- list -->
    <ul v-show="isOpen" class="ql-items">
      <li
          v-for="item in items"
          :key="item.id"
          :class="{ selected: item.path === currentPath, disabled: !item.enabled }"
          class="ql-item"
          @click="onSelect(item)"
      >
        <el-icon
            :style="{
            color: item.iconColor || 'inherit',
            fontSize: item.iconSize || '16px'
          }"
            class="item-icon"
        >
          <component :is="item.icon"/>
        </el-icon>
        <span :style="{ fontSize: item.fontSize || '14px' }" class="item-label">
          {{ item.label }}
        </span>
        <el-icon
            v-if="item.locked"
            :style="{
            color: item.lockedIconColor || 'inherit',
            fontSize: item.lockedIconSize || '14px'
          }"
            class="lock-icon"
        >
          <Lock/>
        </el-icon>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import {computed, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ArrowDown, ArrowUp, Lock, Plus, StarFilled,} from '@element-plus/icons-vue'

interface QuestionItem {
  id: string | number
  label: string
  path: string
  icon: any
  locked?: boolean
  enabled?: boolean
  fontSize?: string

  // 新增图标配置
  iconColor?: string       // e.g. '#ff4d4f' 或 'inherit'
  iconSize?: string        // e.g. '20px', '1.2em'

  // 锁图标也可单独配置
  lockedIconColor?: string
  lockedIconSize?: string
}

// 全局操作图标配置
const addIcon = {color: '#000000', size: '16px'}
const toggleIcon = {color: '#000000', size: '16px'}

const items = ref<QuestionItem[]>([
  {
    id: 1,
    label: '我的收藏',
    path: 'myFavoritesQuestion',
    icon: StarFilled,
    locked: true,
    enabled: true,
    fontSize: '14px',
    iconColor: '#faad14',
    iconSize: '18px',
    lockedIconColor: '#ff4d4f',
    lockedIconSize: '14px',
  },
  // 你可以在这里继续添加更多项，并为它们指定不同的 iconColor / iconSize
])

const route = useRoute()
const router = useRouter()
const isOpen = ref(true)
const currentPath = computed(() => route.path)

const toggleOpen = () => {
  isOpen.value = !isOpen.value
}

const onAdd = () => {
  console.log('点击新增题单')
}

const onSelect = (item: QuestionItem) => {
  if (item.enabled === false) return
  if (route.path !== item.path) {
    router.push(item.path)
  }
}
</script>

<style lang="scss" scoped>
.question-list {
  .ql-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 8px;
    height: 32px;
    cursor: pointer;

    .ql-title {
      font-size: 14px;
      font-weight: bold;
    }

    .ql-actions {
      display: flex;
      align-items: center;
      gap: 4px;

      .action-icon {
        cursor: pointer;
      }
    }
  }

  .ql-items {
    list-style: none;
    margin: 0;
    padding: 4px 0;

    .ql-item {
      display: flex;
      align-items: center;
      padding: 10px 8px;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.2s;

      .item-icon,
      .lock-icon {
        margin-right: 6px;
        display: flex;
        align-items: center;
      }

      .item-label {
        flex: 1;
        line-height: 1;
      }

      &:hover:not(.disabled),
      &.selected {
        background-color: #f5f5f5;
      }

      &.disabled {
        opacity: 0.5;
        cursor: not-allowed;
      }
    }
  }
}
</style>
