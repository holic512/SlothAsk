<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Refresh, Download } from '@element-plus/icons-vue'

const systemStats = ref({
  cpuUsage: 32,
  memoryUsage: 45,
  diskUsage: 60,
  networkSpeed: 5.2,
  responseTime: 156,
  onlineUsers: 325
})

let timer = null

const updateStats = () => {
  // 模拟实时数据更新
  systemStats.value.cpuUsage = Math.floor(Math.random() * 30 + 20)
  systemStats.value.memoryUsage = Math.floor(Math.random() * 20 + 40)
  systemStats.value.onlineUsers = Math.floor(Math.random() * 100 + 280)
}

onMounted(() => {
  timer = setInterval(updateStats, 5000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<template>
  <el-card class="system-status">
    <template #header>
      <div class="card-header">
        <span>系统状态</span>
        <el-tag type="success" size="small">运行正常</el-tag>
      </div>
    </template>

    <div class="status-container">
      <div class="status-item">
        <div class="item-header">
          <span>CPU使用率</span>
          <span class="value">{{ systemStats.cpuUsage }}%</span>
        </div>
        <el-progress 
          :percentage="systemStats.cpuUsage"
          :color="systemStats.cpuUsage > 80 ? '#F56C6C' : ''"
        />
      </div>

      <div class="status-item">
        <div class="item-header">
          <span>内存使用率</span>
          <span class="value">{{ systemStats.memoryUsage }}%</span>
        </div>
        <el-progress 
          :percentage="systemStats.memoryUsage"
          :color="systemStats.memoryUsage > 80 ? '#F56C6C' : ''"
        />
      </div>

      <div class="status-item">
        <div class="item-header">
          <span>磁盘使用率</span>
          <span class="value">{{ systemStats.diskUsage }}%</span>
        </div>
        <el-progress 
          :percentage="systemStats.diskUsage"
          :color="systemStats.diskUsage > 80 ? '#F56C6C' : ''"
        />
      </div>

      <div class="metrics-grid">
        <div class="metric-item">
          <div class="metric-label">网络速度</div>
          <div class="metric-value">{{ systemStats.networkSpeed }} MB/s</div>
        </div>
        <div class="metric-item">
          <div class="metric-label">响应时间</div>
          <div class="metric-value">{{ systemStats.responseTime }} ms</div>
        </div>
        <div class="metric-item">
          <div class="metric-label">在线用户</div>
          <div class="metric-value">{{ systemStats.onlineUsers }}</div>
        </div>
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

.status-container {
  padding: 8px;
}

.status-item {
  margin-bottom: 20px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  
  .value {
    font-weight: bold;
  }
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-top: 20px;
}

.metric-item {
  text-align: center;
  padding: 12px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
}

.metric-label {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-bottom: 4px;
}

.metric-value {
  font-size: 16px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}
</style> 