<template>
  <div class="github-callback">
    <h2>正在处理 GitHub 授权，请稍候...</h2>
    <div>{{ message }}</div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from '@/axios/axios.js'
import {useSessionStore} from '@/pinia/Session.js'
import {useRoute, useRouter} from 'vue-router'

const router = useRouter()
const route = useRoute()

const message = ref('')

/**
 * 登录处理器
 */
const handleLogin = async (code) => {
  const response = await axios.get('/service-user/oauth/github/login', {
    params: {code, state: 'login'}
  })
  const result = response.data

  if (result.status === 200) {
    message.value = '登录成功，欢迎使用 SlothAsk！'
    const store = useSessionStore()
    store.setSession(result.data)

    const redirectPath = Array.isArray(route.query.redirect)
        ? route.query.redirect[0]
        : (route.query.redirect || '/')
    await router.push(redirectPath)
  } else {
    message.value = `登录失败：${result.message || '未知错误'}`
  }
}

/**
 * 绑定处理器
 */
const handleBind = async (code) => {
  const response = await axios.post('/service-user/user/account/bindGithub',
      null,
      {
        params: {code, state: 'bind'}
      })
  const result = response.data

  if (result.status === 200) {
    message.value = 'GitHub 账号绑定成功！'
    await router.push('/account/settings')
  } else {
    message.value = `绑定失败：${result.message || '未知错误'}`
  }
}

/**
 * 处理器映射表
 */
const handlers = {
  login: handleLogin,
  bind: handleBind
}

onMounted(async () => {
  const params = new URLSearchParams(window.location.search)
  const code = params.get('code')
  const state = params.get('state')

  if (!code || !state) {
    message.value = '缺少 GitHub 回调参数，无法继续。'
    return
  }

  const handler = handlers[state]
  if (!handler) {
    message.value = '无效的 state 参数，无法识别操作类型。'
    return
  }

  try {
    await handler(code)
  } catch (error) {
    message.value = '网络异常，请稍后重试。'
    console.error(error)
  }
})
</script>

<style scoped>
</style>
