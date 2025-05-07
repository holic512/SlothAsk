import axios from '@/axios/axios';

// /**
//  * 获取项目下所有分类（不分页）
//  * @param projectId 项目ID
//  * @returns API响应
//  */
// export function getCategoriesByProjectId(projectId: number) {
//   return axios.get(`service-question/user/questionBank/categoriesByProjectId/${projectId}`);
// }

/**
 * 分页获取项目下的分类
 * @param projectId 项目ID
 * @param page 页码
 * @param pageSize 每页条数
 * @returns API响应
 */
export function getCategoriesByProjectIdPaged(projectId: number, page: number = 1, pageSize: number = 12) {
  return axios.get(`service-question/user/questionBank/categoriesByProjectIdPaged/${projectId}`, {
    params: {
      page,
      pageSize
    }
  });
}

/**
 * 获取分类详情
 * @param categoryId 分类ID
 * @returns API响应
 */
export function getCategoryById(categoryId: number) {
  return axios.get(`service-question/user/questionBank/categoryById/${categoryId}`);
}