import axios from "@/axios/axios";
import { Project } from "../types/ProjectType";



// 新增项目
export const ApiAddProject = async (project: Project) => {
    const response = await axios.post(
        'service-question/admin/project/add',
        {
            projectName: project.name,
            projectDescription: project.description,
            sort: project.sortOrder,
            status: project.status
        }
    );
    return response.data;
}

// 更新项目
export const ApiUpdateProject = async (project: Project) => {
    const response = await axios.put(
        'service-question/admin/project/edit',
        {
            id: project.id,
            projectName: project.name,
            projectDescription: project.description,
            sort: project.sortOrder,
            status: project.status
        }
    );
    return response.data;
}

// 删除项目
export const ApiDeleteProject = async (id: number) => {
    const response = await axios.delete(
        `service-question/admin/project/delete/${id}`
    );
    return response.data;
}

// 批量删除项目
export const ApiBatchDeleteProjects = async (ids: number[]) => {
    const response = await axios.delete(
        'service-question/admin/project/delete',
        { data: ids }
    );
    return response.data;
} 