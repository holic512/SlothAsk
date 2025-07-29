import axios from "axios";
import {useSessionStore} from "@/pinia/Session";

// api环境变量
const baseUrl = import.meta.env.VITE_API_BASE_URL;

const instance = axios.create({
    baseURL: baseUrl, // 这里是你的基础 URL

    timeout: 5000, // 可选的超时时间
    headers: {"Content-Type": "application/json"}
});

// 创建请求拦截器
instance.interceptors.request.use(config => {

    const Session = useSessionStore();

    if (Session){
        config.headers["SlothAsk"] = Session.userSession.tokenValue;
    }

    return config;
}, error => {
    return Promise.reject(error);
});


export default instance;