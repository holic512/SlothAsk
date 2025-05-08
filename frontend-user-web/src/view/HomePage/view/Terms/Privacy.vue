<script setup>
import {onMounted} from 'vue';
import {setTitle} from '@/utils/title';

onMounted(() => {
  setTitle('隐私政策');
});
</script>

<template>
  <div class="privacy-container">
    <div class="privacy-content">
      <div class="privacy-header">
        <h1>SlothAsk 隐私政策</h1>
        <p class="update-date">最近更新日期：2024年5月15日</p>
      </div>

      <section class="privacy-section">
        <h2>1. 引言</h2>
        <p>SlothAsk（以下简称"我们"）非常重视您的隐私保护，特别是您的个人信息安全。本隐私政策适用于我们提供的所有产品和服务。本政策描述了我们如何收集、使用、存储、共享和保护您的个人信息，以及您如何访问、更新、控制和保护您的个人信息。</p>
        <p>在使用我们的服务前，请您务必仔细阅读并了解本隐私政策的全部内容。一旦您选择使用或继续使用我们的服务，即表示您同意本隐私政策的全部内容并授权我们按本政策收集、使用、存储和共享您的相关信息。</p>
      </section>

      <section class="privacy-section">
        <h2>2. 我们收集的信息</h2>
        <p>为了向您提供服务，我们可能会收集以下类型的信息：</p>
        
        <h3>2.1 您提供的信息</h3>
        <ul>
          <li>注册信息：当您注册SlothAsk账号时，我们会收集您提供的账号信息，包括但不限于邮箱地址、用户名和密码等。</li>
          <li>个人资料：您可以选择向我们提供更多个人资料，如头像、性别、职业等信息。</li>
          <li>用户内容：您在平台上发布、上传、分享的内容，包括您的提问、回答、评论等。</li>
        </ul>
        
        <h3>2.2 设备和日志信息</h3>
        <ul>
          <li>设备信息：我们可能会收集您使用我们服务的设备相关信息，包括设备型号、操作系统版本、浏览器类型等。</li>
          <li>日志信息：当您使用我们的服务时，我们可能会自动收集服务日志信息，包括您的搜索查询内容、IP地址、浏览器类型、访问日期和时间、访问的页面等。</li>
          <li>位置信息：我们可能会收集与您设备相关的位置信息，以便为您提供更好的服务体验。</li>
        </ul>
        
        <h3>2.3 Cookie和类似技术</h3>
        <p>我们使用Cookie和类似技术收集和存储您的偏好设置、服务使用信息，以实现更好的用户体验和服务改进。您可以通过浏览器设置管理Cookie，但这可能会影响您使用我们服务的某些功能。</p>
      </section>

      <section class="privacy-section">
        <h2>3. 信息的使用</h2>
        <p>我们收集您的个人信息主要用于以下目的：</p>
        <ul>
          <li>提供、维护和改进我们的服务；</li>
          <li>响应您的请求、问题和反馈；</li>
          <li>开发新功能和优化服务体验；</li>
          <li>进行安全监测和防止欺诈行为；</li>
          <li>进行数据分析，以便我们了解和改进服务质量；</li>
          <li>根据法律法规的要求或政府机关的强制命令进行信息披露；</li>
          <li>向您推送可能感兴趣的学习资源和服务。</li>
        </ul>
      </section>

      <section class="privacy-section">
        <h2>4. 信息的共享</h2>
        <p>我们严格保护您的个人信息，不会将其出售给第三方。在以下情况下，我们可能会共享您的个人信息：</p>
        <ul>
          <li>经您事先同意的情况下；</li>
          <li>根据法律法规的要求、强制性的行政执法或司法要求；</li>
          <li>与我们的关联公司共享，他们会遵守本隐私政策；</li>
          <li>与我们的服务提供商和合作伙伴共享，以便他们为我们提供服务，但他们只能在有限的目的内访问和使用这些信息；</li>
          <li>为了保护我们的权利、财产或安全，以及我们的用户或公众的权利、财产或安全；</li>
          <li>如果我们涉及合并、收购或资产出售，您的个人信息可能会作为此类交易的一部分被转移。</li>
        </ul>
      </section>

      <section class="privacy-section">
        <h2>5. 信息安全</h2>
        <p>我们致力于保护您的个人信息安全。我们采取各种安全技术和程序，防止您的信息遭到未经授权的访问、使用或泄露。包括但不限于：</p>
        <ul>
          <li>使用加密技术保护数据传输过程；</li>
          <li>采用严格的数据访问控制机制；</li>
          <li>定期安全评估和内部审计；</li>
          <li>对员工进行数据保护培训。</li>
        </ul>
        <p>尽管我们采取了上述措施，但请您理解，由于技术的限制和可能存在的各种恶意手段，即使我们竭尽所能加强安全措施，也不可能始终保证信息的百分之百安全。</p>
      </section>

      <section class="privacy-section">
        <h2>6. 您的权利</h2>
        <p>根据适用的法律法规，您可能享有以下权利：</p>
        <ul>
          <li>访问权：您有权访问我们持有的与您相关的个人信息。</li>
          <li>更正权：如果您认为我们持有的与您相关的个人信息不准确或不完整，您有权要求我们更正或补充。</li>
          <li>删除权：在特定情况下，您有权要求我们删除您的个人信息。</li>
          <li>限制处理权：在特定情况下，您有权要求我们限制对您个人信息的处理。</li>
          <li>反对权：在特定情况下，您有权反对我们处理您的个人信息。</li>
          <li>数据可携带权：在技术可行的前提下，您有权以结构化、常用的、机器可读的格式获取您提供给我们的个人信息，并有权将这些信息传输给另一个数据控制者。</li>
        </ul>
        <p>如您需要行使上述权利，请通过本政策末尾提供的联系方式与我们联系。我们将在合理的时间内回复您的请求。</p>
      </section>

      <section class="privacy-section">
        <h2>7. 儿童信息保护</h2>
        <p>我们的服务主要面向成人用户。如果您是18岁以下的未成年人，在使用我们的服务前，请确保您已经取得了父母或其他法定监护人的同意。</p>
        <p>如果我们发现自己在未事先获得可证实的父母同意的情况下收集了儿童的个人信息，我们会尽快删除相关数据。</p>
      </section>

      <section class="privacy-section">
        <h2>8. 隐私政策的更新</h2>
        <p>我们可能会不时更新本隐私政策。当我们对本政策进行重大变更时，我们会在网站上发布通知，并在政策顶部更新"最近更新日期"。</p>
        <p>我们鼓励您定期查看本隐私政策，以了解我们如何保护您的信息。您继续使用我们的服务将视为您接受这些更新。</p>
      </section>

      <section class="privacy-section">
        <h2>9. 联系我们</h2>
        <p>如果您对本隐私政策有任何问题、意见或建议，请通过以下方式联系我们：</p>
        <p>电子邮件：privacy@slothask.com</p>
        <p>我们会尽快回复您的请求。</p>
      </section>
    </div>
  </div>
</template>

<style scoped>
.privacy-container {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.privacy-content {
  width: 100%;
  max-width: 800px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 40px;
}

.privacy-header {
  margin-bottom: 40px;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.privacy-header h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
  color: #333;
}

.update-date {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.privacy-section {
  margin-bottom: 30px;
}

.privacy-section h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}

.privacy-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 16px 0 12px;
  color: #444;
}

.privacy-section p {
  line-height: 1.7;
  margin-bottom: 12px;
  font-size: 15px;
  color: #444;
}

.privacy-section ul {
  padding-left: 20px;
  margin-bottom: 16px;
}

.privacy-section li {
  margin-bottom: 8px;
  line-height: 1.6;
  font-size: 15px;
  color: #444;
}

@media (max-width: 768px) {
  .privacy-content {
    padding: 30px 20px;
  }
  
  .privacy-header h1 {
    font-size: 24px;
  }
  
  .privacy-section h2 {
    font-size: 18px;
  }
  
  .privacy-section h3 {
    font-size: 16px;
  }
  
  .privacy-section p,
  .privacy-section li {
    font-size: 14px;
  }
}
</style> 