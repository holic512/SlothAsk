<script setup>
import { ref } from 'vue'
import StatCards from './components/StatCards.vue'
import UserTrendChart from './components/UserTrendChart.vue'
import AnalysisCharts from './components/AnalysisCharts.vue'
import QuestionAnalysis from './components/QuestionAnalysis.vue'
import MembershipStats from './components/MembershipStats.vue'
import RecentActivities from './components/RecentActivities.vue'
import SystemStatus from './components/SystemStatus.vue'

// 扩展统计数据
const stats = ref({
  // 用户相关
  totalUsers: 2458,
  activeUsers: 1890,
  todayNewUsers: 128,
  todayActiveUsers: 956,
  monthlyActiveUsers: 12560,
  
  // 题目相关
  totalQuestions: 3567,
  totalCategories: 25,
  todayNewQuestions: 45,
  weekGrowth: 15,
  todaySubmissions: 3245,
  correctRate: 76.5,
  
  // 会员相关
  totalMembers: 486,
  todayNewMembers: 12,
  membershipRevenue: 25800,
  
  // 系统状态
  systemLoad: 32,
  responseTime: 156,
  onlineUsers: 325,
  
  // 互动数据
  totalComments: 12456,
  todayComments: 234,
  totalLikes: 45678,
  todayLikes: 567
})
</script>

<template>
  <el-scrollbar height="calc(100vh - 60px)" class="dashboard-scrollbar">
    <div class="dashboard-container">
      <!-- 核心统计指标 -->
      <StatCards v-model:stats="stats" class="mb-20"/>
      
      <!-- 主要业务指标 -->
      <el-row :gutter="20" class="dashboard-row">
        <!-- 用户趋势图表 -->
        <el-col :xs="24" :sm="24" :md="24" :lg="16" :xl="16" class="mb-20">
          <UserTrendChart />
        </el-col>
        <!-- 会员统计 -->
        <el-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8" class="mb-20">
          <MembershipStats />
        </el-col>
      </el-row>
      
      <!-- 业务分析区域 -->
      <el-row :gutter="20" class="dashboard-row">
        <!-- 题目分析 -->
        <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="mb-20">
          <QuestionAnalysis />
        </el-col>
        <!-- 综合分析 -->
        <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" class="mb-20">
          <AnalysisCharts />
        </el-col>
      </el-row>
      
      <!-- 辅助信息区域 -->
      <el-row :gutter="20" class="dashboard-row">
        <!-- 系统状态 -->
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" class="mb-20">
          <SystemStatus />
        </el-col>
        <!-- 最近活动 -->
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" class="mb-20">
          <RecentActivities />
        </el-col>
      </el-row>
    </div>
  </el-scrollbar>
</template>

<style scoped>
.dashboard-scrollbar {
  background-color: var(--el-bg-color-page);
}

.dashboard-container {
  padding: 20px;
  min-height: 100%;
}

.dashboard-row {
  margin-bottom: 0;
}

.mb-20 {
  margin-bottom: 20px;
}

:deep(.el-card) {
  height: 100%;
  transition: all 0.3s;
}

:deep(.el-card:hover) {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

/* 增强响应式样式 */
@media screen and (max-width: 768px) {
  .dashboard-container {
    padding: 10px;
  }
  
  .mb-20 {
    margin-bottom: 15px;
  }
  
  :deep(.el-card) {
    margin-bottom: 15px;
  }
}

@media screen and (max-width: 480px) {
  .dashboard-container {
    padding: 8px;
  }
  
  .mb-20 {
    margin-bottom: 10px;
  }
  
  :deep(.el-card) {
    margin-bottom: 10px;
  }
}
</style>