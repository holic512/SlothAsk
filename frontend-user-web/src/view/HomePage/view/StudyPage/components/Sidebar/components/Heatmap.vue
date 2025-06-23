<script lang="ts" setup>
import {computed, onMounted, ref, watch} from 'vue';
import {addDays, format, subDays} from 'date-fns';
import {zhCN} from 'date-fns/locale';

interface HeatmapData {
  date: Date;
  count: number;
  level: number; // 0-4 活跃度等级
}

// 后端数据接口
interface BackendData {
  date: string;
  answerTimes: number;
}

// Props 接收后端数据
const props = defineProps<{
  backendData?: BackendData[];
}>();

const heatmapData = ref<HeatmapData[]>([]);
const hoveredCell = ref<HeatmapData | null>(null);
const hoverTimeout = ref<NodeJS.Timeout | null>(null);
const today = new Date();

// 生成热力图数据
const generateHeatmapData = () => {
  const data: HeatmapData[] = [];
  
  // 如果有后端数据，直接使用后端数据（120天）
  if (props.backendData && props.backendData.length > 0) {
    props.backendData.forEach(backendItem => {
      const backendDate = new Date(backendItem.date);
      const count = backendItem.answerTimes;
      let level = 0;
      
      if (count === 0) level = 0;
      else if (count <= 3) level = 1;
      else if (count <= 7) level = 2;
      else if (count <= 12) level = 3;
      else level = 4;
      
      data.push({ date: backendDate, count, level });
    });
    
    // 按日期排序（从最早到最新）
    data.sort((a, b) => a.date.getTime() - b.date.getTime());
  } else {
    // 如果没有后端数据，生成120天的空数据（次数全为0）
    for (let i = 119; i >= 0; i--) {
      const date = subDays(today, i);
      const count = 0;
      const level = 0;
      
      data.push({ date, count, level });
    }
  }
  
  return data;
};

// 将数据按周组织成网格
const heatmapGrid = computed(() => {
  const grid: HeatmapData[][] = [];
  const data = heatmapData.value;
  
  if (data.length === 0) return grid;
  
  // 动态计算需要的列数（每列6天）
  const totalDays = data.length;
  const weeksNeeded = Math.ceil(totalDays / 6);
  
  // 创建网格
  for (let week = 0; week < weeksNeeded; week++) {
    const weekData: HeatmapData[] = [];
    
    for (let day = 0; day < 6; day++) {
      const dataIndex = week * 6 + day;
      if (dataIndex < data.length) {
        weekData.push(data[dataIndex]);
      } else {
        // 填充空数据
        const emptyDate = addDays(data[data.length - 1].date, dataIndex - data.length + 1);
        weekData.push({ date: emptyDate, count: 0, level: 0 });
      }
    }
    
    grid.push(weekData);
  }
  
  return grid;
});

// 获取活跃度等级对应的颜色类名
const getLevelClass = (level: number) => {
  return `level-${level}`;
};

// 处理鼠标悬停 (添加延迟)
const handleCellHover = (data: HeatmapData) => {
  // 清除之前的定时器
  if (hoverTimeout.value) {
    clearTimeout(hoverTimeout.value);
  }
  
  // 设置延迟显示
  hoverTimeout.value = setTimeout(() => {
    hoveredCell.value = data;
  }, 500); // 500ms 延迟
};

const handleCellLeave = () => {
  // 清除定时器
  if (hoverTimeout.value) {
    clearTimeout(hoverTimeout.value);
    hoverTimeout.value = null;
  }
  
  // 延迟隐藏提示
  setTimeout(() => {
    hoveredCell.value = null;
  }, 100);
};

// 格式化悬停提示文本
const getTooltipText = (data: HeatmapData) => {
  const dateStr = format(data.date, 'yyyy年MM月dd日', { locale: zhCN });
  return `${dateStr}: ${data.count} 次答题`;
};

// 计算总活跃天数和总答题次数
const totalStats = computed(() => {
  const activeDays = heatmapData.value.filter(d => d.count > 0).length;
  const totalCount = heatmapData.value.reduce((sum, d) => sum + d.count, 0);
  return { activeDays, totalCount };
});

// 计算时间范围
const timeRange = computed(() => {
  if (heatmapData.value.length === 0) return { start: '', end: '' };
  
  const startDate = heatmapData.value[0].date;
  const endDate = heatmapData.value[heatmapData.value.length - 1].date;
  
  return {
    start: format(startDate, 'yyyy年MM月dd日', { locale: zhCN }),
    end: format(endDate, 'yyyy年MM月dd日', { locale: zhCN })
  };
});

// 监听后端数据变化
watch(
  () => props.backendData,
  () => {
    heatmapData.value = generateHeatmapData();
  },
  { deep: true, immediate: false }
);

onMounted(() => {
  heatmapData.value = generateHeatmapData();
});
</script>

