import {createApp} from 'vue'
import './style.css'
import App from './App.vue'


const app = createApp(App)


// 配置 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 配置路由
import router from "./router/router.js";
app.use(router)

// 配置 element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

app.use(ElementPlus)
app.mount('#app')