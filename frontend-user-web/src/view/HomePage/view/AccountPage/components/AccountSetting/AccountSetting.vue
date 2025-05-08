<script lang="ts" setup>
import {ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';

const username = ref('');
const email = ref('');
const newPassword = ref('');
const confirmPassword = ref('');
const thirdPartyBinds = ref({
  github: false,
  wechat: false,
  qq: true
});

// 是否有绑定邮箱
const hasEmail = ref(false);

// 模拟更新用户名
const handleUpdateUsername = () => {
  if (!username.value) {
    ElMessage.warning('用户名不能为空');
    return;
  }
  ElMessage.success('用户名修改成功');
};

// 模拟更新邮箱
const handleUpdateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email.value)) {
    ElMessage.warning('请输入正确的邮箱格式');
    return;
  }
  ElMessage.success('邮箱修改成功');
  hasEmail.value = true;
};

// 选择密码验证方式
const showPasswordVerificationChoice = () => {
  if (hasEmail.value) {
    ElMessageBox.confirm(
      '请选择验证方式',
      '修改密码',
      {
        confirmButtonText: '使用旧密码验证',
        cancelButtonText: '使用邮箱验证',
        type: 'info',
        showClose: false,
        distinguishCancelAndClose: true,
        callback: (action) => {
          if (action === 'confirm') {
            verifyWithOldPassword();
          } else if (action === 'cancel') {
            verifyWithEmail();
          }
        }
      }
    );
  } else {
    ElMessageBox.alert(
      '您当前还未绑定邮箱，请先绑定邮箱或使用旧密码验证',
      '提示',
      {
        confirmButtonText: '使用旧密码验证',
        type: 'warning',
        callback: () => {
          verifyWithOldPassword();
        }
      }
    );
  }
};

// 使用旧密码验证
const verifyWithOldPassword = () => {
  ElMessageBox.prompt('请输入当前密码', '密码验证', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputType: 'password',
    inputValidator: (value) => {
      return !!value || '密码不能为空';
    }
  }).then(({ value }) => {
    // 模拟验证成功，显示修改密码表单
    ElMessage.success('验证成功，请设置新密码');
    showResetPasswordForm();
  }).catch(() => {
    // 用户取消操作
  });
};

// 使用邮箱验证
const verifyWithEmail = () => {
  ElMessageBox.prompt('验证码已发送至您的邮箱，请输入验证码', '邮箱验证', {
    confirmButtonText: '验证',
    cancelButtonText: '取消',
    inputPattern: /^\d{6}$/,
    inputErrorMessage: '验证码格式不正确'
  }).then(({ value }) => {
    // 模拟验证成功，显示修改密码表单
    ElMessage.success('验证成功，请设置新密码');
    showResetPasswordForm();
  }).catch(() => {
    // 用户取消操作
  });
};

// 显示重置密码表单
const showResetPasswordForm = () => {
  ElMessageBox.prompt(
    '请输入新密码',
    '修改密码',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputValidator: (value) => {
        return value.length >= 6 || '密码长度至少为6位';
      }
    }
  ).then(({ value }) => {
    // 模拟修改密码成功
    ElMessage.success('密码修改成功');
  }).catch(() => {
    // 用户取消操作
  });
};

// 模拟绑定操作，只允许操作GitHub
const toggleBind = (platform: 'github' | 'wechat' | 'qq') => {
  if (platform !== 'github') {
    ElMessage.info('当前仅支持GitHub绑定');
    return;
  }
  thirdPartyBinds.value[platform] = !thirdPartyBinds.value[platform];
  ElMessage.success(`GitHub已${thirdPartyBinds.value[platform] ? '绑定' : '解绑'}`);
};
</script>

