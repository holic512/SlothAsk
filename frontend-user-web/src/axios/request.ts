import axios from "axios";
import { useSessionStore } from "@/pinia/Session";

const request = axios.create({
    baseURL: "http://localhost:8081/",
    timeout: 10000,
    headers: {
        "Content-Type": "application/json",
    },
});

request.interceptors.request.use(
    (config) => {
        const session = useSessionStore();
        if (session?.userSession?.tokenValue) {
            config.headers["SlothAsk"] = session.userSession.tokenValue;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

request.interceptors.response.use(
    (response) => response.data,
    (error) => {
        console.error("请求错误：", error);
        return Promise.reject(error);
    }
);

export default request;