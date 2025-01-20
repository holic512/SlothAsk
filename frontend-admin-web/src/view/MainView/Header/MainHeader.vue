<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Fold, Expand, Moon, Sunny } from '@element-plus/icons-vue'

const props = defineProps({
  isCollapsed: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:collapsed'])
const route = useRoute()
const isDark = ref(false)

// 切换主题
const toggleTheme = () => {
  isDark.value = !isDark.value
  const html = document.documentElement
  
  if (isDark.value) {
    html.classList.add('dark')
    localStorage.setItem('theme', 'dark')
  } else {
    html.classList.remove('dark')
    localStorage.setItem('theme', 'light')
  }
}

// 初始化主题
onMounted(() => {
  const theme = localStorage.getItem('theme')
  if (theme === 'dark') {
    isDark.value = true
    document.documentElement.classList.add('dark')
  }
})

const toggleCollapse = () => {
  emit('update:collapsed', !props.isCollapsed)
}

// 路由映射表
const routeMap = {
  dashboard: '仪表盘',
  user: {
    name: '用户管理',
    list: '用户列表',
    achievements: '成就管理',
    sign: '签到记录',
    follow: '关注管理'
  },
  question: {
    name: '题库管理',
    project: '项目分类',
    category: '题库分类',
    list: '题目列表'
  },
  comments: {
    name: '评论管理',
    question: '题目评论',
    category: '题库评论',
    rating: '评分管理'
  },
  member: {
    name: '会员管理',
    level: '会员等级',
    list: '会员列表',
    points: '积分管理'
  },
  stats: {
    name: '统计分析',
    question: '刷题统计',
    sign: '签到统计',
    wrong: '错题分析'
  },
  system: {
    name: '系统管理',
    admin: '管理员管理',
    roles: '角色权限',
    logs: '登录日志',
    config: '系统配置'
  }
}

// 计算当前路由的面包屑
const breadcrumbs = computed(() => {
  const paths = route.path.split('/').filter(Boolean)
  const result = []
  
  if (paths[0] === 'main') {
    if (paths[1] === 'dashboard') {
      return [{ title: '仪表盘' }]
    }
    
    if (paths[1] && routeMap[paths[1]]) {
      result.push({ title: routeMap[paths[1]].name })
      if (paths[2] && routeMap[paths[1]][paths[2]]) {
        result.push({ title: routeMap[paths[1]][paths[2]] })
      }
    }
  }
  
  return result
})
</script>

<template>
  <div class="main-header">
    <div class="left-section">
      <el-icon 
        class="collapse-icon"
        @click="toggleCollapse"
      >
        <component :is="props.isCollapsed ? 'Expand' : 'Fold'" />
      </el-icon>
      
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index">
          {{ item.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="right-section">
      <el-tooltip
        :content="isDark ? '切换到明亮模式' : '切换到暗黑模式'"
        placement="bottom"
      >
        <div class="theme-switch" @click="toggleTheme">
          <el-icon :size="20">
            <Moon v-if="!isDark" />
            <Sunny v-else />
          </el-icon>
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<style scoped>
.main-header {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  background-color: var(--el-bg-color);
}

.left-section {
  display: flex;
  align-items: center;
}

.collapse-icon {
  font-size: 18px;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
  transition: all 0.3s;
  color: var(--el-text-color-primary);
}

.collapse-icon:hover {
  background-color: var(--el-fill-color-light);
  color: var(--el-color-primary);
}

.breadcrumb {
  margin-left: 16px;
  font-size: 14px;
}

.right-section {
  display: flex;
  align-items: center;
}

.theme-switch {
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: all 0.3s;
  color: var(--el-text-color-primary);
}

.theme-switch:hover {
  background-color: var(--el-fill-color-light);
  color: var(--el-color-primary);
}

:deep(.el-breadcrumb__inner) {
  color: var(--el-text-color-regular);
  font-weight: normal;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: var(--el-text-color-primary);
  font-weight: 500;
}
</style> 