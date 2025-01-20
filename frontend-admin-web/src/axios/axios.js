import axios from "axios";

const instance = axios.create({
    // baseURL: `${window.location.origin}/api/`,  // 根据当前域名动态设置 baseURL
    baseURL: 'http://localhost:8081/', // 这里是你的基础 URL
    timeout: 10000, // 可选的超时时间
    headers: {"Content-Type": "application/json"}
});

// 创建请求拦截器
instance.interceptors.request.use(config => {


    return config;
}, error => {
    return Promise.reject(error);
});


export default instance;