/**
 * File Name: ApiGetHotQuestions.js
 * Description: 获取热门题目的API服务
 * Author: holic512
 * Created Date: 2025-01-20
 * Version: 1.0
 * Usage:
 * 调用后端接口获取浏览量最高的前10道题目
 */

import axios from "@/axios/axios";

/**
 * 获取热门题目列表
 * @returns {Promise} 返回包含热门题目数据的Promise
 */
export const apiGetHotQuestions = async () => {
      const response = await axios.get(
        "service-question/user/study/hotQuestions"
    )
    return response.data
};