<template>
  <div class="account-settings">
    <!-- 基本信息区域 -->
    <div class="section">
      <div class="section-title">基本信息</div>
      
      <div class="form-row">
        <div class="form-group">
          <label class="form-label">用户名</label>
          <div class="input-group">
            <el-input v-model="username" class="slim-input" placeholder="请输入用户名" />
            <el-button type="primary" @click="handleUpdateUsername">修改</el-button>
          </div>
        </div>
      </div>
      
      <div class="form-row">
        <div class="form-group">
          <label class="form-label">邮箱</label>
          <div class="input-group">
            <el-input v-model="email" class="slim-input" placeholder="请输入邮箱" />
            <el-button type="primary" @click="handleUpdateEmail">修改</el-button>
          </div>
        </div>
      </div>
    </div>
    
    <el-divider />
    
    <!-- 密码修改区域 -->
    <div class="section">
      <div class="section-title">密码修改</div>
      
      <div class="form-row password-section">
        <el-button type="primary" @click="showPasswordVerificationChoice">修改密码</el-button>
        <div class="password-tip">
          点击按钮后将提示您选择验证方式，完成验证后可修改密码
        </div>
      </div>
    </div>
    
    <el-divider />
    
    <!-- 第三方绑定区域 -->
    <div class="section">
      <div class="section-title">第三方绑定</div>
      
      <!-- GitHub 放在第一位 -->
      <div class="form-row auth-row">
        <div class="auth-platform">
          <span class="platform-label">GitHub</span>
          <el-tag :type="thirdPartyBinds.github ? 'success' : 'info'" class="status-tag">
            {{ thirdPartyBinds.github ? '已绑定' : '未绑定' }}
          </el-tag>
        </div>
        <el-button size="small" type="primary" @click="toggleBind('github')">
          {{ thirdPartyBinds.github ? '解绑' : '绑定' }}
        </el-button>
      </div>
      
      <!-- 禁用其他选项 -->
      <div class="form-row auth-row">
        <div class="auth-platform">
          <span class="platform-label">微信</span>
          <el-tag class="status-tag" type="info">
            {{ thirdPartyBinds.wechat ? '已绑定' : '未绑定' }}
          </el-tag>
        </div>
        <div class="action-area">
          <el-button disabled size="small">绑定</el-button>
          <span class="bind-tip">暂不支持</span>
        </div>
      </div>
      
      <div class="form-row auth-row">
        <div class="auth-platform">
          <span class="platform-label">QQ</span>
          <el-tag class="status-tag" type="info">
            {{ thirdPartyBinds.qq ? '已绑定' : '未绑定' }}
          </el-tag>
        </div>
        <div class="action-area">
          <el-button disabled size="small">绑定</el-button>
          <span class="bind-tip">暂不支持</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.account-settings {
  max-width: 700px;
  margin: 0 auto;
  padding: 40px;
  background-color: white;
  border-radius: 12px;
}

.section {
  margin-bottom: 20px;
  padding: 10px 0;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 24px;
}

.form-row {
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  align-items: flex-start;
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

.input-group {
  display: flex;
  flex: 1;
  gap: 12px;
}

.slim-input {
  max-width: 320px;
}

.password-section {
  display: flex;
  align-items: center;
  padding-left: 86px;
}

.password-tip {
  margin-left: 16px;
  color: #909399;
  font-size: 13px;
}

.auth-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px 0 86px;
  margin-bottom: 16px;
}

.auth-platform {
  display: flex;
  align-items: center;
}

.platform-label {
  width: 80px;
  font-size: 14px;
}

.status-tag {
  margin-left: 12px;
}

.action-area {
  display: flex;
  align-items: center;
}

.bind-tip {
  margin-left: 12px;
  color: #909399;
  font-size: 12px;
}

:deep(.el-divider--horizontal) {
  margin: 24px 0;
}

@media (max-width: 768px) {
  .account-settings {
    padding: 20px;
  }
  
  .form-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .form-label {
    text-align: left;
    margin-bottom: 8px;
  }
  
  .input-group {
    flex-direction: column;
    gap: 8px;
  }
  
  .auth-row {
    padding-left: 0;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .password-section {
    padding-left: 0;
    flex-direction: column;
    align-items: flex-start;
  }
  
  .password-tip {
    margin-left: 0;
    margin-top: 8px;
  }
}
</style>
