<script lang="ts" setup>
import {onBeforeUnmount, ref} from 'vue';
import {ElMessage} from 'element-plus';
import {Check, Edit} from '@element-plus/icons-vue';
import {checkPasswordEmpty} from '../service/ApiGetUserAccount';
import {
  initPassword,
  resetPasswordByCode,
  sendPasswordResetCode,
  updatePassword
} from '../service/ApiUpdateUserAccount';
import {validatePassword} from '@/utils/passwordValidator';
import {useSessionStore} from '@/pinia/Session';
import {useRouter} from 'vue-router';

// 获取session store和路由
const sessionStore = useSessionStore();
const router = useRouter();

// 移除email的defineModel
const isHovering = ref(false);

// 密码编辑状态
const isInitPasswordDialogVisible = ref(false);
const isOldPasswordDialogVisible = ref(false);
const isEmailVerifyDialogVisible = ref(false);
const oldPassword = ref('');
const newPassword = ref('');
const confirmPassword = ref('');
const validationErrors = ref<string[]>([]);
const confirmPasswordError = ref('');
// 新增密码安全度状态
const isPasswordSecure = ref(false);

// 各种对话框状态
const initPasswordAlertVisible = ref(false);
const verifyChoiceDialogVisible = ref(false);
// 邮箱验证相关
const emailVerifyCode = ref('');
const emailVerifyError = ref('');
const sendingVerifyCode = ref(false);
const countdown = ref(0);
const countdownTimer = ref<number | null>(null);
// 新增邮箱和验证码UID状态
const emailSentTo = ref('');
const verifyUid = ref('');

// 显示密码验证方式选择
const showPasswordVerificationChoice = async () => {
  try {
    // 先检查密码是否为空（未初始化）
    const response = await checkPasswordEmpty();
    console.log(response);
    if (response.data === true) {
      // 密码未初始化，显示对话框
      initPasswordAlertVisible.value = true;
    } else {
      // 密码已初始化，直接显示验证方式选择对话框
      // 假设用户始终有绑定邮箱
      verifyChoiceDialogVisible.value = true;
    }
  } catch (error) {
    console.error('检查密码状态失败：', error);
    ElMessage.error('检查密码状态失败，请稍后重试');
  }
};

// 确认初始密码提示
const confirmInitPasswordAlert = () => {
  initPasswordAlertVisible.value = false;
  isInitPasswordDialogVisible.value = true;
};

// 使用旧密码验证
const verifyWithOldPassword = () => {
  resetPasswordFields();
  isOldPasswordDialogVisible.value = true;
  verifyChoiceDialogVisible.value = false;
};

// 使用邮箱验证
const verifyWithEmail = () => {
  resetPasswordFields();
  emailVerifyCode.value = '';
  emailVerifyError.value = '';
  isEmailVerifyDialogVisible.value = true;
  verifyChoiceDialogVisible.value = false;

  // 发送验证码到邮箱
  sendEmailVerifyCode();
};

/**
 * 发送邮箱验证码
 * 调用后端接口获取验证码目标邮箱和验证UID
 */
const sendEmailVerifyCode = async () => {
  if (sendingVerifyCode.value || countdown.value > 0) return;
  
  sendingVerifyCode.value = true;
  try {
    // 调用发送验证码API
    const response = await sendPasswordResetCode();
    
    if (response.status === 200) {
      // 从响应中获取目标邮箱和验证UID
      emailSentTo.value = response.data.email || '您的邮箱';
      verifyUid.value = response.data.key || '';
      
      ElMessage.success('验证码已发送至您的邮箱');
      
      // 开始倒计时
      countdown.value = 60;
      countdownTimer.value = window.setInterval(() => {
        countdown.value--;
        if (countdown.value <= 0 && countdownTimer.value) {
          clearInterval(countdownTimer.value);
          countdownTimer.value = null;
        }
      }, 1000);
    } else {
      ElMessage.error(response.message || '发送验证码失败');
    }
  } catch (error) {
    console.error('发送验证码失败：', error);
    ElMessage.error('发送验证码失败，请稍后重试');
  } finally {
    sendingVerifyCode.value = false;
  }
};

