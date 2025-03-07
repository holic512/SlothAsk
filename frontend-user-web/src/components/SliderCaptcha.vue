<template>
  <!-- 使用Teleport将整个验证码组件传送到body下，确保遮罩层能覆盖全屏 -->
  <Teleport to="body">
    <!-- 添加遮罩层，点击遮罩层关闭验证码 -->
    <div class="slider-captcha-mask" @click="closeModal"></div>

    <!-- 验证码主容器 -->
    <div class="slider-captcha-container" @contextmenu.prevent>
      <!-- 验证码头部 -->
      <div class="slider-captcha-header">
        <span>请完成安全验证</span>
        <div class="header-actions">
          <el-icon class="refresh-icon" @click="refreshCaptcha">
            <Refresh/>
          </el-icon>
          <!-- 添加关闭按钮 -->
          <el-icon class="close-icon" @click="closeModal">
            <Close/>
          </el-icon>
        </div>
      </div>

      <!-- 验证码图片区域 -->
      <div class="slider-captcha-body" ref="captchaContainer">
        <!-- 背景图片 -->
        <div class="background-container">
          <img :src="'data:image/' + backgroundImageType + ';base64,' + backgroundImage" alt="背景图片"
               class="background-image" draggable="false" @contextmenu.prevent/>
        </div>

        <!-- 滑块 -->
        <div
            class="slider-block"
            :style="{ left: `${sliderPosition}px`, top: `${sliderYPosition}px` }"
            v-show="backgroundImage && sliderImage"
        >
          <img :src="'data:image/' + sliderImageType + ';base64,' + sliderImage" alt="滑块" class="slider-image"
               draggable="false" @contextmenu.prevent/>
        </div>
      </div>

      <!-- 滑动条区域 -->
      <div class="slider-bar-container">
        <div class="slider-bar">
          <!-- 滑动按钮 -->
          <div
              class="slider-button"
              @mousedown="startDrag"
              @touchstart="startTouchDrag"
              :style="{ left: `${sliderBarPosition}px` }"
              :class="{ 'success': verificationSuccess, 'fail': verificationFail }"
          >
            <el-icon v-if="verificationSuccess">
              <Check/>
            </el-icon>
            <el-icon v-else-if="verificationFail">
              <Close/>
            </el-icon>
            <el-icon v-else>
              <Right/>
            </el-icon>
          </div>
          <!-- 滑动进度条 -->
          <div class="slider-progress" :style="{ width: `${sliderBarPosition}px` }"></div>
          <!-- 提示文字 -->
          <div class="slider-text" v-if="!isDragging && !verificationSuccess">
            向右滑动完成验证
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
/**
 * 滑块验证码组件
 * 
 * 功能：
 * 1. 显示背景图片和滑块，用户通过拖动滑块完成验证
 * 2. 支持鼠标和触摸屏操作
 * 3. 防止图片被拖拽保存或右键保存
 * 4. 支持点击遮罩层、关闭按钮或按ESC键关闭
 * 5. 验证成功后回调父组件
 * 
 * 使用方法：
 * <SliderCaptcha 
 *   :on-success="handleSuccess" 
 *   :on-close="handleClose"
 * />
 */
import {ref, onMounted, computed, onBeforeUnmount} from 'vue';
import {Check, Close, Right, Refresh} from '@element-plus/icons-vue';
import {ElMessage} from 'element-plus';
import axios from '@/axios/axios';

// ===== 验证码数据 =====
/** 滑块图片Base64数据 */
const sliderImage = ref('');
/** 滑块图片类型 */
const sliderImageType = ref('png');
/** 背景图片Base64数据 */
const backgroundImage = ref('');
/** 背景图片类型 */
const backgroundImageType = ref('jpeg');
/** 滑块Y轴位置 */
const targetYPosition = ref(0);

