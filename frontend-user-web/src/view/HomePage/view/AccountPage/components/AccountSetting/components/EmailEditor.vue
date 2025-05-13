<script lang="ts" setup>
import {onBeforeUnmount, ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
import SliderCaptcha from '@/components/SliderCaptcha.vue';
import {Edit} from '@element-plus/icons-vue';
import {checkPasswordEmpty} from '../service/ApiGetUserAccount';
import {validateEmail} from '@/utils/emailValidator';
import {
  sendEmailChangeCode,
  sendVerificationToOriginalEmail,
  updateEmailWithPassword,
  updateEmailWithTwoSteps
} from '../service/ApiUpdateUserAccount';
import {debounce} from 'lodash-es'; // 引入防抖函数
import {onBeforeRouteLeave} from 'vue-router';

// 使用defineModel实现双向绑定
const email = defineModel('email', { required: true });
const emit = defineEmits(['update-email']);

// 邮箱相关状态
const newEmail = ref('');
const verificationCode = ref('');
const isHovering = ref(false);

// 滑块验证码相关
const showCaptchaDialog = ref(false);
const captchaVerified = ref(false);

// 各种对话框状态
const emailDialogVisible = ref(false);
const initPasswordAlertVisible = ref(false);
const verifyChoiceDialogVisible = ref(false);
const passwordVerifyDialogVisible = ref(false);
const emailVerifyDialogVisible = ref(false);

// 邮箱校验错误
const emailErrors = ref<string[]>([]);
const isEmailValid = ref(true);

// 验证UID和邮箱
const oldEmailVerifyUid = ref('');
const newEmailVerifyUid = ref('');
const oldEmailAddress = ref('');
const newEmailAddress = ref('');

// 创建安全的局部密码ref，以确保其生命周期更短
const password = ref('');

// 旧邮箱验证相关
const oldEmailVerifyCode = ref('');
const oldEmailVerifyError = ref('');
const sendingOldVerifyCode = ref(false);
const oldCountdown = ref(0);
let oldCountdownTimer: number | null = null;

// 新邮箱验证相关
const newEmailVerifyCode = ref('');
const newEmailVerifyError = ref('');
const sendingNewVerifyCode = ref(false);
const newCountdown = ref(0);
let newCountdownTimer: number | null = null;

// 启动旧邮箱验证码倒计时
const startOldCountdown = (seconds = 60) => {
  // 先清除可能存在的计时器
  if (oldCountdownTimer) {
    clearInterval(oldCountdownTimer);
    oldCountdownTimer = null;
  }
  
  oldCountdown.value = seconds;
  oldCountdownTimer = window.setInterval(() => {
    oldCountdown.value--;
    if (oldCountdown.value <= 0) {
      clearInterval(oldCountdownTimer!);
      oldCountdownTimer = null;
    }
  }, 1000);
};

// 启动新邮箱验证码倒计时
const startNewCountdown = (seconds = 60) => {
  // 先清除可能存在的计时器
  if (newCountdownTimer) {
    clearInterval(newCountdownTimer);
    newCountdownTimer = null;
  }
  
  newCountdown.value = seconds;
  newCountdownTimer = window.setInterval(() => {
    newCountdown.value--;
    if (newCountdown.value <= 0) {
      clearInterval(newCountdownTimer!);
      newCountdownTimer = null;
    }
  }, 1000);
};

// 管理敏感字段清理
const clearSensitiveFields = () => {
  password.value = '';
  oldEmailVerifyCode.value = '';
  newEmailVerifyCode.value = '';
  oldEmailVerifyError.value = '';
  newEmailVerifyError.value = '';
};

// 显示邮箱修改选择对话框
const showEmailDialog = async () => {
  try {
    // 先检查密码是否为空（未初始化）
    const response = await checkPasswordEmpty();
    if (response.data === true) {
      // 密码未初始化，显示提示对话框
      initPasswordAlertVisible.value = true;
    } else {
      // 密码已初始化，显示验证方式选择对话框
      verifyChoiceDialogVisible.value = true;
    }
  } catch (error) {
    console.error('检查密码状态失败：', error);
    ElMessage.error('检查密码状态失败，请稍后重试');
  }
};

// 关闭初始化密码提示
const closeInitPasswordAlert = () => {
  initPasswordAlertVisible.value = false;
};

// 使用密码验证
const verifyWithPassword = () => {
  resetFields();
  passwordVerifyDialogVisible.value = true;
  verifyChoiceDialogVisible.value = false;
};

// 使用邮箱验证
const verifyWithEmail = () => {
  resetFields();
  emailVerifyDialogVisible.value = true;
  verifyChoiceDialogVisible.value = false;
  
  // 发送验证码到旧邮箱
  sendOldEmailVerifyCode();
};

// 重置所有字段
const resetFields = () => {
  newEmail.value = '';
  verificationCode.value = '';
  clearSensitiveFields();
  emailErrors.value = [];
  isEmailValid.value = true;
  captchaVerified.value = false;
};

// 显示滑块验证码
const showSliderCaptcha = () => {
  if (!newEmail.value) {
    ElMessage.warning('请输入新邮箱地址');
    return;
  }
  
  // 邮箱格式校验
  const result = validateEmail(newEmail.value);
  if (!result.valid) {
    emailErrors.value = result.errors;
    isEmailValid.value = false;
    return;
  }
  
  isEmailValid.value = true;
  emailErrors.value = [];
  showCaptchaDialog.value = true;
};

// 验证码验证成功后的回调
const handleCaptchaSuccess = (uid: string) => {
  captchaVerified.value = true;
  showCaptchaDialog.value = false;

  // 验证成功后自动发送验证码
  setTimeout(() => {
    handleSendNewEmailVerificationCode();
  }, 500);
};

// 关闭验证码弹窗
const closeCaptcha = () => {
  showCaptchaDialog.value = false;
};

// 发送新邮箱验证码
const handleSendNewEmailVerificationCode = async () => {
  if (!captchaVerified.value) {
    showSliderCaptcha();
    return;
  }
  
  // 增加loading锁，防止并发请求
  if (sendingNewVerifyCode.value || newCountdown.value > 0) return;
  
  // 设置发送状态，同时禁用按钮
  sendingNewVerifyCode.value = true;
  
  try {
    // 使用新的API发送验证码
    const response = await sendEmailChangeCode(newEmail.value);
    if (response.status === 200) {
      ElMessage.success('验证码已发送，请查收邮箱');
      newEmailVerifyUid.value = response.data.uid || '';
      newEmailAddress.value = newEmail.value;
      
      // 开始倒计时
      startNewCountdown(60);
    } else if (response.status === 400 && response.message.includes('已被使用')) {
      ElMessage.error('该邮箱已被其他用户绑定，请更换邮箱地址');
    } else {
      ElMessage.error(response.message || '验证码发送失败');
    }
  } catch (error) {
    console.error('发送验证码出错:', error);
    ElMessage.error('发送验证码出错');
  } finally {
    // 确保在任何情况下都重置发送状态
    sendingNewVerifyCode.value = false;
  }
};

// 发送旧邮箱验证码
const sendOldEmailVerifyCode = async () => {
  // 增加loading锁，防止并发请求
  if (sendingOldVerifyCode.value || oldCountdown.value > 0) return;
  
  sendingOldVerifyCode.value = true;
  try {
    // 调用发送验证码到原邮箱的API
    const response = await sendVerificationToOriginalEmail();
    
    if (response.status === 200) {
      oldEmailVerifyUid.value = response.data.uid || '';
      oldEmailAddress.value = response.data.email || '';
      
      ElMessage.success('验证码已发送至您的邮箱');
      
      // 开始倒计时
      startOldCountdown(60);
    } else {
      ElMessage.error(response.message || '发送验证码失败');
    }
  } catch (error) {
    console.error('发送验证码失败：', error);
    ElMessage.error('发送验证码失败，请稍后重试');
  } finally {
    sendingOldVerifyCode.value = false;
  }
};

// 验证新邮箱输入
const validateNewEmail = () => {
  if (!newEmail.value) {
    emailErrors.value = ['请输入新邮箱地址'];
    isEmailValid.value = false;
    return false;
  }
  
  const result = validateEmail(newEmail.value);
  emailErrors.value = result.errors;
  isEmailValid.value = result.valid;
  
  return result.valid;
};

// 创建防抖验证函数
const debouncedValidateEmail = debounce(() => {
  if (newEmail.value) {
    validateNewEmail();
  } else {
    emailErrors.value = [];
    isEmailValid.value = true;
  }
}, 300);

// 邮箱输入时实时验证（使用防抖）
const handleEmailInput = () => {
  debouncedValidateEmail();
};

// 使用密码验证修改邮箱
const confirmPasswordUpdateEmail = async () => {
  if (!password.value) {
    ElMessage.warning('请输入密码');
    return;
  }
  
  if (!validateNewEmail()) {
    return;
  }
  
  if (!newEmailVerifyCode.value) {
    ElMessage.warning('请输入验证码');
    return;
  }
  
  try {
    // 调用密码验证更新邮箱接口
    const response = await updateEmailWithPassword({
      uid: newEmailVerifyUid.value,
      email: newEmail.value,
      code: newEmailVerifyCode.value,
      password: password.value
    });
    
    if (response.status === 200) {
      ElMessage.success('邮箱更新成功');
      email.value = newEmail.value; // 更新当前显示的邮箱
      passwordVerifyDialogVisible.value = false;
      resetFields();
    } else {
      ElMessage.error(response.message || '邮箱更新失败');
    }
  } catch (error) {
    console.error('更新邮箱失败:', error);
    ElMessage.error('更新邮箱失败，请稍后重试');
  }
};

// 使用邮箱验证修改邮箱
const confirmEmailUpdateEmail = async () => {
  if (!validateNewEmail()) {
    return;
  }
  
  if (!/^\d{6}$/.test(oldEmailVerifyCode.value)) {
    oldEmailVerifyError.value = '旧邮箱验证码格式不正确';
    return;
  }
  
  if (!/^\d{6}$/.test(newEmailVerifyCode.value)) {
    newEmailVerifyError.value = '新邮箱验证码格式不正确';
    return;
  }
  
  try {
    // 调用双重验证更新邮箱接口
    const response = await updateEmailWithTwoSteps({
      originalUid: oldEmailVerifyUid.value,
      originalCode: oldEmailVerifyCode.value,
      newEmail: newEmail.value,
      newUid: newEmailVerifyUid.value,
      newCode: newEmailVerifyCode.value
    });
    
    if (response.status === 200) {
      ElMessage.success('邮箱更新成功');
      email.value = newEmail.value; // 更新当前显示的邮箱
      emailVerifyDialogVisible.value = false;
      resetFields();
    } else {
      ElMessage.error(response.message || '邮箱更新失败');
    }
  } catch (error) {
    console.error('更新邮箱失败:', error);
    ElMessage.error('更新邮箱失败，请稍后重试');
  }
};

// 取消按钮方法
const cancelPasswordVerify = () => {
  passwordVerifyDialogVisible.value = false;
  resetFields();
};

const cancelEmailVerify = () => {
  emailVerifyDialogVisible.value = false;
  resetFields();
};

// 验证码验证成功后的回调函数
const captchaSuccessOn = (uid: string) => {
  handleCaptchaSuccess(uid);
};

// 监听对话框关闭，清除敏感数据
watch(passwordVerifyDialogVisible, (newVal) => {
  if (!newVal) {
    password.value = '';
  }
});

watch(emailVerifyDialogVisible, (newVal) => {
  if (!newVal) {
    oldEmailVerifyCode.value = '';
    newEmailVerifyCode.value = '';
  }
});

// 组件销毁时清理所有资源
onBeforeUnmount(() => {
  // 取消防抖函数订阅
  debouncedValidateEmail.cancel();
  
  // 清除所有定时器
  if (oldCountdownTimer) {
    clearInterval(oldCountdownTimer);
    oldCountdownTimer = null;
  }
  
  if (newCountdownTimer) {
    clearInterval(newCountdownTimer);
    newCountdownTimer = null;
  }
  
  // 清空敏感字段
  clearSensitiveFields();
});

// 尝试在路由离开时清空敏感信息
try {
  onBeforeRouteLeave(() => {
    clearSensitiveFields();
    
    // 清除所有定时器
    if (oldCountdownTimer) {
      clearInterval(oldCountdownTimer);
      oldCountdownTimer = null;
    }
    
    if (newCountdownTimer) {
      clearInterval(newCountdownTimer);
      newCountdownTimer = null;
    }
  });
} catch (e) {
  // 非路由组件可能会出错，忽略异常
}
</script>

<template>
  <div class="form-row">
    <label class="form-label">邮箱</label>
    
    <div class="form-content">
      <div class="email-display"
           @mouseenter="isHovering = true"
           @mouseleave="isHovering = false">
        <div class="email-container">
          <span class="email-text">{{ email }}</span>
        </div>
        <el-button 
            v-if="isHovering"
            :icon="Edit"
            class="edit-button"
            link
            type="primary"
            @click="showEmailDialog">修改</el-button>
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
      <p>您的账号密码还没有设置，请先初始化密码后再使用密码修改邮箱</p>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="closeInitPasswordAlert">确定</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 验证方式选择对话框 -->
  <el-dialog
      v-model="verifyChoiceDialogVisible"
      :close-on-click-modal="false"
      :show-close="true"
      append-to-body
      title="修改邮箱"
      width="400px"
  >
    <div class="dialog-content">
      <p>请选择验证方式</p>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="verifyWithEmail">使用邮箱验证</el-button>
        <el-button type="primary" @click="verifyWithPassword">使用密码验证</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 使用密码验证修改邮箱对话框 -->
  <el-dialog
      v-model="passwordVerifyDialogVisible"
      :close-on-click-modal="false"
      append-to-body
      title="修改邮箱"
      width="480px"
  >
    <div class="password-form">
      <!-- 密码输入 -->
      <el-input
          v-model="password"
          placeholder="请输入密码"
          show-password
          type="password"
      />
      
      <!-- 新邮箱输入 -->
      <div class="mb-3">
        <el-input
            v-model="newEmail"
            :status="!isEmailValid ? 'error' : ''"
            placeholder="请输入新邮箱地址"
            @input="handleEmailInput"
        />
        <div v-if="emailErrors.length > 0" class="validation-errors">
          <p v-for="(error, index) in emailErrors" :key="index" class="error-item">
            {{ error }}
          </p>
        </div>
      </div>
      
      <!-- 验证码 -->
      <div class="verify-code-section">
        <div style="display: flex; gap: 10px">
          <el-input
              v-model="newEmailVerifyCode"
              :status="newEmailVerifyError ? 'error' : ''"
              placeholder="请输入验证码"
          />
          <el-button
              :disabled="newCountdown > 0"
              type="primary"
              @click="showSliderCaptcha">
              {{ newCountdown > 0 ? `获取验证码(${newCountdown}s)` : '获取验证码' }}
          </el-button>
        </div>
        <div v-if="newEmailVerifyError" class="error-message">
          {{ newEmailVerifyError }}
        </div>
      </div>
      

      <div class="text-muted" style="font-size: 12px">
        验证码将发送至您输入的新邮箱，请注意查收
      </div>
    </div>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelPasswordVerify">取消</el-button>
        <el-button type="primary" @click="confirmPasswordUpdateEmail">
          确认修改
        </el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 使用邮箱验证修改邮箱对话框 -->
  <el-dialog
      v-model="emailVerifyDialogVisible"
      :close-on-click-modal="false"
      append-to-body
      title="修改邮箱"
      width="480px"
  >
    <div class="password-form">
      <!-- 旧邮箱验证码 -->
      <div class="verify-code-section">
        <div class="verify-code-header">
          <span>验证码已发送到您当前的邮箱</span>
          <el-button 
              :disabled="oldCountdown > 0"
              link 
              type="primary"
              @click="sendOldEmailVerifyCode">
              {{ oldCountdown > 0 ? `重发(${oldCountdown}s)` : '重发' }}
          </el-button>
        </div>
        <el-input
            v-model="oldEmailVerifyCode"
            :status="oldEmailVerifyError ? 'error' : ''"
            placeholder="请输入当前邮箱的验证码"
        />
        <div v-if="oldEmailVerifyError" class="error-message">
          {{ oldEmailVerifyError }}
        </div>
      </div>
      
      <!-- 新邮箱输入 -->
      <div class="mb-3">
        <el-input
            v-model="newEmail"
            :status="!isEmailValid ? 'error' : ''"
            placeholder="请输入新邮箱地址"
            @input="handleEmailInput"
        />
        <div v-if="emailErrors.length > 0" class="validation-errors">
          <p v-for="(error, index) in emailErrors" :key="index" class="error-item">
            {{ error }}
          </p>
        </div>
      </div>
      
      <!-- 新邮箱验证码 -->
      <div class="verify-code-section">
        <div style="display: flex; gap: 10px">
          <el-input
              v-model="newEmailVerifyCode"
              :status="newEmailVerifyError ? 'error' : ''"
              placeholder="请输入新邮箱的验证码"
          />
          <el-button
              :disabled="newCountdown > 0"
              type="primary"
              @click="showSliderCaptcha">
              {{ newCountdown > 0 ? `获取验证码(${newCountdown}s)` : '获取验证码' }}
          </el-button>
        </div>
        <div v-if="newEmailVerifyError" class="error-message">
          {{ newEmailVerifyError }}
        </div>
      </div>

      <div class="text-muted" style="font-size: 12px">
        您需要完成当前邮箱和新邮箱的双重验证才能修改邮箱
      </div>
    </div>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelEmailVerify">取消</el-button>
        <el-button type="primary" @click="confirmEmailUpdateEmail">
          确认修改
        </el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 滑块验证码弹窗 -->
  <SliderCaptcha
    v-if="showCaptchaDialog"
    :on-close="closeCaptcha"
    :on-success="captchaSuccessOn"
  />
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

.email-display {
  display: flex;
  align-items: center;
}

.email-container {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  background: #f9f9f9;
  border-radius: 8px;
  min-width: 200px;
  transition: background 0.2s;
}

.email-container:hover {
  background: #f1f1f1;
}

.email-text {
  font-size: 14px;
  color: #333;
}

.edit-button {
  margin-left: 8px;
}

.email-empty {
  display: flex;
  align-items: center;
  padding: 6px 12px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.empty-text {
  font-size: 14px;
  color: #909399;
  margin-right: 10px;
}

.password-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.input-group {
  display: flex;
  flex: 1;
  gap: 12px;
}

.slim-input {
  max-width: 320px;
}

.mb-3 {
  margin-bottom: 12px;
}

.w-100 {
  width: 100%;
}

.flex-grow-1 {
  flex-grow: 1;
}

.text-success {
  color: #67c23a;
}

.text-muted {
  color: #909399;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.validation-errors {
  padding: 8px 12px;
  background-color: #fff2f0;
  border-radius: 4px;
  border-left: 3px solid #f56c6c;
  margin-top: 8px;
}

.error-item {
  color: #f56c6c;
  font-size: 12px;
  line-height: 1.5;
  margin: 2px 0;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
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

@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
  }
  
  .form-label {
    text-align: left;
    margin-bottom: 8px;
    width: 100%;
  }
  
  .input-group {
    flex-direction: column;
    gap: 8px;
  }
  
  .slim-input {
    max-width: 100%;
  }
  
  .email-container {
    width: 100%;
  }
}
</style>