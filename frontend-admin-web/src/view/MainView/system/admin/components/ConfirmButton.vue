<template>
  <el-popconfirm
    :title="title"
    @confirm="handleConfirm"
  >
    <template #reference>
      <el-button
        v-bind="$attrs"
        :loading="loading"
      >
        <slot />
      </el-button>
    </template>
  </el-popconfirm>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  loading?: boolean
}>()

const emit = defineEmits<{
  confirm: []
}>()

const handleConfirm = () => {
  emit('confirm')
}
</script> 