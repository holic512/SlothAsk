<template>
  <el-switch
    v-model="currentStatus"
    :loading="loading"
    :active-value="1"
    :inactive-value="0"
    @change="handleChange"
  />
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  status: number
  loading?: boolean
}>()

const emit = defineEmits<{
  'update:status': [value: number]
  'change': [value: number]
}>()

const currentStatus = ref(props.status)

watch(
  () => props.status,
  (val) => {
    currentStatus.value = val
  }
)

const handleChange = (value: number) => {
  emit('update:status', value)
  emit('change', value)
}
</script> 