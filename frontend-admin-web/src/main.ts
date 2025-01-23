import { createApp } from 'vue'
import './style.css'

import App from './App.vue'
import router from './router/router.js'

const app = createApp(App)

// 配置 element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
app.use(ElementPlus)

// 配置 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// pinia
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
const pinia = createPinia()
app.use(pinia)
pinia.use(piniaPluginPersistedstate)


app.use(router)
app.mount('#app')