// 重置密码字段
const resetPasswordFields = () => {
  oldPassword.value = '';
  newPassword.value = '';
  confirmPassword.value = '';
  validationErrors.value = [];
  confirmPasswordError.value = '';
};

// 验证密码
const validateInput = () => {
  const result = validatePassword(newPassword.value);
  validationErrors.value = result.errors;
  isPasswordSecure.value = result.valid;
  return result.valid;
};

// 验证确认密码
const validateConfirmPassword = () => {
  if (newPassword.value !== confirmPassword.value) {
    confirmPasswordError.value = '两次输入的密码不一致';
    return false;
  }
  confirmPasswordError.value = '';
  return true;
};

// 密码输入时实时验证
const handlePasswordInput = () => {
  if (newPassword.value) {
    validateInput();
  } else {
    validationErrors.value = [];
    isPasswordSecure.value = false;
  }
};

// 确认密码输入时实时验证
const handleConfirmPasswordInput = () => {
  if (confirmPassword.value) {
    validateConfirmPassword();
  } else {
    confirmPasswordError.value = '';
  }
};

// 退出登录并跳转到登录页
const logout = () => {
  // 清空用户会话信息
  sessionStore.clearSession();

  // 跳转到登录页
  router.push('/sign');
};

// 确认修改密码 - 分成三个不同的确认方法
const confirmInitPassword = async () => {
  if (!newPassword.value) {
    ElMessage.warning('新密码不能为空');
    return;
  }

  if (!confirmPassword.value) {
    ElMessage.warning('请确认新密码');
    return;
  }

  // 验证新密码格式
  if (!validateInput()) {
    return;
  }

  // 验证确认密码
  if (!validateConfirmPassword()) {
    return;
  }

  try {
    // 调用密码初始化接口
    const response = await initPassword({password: newPassword.value});
    if (response.status === 200) {
      ElMessage.success('密码设置成功');
      isInitPasswordDialogVisible.value = false;
    } else {
      ElMessage.error(response.message || '密码设置失败');
    }
  } catch (error) {
    console.error('密码设置失败：', error);
    ElMessage.error('操作失败，请稍后重试');
  }
};

const confirmOldPasswordChange = async () => {
  if (!oldPassword.value) {
    ElMessage.warning('请输入当前密码');
    return;
  }

  if (!newPassword.value) {
    ElMessage.warning('新密码不能为空');
    return;
  }

  if (!confirmPassword.value) {
    ElMessage.warning('请确认新密码');
    return;
  }

  // 验证新密码格式
  if (!validateInput()) {
    return;
  }

  // 验证确认密码
  if (!validateConfirmPassword()) {
    return;
  }

  try {
    // 调用密码修改接口
    const response = await updatePassword({
      oldPassword: oldPassword.value,
      newPassword: newPassword.value
    });

    if (response.status === 200) {
      ElMessage.success('密码修改成功，请重新登录');
      isOldPasswordDialogVisible.value = false;

      // 修改密码成功后，延迟一小段时间再退出登录，让用户能看到成功提示
      setTimeout(() => {
        logout();
      }, 1000);
    } else {
      ElMessage.error(response.message || '密码修改失败');
    }
  } catch (error) {
    console.error('密码修改失败：', error);
    ElMessage.error('操作失败，请稍后重试');
  }
};

