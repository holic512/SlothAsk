<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3 class="section-title" @click="toggleSection(0)">
          SlothAsk
          <span :class="{ 'active': activeSections[0] }" class="toggle-icon">+</span>
        </h3>
        <ul :class="{ 'active': activeSections[0] }">
          <li><a href="#">面试</a></li>
          <li><a href="#">考研</a></li>
          <li><a href="#">考公</a></li>
          <li><a href="#">考编</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h3 class="section-title" @click="toggleSection(1)">
          服务
          <span :class="{ 'active': activeSections[1] }" class="toggle-icon">+</span>
        </h3>
        <ul :class="{ 'active': activeSections[1] }">
          <li><a href="#">帮助中心</a></li>
          <li><a href="#">最新功能</a></li>
          <li><a href="#">服务条款</a></li>
          <li><a href="#">开发者中心</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h3 class="section-title" @click="toggleSection(2)">
          商务
          <span :class="{ 'active': activeSections[2] }" class="toggle-icon">+</span>
        </h3>
        <ul :class="{ 'active': activeSections[2] }">
          <li><a href="#">社区合作</a></li>
          <li><a href="#">活动</a></li>
          <li><a href="#">产品推广</a></li>
          <li><a href="#">校园合作</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h3 class="section-title" @click="toggleSection(3)">
          联系我们
          <span :class="{ 'active': activeSections[3] }" class="toggle-icon">+</span>
        </h3>
        <ul :class="{ 'active': activeSections[3] }">
          <li><a href="#">电话</a></li>
          <li><a href="#">邮箱</a></li>
          <li><a href="#">公众号</a></li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <div class="">
        <a href="#">© 2025 SlothAsk</a>
      </div>
    </div>
  </footer>
</template>

<script setup>
import {ref} from 'vue';

// 控制各部分展开/折叠的状态
const activeSections = ref([true, false, false, false]);
const isMobile = ref(window.innerWidth <= 768);

// 监听窗口大小变化，更新移动端状态
window.addEventListener('resize', () => {
  isMobile.value = window.innerWidth <= 768;
  // 在非移动端时展开所有部分
  if (!isMobile.value) {
    activeSections.value = [true, true, true, true];
  } else {
    // 移动端时只展开第一部分
    activeSections.value = [true, false, false, false];
  }
});

// 初始化
if (!isMobile.value) {
  activeSections.value = [true, true, true, true];
}

// 切换部分展开/折叠
const toggleSection = (index) => {
  if (isMobile.value) {
    activeSections.value[index] = !activeSections.value[index];
  }
};
</script>

<style scoped>
.footer {
  background-color: #1a1a1a;
  color: #ffffff;
  padding: 40px 0 20px;
  width: 100%;
  box-sizing: border-box;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  padding: 0 20px;
}

.footer-section {
  margin-bottom: 30px;
  min-width: 200px;
  flex: 1 1 auto;
  padding: 0 15px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: default;
}

.toggle-icon {
  display: none;
}

.footer-section h3 {
  font-size: 16px;
  margin-bottom: 20px;
  color: #ffffff;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 500px;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section ul li a {
  color: #999999;
  text-decoration: none;
  transition: color 0.3s ease;
  display: inline-block;
  padding: 3px 0;
}

.footer-section ul li a:hover {
  color: #ffffff;
}

.footer-bottom {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #333333;
}

.footer-bottom a {
  color: #666666;
  text-decoration: none;
  margin: 5px 0;
  font-size: 14px;
  display: inline-block;
  padding: 5px;
}

@media (max-width: 992px) {
  .footer-section {
    min-width: 150px;
    flex: 1 1 30%;
  }
}

@media (max-width: 768px) {
  .footer {
    padding: 20px 0 10px;
  }
  
  .footer-content {
    flex-direction: column;
    align-items: stretch;
    text-align: left;
    padding: 0 10px;
  }

  .footer-section {
    margin-bottom: 10px;
    width: 100%;
    flex: none;
    padding: 0 5px;
    border-bottom: 1px solid #333333;
  }
  
  .section-title {
    cursor: pointer;
    padding: 10px 0;
    margin-bottom: 0;
  }
  
  .toggle-icon {
    display: inline-block;
    font-size: 18px;
    transition: transform 0.3s ease;
  }
  
  .toggle-icon.active {
    transform: rotate(45deg);
  }
  
  .footer-section h3 {
    margin-bottom: 0;
    font-size: 15px;
  }
  
  .footer-section ul {
    max-height: 0;
    opacity: 0;
  }
  
  .footer-section ul.active {
    max-height: 500px;
    opacity: 1;
    padding: 5px 0 10px;
  }
  
  .footer-section ul li {
    margin-bottom: 8px;
  }
  
  .footer-section ul li a {
    padding: 5px 0;
    font-size: 14px;
  }
  
  .footer-bottom {
    margin-top: 15px;
    padding-top: 10px;
  }
  
  .footer-bottom a {
    font-size: 12px;
    padding: 3px;
  }
}

@media (max-width: 480px) {
  .footer {
    padding: 10px 0 5px;
  }
  
  .footer-section {
    margin-bottom: 5px;
    padding: 0;
  }
  
  .footer-section h3 {
    font-size: 14px;
  }
  
  .footer-section ul li a {
    font-size: 13px;
    padding: 4px 0;
  }
  
  .footer-content {
    padding: 0 8px;
  }
  
  .footer-bottom {
    margin-top: 10px;
    padding-top: 8px;
  }
}
</style>