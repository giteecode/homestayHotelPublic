import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/router/permission.ts'
// import '@/router/permission.js'
import store from './store'
// 全局css样式
import '@/assets/css/global.css'

// 引入element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 暗黑模式
import 'element-plus/theme-chalk/dark/css-vars.css'

const app = createApp(App)
//全局注册elementIcon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
declare const window: any;
window.router=router
app.use(router).use(store).use(ElementPlus, {locale: zhCn,size:'small'}).mount('#app')

// createApp(App).use(router).use(store).use(ElementPlus, {locale: zhCn,size:'small'}).mount('#app')


// 解决element-plus icon不生效
// const app = createApp(App)
// for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
//     app.component(key, component)
// }