<script setup>
import { ref } from 'vue'
import { Refresh, Download } from '@element-plus/icons-vue'

const memberStats = ref({
  totalMembers: 486,
  monthlyGrowth: 45,
  renewalRate: 85,
  averageSpending: 258
})

const levelDistribution = ref([
  { level: '普通会员', count: 320 },
  { level: '黄金会员', count: 120 },
  { level: '铂金会员', count: 46 }
])
</script>

<template>
  <el-card class="membership-stats">
    <template #header>
      <div class="card-header">
        <span>会员统计</span>
        <el-button-group>
          <el-button :icon="Refresh" circle size="small" />
          <el-button :icon="Download" circle size="small" />
        </el-button-group>
      </div>
    </template>
    
    <div class="stats-grid">
      <div class="stat-item">
        <div class="stat-label">总会员数</div>
        <div class="stat-value">{{ memberStats.totalMembers }}</div>
      </div>
      <div class="stat-item">
        <div class="stat-label">月增长</div>
        <div class="stat-value">{{ memberStats.monthlyGrowth }}</div>
      </div>
      <div class="stat-item">
        <div class="stat-label">续费率</div>
        <div class="stat-value">{{ memberStats.renewalRate }}%</div>
      </div>
      <div class="stat-item">
        <div class="stat-label">平均消费</div>
        <div class="stat-value">¥{{ memberStats.averageSpending }}</div>
      </div>
    </div>

    <el-divider />
    
    <div class="level-distribution">
      <div class="section-title">会员等级分布</div>
      <div v-for="item in levelDistribution" :key="item.level" class="level-item">
        <span>{{ item.level }}</span>
        <el-progress 
          :percentage="Math.round(item.count / memberStats.totalMembers * 100)"
          :stroke-width="8"
        />
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.section-title {
  margin-bottom: 16px;
  font-weight: bold;
}

.level-item {
  margin-bottom: 12px;
  
  span {
    display: block;
    margin-bottom: 4px;
    color: var(--el-text-color-secondary);
  }
}
</style> 