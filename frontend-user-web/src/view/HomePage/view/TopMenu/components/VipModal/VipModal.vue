<script lang="ts" setup>
import {defineEmits, defineProps, onMounted, ref} from 'vue';
import {Check, Close, Star} from '@element-plus/icons-vue';
import {announcementConfig, vipPlansConfig} from './vipPlansConfig';
import {checkVipStatus, getUserVipInfo} from '../../Api/ApiVip';
import {ElMessage} from 'element-plus';
import {isUserLoggedIn} from '@/utils/useIsLoggedIn';

// 定义事件
const emit = defineEmits(['close', 'selectPlan']);

// 接收父组件传递的当前会员等级
const props = defineProps({
  currentVipLevel: {
    type: Number,
    default: 0 // 0: 免费, 1: 月付, 2: 永久
  }
});

// 响应式数据
const userVipInfo = ref(null);
const isVipActive = ref(false);
const loading = ref(false);
const currentVipLevel = ref(props.currentVipLevel);

// 获取用户VIP信息
const fetchUserVipInfo = async () => {
  try {
    loading.value = true;
    
    // 检查用户是否已登录
    if (!isUserLoggedIn()) {
      // 未登录时设置默认免费方案
      currentVipLevel.value = 0;
      isVipActive.value = false;
      userVipInfo.value = null;
      return;
    }
    
    // 获取VIP详细信息
    const vipInfoResponse = await getUserVipInfo();
    if (vipInfoResponse.status === 200) {
      userVipInfo.value = vipInfoResponse.data;
      // 根据VIP类型更新当前等级
      currentVipLevel.value = vipInfoResponse.data.vipType;
    }
    
    // 检查VIP状态
    const statusResponse = await checkVipStatus();
    if (statusResponse.status === 200) {
      isVipActive.value = statusResponse.data;
    }
    
  } catch (error) {
    console.error('获取VIP信息失败:', error);
    // 如果获取失败，使用默认值
    currentVipLevel.value = 0;
    isVipActive.value = false;
    // 显示错误提示（可选）
    ElMessage.warning('获取VIP信息失败，显示默认状态');
  } finally {
    loading.value = false;
  }
};

// 判断是否为当前套餐
const isCurrentPlan = (planId: number) => {
  // 如果是免费套餐(id=0)，当没有VIP或VIP不生效时显示为当前套餐
  if (planId === 0) {
    return !isVipActive.value || currentVipLevel.value === 0;
  }
  // 其他套餐需要VIP生效且类型匹配
  return currentVipLevel.value === planId && isVipActive.value;
};

// 判断按钮是否应该被禁用（低等级按钮在高等级用户面前禁用）
const isButtonDisabled = (planId: number) => {
  // 当前套餐总是禁用
  if (isCurrentPlan(planId)) {
    return true;
  }
  
  // 如果用户有VIP且VIP生效
  if (isVipActive.value && currentVipLevel.value > 0) {
    // 禁用所有低于当前等级的套餐按钮
    return planId < currentVipLevel.value;
  }
  
  return false;
};

// 处理套餐选择
const handleSelectPlan = (planId: number) => {
  emit('selectPlan', planId);
};

// 关闭弹窗
const handleClose = () => {
  emit('close');
};

// 组件挂载时获取VIP信息
onMounted(() => {
  fetchUserVipInfo();
});
</script>