const confirmEmailVerifyPasswordChange = async () => {
  // 验证邮箱验证码格式
  if (!/^\d{6}$/.test(emailVerifyCode.value)) {
    emailVerifyError.value = '验证码格式不正确';
    return;
  }

  if (!newPassword.value) {
    ElMessage.warning('新密码不能为空');
    return;
  }

  if (!confirmPassword.value) {
    ElMessage.warning('请确认新密码');
    return;
  }

  // 验证新密码格式
  if (!validateInput()) {
    return;
  }

  // 验证确认密码
  if (!validateConfirmPassword()) {
    return;
  }

  try {
    // 使用邮箱验证码修改密码
    if (!verifyUid.value) {
      ElMessage.error('验证UID丢失，请重新发送验证码');
      return;
    }
    
    // 调用验证邮箱验证码API并修改密码
    const response = await resetPasswordByCode({
      key: verifyUid.value,
      code: emailVerifyCode.value,
      newPassword: newPassword.value
    });
    
    if (response.status === 200) {
      ElMessage.success('密码修改成功，请重新登录');
      isEmailVerifyDialogVisible.value = false;
      
      // 修改密码成功后，延迟一小段时间再退出登录
      setTimeout(() => {
        logout();
      }, 1000);
    } else if (response.status === 400 && response.message.includes('验证码')) {
      emailVerifyError.value = response.message || '验证码错误或已过期';
    } else {
      ElMessage.error(response.message || '密码修改失败');
    }
  } catch (error) {
    console.error('密码修改失败：', error);
    ElMessage.error('操作失败，请稍后重试');
  }
};

// 取消修改密码 - 分成三个不同的取消方法
const cancelInitPassword = () => {
  isInitPasswordDialogVisible.value = false;
  resetPasswordFields();
};

const cancelOldPasswordChange = () => {
  isOldPasswordDialogVisible.value = false;
  resetPasswordFields();
};

const cancelEmailVerifyPasswordChange = () => {
  isEmailVerifyDialogVisible.value = false;
  resetPasswordFields();
};

// 组件销毁时清除定时器
onBeforeUnmount(() => {
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value);
    countdownTimer.value = null;
  }
});
</script>

