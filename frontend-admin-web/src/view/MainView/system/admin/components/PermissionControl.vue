<template>
  <el-card class="permission-card">
    <template #header>
      <div class="card-header">
        <span>权限设置</span>
        <el-button-group>
          <el-button type="primary" @click="handleSelectAll">全选</el-button>
          <el-button @click="handleClearAll">清空</el-button>
        </el-button-group>
      </div>
    </template>

    <el-tree
      ref="treeRef"
      :data="permissionTree"
      :props="defaultProps"
      show-checkbox
      node-key="id"
      :default-checked-keys="checkedKeys"
      @check="handleCheck"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <el-icon v-if="data.icon"><component :is="data.icon" /></el-icon>
          <span>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { ElTree } from 'element-plus'

interface TreeNode {
  id: number
  label: string
  children?: TreeNode[]
  icon?: string
}

const props = defineProps<{
  permissions: number[]
}>()

const emit = defineEmits<{
  'update:permissions': [value: number[]]
}>()

const treeRef = ref<InstanceType<typeof ElTree>>()
const checkedKeys = ref<number[]>(props.permissions)

const defaultProps = {
  children: 'children',
  label: 'label'
}

// 示例权限树数据
const permissionTree = ref<TreeNode[]>([
  {
    id: 1,
    label: '系统管理',
    icon: 'Setting',
    children: [
      { id: 11, label: '用户管理', icon: 'User' },
      { id: 12, label: '角色管理', icon: 'UserFilled' },
      { id: 13, label: '菜单管理', icon: 'Menu' }
    ]
  },
  {
    id: 2,
    label: '内容管理',
    icon: 'Document',
    children: [
      { id: 21, label: '文章管理', icon: 'Document' },
      { id: 22, label: '分类管理', icon: 'Folder' },
      { id: 23, label: '标签管理', icon: 'Collection' }
    ]
  }
])

const handleCheck = (data: TreeNode, checked: { checkedKeys: number[] }) => {
  emit('update:permissions', checked.checkedKeys)
}

const handleSelectAll = () => {
  const allKeys = getAllKeys(permissionTree.value)
  treeRef.value?.setCheckedKeys(allKeys)
}

// 辅助函数：获取所有节点的id
const getAllKeys = (nodes: TreeNode[]): number[] => {
  const keys: number[] = []
  const traverse = (nodes: TreeNode[]) => {
    nodes.forEach(node => {
      keys.push(node.id)
      if (node.children) {
        traverse(node.children)
      }
    })
  }
  traverse(nodes)
  return keys
}

const handleClearAll = () => {
  treeRef.value?.setCheckedKeys([])
}
</script>

<style scoped>
.permission-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.custom-tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style> 