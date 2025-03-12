/**
 * File Name: Register.vue
 * Description: 用户注册页面组件
 * Author: holic512
 * Created Date: 2025-03-11
 * Usage: 处理新用户注册流程，收集用户信息并完成注册
 */

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Message, Lock, User, ArrowLeft } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { apiRegister } from '../service/ApiRegister';
import {useSessionStore} from "@/pinia/Session";

const router = useRouter();
const route = useRoute();

// 表单数据
const registerForm = ref({
  email: '',      // 邮箱
  username: '',   // 用户名
  password: '',   // 密码
  confirmPassword: '', // 确认密码
  uid: ''         // 拟注册的uid
});

// 加载状态
const loading = ref(false);

// 表单规则
const rules = ref<FormRules>({
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度应在2-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
});

// 表单引用
const formRef = ref<FormInstance>();

// 处理注册
const handleRegister = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  
  await formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        // 调用注册API
        const response = await apiRegister({
          email: registerForm.value.email,
          username: registerForm.value.username,
          password: registerForm.value.password,
          uid: registerForm.value.uid
        });

        if (response.status === 200) {
          ElMessage.success('注册成功，即将跳转');

          // 保存token
          const store = useSessionStore()
          store.setSession(response.data)
          
          // 获取重定向地址，如果没有则跳转到登录页
          const redirectPath = route.query.redirect as string || '/sign/email';
          // 注册成功后跳转到指定页面
          setTimeout(() => {
            router.push(redirectPath);
          }, 1500);
        } else if (response.status === 400 && response.message === '用户名已被注册，请更换其他用户名') {
          ElMessage.error('用户名已被注册，请更换其他用户名');
          // 清空用户名输入框
          registerForm.value.username = '';
          // 聚焦用户名输入框
          formRef.value?.validateField('username');
        } else {
          ElMessage.error(response.message || '注册失败');
        }
      } catch (error: any) {
        console.error('注册失败:', error);
        ElMessage.error(error.response?.data?.message || '注册失败，请稍后重试');
      } finally {
        loading.value = false;
      }
    }
  });
};

// 返回登录页
const backToLogin = () => {
  router.push('/sign/email');
};

// 在组件挂载时，如果有参数，自动填充相关字段
onMounted(() => {
  const email = route.query.email as string;
  const uid = route.query.uid as string;
  
  if (email) {
    registerForm.value.email = email;
  }
  if (uid) {
    registerForm.value.uid = uid;
  }
});
</script>

<template>
  <div class="register-form">
    <el-form
      ref="formRef"
      :model="registerForm"
      :rules="rules"
      @submit.prevent="handleRegister(formRef)"
    >
      <!-- 邮箱输入框 -->
      <el-form-item prop="email">
        <el-input
          v-model="registerForm.email"
          placeholder="请输入邮箱"
          :disabled="!!route.query.email"
          size="large"
          class="custom-input"
        >
          <template #prefix>
            <el-icon class="input-icon"><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 用户名输入框 -->
      <el-form-item prop="username">
        <el-input
          v-model="registerForm.username"
          placeholder="请输入用户名"
          size="large"
          class="custom-input"
        >
          <template #prefix>
            <el-icon class="input-icon"><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 密码输入框 -->
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          placeholder="请输入密码"
          size="large"
          class="custom-input"
          show-password
        >
          <template #prefix>
            <el-icon class="input-icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 确认密码输入框 -->
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          placeholder="请确认密码"
          size="large"
          class="custom-input"
          show-password
        >
          <template #prefix>
            <el-icon class="input-icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 注册按钮 -->
      <el-form-item>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleRegister(formRef)"
          class="register-btn"
        >
          完成注册
        </el-button>
      </el-form-item>

      <!-- 返回登录 -->
      <div class="back-to-login">
        <a @click="backToLogin">
          <el-icon><ArrowLeft /></el-icon>
          返回登录
        </a>
      </div>
    </el-form>
  </div>
</template>

<style scoped>
.register {
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

.password-toggle {
  color: #666;
  font-size: 16px;
  cursor: pointer;
  transition: color 0.2s ease;
}

.password-toggle:hover {
  color: #1a1a1a;
}

.register-btn {
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

.register-btn:hover {
  transform: translateY(-1px);
  background-color: #000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.back-to-login {
  text-align: center;
  margin-top: 20px;
}

.back-to-login a {
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

.back-to-login a:hover {
  color: #1a1a1a;
  background-color: #eee;
}

.el-form-item {
  margin-bottom: 16px;
}

.el-form-item:last-child {
  margin-bottom: 0;
}

.email-display {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
  padding: 12px 16px;
  background-color: #f5f5f5;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.email-display .icon {
  color: #1a1a1a;
  font-size: 16px;
}

.email-display .text {
  color: #1a1a1a;
  font-weight: 500;
}
</style> 