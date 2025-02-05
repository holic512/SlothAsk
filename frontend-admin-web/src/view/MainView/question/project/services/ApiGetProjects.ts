// 获取项目列表
import axios from "@/axios/axios";

export const ApiGetProjects = async (params: {
    search?: string,
    sortType?: number,
    pageNum?: number,
    pageSize?: number
}) => {
    const response = await axios.get(
        'service-question/admin/project/list',
        {
            params: {
                search: params.search,
                sortType: params.sortType,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            }
        }
    );
    return response.data;
}