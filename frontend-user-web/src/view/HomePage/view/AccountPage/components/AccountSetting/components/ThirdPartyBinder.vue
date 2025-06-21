<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {checkGithubBound} from '../service/ApiGetUserAccount';
import {sendGithubBindCode, unbindGithub, verifyGithubBindCode} from '../service/ApiUpdateUserAccount';
import {generateGithubAuthUrl} from '@/utils/generateGithubAuthUrl';

// 绑定状态
const binds = ref({
  github: false,
  wechat: false,
  qq: false
});

// 加载状态
const loading = ref({
  github: false,
  wechat: false,
  qq: false
});

// 邮箱验证码
const githubBindCode = ref({
  uid: '',
  code: '',
  email: ''
});

// 输入验证码对话框可见性
const showCodeDialog = ref(false);

// 初始化加载
onMounted(async () => {
  await loadGithubBindStatus();
});

// 加载GitHub绑定状态
const loadGithubBindStatus = async () => {
  try {
    loading.value.github = true;
    const res = await checkGithubBound();
    if (res && res.status === 200) {
      binds.value.github = res.data;
    }
  } catch (error) {
    ElMessage.error('获取GitHub绑定状态失败');
    console.error('获取GitHub绑定状态失败:', error);
  } finally {
    loading.value.github = false;
  }
};

// 发送GitHub绑定验证码
const sendGithubBindVerficationCode = async () => {
  try {
    loading.value.github = true;
    const res = await sendGithubBindCode();
    if (res && res.status === 200 && res.data) {
      githubBindCode.value.uid = res.data.uid;
      githubBindCode.value.email = res.data.email;
      showCodeDialog.value = true;
      ElMessage.success(`验证码已发送至邮箱 ${res.data.email}`);
    } else {
      ElMessage.error(res?.message || '发送验证码失败');
    }
  } catch (error) {
    ElMessage.error('发送验证码失败，请重试');
    console.error('发送GitHub绑定验证码失败:', error);
  } finally {
    loading.value.github = false;
  }
};

// 验证GitHub绑定验证码并跳转授权
const verifyCodeAndAuthorize = async () => {
  try {
    if (!githubBindCode.value.code) {
      ElMessage.warning('请输入验证码');
      return;
    }

    loading.value.github = true;
    const res = await verifyGithubBindCode({
      uid: githubBindCode.value.uid,
      code: githubBindCode.value.code
    });

    if (res && res.status === 200) {
      showCodeDialog.value = false;
      // 验证成功，跳转到GitHub授权页面
      window.location.href = generateGithubAuthUrl('bind');
    } else {
      ElMessage.error(res?.message || '验证码错误或已过期');
    }
  } catch (error) {
    ElMessage.error('验证码验证失败，请重试');
    console.error('验证GitHub绑定验证码失败:', error);
  } finally {
    loading.value.github = false;
  }
};

// 绑定/解绑GitHub账号
const toggleGithubBind = async () => {
  try {
    // 如果已绑定，确认是否解绑
    if (binds.value.github) {
      await ElMessageBox.confirm(
          '确定要解除GitHub账号绑定吗？解绑后将无法使用GitHub账号登录。',
          '解绑确认',
          {
            confirmButtonText: '确认解绑',
            cancelButtonText: '取消',
            type: 'warning'
          }
      );

      loading.value.github = true;
      const res = await unbindGithub();
      if (res && res.status === 200) {
        ElMessage.success('解绑GitHub账号成功');
        binds.value.github = false;
      } else {
        ElMessage.error(res?.message || '解绑失败');
      }
    } else {
      // 如果未绑定，先发送验证码
      await sendGithubBindVerficationCode();
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败，请重试');
      console.error('GitHub账号绑定/解绑失败:', error);
    }
  } finally {
    loading.value.github = false;
  }
};

// 关闭验证码对话框
const closeCodeDialog = () => {
  showCodeDialog.value = false;
  githubBindCode.value.code = '';
};
</script>

<template>
  <div class="third-party-section">
    <div class="section-description">
      绑定第三方账号后，可以直接使用第三方账号登录。
    </div>

    <!-- GitHub 绑定选项 -->
    <div class="auth-row">
      <div class="auth-info">

        <img alt="" class="auth-icon" src="/HomePage/AccountSetting/github-fill.png">

        <div class="auth-details">
          <div class="auth-name">GitHub</div>
          <div :class="{ 'bound': binds.github }" class="auth-status">
            {{ binds.github ? '已绑定' : '未绑定' }}
          </div>
        </div>
      </div>

      <div class="auth-action">
        <el-button
            :loading="loading.github"
            :plain="binds.github"
            type="primary"
            @click="toggleGithubBind"
        >
          {{ binds.github ? '解绑' : '绑定' }}
        </el-button>
      </div>
    </div>

    <!-- 微信绑定选项（暂不支持） -->
    <div class="auth-row disabled">
      <div class="auth-info">

        <img alt="" class="auth-icon" src="/HomePage/AccountSetting/wechat-fill.png">

        <div class="auth-details">
          <div class="auth-name">微信</div>
          <div class="auth-status">
            未绑定
            <span class="unsupported-tip">（暂不支持）</span>
          </div>
        </div>
      </div>
    </div>

    <!-- QQ绑定选项（暂不支持） -->
    <div class="auth-row disabled">
      <div class="auth-info">
        <img alt="" class="auth-icon" src="/HomePage/AccountSetting/QQ-square-fill.png">
        <div class="auth-details">
          <div class="auth-name">QQ</div>
          <div class="auth-status">
            未绑定
            <span class="unsupported-tip">（暂不支持）</span>
          </div>
        </div>
      </div>
    </div>

    <!-- GitHub绑定验证码对话框 -->
    <el-dialog
        v-model="showCodeDialog"
        :close-on-click-modal="false"
        :show-close="true"
        append-to-body
        title="GitHub账号绑定验证"
        width="400px"
        @close="closeCodeDialog"
    >
      <div class="verification-dialog">
        <p>为保证账号安全，请输入发送至 {{ githubBindCode.email }} 的验证码</p>
        <el-input
            v-model="githubBindCode.code"
            class="code-input"
            maxlength="6"
            placeholder="请输入验证码"
        ></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeCodeDialog">取消</el-button>
          <el-button :loading="loading.github" type="primary" @click="verifyCodeAndAuthorize">
            验证并授权
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.section-description {
  font-size: 14px;
  color: #606266;
  margin-bottom: 24px;
}

.auth-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #EBEEF5;
}

.auth-row:last-child {
  border-bottom: none;
}

.auth-row.disabled {
  opacity: 0.7;
}

.auth-info {
  display: flex;
  align-items: center;
}

.auth-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  color: #fff;
}

.github-icon {
  background-color: #24292e;
}

.wechat-icon {
  background-color: #07C160;
}

.qq-icon {
  background-color: #12B7F5;
}

.auth-details {
  display: flex;
  flex-direction: column;
}

.auth-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.auth-status {
  font-size: 12px;
  color: #909399;
}

.auth-status.bound {
  color: #67C23A;
}

.auth-action {
  display: flex;
  align-items: center;
}

.unsupported-tip {
  color: #909399;
}

.verification-dialog p {
  margin-bottom: 20px;
  color: #606266;
}

.code-input {
  margin-bottom: 10px;
}

@media (max-width: 768px) {
  .auth-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .auth-action {
    margin-top: 16px;
    margin-left: 56px;
  }
}
</style>