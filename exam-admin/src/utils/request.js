import axios from 'axios'
import router from "@/router"
import {
    ElMessage
} from 'element-plus'
const request = axios.create({
    baseURL: "/api", //全局设置/api
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    //判断是否存在token，如果存在将每个页面header都添加token
    if (localStorage.getItem("token")) {
        config.headers['token'] = localStorage.getItem("token")
    }

    //判断是否存在手机号，如果存在将每个页面header都添加手机号
    if (sessionStorage.getItem("phone")) {
        config.headers['phone'] = sessionStorage.getItem("phone")
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request