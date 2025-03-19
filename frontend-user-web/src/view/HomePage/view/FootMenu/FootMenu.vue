<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3>SlothAsk</h3>
        <ul>
          <li><a href="#">面试</a></li>
          <li><a href="#">考研</a></li>
          <li><a href="#">考公</a></li>
          <li><a href="#">考编</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h3>服务</h3>
        <ul>
          <li><a href="#">帮助中心</a></li>
          <li><a href="#">最新功能</a></li>
          <li><a href="#">服务条款</a></li>
          <li><a href="#">开发者中心</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h3>商务</h3>
        <ul>
          <li><a href="#">社区合作</a></li>
          <li><a href="#">活动</a></li>
          <li><a href="#">产品推广</a></li>
          <li><a href="#">校园合作</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h3>联系我们</h3>
        <ul>
          <li><a href="#">电话</a></li>
          <li><a href="#">邮箱</a></li>
          <li><a href="#">公众号</a></li>
        </ul>
      </div>

    </div>
    <div class="footer-bottom">
      <div class="">
        <a href="#">© 2024 SlothAsk</a>
      </div>

      <div>
        <a href="#">ICP备案号：xxx</a>
      </div>

    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
  background-color: #1a1a1a;
  color: #ffffff;
  padding: 40px 0 20px;
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
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section ul li a {
  color: #999999;
  text-decoration: none;
  transition: color 0.3s ease;
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
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .footer-section {
    margin-bottom: 40px;
  }

}
</style>