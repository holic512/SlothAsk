<script setup lang="ts">
import { ref } from 'vue'

const config = ref({
  systemName: 'SlothAsk',
  version: '1.0.0',
  maintenanceMode: false
})

const saveConfig = () => {
  console.log('配置已保存:', config.value)
  // 在这里可以添加保存配置的逻辑，比如调用 API
}
</script>

<template>
  <div class="config-container">
    <h2>系统配置</h2>
    <el-form :model="config" label-width="120px" class="config-form">
      <el-form-item label="系统名称">
        <el-input v-model="config.systemName" placeholder="请输入系统名称"></el-input>
      </el-form-item>
      <el-form-item label="版本号">
        <el-input v-model="config.version" placeholder="请输入版本号"></el-input>
      </el-form-item>
      <el-form-item label="维护模式">
        <el-switch v-model="config.maintenanceMode"></el-switch>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveConfig">保存配置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.config-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 600;
  color: #1f2f3d;
}

.config-form {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
</style> 