<template>
  <div class="vip-modal-overlay" @click="handleClose">
    <div class="vip-modal-container" @click.stop>
      <!-- 弹窗头部 -->
      <div class="modal-header">
        <button class="close-button" @click="handleClose">
          <el-icon :size="16">
            <Close/>
          </el-icon>
        </button>
      </div>

      <!-- 公告栏 -->
      <div class="announcement-banner">
        <div class="announcement-content">
          <el-icon :color="announcementConfig.iconColor" :size="16">
            <Star/>
          </el-icon>
          <span class="announcement-text">{{ announcementConfig.text }}</span>
        </div>
      </div>

      <!-- VIP信息显示区域 -->
      <div v-if="userVipInfo && isVipActive" class="vip-info-section">
        <div class="vip-info-content">
          <span class="vip-status-text">当前VIP状态：</span>
          <span :class="`vip-type-${userVipInfo.vipType}`" class="vip-type-badge">
            {{ vipPlansConfig.find(plan => plan.id === userVipInfo.vipType)?.name || '未知' }}
          </span>
          <span v-if="userVipInfo.endTime" class="vip-expire-text">
            到期时间：{{ new Date(userVipInfo.endTime).toLocaleDateString() }}
          </span>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-section">
        <div class="loading-content">
          <div class="loading-spinner">
            <div class="spinner-ring"></div>
            <div class="spinner-ring"></div>
            <div class="spinner-ring"></div>
          </div>
          <div class="loading-text">
            <span class="loading-main-text">正在获取VIP信息</span>
            <div class="loading-dots">
              <span class="dot">.</span>
              <span class="dot">.</span>
              <span class="dot">.</span>
            </div>
          </div>
        </div>
        <div class="loading-progress">
          <div class="progress-bar"></div>
        </div>
      </div>

      <!-- 水平布局模块 (≥835px) -->
      <el-scrollbar v-if="!loading" class="horizontal-layout">
        <div class="horizontal-plans-container">
          <div v-for="plan in vipPlansConfig" :key="plan.id" 
               :class="[plan.className, { 'current-plan': isCurrentPlan(plan.id) }]"
               class="horizontal-plan-card">
            <div v-if="isCurrentPlan(plan.id)" class="current-plan-badge">
              <el-icon :size="14">
                <Star/>
              </el-icon>
              <span>您当前的套餐</span>
            </div>
            <div class="plan-header">
              <h3 class="plan-name">{{ plan.name }}</h3>
              <div class="plan-price">
                <span class="price-number">{{ plan.price }}</span>
                <span class="price-unit">{{ plan.unit }}</span>
              </div>
            </div>
            <div class="plan-button-section">
              <button :class="[plan.buttonClassName, { 'current-button': isCurrentPlan(plan.id) }]"
                      :disabled="isButtonDisabled(plan.id)"
                      class="plan-button"
                      @click="handleSelectPlan(plan.id)">
                {{ isCurrentPlan(plan.id) ? '当前套餐' : (isVipActive && currentVipLevel > 0 && plan.id < currentVipLevel) ? '已拥有更高等级' : '获取VIP' }}
              </button>
            </div>
            <div class="plan-features">
              <div v-for="feature in plan.features" :key="feature" class="feature-item">
                <el-icon :color="plan.iconColor" :size="14">
                  <Check/>
                </el-icon>
                <span class="feature-text">{{ feature }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>

      <!-- 垂直布局模块 (<835px) -->
      <el-scrollbar v-if="!loading" class="vertical-layout" height="65vh">
        <div class="vertical-plans-container">
          <div v-for="plan in vipPlansConfig" :key="plan.id" 
               :class="[plan.className, { 'current-plan': isCurrentPlan(plan.id) }]"
               class="vertical-plan-card">
            <div v-if="isCurrentPlan(plan.id)" class="current-plan-badge">
              <el-icon :size="14">
                <Star/>
              </el-icon>
              <span>您当前的套餐</span>
            </div>
            <div class="plan-header">
              <h3 class="plan-name">{{ plan.name }}</h3>
              <div class="plan-price">
                <span class="price-number">{{ plan.price }}</span>
                <span class="price-unit">{{ plan.unit }}</span>
              </div>
            </div>
            <div class="plan-button-section">
              <button :class="[plan.buttonClassName, { 'current-button': isCurrentPlan(plan.id) }]"
                      :disabled="isButtonDisabled(plan.id)"
                      class="plan-button"
                      @click="handleSelectPlan(plan.id)">
                {{ isCurrentPlan(plan.id) ? '当前套餐' : (isVipActive && currentVipLevel > 0 && plan.id < currentVipLevel) ? '已拥有更高等级' : '获取VIP' }}
              </button>
            </div>
            <div class="plan-features">
              <div v-for="feature in plan.features" :key="feature" class="feature-item">
                <el-icon :color="plan.iconColor" :size="14">
                  <Check/>
                </el-icon>
                <span class="feature-text">{{ feature }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>

      <!-- 弹窗底部 -->
      <div class="modal-footer">
        <p class="footer-text">选择适合您的会员套餐，解锁更多功能</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 基础弹窗样式 */
.vip-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 20px;
}

.vip-modal-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  max-width: 1200px;
  width: 100%;
  max-height: 90vh;
  overflow: hidden;
  position: relative;
  display: flex;
  flex-direction: column;
}

/* 弹窗头部 */
.modal-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 32px 16px;
  position: relative;
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
  color: #666;
  transition: all 0.2s;
  position: absolute;
  right: 16px;
  top: 55%;
  transform: translateY(-50%);
}

