<template>
  <el-select
    v-model="selectedRole"
    class="role-select"
    :loading="loading"
    :disabled="disabled"
    clearable
    filterable
    placeholder="请选择角色"
    @change="handleChange"
  >
    <el-option
      v-for="role in roleList"
      :key="role.id"
      :label="role.name"
      :value="role.id"
    >
      <div class="role-option">
        <span>{{ role.name }}</span>
        <el-tag size="small" :type="role.type">
          {{ role.description }}
        </el-tag>
      </div>
    </el-option>
  </el-select>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { Role } from '../types/role'

const props = defineProps<{
  modelValue?: number
  roleList: Role[]
  loading?: boolean
  disabled?: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: number | undefined]
  'change': [value: number | undefined]
}>()

const selectedRole = ref(props.modelValue)

watch(
  () => props.modelValue,
  (val) => {
    selectedRole.value = val
  }
)

const handleChange = (value: number | undefined) => {
  emit('update:modelValue', value)
  emit('change', value)
}
</script>

<style scoped>
.role-select {
  width: 100%;
}

.role-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style> 