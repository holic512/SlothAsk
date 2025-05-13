<script lang="ts" setup>
import {computed, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {Edit} from '@element-plus/icons-vue';
import {validateUsername} from '@/utils/usernameValidator';

// 定义双向绑定模型
const username = defineModel<string>('username', {required: true});
const remainingChanges = defineModel<number>('remainingChanges', {required: true});
const emit = defineEmits<{ (e: 'update-username', newName: string): void }>();

// 本地状态
const tempUsername = ref('');
const isEditing = ref(false);
const isHovering = ref(false);
const validationErrors = ref<string[]>([]);

// 计算属性：是否可编辑
const canEdit = computed(() => remainingChanges.value > 0);

// 开始编辑
const startEdit = () => {
  tempUsername.value = username.value;
  validationErrors.value = [];
  isEditing.value = true;
};

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false;
  tempUsername.value = '';
  validationErrors.value = [];
};

// 验证用户名
const validateInput = () => {
  const result = validateUsername(tempUsername.value.trim());
  validationErrors.value = result.errors;
  return result.valid;
};

// 确认编辑
const confirmEdit = async () => {
  const newName = tempUsername.value.trim();

  // 空值检查
  if (!newName) {
    ElMessage.warning('用户名不能为空');
    return;
  }

  // 未修改检查
  if (newName === username.value) {
    ElMessage.info('用户名未修改');
    cancelEdit();
    return;
  }

  // 用户名校验
  if (!validateInput()) {
    return;
  }

  try {
    const {value} = await ElMessageBox.prompt(
        `修改用户名操作不可回滚，请输入 "${newName}" 以确认修改`,
        '确认修改用户名',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          inputPlaceholder: '请再次输入新用户名以确认',
          inputValidator: (val: string) => {
            if (val !== newName) {
              return '输入内容与新用户名不一致';
            }
            return true;
          },
          inputErrorMessage: '输入内容与新用户名不一致',
          type: 'warning',
        }
    );

    if (value === newName) {
      emit('update-username', newName);
      isEditing.value = false;
    }
  } catch {
    // 用户取消
  }
};

// 用户名输入时实时验证
const handleUsernameInput = () => {
  if (tempUsername.value.trim()) {
    validateInput();
  } else {
    validationErrors.value = [];
  }
};
</script>

<template>
  <div class="form-row">
    <label class="form-label">用户名</label>
    <div class="form-content">
      <!-- 显示模式 -->
      <div v-if="!isEditing"
           class="username-display"
           @mouseenter="isHovering = true"
           @mouseleave="isHovering = false"
      >
        <div class="username-container">
          <span class="username-text">{{ username }}</span>
        </div>
        <el-button 
            v-if="isHovering"
            :icon="Edit"
            class="edit-button"
            link
            type="primary"
            @click="startEdit">修改</el-button>
      </div>


      <!-- 编辑模式 -->
      <div v-else class="username-edit">
        <div style="display: flex; align-items: center;gap: 8px">
          <el-input
              v-model="tempUsername"
              :status="validationErrors.length > 0 ? 'error' : ''"
              placeholder="请输入新用户名"
              @input="handleUsernameInput"
          />
          <div class="button-group">
            <el-button size="small" type="primary" @click="confirmEdit">确认</el-button>
            <el-button size="small" @click="cancelEdit">取消</el-button>
          </div>
        </div>
        <!-- 验证错误提示 -->
        <div v-if="validationErrors.length > 0" class="validation-errors">
          <p v-for="(error, index) in validationErrors" :key="index" class="error-item">
            {{ error }}
          </p>
        </div>

        <!-- 用户名规则提示 -->
        <div v-else class="username-rules">
          <p class="rules-title">用户名规则：</p>
          <ul class="rules-list">
            <li>长度必须在4到20个字符之间</li>
            <li>仅允许英文字母、数字、下划线（_）、点（.）或连字符（-）</li>
            <li>点（.）或连字符（-）不能作为首尾字符</li>
            <li>点（.）或连字符（-）不能连续出现</li>
            <li>不能包含系统保留的违禁词（如admin、root等）</li>
          </ul>
        </div>
      </div>


      <!-- 剩余次数 -->
      <div class="remaining-changes">
        本月剩余修改次数：{{ remainingChanges }}
      </div>


    </div>
  </div>
</template>

<style scoped>
.form-row {
  display: flex;
  margin-bottom: 20px;
}

.form-label {
  width: 70px;
  font-size: 14px;
  font-weight: 500;
  color: #1a1a1a;
  line-height: 32px;
  text-align: right;
  margin-right: 16px;
  flex-shrink: 0;
}

.form-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.username-display {
  display: flex;
}

.username-container {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  background: #f9f9f9;
  border-radius: 8px;
  min-width: 200px;
  transition: background 0.2s;
}

.username-container:hover {
  background: #f1f1f1;
}

.username-text {
  font-size: 14px;
  color: #333;
}

.edit-link {
  margin-left: 8px;
}

.username-edit {
  display: flex;
  gap: 8px;
  max-width: 345px;

  flex-direction: column;
}

.button-group {
  display: flex;

}

.validation-errors {
  margin-top: 4px;
  padding: 8px 12px;
  background-color: #fff2f0;
  border-radius: 4px;
  border-left: 3px solid #f56c6c;
}

.error-item {
  color: #f56c6c;
  font-size: 12px;
  line-height: 1.5;
  margin: 2px 0;
}

.username-rules {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #f4f9ff;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

.rules-title {
  font-size: 13px;
  font-weight: 500;
  color: #606266;
  margin: 0 0 5px 0;
}

.rules-list {
  padding-left: 15px;
  margin: 0;
}

.rules-list li {
  font-size: 12px;
  color: #606266;
  line-height: 1.5;
  margin: 2px 0;
}

.remaining-changes {
  margin-top: 6px;
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
  }

  .form-label {
    text-align: left;
    margin-bottom: 8px;
    width: 100%;
  }

  .username-container,
  .username-edit {
    width: 100%;
  }
}
</style>