.close-button:hover {
  background-color: #f5f5f5;
  color: #333;
}

/* 公告栏 */
.announcement-banner {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  border: 1px solid #bae6fd;
  border-radius: 8px;
  margin: 12px 24px;
  padding: 8px 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
}

.announcement-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.announcement-text {
  font-size: 14px;
  font-weight: 500;
  color: #0369a1;
  letter-spacing: 0.5px;
}

/* 水平布局模块 (≥835px) */
.horizontal-layout {
  display: block;
  max-height: 60vh;
}

.horizontal-plans-container {
  display: flex;
  flex-direction: row;
  gap: 24px;
  padding: 32px;
  justify-content: center;
  align-items: stretch;
}

.horizontal-plan-card {
  background: white;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  padding: 32px 24px;
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;
  flex: 1;
  min-width: 190px;
  max-width: 320px;
  min-height: 420px;
  display: flex;
  flex-direction: column;
}

.horizontal-plan-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.horizontal-plan-card.free-plan {
  border-color: #909399;
}

.horizontal-plan-card.free-plan:hover {
  border-color: #909399;
}

.horizontal-plan-card.free-plan.current-plan {
  border-color: #909399;
  background: #f4f4f5;
}

.horizontal-plan-card.monthly-plan {
  border-color: #409EFF;
}

.horizontal-plan-card.monthly-plan:hover {
  border-color: #409EFF;
}

.horizontal-plan-card.monthly-plan.current-plan {
  border-color: #409EFF;
  background: #ecf5ff;
}

.horizontal-plan-card.lifetime-plan {
  border-color: #F56C6C;
}

.horizontal-plan-card.lifetime-plan:hover {
  border-color: #F56C6C;
}

.horizontal-plan-card.lifetime-plan.current-plan {
  border-color: #F56C6C;
  background: #fef0f0;
}

/* 垂直布局模块 (<835px) */
.vertical-layout {
  display: none;
  height: 65vh;
}

.vertical-layout .el-scrollbar__wrap {
  overflow-x: hidden;
}

.vertical-layout .el-scrollbar__bar.is-vertical {
  width: 6px;
  right: 2px;
}

.vertical-layout .el-scrollbar__thumb {
  background-color: #c1c1c1;
  border-radius: 3px;
}

.vertical-layout .el-scrollbar__thumb:hover {
  background-color: #a8a8a8;
}

.vertical-plans-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 32px 16px;
  align-items: center;
}

.vertical-plan-card {
  background: white;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  padding: 24px 20px;
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;
  width: 100%;
  max-width: 320px;
  min-height: 380px;
  display: flex;
  flex-direction: column;
}

.vertical-plan-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.vertical-plan-card.free-plan {
  border-color: #909399;
}

.vertical-plan-card.free-plan:hover {
  border-color: #909399;
}

.vertical-plan-card.free-plan.current-plan {
  border-color: #909399;
  background: #f4f4f5;
}

.vertical-plan-card.monthly-plan {
  border-color: #409EFF;
}

.vertical-plan-card.monthly-plan:hover {
  border-color: #409EFF;
}

.vertical-plan-card.monthly-plan.current-plan {
  border-color: #409EFF;
  background: #ecf5ff;
}

.vertical-plan-card.lifetime-plan {
  border-color: #F56C6C;
}

.vertical-plan-card.lifetime-plan:hover {
  border-color: #F56C6C;
}

.vertical-plan-card.lifetime-plan.current-plan {
  border-color: #F56C6C;
  background: #fef0f0;
}

