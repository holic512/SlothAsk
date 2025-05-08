<template>
  <div class="github-callback">
    <h2>正在登录，请稍候...</h2>
    <div>{{ message }}</div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from "@/axios/axios.js";
import {useSessionStore} from "@/pinia/Session";
import {useRoute, useRouter} from "vue-router";

const router = useRouter();
const route = useRoute();

const message = ref('')

onMounted(async () => {
  const params = new URLSearchParams(window.location.search)
  const code = params.get('code')
  const state = params.get('state')

  if (!code || !state) {
    message.value = '缺少 GitHub 登录回调参数，无法完成登录。'
    return
  }

  try {
    const response = await axios.get(
        'service-user/oauth/github/callback',
        {
          params: {
            'code': code,
            'state': state
          },
        }
    )


    const result = response.data

    if (result.status === 200) {
      message.value = `欢迎您，${result}！`
      // 保存token
      const store = useSessionStore();
      store.setSession(result.data);

      // 获取重定向地址，如果没有则跳转到首页
      const redirectPath = Array.isArray(route.query.redirect)
          ? route.query.redirect[0]
          : (route.query.redirect || '/');

      // 确保redirectPath不为null
      await router.push(redirectPath ? redirectPath : '/');


    } else {
      message.value = `登录失败：${result.message || '未知错误'}`
    }
  } catch (err) {
    message.value = '网络异常，请稍后重试。'
    console.error(err)
  }
})
</script>

<style scoped>
.github-callback {
  padding: 2rem;
  font-family: Arial, sans-serif;
}
</style>
