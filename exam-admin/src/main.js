import {
  createApp
} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import './assets/css/globle.css'
// 引入全局签名加密法
import JsEncrypt from 'jsencrypt'
// 引入图标
import './assets/icon/iconfont.css'
import SlideVerify from 'vue-monoplasty-slide-verify';
//定义加密方法
const encryption = (obj) => {
  let encrypt = new JsEncrypt()
  // 公钥
  encrypt.setPublicKey(
    'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoKqtNC8+tYWNAUHN9DENskzRiq4AY8geY8JrFXWw0A6lFCbLie/KcoD78ZJ8QFpQWlx5fHy5tuB+3GfdB829CYK8FljmElsqvIj5q4NQGnBrSU9UUZuzRA85YsQ5fBmwJZTi9lYg+chIFkXwKIgW22PyWu4sIjwKdLP+q0EJBQRYJ4R/YTrVyG8Hme1DEt/4iD+885mjgY2v7yYz5NmhCV4sZC0sZFMnBPidLs5wS49JZ4/YuKuMwtwRT2r9/o3sV6VNNUZtVYBz8fl5URQtJ4yxI3hv9uJAzulOnXMy8UYwBVG0b0sE4ExF+ylWXmuUwKbupZ9asjsHJPZTGUUwtQIDAQAB'
  )
  return encrypt.encrypt(obj)
}
const app = createApp(App)
// 添加到全局中
app.config.globalProperties.$encryption = encryption
app.use(store)
app.use(router)
app.use(SlideVerify)
app.use(ElementPlus)
app.mount('#app')