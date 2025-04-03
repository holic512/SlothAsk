import {defineStore} from "pinia";
import {ref} from "vue";
import {getProject} from "../service/ApiGetProject"
import {Project} from "../interface/ProjectInterface"

export const useProjectStore = defineStore('project', () => {
    const projects = ref<Project[]>([]);
    // 获取项目列表
    const fetchProjects = async () => {
        const response = await getProject();
        projects.value = response.data.data;
    };

    return {
        projects,
        fetchProjects
    };
});