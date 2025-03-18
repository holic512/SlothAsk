<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

const userInfo = ref({
  nickname: '测试用户',
  email: 'test@example.com',
  bio: '这是一段个人简介',
  joinDate: '2024-01-01',
  solvedProblems: 128,
  correctRate: '85%'
});

const handleUpdateProfile = () => {
  ElMessage.success('更新成功');
};
</script>

<template>
  <div class="profile-container">
    <div class="profile-header">
      <h2>个人资料</h2>
      <el-button type="primary" @click="handleUpdateProfile">保存修改</el-button>
    </div>

    <div class="profile-content">
      <el-form label-width="120px">
        <el-form-item label="昵称">
          <el-input v-model="userInfo.nickname" placeholder="请输入昵称" />
        </el-form-item>
        
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="个人简介">
          <el-input
            v-model="userInfo.bio"
            type="textarea"
            :rows="4"
            placeholder="请输入个人简介"
          />
        </el-form-item>
      </el-form>

      <div class="profile-stats">
        <div class="stat-item">
          <div class="stat-label">加入时间</div>
          <div class="stat-value">{{ userInfo.joinDate }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-label">已解决题目</div>
          <div class="stat-value">{{ userInfo.solvedProblems }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-label">正确率</div>
          <div class="stat-value">{{ userInfo.correctRate }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.profile-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.profile-content {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-top: 32px;
  padding-top: 32px;
  border-top: 1px solid #eee;
}

.stat-item {
  text-align: center;
}

.stat-label {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  color: #333;
  font-size: 20px;
  font-weight: 500;
}
</style> 