<template>
  <div class="form-row">
    <label class="form-label">密码</label>

    <div class="form-content">
      <div class="password-display"
           @mouseenter="isHovering = true"
           @mouseleave="isHovering = false">
        <div class="password-container">
          <span class="password-text">********</span>
        </div>
        <el-button
            v-if="isHovering"
            :icon="Edit"
            class="edit-button"
            link
            type="primary"
            @click="showPasswordVerificationChoice">修改
        </el-button>
      </div>
    </div>
  </div>

  <!-- 密码未初始化提示对话框 -->
  <el-dialog
      v-model="initPasswordAlertVisible"
      :close-on-click-modal="false"
      :show-close="true"
      append-to-body
      title="提示"
      width="400px"
  >
    <div class="dialog-content">
      <p>你的账号密码还没有设置过,请设置你的密码吧</p>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="confirmInitPasswordAlert">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 验证方式选择对话框 -->
  <el-dialog
      v-model="verifyChoiceDialogVisible"
      :close-on-click-modal="false"
      :show-close="true"
      append-to-body
      title="修改密码"
      width="400px"
  >
    <div class="dialog-content">
      <p>请选择验证方式</p>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="verifyWithEmail">使用邮箱验证</el-button>
        <el-button type="primary" @click="verifyWithOldPassword">使用旧密码验证</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 密码修改对话框 (使用旧密码验证) -->
  <el-dialog
      v-model="isOldPasswordDialogVisible"
      :close-on-click-modal="false"
      append-to-body
      title="修改密码"
      width="450px"
      @closed="cancelOldPasswordChange"
  >
    <div class="password-form">
      <!-- 旧密码输入 -->
      <el-input
          v-model="oldPassword"
          placeholder="请输入当前密码"
          show-password
          type="password"
      />

      <!-- 新密码输入 -->
      <el-input
          v-model="newPassword"
          :status="validationErrors.length > 0 ? 'error' : ''"
          placeholder="请输入新密码"
          show-password
          type="password"
          @input="handlePasswordInput"
      />

      <!-- 确认密码输入 -->
      <el-input
          v-model="confirmPassword"
          :status="confirmPasswordError ? 'error' : ''"
          placeholder="请确认新密码"
          show-password
          type="password"
          @input="handleConfirmPasswordInput"
      />
      <div v-if="confirmPasswordError" class="error-message">
        {{ confirmPasswordError }}
      </div>

      <!-- 验证错误提示 -->
      <div v-if="validationErrors.length > 0" class="validation-errors">
        <p v-for="(error, index) in validationErrors" :key="index" class="error-item">
          {{ error }}
        </p>
      </div>

      <!-- 密码安全度提示 -->
      <div v-else-if="isPasswordSecure && newPassword" class="password-secure">
        <p class="secure-title">
          <el-icon><Check /></el-icon> 密码安全度评估
        </p>
        <p class="secure-message">恭喜！您设置的密码符合安全要求，具有较高的防护能力。</p>
      </div>

      <!-- 密码规则提示 -->
      <div v-else class="password-rules">
        <p class="rules-title">密码规则：</p>
        <ul class="rules-list">
          <li>长度必须在8到32个字符之间</li>
          <li>必须包含至少一个大写字母 (A-Z)</li>
          <li>必须包含至少一个小写字母 (a-z)</li>
          <li>必须包含至少一个数字 (0-9)</li>
          <li>必须包含至少一个特殊字符（如 !@#$%^&*() 等）</li>
          <li>不能包含空白字符</li>
          <li>不能有超过2个连续相同字符</li>
          <li>不能包含常见弱密码</li>
        </ul>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelOldPasswordChange">取消</el-button>
        <el-button type="primary" @click="confirmOldPasswordChange">确认</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 密码修改对话框 (使用邮箱验证) -->
  <el-dialog
      v-model="isEmailVerifyDialogVisible"
      :close-on-click-modal="false"
      append-to-body
      title="修改密码"
      width="450px"
      @closed="cancelEmailVerifyPasswordChange"
  >
    <div class="password-form">
      <!-- 邮箱验证码输入 -->
      <div class="verify-code-section">
        <div class="verify-code-header">
          <span>验证码已发送到 {{ emailSentTo }}</span>
          <el-button 
            :disabled="countdown > 0"
            link 
            type="primary"
            @click="sendEmailVerifyCode">
            {{ countdown > 0 ? `重发(${countdown}s)` : '重发' }}
          </el-button>
        </div>
        <el-input
            v-model="emailVerifyCode"
            :status="emailVerifyError ? 'error' : ''"
            placeholder="请输入验证码"
        />
        <div v-if="emailVerifyError" class="error-message">
          {{ emailVerifyError }}
        </div>
      </div>

      <!-- 新密码输入 -->
      <el-input
          v-model="newPassword"
          :status="validationErrors.length > 0 ? 'error' : ''"
          placeholder="请输入新密码"
          show-password
          type="password"
          @input="handlePasswordInput"
      />

      <!-- 确认密码输入 -->
      <el-input
          v-model="confirmPassword"
          :status="confirmPasswordError ? 'error' : ''"
          placeholder="请确认新密码"
          show-password
          type="password"
          @input="handleConfirmPasswordInput"
      />
      <div v-if="confirmPasswordError" class="error-message">
        {{ confirmPasswordError }}
      </div>

      <!-- 验证错误提示 -->
      <div v-if="validationErrors.length > 0" class="validation-errors">
        <p v-for="(error, index) in validationErrors" :key="index" class="error-item">
          {{ error }}
        </p>
      </div>

      <!-- 密码安全度提示 -->
      <div v-else-if="isPasswordSecure && newPassword" class="password-secure">
        <p class="secure-title">
          <el-icon><Check /></el-icon> 密码安全度评估
        </p>
        <p class="secure-message">恭喜！您设置的密码符合安全要求，具有较高的防护能力。</p>
      </div>

      <!-- 密码规则提示 -->
      <div v-else class="password-rules">
        <p class="rules-title">密码规则：</p>
        <ul class="rules-list">
          <li>长度必须在8到32个字符之间</li>
          <li>必须包含至少一个大写字母 (A-Z)</li>
          <li>必须包含至少一个小写字母 (a-z)</li>
          <li>必须包含至少一个数字 (0-9)</li>
          <li>必须包含至少一个特殊字符（如 !@#$%^&*() 等）</li>
          <li>不能包含空白字符</li>
          <li>不能有超过2个连续相同字符</li>
          <li>不能包含常见弱密码</li>
        </ul>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelEmailVerifyPasswordChange">取消</el-button>
        <el-button type="primary" @click="confirmEmailVerifyPasswordChange">确认</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 密码初始设置对话框 -->
  <el-dialog
      v-model="isInitPasswordDialogVisible"
      :close-on-click-modal="false"
      append-to-body
      title="设置密码"
      width="450px"
      @closed="cancelInitPassword"
  >
    <div class="password-form">
      <!-- 新密码输入 -->
      <el-input
          v-model="newPassword"
          :status="validationErrors.length > 0 ? 'error' : ''"
          placeholder="请输入新密码"
          show-password
          type="password"
          @input="handlePasswordInput"
      />

      <!-- 确认密码输入 -->
      <el-input
          v-model="confirmPassword"
          :status="confirmPasswordError ? 'error' : ''"
          placeholder="请确认新密码"
          show-password
          type="password"
          @input="handleConfirmPasswordInput"
      />
      <div v-if="confirmPasswordError" class="error-message">
        {{ confirmPasswordError }}
      </div>

      <!-- 验证错误提示 -->
      <div v-if="validationErrors.length > 0" class="validation-errors">
        <p v-for="(error, index) in validationErrors" :key="index" class="error-item">
          {{ error }}
        </p>
      </div>

      <!-- 密码安全度提示 -->
      <div v-else-if="isPasswordSecure && newPassword" class="password-secure">
        <p class="secure-title">
          <el-icon><Check /></el-icon> 密码安全度评估
        </p>
        <p class="secure-message">恭喜！您设置的密码符合安全要求，具有较高的防护能力。</p>
      </div>

      <!-- 密码规则提示 -->
      <div v-else class="password-rules">
        <p class="rules-title">密码规则：</p>
        <ul class="rules-list">
          <li>长度必须在8到32个字符之间</li>
          <li>必须包含至少一个大写字母 (A-Z)</li>
          <li>必须包含至少一个小写字母 (a-z)</li>
          <li>必须包含至少一个数字 (0-9)</li>
          <li>必须包含至少一个特殊字符（如 !@#$%^&*() 等）</li>
          <li>不能包含空白字符</li>
          <li>不能有超过2个连续相同字符</li>
          <li>不能包含常见弱密码</li>
        </ul>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelInitPassword">取消</el-button>
        <el-button type="primary" @click="confirmInitPassword">确认</el-button>
      </span>
    </template>
  </el-dialog>
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
}

