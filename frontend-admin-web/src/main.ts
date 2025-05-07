import {createApp} from 'vue'
import './style.css'

import App from './App.vue'
import router from './router/router.js'
// 配置 element-plus
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'

// 配置 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// pinia
import {createPinia} from 'pinia'
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

const pinia = createPinia()
app.use(pinia)
pinia.use(piniaPluginPersistedstate)


app.use(router)
app.mount('#app')