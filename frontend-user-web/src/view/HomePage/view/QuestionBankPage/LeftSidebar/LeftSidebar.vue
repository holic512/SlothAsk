<template>
  <div class="sidebar-menu">
    <div class="button-menu">
      <button
          v-for="item in menuItems"
          :key="item.path"
          :class="[
          'sidebar-btn',
          { active: isActive(item), disabled: !item.enabled }
        ]"
          :disabled="!item.enabled"
          @click="onClick(item)"
      >
        <el-icon class="icon" color="#000000" size="18">
          <component :is="item.icon"/>
        </el-icon>
        <!-- 支持自定义 fontSize -->
        <span :style="{ fontSize: item.fontSize || '14px' }">
          {{ item.label }}
        </span>
      </button>
    </div>

    <div class="divider"></div>

    <ProblemList/>
  </div>
</template>

<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {ElIcon} from 'element-plus'
import {Document, Notebook} from '@element-plus/icons-vue'
import ProblemList from "@/view/HomePage/view/QuestionBankPage/LeftSidebar/components/ProblemList.vue";

interface MenuItem {
  label: string
  path: string
  icon: any
  fontSize?: string    // 可选，传入 '16px'、'1rem' 等
  enabled?: boolean    // 可选，默认启用
}

const route = useRoute()
const router = useRouter()

const menuItems: MenuItem[] = [
  {label: '题库', path: '/questionbank/questionbank', icon: Document, fontSize: '16px', enabled: true},
  {label: '面试笔记', path: '/interview-notes', icon: Notebook, fontSize: '16px', enabled: false},
  // 可以继续增删、调整 fontSize、enabled
]

const isActive = (item: MenuItem) => route.path === item.path

const onClick = (item: MenuItem) => {
  if (!item.enabled) return
  if (route.path !== item.path) {
    router.push(item.path)
  }
}
</script>

<style lang="scss" scoped>
.sidebar-menu {
  width: 100%;
  height: 100%;
  padding: 24px 16px;
  box-sizing: border-box;

  .button-menu {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 16px;

    .sidebar-btn {
      width: 220px;
      height: 46px;
      display: flex;
      align-items: center; /* 垂直居中 */
      justify-content: flex-start;
      padding: 0 12px;
      border-radius: 6px;
      background-color: transparent;
      border: none;
      cursor: pointer;
      transition: background-color 0.2s;

      .icon {
        margin-right: 8px;
        display: flex;
        align-items: center; /* 图标水平垂直居中 */
      }

      span {
        font-weight: 600;
        // 为保证文字与图标对齐
        margin-top: 2px;
        line-height: 1; /* 和图标保持垂直对齐 */
      }

      &:hover:not(.disabled) {
        background-color: #f5f5f5;
      }

      &.active {
        background-color: #f5f5f5;
      }

      &.disabled {
        opacity: 0.5;
        cursor: not-allowed;
        pointer-events: none;
      }
    }
  }

  .divider {
    height: 1px;
    background-color: #eaeaea;
    margin: 16px 0;
    width: 100%;
  }
}
</style>
