<script setup lang="ts">
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {Key, Lock, Message} from '@element-plus/icons-vue';
import {ElMessage} from 'element-plus';
import SliderCaptcha from '../../../components/SliderCaptcha.vue';
import {apiSendSignVerificationCode} from "@/view/Sign/service/ApiSendSignVerificationCode";
import {ApiVerifySignVerificationCode} from "@/view/Sign/service/ApiVerifySignVerificationCode";
import {useSessionStore} from "@/pinia/Session";

const router = useRouter();
const route = useRoute();
const email = ref('');
const verificationCode = ref('');
const countdown = ref(0);
const loading = ref(false);
const sendingEmail = ref(false);

// 滑块验证码相关
const showCaptchaDialog = ref(false);
const captchaVerified = ref(false);

// 发送验证码
const sendVerificationCode = async () => {
  if (!email.value) {
    ElMessage.warning('请输入邮箱地址');
    return;
  }

  // 如果未通过验证，显示滑块验证码
  if (!captchaVerified.value) {
    showCaptchaDialog.value = true;
    return;
  }

  // 已通过验证，直接发送验证码
  await sendEmailCode();
};

// 验证码验证成功后的回调
const handleCaptchaSuccess = (uid: string) => {
  captchaVerified.value = true;
  showCaptchaDialog.value = false;

  // 验证成功后自动发送验证码
  setTimeout(() => {
    sendEmailCode();
  }, 500);
};

// 关闭验证码弹窗
const closeCaptcha = () => {
  showCaptchaDialog.value = false;
};

// 实际发送邮箱验证码的函数
const sendEmailCode = async () => {
  loading.value = true;
  sendingEmail.value = true;
  
  try {
    // 调用发送验证码API
    const response = await apiSendSignVerificationCode(email.value)

    if (response.status === 200) {
      ElMessage.success('验证码已发送，请查收邮箱');
      countdown.value = 60;
      const timer = setInterval(() => {
        countdown.value--;
        if (countdown.value <= 0) {
          clearInterval(timer);
          // 倒计时结束后重置验证状态，下次需要重新验证
          captchaVerified.value = false;
        }
      }, 1000);
    } else {
      ElMessage.error(response.data.message || '发送验证码失败');
      captchaVerified.value = false;
    }
  } catch (error) {
    console.error('发送验证码失败:', error);
    ElMessage.error('发送验证码失败，请稍后重试');
    captchaVerified.value = false;
  } finally {
    loading.value = false;
    sendingEmail.value = false;
  }
};