// ===== 滑块位置状态 =====
/** 滑块在图片上的X轴位置 */
const sliderPosition = ref(0);
/** 滑块在图片上的Y轴位置 */
const sliderYPosition = ref(0);
/** 滑动条上滑块的位置 */
const sliderBarPosition = ref(0);
/** 是否正在拖动中 */
const isDragging = ref(false);
/** 验证是否成功 */
const verificationSuccess = ref(false);
/** 验证是否失败 */
const verificationFail = ref(false);
/** 验证码容器DOM引用 */
const captchaContainer = ref<HTMLElement | null>(null);

/** 滑动条的最大宽度 */
let maxSliderBarWidth = 0;

/**
 * 计算滑块的缩放比例
 * 根据容器宽度与原始图片宽度的比例计算
 */
const sliderScale = computed(() => {
  if (!captchaContainer.value) return 1;
  const containerWidth = captchaContainer.value.clientWidth;
  // 假设背景图原始宽度为 320px (调整后的尺寸)
  return containerWidth / 320;
});

/**
 * 组件初始化
 * 加载验证码并添加事件监听
 */
onMounted(() => {
  // 加载验证码
  loadCaptcha();

  // 添加全局事件监听
  window.addEventListener('mousemove', onDrag);
  window.addEventListener('mouseup', stopDrag);
  window.addEventListener('touchmove', onTouchDrag, {passive: false});
  window.addEventListener('touchend', stopTouchDrag);

  // 禁用整个文档的图片拖拽功能
  document.addEventListener('dragstart', preventDrag);

  // 添加ESC键关闭功能
  document.addEventListener('keydown', handleKeyDown);
});

/**
 * 组件销毁时清理
 * 移除所有事件监听器
 */
onBeforeUnmount(() => {
  window.removeEventListener('mousemove', onDrag);
  window.removeEventListener('mouseup', stopDrag);
  window.removeEventListener('touchmove', onTouchDrag);
  window.removeEventListener('touchend', stopTouchDrag);

  // 移除拖拽防护
  document.removeEventListener('dragstart', preventDrag);

  // 移除ESC键监听
  document.removeEventListener('keydown', handleKeyDown);
});

/**
 * 处理键盘事件
 * 当按下ESC键时关闭验证码
 */
const handleKeyDown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') {
    closeModal();
  }
};

/**
 * 关闭验证码模态框
 * 调用父组件传入的关闭方法
 */
const closeModal = () => {
  props.onClose?.();
};

/**
 * 防止图片被拖拽
 * 阻止默认的拖拽行为
 */
const preventDrag = (e: DragEvent) => {
  const target = e.target as HTMLElement;
  if (target.tagName === 'IMG') {
    e.preventDefault();
  }
};

/** 验证码唯一标识符 */
let captchaUid: string;

/**
 * 加载验证码
 * 从服务器获取验证码图片和相关数据
 */
const loadCaptcha = async () => {
  try {
    const response = await axios.get("service-user/user/captcha/captcha");

    if (response.data.status === 200) {
      const data = response.data.data;
      // 设置图片数据
      sliderImage.value = data.sliderImageBase64;
      sliderImageType.value = data.sliderImageType || 'png';
      backgroundImage.value = data.backgroundImageBase64;
      backgroundImageType.value = data.backgroundImageType || 'jpeg';
      targetYPosition.value = data.yPosition;
      
      // 保存验证码唯一标识
      captchaUid = data.captchaUid;

      // 重置状态
      sliderPosition.value = 0;
      sliderBarPosition.value = 0;
      verificationSuccess.value = false;
      verificationFail.value = false;

      // 设置初始Y位置
      sliderYPosition.value = targetYPosition.value * sliderScale.value;

      // 计算滑动条最大宽度
      if (captchaContainer.value) {
        maxSliderBarWidth = captchaContainer.value.clientWidth - 40; // 减去滑块按钮的宽度
      }
    } else {
      ElMessage.error('获取验证码失败');
    }
  } catch (error) {
    console.error('获取验证码出错:', error);
    ElMessage.error('获取验证码出错');
  }
};

/**
 * 刷新验证码
 * 重新加载新的验证码
 */