/* 当前套餐标识 */
.current-plan-badge {
  position: absolute;
  top: 0;
  right: 0;
  color: white;
  padding: 6px 12px;
  border-bottom-left-radius: 8px;
  font-size: 12px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.free-plan .current-plan-badge {
  background: #909399;
}

.monthly-plan .current-plan-badge {
  background: #409EFF;
}

.lifetime-plan .current-plan-badge {
  background: #F56C6C;
}

/* 套餐头部 */
.plan-header {
  margin-bottom: 28px;
}

.plan-name {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
}

.plan-price {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.price-number {
  font-size: 32px;
  font-weight: 700;
}

.free-plan .price-number {
  color: #909399;
}

.monthly-plan .price-number {
  color: #409EFF;
}

.lifetime-plan .price-number {
  color: #F56C6C;
}

.price-unit {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

/* 按钮区域 */
.plan-button-section {
  margin-bottom: 28px;
  margin-top: auto;
}

.plan-button {
  width: 100%;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 12px 24px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.plan-button:hover:not(:disabled) {
  opacity: 0.9;
  transform: translateY(-1px);
}

.plan-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.plan-button.current-button {
  background: #67C23A;
}

.free-button {
  background: #909399;
}

.monthly-button {
  background: #409EFF;
}

.lifetime-button {
  background: #F56C6C;
}

/* 功能列表 */
.plan-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
  flex-grow: 1;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.feature-text {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
}

/* VIP信息显示区域 */
.vip-info-section {
  padding: 16px 32px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-bottom: 1px solid #e0e0e0;
}

.vip-info-content {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.vip-status-text {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.vip-type-badge {
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 600;
  color: white;
}

.vip-type-0 {
  background: #909399;
}

.vip-type-1 {
  background: #409EFF;
}

.vip-type-2 {
  background: #F56C6C;
}

.vip-expire-text {
  font-size: 12px;
  color: #999;
  margin-left: auto;
}

/* 加载状态 */
.loading-section {
  padding: 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 50%, #f8f9fa 100%);
  background-size: 200% 200%;
  animation: gradientShift 3s ease-in-out infinite;
  border-bottom: 1px solid #e0e0e0;
  min-height: 540px;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.loading-spinner {
  position: relative;
  width: 50px;
  height: 50px;
}

.spinner-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 3px solid transparent;
  border-radius: 50%;
  animation: spin 2s linear infinite;
}

.spinner-ring:nth-child(1) {
  border-top-color: #409EFF;
  animation-delay: 0s;
}

.spinner-ring:nth-child(2) {
  border-right-color: #67C23A;
  animation-delay: 0.3s;
  width: 80%;
  height: 80%;
  top: 10%;
  left: 10%;
}

.spinner-ring:nth-child(3) {
  border-bottom-color: #F56C6C;
  animation-delay: 0.6s;
  width: 60%;
  height: 60%;
  top: 20%;
  left: 20%;
}

.loading-text {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

.loading-main-text {
  color: #333;
}

.loading-dots {
  display: flex;
  gap: 2px;
}

.dot {
  animation: bounce 1.4s ease-in-out infinite both;
  color: #409EFF;
  font-weight: bold;
}

.dot:nth-child(1) { animation-delay: -0.32s; }
.dot:nth-child(2) { animation-delay: -0.16s; }
.dot:nth-child(3) { animation-delay: 0s; }

.loading-progress {
  width: 100%;
  max-width: 200px;
  height: 3px;
  background: #e0e0e0;
  border-radius: 2px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #409EFF, #67C23A, #F56C6C, #409EFF);
  background-size: 200% 100%;
  border-radius: 2px;
  animation: progressMove 2s ease-in-out infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1.2);
    opacity: 1;
  }
}

@keyframes gradientShift {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes progressMove {
  0% {
    background-position: -200% 0;
    transform: scaleX(0);
  }
  50% {
    transform: scaleX(1);
  }
  100% {
    background-position: 200% 0;
    transform: scaleX(0);
  }
}

/* 弹窗底部 */
.modal-footer {
  padding: 16px 32px 24px;
  text-align: center;
}

.footer-text {
  margin: 0;
  font-size: 14px;
  color: #666;
}

/* 媒体查询 - 布局切换 */
@media (max-width: 834px) {
  .horizontal-layout {
    display: none;
  }

  .vertical-layout {
    display: block;
  }

  .vip-modal-container {
    margin: 10px;
    max-height: 95vh;
  }

  .modal-header {
    padding: 12px 16px 6px;
  }

  .announcement-banner {
    margin: 12px 16px;
    padding: 10px 12px;
  }

  .announcement-text {
    font-size: 13px;
  }

  .vertical-plans-container {
    gap: 12px;
    padding: 24px 16px;
  }

  .vertical-plan-card {
    min-height: 360px;
  }

  .modal-footer {
    padding: 12px 24px 20px;
  }
}

@media (max-width: 480px) {
  .vertical-plans-container {
    padding: 20px 12px;
    gap: 10px;
  }

  .vertical-plan-card {
    padding: 20px 16px;
    min-height: 340px;
  }

  .plan-name {
    font-size: 18px;
  }

  .price-number {
    font-size: 28px;
  }
}
</style>