// 登录
const handleLogin = async () => {
  if (!email.value || !verificationCode.value) {
    ElMessage.warning('请填写完整信息');
    return;
  }

  loading.value = true;
  try {
    // 调用登录API
    const response = await ApiVerifySignVerificationCode(email.value, verificationCode.value);

    if (response.status === 200) {

      // 保存token
      const store = useSessionStore();
      store.setSession(response.data);

      ElMessage.success('登录成功');

      // 获取重定向地址，如果没有则跳转到首页
      const redirectPath = Array.isArray(route.query.redirect)
          ? route.query.redirect[0]
          : (route.query.redirect || '/');

      // 确保redirectPath不为null
      await router.push(redirectPath ? redirectPath : '/');
    }
    //以升级成无感注入

    // else if (response.status === 201) {
    //   // 验证成功但用户未注册，跳转到注册页面
    //   ElMessage.info('请完成注册');
    //
    //   // 获取重定向地址
    //   const redirectPath = Array.isArray(route.query.redirect)
    //       ? route.query.redirect[0]
    //       : (route.query.redirect || '/');
    //
    //   router.push({
    //     path: '/sign/register',
    //     query: {
    //       email: email.value,           // 传递邮箱
    //       uid: response.data,           // 传递拟注册的uid
    //       redirect: redirectPath        // 传递重定向地址
    //     }
    //   });
    // }
    else {
      ElMessage.error(response.message || '登录失败');
    }
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error('登录失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 切换到密码登录
const switchToPasswordLogin = () => {
  router.push({
    path: '/sign/password',
    query: route.query // 保持重定向参数
  });
};

// 验证码验证成功后的 方法
const captchaSuccessOn = (uid: string) => {
  handleCaptchaSuccess(uid);
}
</script>

<template>
  <div class="email-login">
    <el-form @submit.prevent="handleLogin">
      <!-- 邮箱输入 -->
      <el-form-item>
        <el-input
            v-model="email"
            placeholder="请输入邮箱"
            size="large"
            class="custom-input"
            :autofocus="false"
        >
          <template #prefix>
            <el-icon class="input-icon">
              <Message/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 验证码输入 -->
      <el-form-item>
        <div class="verification-code-container">
          <el-input
              v-model="verificationCode"
              placeholder="请输入验证码"
              size="large"
              class="custom-input"
              :autofocus="false"
          >
            <template #prefix>
              <el-icon class="input-icon">
                <Key/>
              </el-icon>
            </template>
          </el-input>
          <el-button
              type="primary"
              :loading="countdown > 0 || sendingEmail"
              :disabled="countdown > 0"
              @click="sendVerificationCode"
              size="large"
              class="send-code-btn"
          >
            <span v-if="sendingEmail">发送中...</span>
            <span v-else-if="countdown > 0">{{ countdown }}s后重新发送</span>
            <span v-else>发送验证码</span>
          </el-button>
        </div>
      </el-form-item>

      <!-- 登录按钮 -->
      <el-form-item>
        <el-button
            type="primary"
            :loading="loading"
            @click="handleLogin"
            class="login-btn"
        >
          登录 / 注册
        </el-button>
      </el-form-item>

      <!-- 切换到密码登录 -->
      <div class="switch-login-type">
        <a @click="switchToPasswordLogin">
          <el-icon>
            <Lock/>
          </el-icon>
          使用密码登录
        </a>
      </div>
    </el-form>
  </div>

  <!-- 滑块验证码弹窗 -->
  <SliderCaptcha
      v-if="showCaptchaDialog"
      :on-success="captchaSuccessOn"
      :on-close="closeCaptcha"
  />


</template>

<style scoped>
.email-login {
  width: 100%;
  animation: fadeIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.verification-code-container {
  display: flex;
  gap: 8px;
}

.verification-code-container .el-input {
  flex: 1;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.08);
  padding: 0 16px;
  transition: all 0.2s ease;
  background-color: #fff;
  height: 44px;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.15);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.2);
}

.custom-input :deep(.el-input__inner) {
  color: #1a1a1a;
  font-size: 14px;
  font-weight: 400;
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: #999;
  font-weight: 400;
}

.input-icon {
  color: #666;
  font-size: 16px;
  margin-right: 6px;
}

.send-code-btn {
  min-width: 120px;
  border-radius: 12px;
  transition: all 0.2s ease;
  font-weight: 500;
  font-size: 14px;
  letter-spacing: 0.3px;
  height: 44px;
  background-color: #1a1a1a;
  color: #fff;
  border: none;
  padding: 0 16px;
}

.send-code-btn:not(:disabled):hover {
  transform: translateY(-1px);
  background-color: #000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.send-code-btn:disabled {
  background-color: #f5f5f5;
  color: #999;
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.login-btn {
  width: 100%;
  border-radius: 12px;
  height: 44px;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: all 0.2s ease;
  margin-top: 8px;
  background-color: #1a1a1a;
  color: #fff;
  border: none;
}

.login-btn:hover {
  transform: translateY(-1px);
  background-color: #000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.switch-login-type {
  text-align: center;
  margin-top: 20px;
}

.switch-login-type a {
  color: #666;
  text-decoration: none;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  border-radius: 10px;
  background-color: #f5f5f5;
}

.switch-login-type a:hover {
  color: #1a1a1a;
  background-color: #eee;
}

.el-form-item {
  margin-bottom: 16px;
}

.el-form-item:last-child {
  margin-bottom: 0;
}
</style>