const refreshCaptcha = () => {
  loadCaptcha();
};

/**
 * 开始鼠标拖动
 * 初始化拖动状态
 */
const startDrag = (e: MouseEvent) => {
  if (verificationSuccess.value) return;

  e.preventDefault();
  isDragging.value = true;
  verificationFail.value = false;
};

/**
 * 鼠标拖动中
 * 计算并更新滑块位置
 */
const onDrag = (e: MouseEvent) => {
  if (!isDragging.value) return;

  // 计算新位置
  const containerRect = captchaContainer.value?.getBoundingClientRect();
  if (!containerRect) return;

  // 计算滑块在滑动条上的位置
  let newPosition = e.clientX - containerRect.left - 20; // 20 是滑块按钮宽度的一半

  // 限制范围
  newPosition = Math.max(0, Math.min(newPosition, maxSliderBarWidth));

  // 直接更新位置，不使用过渡效果
  updateSliderPosition(newPosition);
};

/**
 * 停止鼠标拖动
 * 验证滑块位置是否正确
 */
const stopDrag = () => {
  if (!isDragging.value) return;

  isDragging.value = false;
  verifyPosition();
};

/**
 * 开始触摸拖动
 * 初始化触摸拖动状态
 */
const startTouchDrag = (e: TouchEvent) => {
  if (verificationSuccess.value) return;

  e.preventDefault();
  isDragging.value = true;
  verificationFail.value = false;
};

/**
 * 触摸拖动中
 * 计算并更新滑块位置
 */
const onTouchDrag = (e: TouchEvent) => {
  if (!isDragging.value) return;

  // 阻止默认行为，防止页面滚动
  e.preventDefault();

  // 获取触摸点
  const touch = e.touches[0];

  // 计算新位置
  const containerRect = captchaContainer.value?.getBoundingClientRect();
  if (!containerRect) return;

  // 计算滑块在滑动条上的位置
  let newPosition = touch.clientX - containerRect.left - 20; // 20 是滑块按钮宽度的一半

  // 限制范围
  newPosition = Math.max(0, Math.min(newPosition, maxSliderBarWidth));

  // 直接更新位置，不使用过渡效果
  updateSliderPosition(newPosition);
};

/**
 * 停止触摸拖动
 * 验证滑块位置是否正确
 */
const stopTouchDrag = () => {
  if (!isDragging.value) return;

  isDragging.value = false;
  verifyPosition();
};

/**
 * 更新滑块位置
 * 根据滑动条位置计算滑块在图片上的位置
 * @param newPosition 新的滑动条位置
 */
const updateSliderPosition = (newPosition: number) => {
  // 更新位置
  sliderBarPosition.value = newPosition;

  // 计算滑块在图片上的位置
  // 根据滑动条位置比例计算滑块在图片上的位置
  const containerRect = captchaContainer.value?.getBoundingClientRect();
  if (!containerRect) return;

  const ratio = newPosition / maxSliderBarWidth;
  const maxSliderPosition = containerRect.width - 50; // 50 是滑块宽度
  sliderPosition.value = ratio * maxSliderPosition;
};

/**
 * 验证滑块位置是否正确
 * 将位置信息发送到服务器进行验证
 */
const verifyPosition = async () => {
  try {
    // 验证位置是否正确，将滑块位置发送到后端进行验证
    const response = await axios.post(
      "service-user/user/captcha/validate",
      {
        validateUid: captchaUid,  // 验证码唯一标识符
        validateX: Math.round(sliderPosition.value),  // 四舍五入取整数位置值
      }
    );

    if (response.data.status === 200) {
      // 验证成功
      verificationSuccess.value = true;
      ElMessage.success('验证成功');

      // 调用父组件的成功回调，传递验证码唯一标识
      props.onSuccess(captchaUid);
    } else {
      // 验证失败
      verificationFail.value = true;
      ElMessage.error(response.data.message || '验证失败，请重试');

      // 失败动画后重置状态并刷新验证码
      setTimeout(() => {
        sliderPosition.value = 0;
        sliderBarPosition.value = 0;
        verificationFail.value = false;
      }, 1000);

      refreshCaptcha();
    }
  } catch (error) {
    // 请求异常处理
    console.error('验证请求失败:', error);
    verificationFail.value = true;
    ElMessage.error('网络异常，请稍后重试');
    
    // 失败动画后重置状态
    setTimeout(() => {
      sliderPosition.value = 0;
      sliderBarPosition.value = 0;
      verificationFail.value = false;
    }, 1000);
    
    // 刷新验证码
    refreshCaptcha();
  }
};

