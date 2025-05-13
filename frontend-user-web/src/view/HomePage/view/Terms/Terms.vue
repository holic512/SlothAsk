<script setup>
import {onMounted} from 'vue';
import {setTitle} from '@/utils/title';

onMounted(() => {
  setTitle('用户协议');
});
</script>

<template>
  <div class="terms-container">
    <div class="terms-content">
      <div class="terms-header">
        <h1>SlothAsk 用户协议</h1>
        <p class="update-date">最近更新日期：2024年5月15日</p>
      </div>

      <section class="terms-section">
        <h2>1. 协议的接受与修改</h2>
        <p>欢迎您使用SlothAsk（"本平台"）提供的服务。本协议是您与SlothAsk之间关于您使用本平台服务所订立的协议。在您注册成为本平台用户前，请您仔细阅读本协议，您点击"注册"或"登录"按钮后，即视为您已阅读并同意本协议的全部内容。</p>
        <p>本平台有权在必要时修改本协议条款。您可以在相关页面中查阅最新版本的协议条款。本协议条款变更后，如果您继续使用本平台提供的服务，即视为您已接受修改后的协议。</p>
      </section>

      <section class="terms-section">
        <h2>2. 账号注册与安全</h2>
        <p>2.1 注册资格：您确认，在您完成注册程序或以其他方式使用本平台时，您应当具有中华人民共和国法律规定的与您行为相适应的民事行为能力。若您不具备前述与您行为相适应的民事行为能力，则您及您的监护人应依照法律规定承担相应的责任。</p>
        <p>2.2 注册信息：您保证您在注册和使用本平台时提供的个人资料真实、准确、完整、合法有效，且保证这些资料在使用本平台服务期间持续有效。如因您提供的个人资料不实或不准确导致您在使用本平台服务过程中产生任何损失，由您自行承担。</p>
        <p>2.3 用户名规则：注册用户名须满足以下要求：</p>
        <ul>
          <li>长度要求：4～20 个字符（含英文、数字、下划线）；</li>
          <li>允许字符：英文字母（大小写不限）、数字、下划线 (<code>_</code>)；</li>
          <li>必须以英文字母开头；</li>
          <li>仅限上述字符，不得包含空格或其他特殊符号；</li>
          <li>用户名在本平台需保持唯一，不得重复。</li>
        </ul>
        <p>2.4 账号安全：您需要对您的账号和密码保密，且需对您在该账号下的所有活动（包括但不限于信息披露、发布信息、网上点击同意或提交等）承担责任。若您发现任何非法使用您账号或存在安全漏洞的情况，请立即联系我们。</p>
      </section>

      <section class="terms-section">
        <h2>3. 用户行为规范</h2>
        <p>3.1 您在使用本平台服务时，必须遵守中华人民共和国相关法律法规的规定，不得利用本平台服务从事违法违规行为，包括但不限于以下行为：</p>
        <ul>
          <li>发布、传送、传播、储存违反国家法律法规禁止的内容；</li>
          <li>发布、传送、传播、储存侵害他人知识产权、商业秘密等合法权利的内容；</li>
          <li>恶意注册、使用、操作账号，或者进行破坏、干扰正常服务的行为；</li>
          <li>未经本平台授权使用爬虫等技术批量获取平台数据；</li>
          <li>恶意使用系统漏洞获取平台资源；</li>
          <li>其他违反法律法规、社会公德、社会公共利益或可能损害本平台和其他用户合法权益的行为。</li>
        </ul>
        <p>3.2 本平台有权对违反上述规定的内容予以删除，并视情节严重程度对违规账号处以包括但不限于警告、限制或禁止使用部分或全部功能、账号封禁直至注销的处罚，并保留追究相关法律责任的权利。</p>
      </section>

      <section class="terms-section">
        <h2>4. 知识产权</h2>
        <p>4.1 本平台所包含的所有内容，包括但不限于文本、图形、标志、按钮图标、图像、音频片段、数字下载、数据编辑和软件，均为本平台或其内容提供者的财产，受中国和国际版权法的保护。</p>
        <p>4.2 未经本平台事先书面许可，您不得以任何方式使用、复制、修改、传播、分发、复制或公开展示上述内容。</p>
        <p>4.3 您在本平台上发布的内容，您保证对该等内容享有合法权利。您在本平台上发布内容的同时，即视为您授予本平台一项全球性的、免费的、非独家的、可转让的、可再许可的、永久的使用该等内容的权利。</p>
      </section>

      <section class="terms-section">
        <h2>5. 服务变更、中断或终止</h2>
        <p>5.1 您理解并同意，本平台提供的服务是按照现有技术和条件所能达到的现状提供的。本平台会尽最大努力保持服务的连贯性和稳定性，但不能保证其服务不会中断，也不能保证您所使用服务的实时性、安全性和准确性。</p>
        <p>5.2 本平台保留随时变更、中断或终止服务的权利。本平台行使变更、中断或终止服务的权利，不需对您或任何第三方负责。</p>
      </section>

      <section class="terms-section">
        <h2>6. 免责声明</h2>
        <p>6.1 您理解并同意，本平台不对因下述任何情况而导致的任何损害赔偿承担责任，包括但不限于利润、商誉、使用、数据等方面的损失或其他无形损失的损害赔偿：</p>
        <ul>
          <li>本平台服务的使用或无法使用；</li>
          <li>通过本平台购买或获取的任何产品、数据、信息或服务；</li>
          <li>您的传输或数据遭到未获授权的访问或变更；</li>
          <li>任何第三方在本平台上发布或提供的声明或行为；</li>
          <li>其他与本平台服务相关的事宜。</li>
        </ul>
        <p>6.2 本平台对任何第三方通过本平台发送服务或包含在服务中的任何内容不承担责任。</p>
      </section>

      <section class="terms-section">
        <h2>7. 法律适用与争议解决</h2>
        <p>7.1 本协议适用中华人民共和国大陆地区法律。</p>
        <p>7.2 因本协议引起的或与本协议有关的任何争议，各方应友好协商解决；协商不成的，任何一方均有权将有关争议提交至本平台运营者住所地有管辖权的人民法院诉讼解决。</p>
      </section>

      <section class="terms-section">
        <h2>8. 其他条款</h2>
        <p>8.1 本协议构成您与本平台之间的完整协议，规范您对本平台服务的使用行为。</p>
        <p>8.2 本协议部分条款无效或不可执行的，不影响其他条款的效力。</p>
        <p>8.3 本平台对本协议拥有最终解释权。</p>
      </section>
    </div>
  </div>
</template>

<style scoped>
.terms-container {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.terms-content {
  width: 100%;
  max-width: 800px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 40px;
}

.terms-header {
  margin-bottom: 40px;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.terms-header h1 {
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

.terms-section {
  margin-bottom: 30px;
}

.terms-section h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}

.terms-section p {
  line-height: 1.7;
  margin-bottom: 12px;
  font-size: 15px;
  color: #444;
}

.terms-section ul {
  padding-left: 20px;
  margin-bottom: 16px;
}

.terms-section li {
  margin-bottom: 8px;
  line-height: 1.6;
  font-size: 15px;
  color: #444;
}

@media (max-width: 768px) {
  .terms-content {
    padding: 30px 20px;
  }

  .terms-header h1 {
    font-size: 24px;
  }

  .terms-section h2 {
    font-size: 18px;
  }

  .terms-section p,
  .terms-section li {
    font-size: 14px;
  }
}
</style>
