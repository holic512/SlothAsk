<script setup>
import { ref } from 'vue'
import { User, QuestionFilled, Collection, View } from '@element-plus/icons-vue'

const stats = defineModel('stats', {
  default: () => ({
    totalUsers: 0,
    activeUsers: 0,
    totalQuestions: 0,
    totalCategories: 0,
    todayNewUsers: 0,
    todayActiveUsers: 0,
    todayNewQuestions: 0,
    weekGrowth: 0
  })
})

const cards = [
  {
    title: '用户统计',
    icon: User,
    mainValue: stats.value.totalUsers,
    mainLabel: '总用户数',
    subValue: stats.value.todayNewUsers,
    subLabel: '今日新增',
    color: '#409EFF',
    growth: '+12%'
  },
  {
    title: '活跃统计',
    icon: View,
    mainValue: stats.value.activeUsers,
    mainLabel: '活跃用户',
    subValue: stats.value.todayActiveUsers,
    subLabel: '今日活跃',
    color: '#67C23A',
    growth: '+5%'
  },
  {
    title: '题目统计',
    icon: QuestionFilled,
    mainValue: stats.value.totalQuestions,
    mainLabel: '题目总数',
    subValue: stats.value.todayNewQuestions,
    subLabel: '今日新增',
    color: '#E6A23C',
    growth: '+8%'
  },
  {
    title: '分类统计',
    icon: Collection,
    mainValue: stats.value.totalCategories,
    mainLabel: '题库分类',
    subValue: stats.value.weekGrowth,
    subLabel: '周增长率',
    color: '#F56C6C',
    growth: '+3%'
  }
]
</script>

<template>
  <div class="stat-cards">
    <el-row :gutter="20">
      <el-col 
        v-for="(card, index) in cards" 
        :key="index" 
        :xs="24"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="6"
        class="stat-card-col"
      >
        <el-card class="stat-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon" :style="{ backgroundColor: card.color + '20' }">
              <el-icon :style="{ color: card.color }">
                <component :is="card.icon" />
              </el-icon>
            </div>
            <div class="card-info">
              <div class="main-info">
                <div class="value">{{ card.mainValue.toLocaleString() }}</div>
                <div class="label">{{ card.mainLabel }}</div>
              </div>
              <div class="sub-info">
                <div class="sub-item">
                  <span class="sub-label">{{ card.subLabel }}</span>
                  <span class="sub-value">{{ card.subValue }}</span>
                </div>
                <div class="growth" :style="{ color: card.color }">
                  {{ card.growth }}
                  <el-icon><CaretTop /></el-icon>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stat-cards {
  width: 100%;
  margin-bottom: 20px;
}

.stat-card-col {
  margin-bottom: 20px;
}

.stat-card {
  height: 100%;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.card-icon {
  padding: 12px;
  border-radius: 8px;
  font-size: 24px;
  transition: all 0.3s ease;
}

.card-info {
  flex: 1;
}

.main-info {
  margin-bottom: 8px;
}

.value {
  font-size: 24px;
  font-weight: bold;
  line-height: 1.2;
}

.label {
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.sub-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.sub-item {
  display: flex;
  flex-direction: column;
}

.sub-label {
  color: var(--el-text-color-secondary);
}

.growth {
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 500;
}

/* 响应式样式调整 */
@media screen and (max-width: 1200px) {
  .value {
    font-size: 22px;
  }
  
  .card-icon {
    padding: 10px;
    font-size: 22px;
  }
}

@media screen and (max-width: 992px) {
  .stat-card-col {
    margin-bottom: 15px;
  }
  
  .card-content {
    gap: 12px;
  }
}

@media screen and (max-width: 768px) {
  .value {
    font-size: 20px;
  }
  
  .card-icon {
    padding: 8px;
    font-size: 20px;
  }
  
  .label {
    font-size: 13px;
  }
  
  .sub-info {
    font-size: 12px;
  }
}

@media screen and (max-width: 480px) {
  .stat-card-col {
    margin-bottom: 10px;
  }
  
  .card-content {
    gap: 10px;
  }
  
  .value {
    font-size: 18px;
  }
  
  .card-icon {
    padding: 8px;
    font-size: 18px;
  }
}
</style> 