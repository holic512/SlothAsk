<script setup>
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {setTitle} from "@/utils/title";
import {generateGithubAuthUrl} from "@/view/Sign/service/useGithubAuth";

const router = useRouter();
const isAgreeTerms = ref(false);

// 处理协议同意状态
const handleTermsChange = (value) => {
  isAgreeTerms.value = value;
};

// 跳转到用户协议页面
const goToTerms = () => {
  router.push('/terms');
};

// 跳转到隐私政策页面
const goToPrivacy = () => {
  router.push('/privacy');
};

onMounted(() => {
  setTitle('登录');
});

function redirectToGitHub() {
  window.location.href = generateGithubAuthUrl()
}

</script>

<template>
  <div class="sign-container">
    <div class="background-decoration">
      <!-- 增强的背景装饰元素 -->
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
      <div class="circle circle-4"></div>
      <div class="circle circle-5"></div>

      <!-- 添加几何图形元素 -->
      <div class="geometric-shape shape-1"></div>
      <div class="geometric-shape shape-2"></div>
      <div class="geometric-shape shape-3"></div>

      <!-- 添加波浪背景 -->
      <div class="wave-container">
        <div class="wave wave-1"></div>
        <div class="wave wave-2"></div>
      </div>

      <!-- 添加格子装饰 -->
      <div class="grid-decoration"></div>
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
        <div class="third-party-icons" @click="redirectToGitHub">
          <img alt="GitHub" class="third-party-icon " src="./public/github.png"/>
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
  background: linear-gradient(135deg, #e9f5ff 0%, #f0f7ff 100%);
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
  background: rgba(64, 158, 255, 0.05);
  backdrop-filter: blur(10px);
  animation: float 8s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -50px;
  background: rgba(64, 158, 255, 0.06);
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  left: -50px;
  background: rgba(64, 158, 255, 0.08);
  animation-delay: 2s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 15%;
  background: rgba(64, 158, 255, 0.05);
  animation-delay: 4s;
}

.circle-4 {
  width: 100px;
  height: 100px;
  top: 20%;
  right: 20%;
  background: rgba(64, 158, 255, 0.07);
  animation-delay: 1s;
  animation-duration: 6s;
}

.circle-5 {
  width: 80px;
  height: 80px;
  bottom: 20%;
  right: 10%;
  background: rgba(64, 158, 255, 0.06);
  animation-delay: 3s;
  animation-duration: 10s;
}

/* 几何图形装饰 */
.geometric-shape {
  position: absolute;
  opacity: 0.06;
  background: #409eff;
  animation: rotate 20s infinite linear;
}

.shape-1 {
  width: 120px;
  height: 120px;
  top: 15%;
  left: 5%;
  transform: rotate(45deg);
  clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%);
  animation-delay: 0s;
  animation-duration: 25s;
}

.shape-2 {
  width: 150px;
  height: 150px;
  bottom: 10%;
  right: 5%;
  clip-path: polygon(50% 0%, 100% 38%, 82% 100%, 18% 100%, 0% 38%);
  animation-delay: 2s;
  animation-duration: 30s;
}

.shape-3 {
  width: 100px;
  height: 100px;
  top: 40%;
  right: 45%;
  clip-path: polygon(25% 0%, 75% 0%, 100% 50%, 75% 100%, 25% 100%, 0% 50%);
  animation-delay: 4s;
  animation-duration: 35s;
}

/* 波浪效果 */
.wave-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  overflow: hidden;
}

.wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 200%;
  height: 100%;
  background-repeat: repeat-x;
  opacity: 0.05;
}

.wave-1 {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z' fill='%23409EFF'/%3E%3C/svg%3E");
  background-size: 1200px 100px;
  animation: wave 25s linear infinite;
  z-index: 1;
}

.wave-2 {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 120' preserveAspectRatio='none'%3E%3Cpath d='M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z' fill='%23409EFF'/%3E%3C/svg%3E");
  background-size: 1200px 100px;
  animation: wave 20s linear infinite;
  animation-delay: -5s;
  opacity: 0.03;
  z-index: 0;
}

/* 格子装饰 */
.grid-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(transparent 95%, rgba(64, 158, 255, 0.05) 1px),
  linear-gradient(90deg, transparent 95%, rgba(64, 158, 255, 0.05) 1px);
  background-size: 30px 30px;
  opacity: 0.4;
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

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes wave {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}

.sign-content {
  width: 360px;
  padding: 32px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(64, 158, 255, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  z-index: 1;
  transition: all 0.3s ease;
}

.sign-content:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 50px rgba(64, 158, 255, 0.2);
}

.logo-container {
  text-align: center;
  margin-bottom: 24px;
}

.logo {
  width: 120px;
  height: auto;
  margin-bottom: 12px;
}

.welcome-text {
  font-size: 22px;
  color: #1a1a1a;
  margin: 0;
  font-weight: 600;
  margin-bottom: 4px;
  letter-spacing: -0.5px;
}

.slogan {
  color: #666;
  font-size: 14px;
  margin: 0;
  font-weight: 400;
}

.main-content {
  margin-bottom: 24px;
}

.third-party-login {
  margin-top: 4px;
  padding-top: 4px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
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
  border-top: 1px solid rgba(0, 0, 0, 0.08);
}

.divider span {
  padding: 0 16px;
  font-size: 14px;
}

.third-party-icons {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 16px;
}

.third-party-icon {
  width: 28px;
  height: 28px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 18px;
  color: white;

}

.third-party-icon:hover {
  transform: translateY(-2px);

  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}


.terms-agreement {
  margin-top: 20px;
  text-align: center;
  font-size: 13px;
  color: #666;
  line-height: 1.5;
}

.terms-agreement a {
  color: #1a1a1a;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s ease;
  border-bottom: 1px solid transparent;
}

.terms-agreement a:hover {
  border-bottom-color: #1a1a1a;
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
    font-size: 22px;
  }
}
</style>