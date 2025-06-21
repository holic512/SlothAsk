<template>
  <footer class="footer">
    <div class="footer-container">
      <div class="footer-left">
        <div class="footer-logo">
          <img alt="Intrepid Logo" class="logo-img" src="/HomePage/logo.ico" />
          <div class="logo-text">
            <p class="logo-text-small">Powered by</p>
            <p class="logo-text-large">SlothAsk</p>
          </div>
        </div>
        <div class="policy-links">
          <a class="privacy-link" href="/privacy">隐私政策</a>
          <span class="separator">|</span>
          <a class="privacy-link" href="/terms">用户协议</a>
        </div>
        <p class="copyright-text">
          Copyright © 2025 sloth.online | Copyright © 2025 sloth.online, LLC
        </p>
      </div>

      <div class="footer-links">
        <div class="link-group">
          <span class="disabled-link">首页</span>
          <span class="disabled-link">关于我们</span>
          <span class="disabled-link">作者团队</span>
        </div>
        <div class="link-group">
          <span class="disabled-link">投稿指南</span>
          <span class="disabled-link">编辑标准</span>
          <span class="disabled-link">可持续发展</span>
        </div>
        <div class="link-group">
          <span class="disabled-link">联系我们</span>
          <span class="disabled-link">订阅邮件</span>
        </div>
        <div class="link-group">
          <span class="disabled-link">爱心支持</span>
          <span class="disabled-link">站内搜索</span>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer',
};
</script>

<style scoped>
.footer {
  background-color: #1e262c;
  color: #a8b2b9;
  padding: clamp(1.5rem, 3vw, 2.5rem) clamp(1rem, 2vw, 2rem);
  width: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
}

.footer-container {
  width: 100%;
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding: 0 clamp(0.5rem, 2vw, 1rem);
  box-sizing: border-box;
}

@media (min-width: 768px) {
  .footer-container {
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
    flex-wrap: wrap;
  }
}

.footer-left {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo-img {
  width: clamp(2rem, 5vw, 4rem);
  height: clamp(2rem, 5vw, 4rem);
  object-fit: contain;
}

.logo-text {
  display: flex;
  flex-direction: column;
  justify-content: center;
  line-height: 1.2;
}

.logo-text-small {
  color: #ffffff;
  font-size: clamp(0.8rem, 1vw, 1.2rem);
  margin-top: 0;
  margin-bottom: 0.1rem;
  line-height: 1;
}

.logo-text-large {
  color: #ffffff;
  font-size: clamp(1.2rem, 1.7vw, 1.7rem);
  font-weight: 900;
  line-height: 1;
  margin-top: 0.1rem;
  margin-bottom: 0;
}

.policy-links {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.privacy-link {
  font-size: 0.75rem;
  font-weight: 600;
  color: #5da9dd;
  text-decoration: none;
}

.privacy-link:hover {
  text-decoration: underline;
}

.separator {
  color: #a8b2b9;
  font-size: 0.75rem;
}

.copyright-text {
  font-size: 0.7rem;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  font-size: 0.8rem;
  padding: 0.5rem;
  width: 100%;
  box-sizing: border-box;
}

@media (min-width: 768px) {
  .footer-links {
    grid-template-columns: repeat(4, 1fr);
    max-width: 800px;
  }
}

.link-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.link-group a {
  color: #b8c2c8;
  text-decoration: none;
  font-size: clamp(0.75rem, 0.9vw, 0.9rem);
}

.link-group a:hover {
  text-decoration: underline;
}

.disabled-link {
  color: #6c757d;
  font-size: clamp(0.75rem, 0.9vw, 0.9rem);
  cursor: not-allowed;
  opacity: 0.6;
}

@media (max-width: 480px) {
  .footer-container {
    padding: 1rem 0.5rem;
  }
  .footer-links {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
  .link-group {
    gap: 0.3rem;
  }
  .link-group a {
    font-size: 0.75rem;
  }
  .logo-text-small {
    font-size: 0.8rem;
  }
  .logo-text-large {
    font-size: 1.2rem;
  }
}

</style>