<template>
  <div class="heatmap-card">
    <div class="heatmap-header">
      <div class="header-top">
        <h3 class="title">学习活跃度</h3>

      </div>
              <div class="stats">
          <span class="stat-item">
            <svg class="stat-icon" height="12" viewBox="0 0 16 16" width="12">
              <path d="M8 0C3.58 0 0 3.58 0 8s3.58 8 8 8 8-3.58 8-8-3.58-8-8-8zm3.707 5.293a1 1 0 0 1 0 1.414L8.414 10a1 1 0 0 1-1.414 0L4.293 7.293a1 1 0 1 1 1.414-1.414L7 7.172l2.293-2.293a1 1 0 0 1 1.414 0z" fill="#3b82f6"/>
            </svg>
            <strong>{{ totalStats.activeDays }} / {{ heatmapData.length }}</strong> 活跃天
          </span>
          <span class="stat-item">
            <svg class="stat-icon" height="12" viewBox="0 0 16 16" width="12">
              <path d="M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0zM1.5 8a6.5 6.5 0 1 0 13 0 6.5 6.5 0 0 0-13 0zm7-3.25v2.992l2.028.812a.75.75 0 0 1-.557 1.392l-2.5-1A.751.751 0 0 1 7 8.25v-3.5a.75.75 0 0 1 1.5 0z" fill="#3b82f6"/>
            </svg>
            <strong>{{ totalStats.totalCount }}</strong> 次答题
          </span>
        </div>
    </div>
    
    <div class="heatmap-container">
      <div class="heatmap-grid">
        <div 
          v-for="(week, weekIndex) in heatmapGrid" 
          :key="weekIndex" 
          class="week-column"
        >
          <div 
            v-for="(day, dayIndex) in week" 
            :key="dayIndex"
            :class="getLevelClass(day.level)"
            class="day-cell"
            @mouseenter="handleCellHover(day)"
            @mouseleave="handleCellLeave"
          >
          </div>
        </div>
      </div>

            <div class="time-range">
        <span class="time-text">{{ timeRange.start }} - {{ timeRange.end }}</span>
      </div>
      
      <div class="legend">
        <span class="legend-text">少</span>
        <div class="legend-levels">
          <div class="legend-cell level-0"></div>
          <div class="legend-cell level-1"></div>
          <div class="legend-cell level-2"></div>
          <div class="legend-cell level-3"></div>
          <div class="legend-cell level-4"></div>
        </div>
        <span class="legend-text">多</span>
      </div>
    </div>

    
    
    <!-- 悬停提示 -->
    <div v-if="hoveredCell" class="tooltip">
      {{ getTooltipText(hoveredCell) }}
    </div>
  </div>
</template>

<style scoped>
.heatmap-card {
  padding: 16px 20px;
  border-radius: 12px;
  background-color: #ffffff;
  border: 1px solid #e9ecef;
  font-family: 'ZCOOL XiaoWei', sans-serif;
  position: relative;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.heatmap-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  border-color: #28a745;
}

.heatmap-header {
  margin-bottom: 16px;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.time-range {
  display: flex;
  justify-content: center;
  margin-bottom: 4px;
}

.time-text {
  font-size: 0.75rem;
  color: #868e96;
  opacity: 0.8;
  font-weight: 400;
  letter-spacing: 0.5px;
}

.title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.stats {
  display: flex;
  gap: 12px;
}

.stat-item {
  font-size: 0.85rem;
  color: #6c757d;
  background-color: #f8f9fa;
  padding: 4px 8px;
  border-radius: 6px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-icon {
  flex-shrink: 0;
  opacity: 0.8;
}

.stat-item:hover {
  background-color: #e9ecef;
  color: #495057;
}

.heatmap-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.heatmap-grid {
  display: flex;
  gap: 2px;
  justify-content: center;
  padding: 4px;
  max-width: 100%;
}

.week-column {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.day-cell {
  width: 10px;
  height: 10px;
  border-radius: 2px;
  border: 1px solid rgba(27, 31, 35, 0.06);
  cursor: pointer;
  transition: all 0.2s ease;
}

.day-cell:hover {
  transform: scale(1.1);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-color: rgba(27, 31, 35, 0.15);
  z-index: 10;
  position: relative;
}

/* 活跃度等级颜色 */
.level-0 {
  background-color: #ebedf0;
}

.level-1 {
  background-color: #c6e48b;
}

.level-2 {
  background-color: #7bc96f;
}

.level-3 {
  background-color: #239a3b;
}

.level-4 {
  background-color: #196127;
}

.legend {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  margin-top: 8px;
}

.legend-text {
  font-size: 0.75rem;
  color: #6c757d;
}

.legend-levels {
  display: flex;
  gap: 2px;
}

.legend-cell {
    width: 10px;
    height: 10px;
    border-radius: 2px;
    border: 1px solid rgba(27, 31, 35, 0.06);
  }

.tooltip {
  position: absolute;
  bottom: -35px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #2c3e50;
  color: white;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 0.8rem;
  white-space: nowrap;
  z-index: 1000;
  opacity: 0.9;
  animation: fadeIn 0.2s ease;
}

.tooltip::before {
  content: '';
  position: absolute;
  top: -4px;
  left: 50%;
  transform: translateX(-50%);
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-bottom: 4px solid #2c3e50;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(5px);
  }
  to {
    opacity: 0.9;
    transform: translateX(-50%) translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .heatmap-grid {
    gap: 1px;
  }
  
  .day-cell {
    width: 7px;
    height: 7px;
  }
  
  .week-column {
    gap: 1px;
  }
  
  .title {
    font-size: 1.1rem;
  }
  
  .stats {
    flex-direction: column;
    gap: 4px;
  }
  
  .time-text {
    font-size: 0.7rem;
  }
  
  .header-top {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .day-cell {
    width: 5px;
    height: 5px;
  }
  
  .heatmap-grid {
    gap: 1px;
  }
  
  .legend-cell {
    width: 6px;
    height: 6px;
  }
  
  .time-text {
    font-size: 0.65rem;
  }
  
  .title {
    font-size: 1rem;
  }
}
</style>