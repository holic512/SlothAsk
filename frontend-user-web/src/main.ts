import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
// 配置 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 配置路由
import router from "./router/router.js";
// 配置 element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// pinia
import {createPinia} from 'pinia'
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
// 国际化
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(router)

const pinia = createPinia()
app.use(pinia)
pinia.use(piniaPluginPersistedstate)

app.use(ElementPlus, {
    locale: zhCn,
})

app.mount('#app')