<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPermissionTree, updateRolePermissions } from '../api/roleService'
import { mockPermissionTemplates } from '../../mock'
import type { Permission, PermissionTemplate } from '../types/permission'

const props = defineProps<{
  modelValue: boolean
  roleId: number
  readonly?: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const loading = ref(false)
const treeData = ref<Permission[]>([])
const checkedKeys = ref<number[]>([])
const treeRef = ref()

// 模板选择相关
const showTemplateSelect = ref(false)
const selectedTemplate = ref<PermissionTemplate>()

const handleClose = () => {
  emit('update:modelValue', false)
}

const loadPermissionTree = async () => {
  loading.value = true
  try {
    const { data } = await getPermissionTree(props.roleId)
    treeData.value = data.tree
    checkedKeys.value = data.checkedKeys
  } catch (error) {
    console.error('获取权限树失败:', error)
  } finally {
    loading.value = false
  }
}

const handleApplyTemplate = (template: PermissionTemplate) => {
  ElMessageBox.confirm(
    `确定要应用"${template.name}"模板吗？这将覆盖当前的权限设置。`,
    '提示',
    {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    }
  ).then(() => {
    checkedKeys.value = template.permissions
    showTemplateSelect.value = false
    ElMessage.success('模板应用成功')
  })
}

const handleSubmit = async () => {
  if (!treeRef.value) return
  
  const { checkedKeys, halfCheckedKeys } = treeRef.value.getCheckedKeys(true)
  const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys]
  
  loading.value = true
  try {
    const response = await updateRolePermissions({
      roleId: props.roleId,
      permissions: allCheckedKeys
    })
    
    if (response.code === 0) {
      ElMessage.success('权限配置成功')
      handleClose()
    } else {
      ElMessage.error(response.message || '权限配置失败')
    }
  } catch (error) {
    console.error('权限配置失败:', error)
    ElMessage.error('权限配置失败')
  } finally {
    loading.value = false
  }
}

watch(() => props.modelValue, (val) => {
  if (val && props.roleId) {
    loadPermissionTree()
  }
})
</script>

<template>
  <el-dialog
    :model-value="modelValue"
    :title="readonly ? '查看权限' : '配置权限'"
    width="600px"
    @close="handleClose"
    class="permission-dialog"
  >
    <div class="dialog-toolbar" v-if="!readonly">
      <el-button 
        type="primary" 
        plain 
        @click="showTemplateSelect = true"
      >
        应用权限模板
      </el-button>
    </div>
    
    <div v-loading="loading" class="tree-container">
      <el-tree
        ref="treeRef"
        :data="treeData"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedKeys"
        :disabled="readonly"
        :props="{
          label: 'name',
          children: 'children'
        }"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ data.name }}</span>
            <span class="permission-info">
              <el-tag 
                size="small" 
                :type="data.type === 'menu' ? 'success' : 'info'"
              >
                {{ data.type }}
              </el-tag>
              <span class="permission-code">{{ data.code }}</span>
            </span>
          </span>
        </template>
      </el-tree>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">{{ readonly ? '关闭' : '取消' }}</el-button>
        <el-button
          v-if="!readonly"
          type="primary"
          :loading="loading"
          @click="handleSubmit"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 模板选择对话框 -->
  <el-dialog
    v-model="showTemplateSelect"
    title="选择权限模板"
    width="500px"
    append-to-body
  >
    <el-scrollbar height="400px">
      <div class="template-list">
        <div
          v-for="template in mockPermissionTemplates"
          :key="template.id"
          class="template-item"
          @click="handleApplyTemplate(template)"
        >
          <div class="template-header">
            <span class="template-name">{{ template.name }}</span>
            <el-tag 
              size="small"
              :type="{
                super: 'danger',
                high: 'warning',
                medium: 'success',
                low: 'info'
              }[template.level]"
            >
              {{ template.level }}
            </el-tag>
          </div>
          <div class="template-desc">{{ template.description }}</div>
          <div class="template-stats">
            <span>菜单权限: {{ template.menus.length }}个</span>
            <span>操作权限: {{ template.permissions.length }}个</span>
          </div>
        </div>
      </div>
    </el-scrollbar>
  </el-dialog>
</template>

<style scoped>
.permission-dialog {
  :deep(.el-dialog__body) {
    padding: 20px;
  }
}

.dialog-toolbar {
  margin-bottom: 16px;
}

.tree-container {
  max-height: 400px;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.dialog-footer {
  text-align: right;
  padding-top: 10px;
  border-top: 1px solid #dcdfe6;
}

.template-list {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 16px;
  padding: 16px;
}

.template-item {
  padding: 16px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    border-color: var(--el-color-primary);
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  }
}

.template-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.template-name {
  font-size: 16px;
  font-weight: 500;
}

.template-desc {
  color: var(--el-text-color-secondary);
  margin-bottom: 12px;
  font-size: 13px;
}

.template-stats {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

:deep(.el-tree-node__content) {
  height: 32px;
}

:deep(.el-tree) {
  background: transparent;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.permission-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.permission-code {
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 2px;
}

:deep(.el-tag--small) {
  height: 20px;
  padding: 0 6px;
  font-size: 11px;
}
</style> 