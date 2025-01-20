<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)

const initChart = () => {
  const chart = echarts.init(chartRef.value)
  
  const option = {
    title: {
      text: '题目分析统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      bottom: '0%',
      data: ['提交量', '正确率']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['简单', '中等', '困难']
    },
    yAxis: [
      {
        type: 'value',
        name: '提交量',
        min: 0,
        max: 1000,
        interval: 200
      },
      {
        type: 'value',
        name: '正确率',
        min: 0,
        max: 100,
        interval: 20,
        axisLabel: {
          formatter: '{value}%'
        }
      }
    ],
    series: [
      {
        name: '提交量',
        type: 'bar',
        data: [820, 632, 401]
      },
      {
        name: '正确率',
        type: 'line',
        yAxisIndex: 1,
        data: [85, 62, 41]
      }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

onMounted(() => {
  initChart()
})
</script>

<template>
  <el-card class="question-analysis">
    <template #header>
      <div class="card-header">
        <span>题目分析</span>
        <el-button-group>
          <el-button :icon="Refresh" circle size="small" @click="initChart" />
          <el-button :icon="Download" circle size="small" />
        </el-button-group>
      </div>
    </template>
    <div ref="chartRef" style="height: 300px"></div>
  </el-card>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 