.password-display {
  display: flex;
  align-items: center;
}

.password-container {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  background: #f9f9f9;
  border-radius: 8px;
  min-width: 200px;
  transition: background 0.2s;
}

.password-container:hover {
  background: #f1f1f1;
}

.password-text {
  font-size: 14px;
  color: #333;
}

.edit-button {
  margin-left: 8px;
}

.dialog-content {
  margin-bottom: 15px;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}

/* 密码表单样式 */
.password-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.validation-errors {
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

.password-rules {
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

.verify-code-section {
  margin-bottom: 12px;
}

.verify-code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.password-secure {
  padding: 8px 12px;
  background-color: #f0f9eb;
  border-radius: 4px;
  border-left: 3px solid #67c23a;
  margin-top: 8px;
}

.secure-title {
  font-size: 13px;
  font-weight: 500;
  color: #67c23a;
  margin: 0 0 5px 0;
  display: flex;
  align-items: center;
}

.secure-title .el-icon {
  margin-right: 5px;
}

.secure-message {
  font-size: 12px;
  color: #5b7d33;
  line-height: 1.5;
  margin: 2px 0;
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

  .password-section {
    flex-direction: column;
    align-items: flex-start;
  }

  .password-tip {
    margin-left: 0;
    margin-top: 8px;
  }

  .password-container {
    width: 100%;
  }
}
</style>