<script setup lang="ts">
import { ref, watch } from 'vue'
import type { DeptInfo } from '@/types/dept'

const props = defineProps<{
  modelValue?: number[] | number
  multiple?: boolean
  disabled?: boolean
  placeholder?: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: number[] | number]
}>()

// 部门树数据
const deptData = ref<DeptInfo[]>([
  {
    id: 1,
    name: '总公司',
    parentId: 0,
    sort: 1,
    children: [
      {
        id: 2,
        name: '研发部',
        parentId: 1,
        sort: 1
      },
      {
        id: 3,
        name: '运营部',
        parentId: 1,
        sort: 2
      },
      {
        id: 4,
        name: '市场部',
        parentId: 1,
        sort: 3
      }
    ]
  }
])

// 选中的值
const selectedKeys = ref<number[] | number>(props.modelValue || (props.multiple ? [] : undefined))

// 监听选中值变化
watch(
  () => props.modelValue,
  (val) => {
    selectedKeys.value = val || (props.multiple ? [] : undefined)
  }
)

// 处理选择变化
const handleChange = (val: number[] | number) => {
  selectedKeys.value = val
  emit('update:modelValue', val)
}

// 获取节点的key
const getNodeKey = (node: DeptInfo) => {
  return node.id
}

// 获取节点的标签
const getNodeLabel = (node: DeptInfo) => {
  return node.name
}
</script>

<template>
  <el-tree-select
    v-model="selectedKeys"
    :data="deptData"
    :props="{
      value: 'id',
      label: 'name',
      children: 'children'
    }"
    :multiple="multiple"
    :disabled="disabled"
    :placeholder="placeholder"
    :clearable="true"
    class="w-full"
    @change="handleChange"
  />
</template>

<style scoped>
.w-full {
  width: 100%;
}
</style> 