<script setup>
import { computed } from 'vue'
import { useCreatorStore } from '../../../../../stores/creator'
import { storeToRefs } from 'pinia'

const creatorStore = useCreatorStore()
const { creators } = storeToRefs(creatorStore)

const props = defineProps({
  creatorId: {
    type: String,
    required: true
  }
})

const creatorInfo = computed(() => {
  return creators.value[props.creatorId] || null
})
</script>

<template>
  <div v-if="creatorInfo" class="creator-card">
    <div class="creator-header">
      <span class="creator-name">{{ creatorInfo.name }}</span>
      <el-tag size="small" effect="plain">{{ creatorInfo.role }}</el-tag>
    </div>
    <div class="creator-info">
      <div class="info-item">
        <span class="label">ID：</span>
        <span>{{ creatorInfo.id }}</span>
      </div>
      <div class="info-item">
        <span class="label">部门：</span>
        <span>{{ creatorInfo.department }}</span>
      </div>
      <div class="info-item">
        <span class="label">邮箱：</span>
        <span>{{ creatorInfo.email }}</span>
      </div>
      <div class="info-item">
        <span class="label">电话：</span>
        <span>{{ creatorInfo.phone }}</span>
      </div>
    </div>
  </div>
  <div v-else class="creator-card">
    <div class="no-data">未找到创建者信息</div>
  </div>
</template>

<style scoped>
.creator-card {
  padding: 12px;
  min-width: 280px;
}

.creator-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.creator-name {
  font-size: 16px;
  font-weight: 500;
}

.creator-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.label {
  color: var(--el-text-color-secondary);
  width: 50px;
}

.no-data {
  color: var(--el-text-color-secondary);
  text-align: center;
  padding: 20px 0;
}
</style>
