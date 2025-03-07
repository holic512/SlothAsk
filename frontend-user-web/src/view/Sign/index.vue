<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const isAgreeTerms = ref(false);

// 处理协议同意状态
const handleTermsChange = (value) => {
  isAgreeTerms.value = value;
};

// 跳转到用户协议页面
const goToTerms = () => {
  // TODO: 实现跳转到用户协议页面
};

// 跳转到隐私政策页面
const goToPrivacy = () => {
  // TODO: 实现跳转到隐私政策页面
};
</script>

<template>
  <div class="sign-container">
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="sign-content">
      <!-- Logo -->
      <div class="logo-container">
        <img src="./public/image-removebg-preview.png" alt="Logo" class="logo"/>
        <h2 class="welcome-text">欢迎来到 SlothAsk</h2>
        <p class="slogan">高效解决问题，轻松学习编程</p>
      </div>

      <!-- 主要内容区域 -->
      <div class="main-content">
        <router-view></router-view>
      </div>

      <!-- 第三方登录 -->
      <div class="third-party-login">
        <div class="third-party-icons">
          <div class="third-party-icon github">
            <i class="fab fa-github"></i>
          </div>
          <div class="third-party-icon google">
            <i class="fab fa-google"></i>
          </div>
          <div class="third-party-icon wechat">
            <i class="fab fa-weixin"></i>
          </div>
        </div>
      </div>

      <!-- 用户协议 -->
      <div class="terms-agreement">
        <el-text>注册或登录即代表您同意</el-text>
        <a @click.prevent="goToTerms" href="#">《用户协议》</a>
        <el-text>和</el-text>
        <a @click.prevent="goToPrivacy" href="#">《隐私政策》</a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.sign-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow: hidden;
  user-select: none;
}

.background-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  animation: float 8s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -50px;
  background: linear-gradient(45deg, rgba(66, 133, 244, 0.1), rgba(66, 133, 244, 0.2));
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  left: -50px;
  background: linear-gradient(45deg, rgba(234, 67, 53, 0.1), rgba(234, 67, 53, 0.2));
  animation-delay: 2s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 15%;
  background: linear-gradient(45deg, rgba(251, 188, 5, 0.1), rgba(251, 188, 5, 0.2));
  animation-delay: 4s;
}

@keyframes float {
  0% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
  100% {
    transform: translateY(0) rotate(0deg);
  }
}

.sign-content {
  width: 360px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.18);
  z-index: 1;
}



.logo-container {
  text-align: center;
  margin-bottom: 24px;
}

.logo {
  width: 120px;
  height: auto;
  transition: transform 0.3s ease;
  
  border-radius: 4px;
}

.logo:hover {
  transform: scale(1.05);
}

.welcome-text {
  font-size: 28px;
  color: #2c3e50;
  margin: 0;
  font-weight: 600;
  margin-bottom: 8px;
}

.slogan {
  color: #7f8c8d;
  font-size: 16px;
  margin-top: 0;
}

.main-content {
  margin-bottom: 30px;
}

.third-party-login {
  margin-top: 24px;
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  color: #606266;
  margin: 16px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  border-top: 1px solid #dcdfe6;
}

.divider span {
  padding: 0 16px;
  font-size: 14px;
}

.third-party-icons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 16px;
}

.third-party-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 20px;
  color: white;
}

.third-party-icon:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.github {
  background: #333;
}

.google {
  background: #DB4437;
}

.wechat {
  background: #07C160;
}

.terms-agreement {
  margin-top: 24px;
  text-align: center;
  font-size: 14px;
}

.terms-agreement a {
  color: #409eff;
  text-decoration: none;
  transition: color 0.3s ease;
}

.terms-agreement a:hover {
  color: #66b1ff;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sign-content {
    width: 90%;
    padding: 30px;
  }
  
  .logo {
    width: 48px;
  }
  
  .welcome-text {
    font-size: 24px;
  }
}
</style>