/**
 * 组件属性定义
 */
const props = defineProps<{
  /** 验证成功回调函数，参数为验证码唯一标识 */
  onSuccess: (uid: string) => void;
  /** 关闭验证码回调函数 */
  onClose?: () => void;
}>();

</script>

<style scoped>
/* 遮罩层样式 */
.slider-captcha-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.1);
  z-index: 9998;
}

/* 验证码容器样式 */
.slider-captcha-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999;
  width: 100%;
  max-width: 320px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  user-select: none;
  margin: 0 auto;
  animation: captchaFadeIn 0.3s ease;
}

/* 验证码淡入动画 */
@keyframes captchaFadeIn {
  from {
    opacity: 0;
    transform: translate(-50%, -48%);
  }
  to {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

/* 验证码头部样式 */
.slider-captcha-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  border-bottom: 1px solid #f0f0f0;
}

/* 头部按钮容器 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* 刷新和关闭按钮样式 */
.refresh-icon, .close-icon {
  cursor: pointer;
  color: #909399;
  transition: all 0.3s;
}

/* 刷新按钮悬停效果 */
.refresh-icon:hover {
  color: #409EFF;
  transform: rotate(180deg);
}

/* 关闭按钮悬停效果 */
.close-icon:hover {
  color: #F56C6C;
  transform: scale(1.1);
}

/* 验证码图片区域样式 */
.slider-captcha-body {
  position: relative;
  width: 100%;
  height: 160px;
  overflow: hidden;
}

/* 背景图片容器 */
.background-container {
  width: 100%;
  height: 100%;
}

/* 背景图片样式 */
.background-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  -webkit-user-drag: none;
  -khtml-user-drag: none;
  -moz-user-drag: none;
  -o-user-drag: none;
  user-drag: none;
  pointer-events: none;
}

/* 滑块样式 */
.slider-block {
  position: absolute;
  top: 0;
  left: 0;
  /* 移除transition，使拖动更加流畅 */
}

/* 滑块图片样式 */
.slider-image {
  width: 50px;
  height: 50px;
  object-fit: contain;
  -webkit-user-drag: none;
  -khtml-user-drag: none;
  -moz-user-drag: none;
  -o-user-drag: none;
  user-drag: none;
  pointer-events: none;
}

/* 滑动条容器样式 */
.slider-bar-container {
  padding: 15px;
}

/* 滑动条样式 */
.slider-bar {
  position: relative;
  width: 100%;
  height: 40px;
  background-color: #f5f5f5;
  border-radius: 20px;
}

/* 滑动按钮样式 */
.slider-button {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  background-color: #fff;
  border-radius: 50%;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 2;
  transition: background-color 0.3s;
  /* 移除位置的transition，保留颜色变化的transition */
}

/* 验证成功状态 */
.slider-button.success {
  background-color: #67C23A;
  color: white;
}

/* 验证失败状态 */
.slider-button.fail {
  background-color: #F56C6C;
  color: white;
}

/* 滑动进度条样式 */
.slider-progress {
  position: absolute;
  top: 0;
  left: 0;
  height: 40px;
  background-color: rgba(64, 158, 255, 0.2);
  border-radius: 20px;
  z-index: 1;
}

/* 提示文字样式 */
.slider-text {
  position: absolute;
  width: 100%;
  text-align: center;
  line-height: 40px;
  color: #909399;
  font-size: 14px;
}
</style>  