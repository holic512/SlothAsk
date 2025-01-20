<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'

const userStatusRef = ref(null)
const questionDifficultyRef = ref(null)

let charts = {
  userStatus: null,
  questionDifficulty: null
}

// 用户状态分布
const initUserStatusChart = () => {
  charts.userStatus = echarts.init(userStatusRef.value)
  charts.userStatus.setOption({
    title: {
      text: '用户状态分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '用户状态',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 1048, name: '正常用户' },
          { value: 235, name: '禁用用户' },
          { value: 580, name: '未激活' }
        ]
      }
    ]
  })
}

// 题目难度分布
const initQuestionDifficultyChart = () => {
  charts.questionDifficulty = echarts.init(questionDifficultyRef.value)
  charts.questionDifficulty.setOption({
    title: {
      text: '题目难度分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: ['简单', '中等', '困难'],
        axisTick: {
          alignWithLabel: true
        }
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: '题目数量',
        type: 'bar',
        barWidth: '60%',
        data: [
          {
            value: 1200,
            itemStyle: { color: '#67C23A' }
          },
          {
            value: 900,
            itemStyle: { color: '#E6A23C' }
          },
          {
            value: 300,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
    ]
  })
}

const handleResize = () => {
  Object.values(charts).forEach(chart => chart?.resize())
}

onMounted(() => {
  initUserStatusChart()
  initQuestionDifficultyChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  Object.values(charts).forEach(chart => chart?.dispose())
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <el-card class="analysis-charts-container">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" class="chart-col">
        <el-card class="chart-card" shadow="hover">
          <div ref="userStatusRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" class="chart-col">
        <el-card class="chart-card" shadow="hover">
          <div ref="questionDifficultyRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped>
.analysis-charts-container {
  margin-bottom: 20px;
}

.chart-col {
  margin-bottom: 20px;
}

.chart-card {
  height: 100%;
  transition: all 0.3s ease;
}

.chart-card:hover {
  transform: translateY(-5px);
}

.chart {
  height: 300px;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .chart {
    height: 250px;
  }
  
  .chart-col {
    margin-bottom: 10px;
  }
}

@media screen and (max-width: 480px) {
  .chart {
    height: 200px;
  }
}
</style> 