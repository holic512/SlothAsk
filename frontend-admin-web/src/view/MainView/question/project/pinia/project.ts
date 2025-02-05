import {defineStore} from 'pinia'
import {ref, watch} from 'vue'
import type {Project} from '../types/ProjectType'
import {
    ApiAddProject,
    ApiUpdateProject,
    ApiDeleteProject,
    ApiBatchDeleteProjects
} from '../services/ApiProject'
import {ElMessage} from 'element-plus'
import {ApiGetProjects} from "@/view/MainView/question/project/services/ApiGetProjects";

export const useProjectStore = defineStore('project', () => {
    // 状态定义
    const projects = ref<Project[]>([])
    const loading = ref(false)
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const searchKeyword = ref('')
    const sortType = ref(0)

    // actions
    const setSearchKeyword = (keyword: string) => {
        searchKeyword.value = keyword
        currentPage.value = 1 // 重置页码
    }

    const setSortType = (type: number) => {
        sortType.value = type
        currentPage.value = 1 // 重置页码
    }

    // 获取表单列表
    const fetchProjects = async () => {
        try {
            loading.value = true
            const response = await ApiGetProjects({
                search: searchKeyword.value,
                sortType: sortType.value,
                pageNum: currentPage.value,
                pageSize: pageSize.value
            })

            
            if (response.status === 200 && response.data) {
                projects.value = response.data.list
                total.value = response.data.total
            } else {
                ElMessage.error(response.message || '获取项目列表失败')
            }
        } catch (error) {
            console.error('获取项目列表失败:', error)
            ElMessage.error('获取项目列表失败')
        } finally {
            loading.value = false
        }
    }

    // 监听分页组件的变化
    watch(currentPage, () => {
        fetchProjects()
    })

    watch(pageSize, () => {
        currentPage.value = 1 // 切换每页条数时重置为第一页
        fetchProjects()
    })

    // 创建项目
    const createProject = async (project: Project) => {
        try {
            loading.value = true
            const response = await ApiAddProject(project)

            if (response.status === 200) {
                ElMessage.success('创建项目成功')
                await fetchProjects()
                return true
            } else {
                ElMessage.error(response.message || '创建项目失败')
                return false
            }
        } catch (error) {
            console.error('创建项目失败:', error)
            ElMessage.error('创建项目失败')
            return false
        } finally {
            loading.value = false
        }
    }

    // 更新项目
    const updateProject = async (project: Project) => {
        try {
            loading.value = true
            const response = await ApiUpdateProject(project)
            if (response.status === 200) {
                ElMessage.success('更新项目成功')
                await fetchProjects()
                return true
            } else {
                ElMessage.error(response.message || '更新项目失败')
                return false
            }
        } catch (error) {
            console.error('更新项目失败:', error)
            ElMessage.error('更新项目失败')
            return false
        } finally {
            loading.value = false
        }
    }

    // 删除项目
    const deleteProject = async (id: number) => {
        try {
            loading.value = true
            const response = await ApiDeleteProject(id)
            console.log(response)
            if (response.status === 200) {
                ElMessage.success('删除项目成功')

                currentPage.value = 1 // 重置页码
                await fetchProjects()

                return true
            } else {
                ElMessage.error(response.message || '删除项目失败')
                return false
            }
        } catch (error) {
            console.error('删除项目失败:', error)
            ElMessage.error('删除项目失败')
            return false
        } finally {
            loading.value = false
        }
    }

    // 批量删除项目
    const batchDeleteProjects = async (ids: number[]) => {
        try {
            loading.value = true
            const response = await ApiBatchDeleteProjects(ids)
            if (response.status === 200) {
                ElMessage.success('批量删除成功')

                currentPage.value = 1 // 重置页码
                await fetchProjects()
                return true
            } else {
                ElMessage.error(response.message || '批量删除失败')
                return false
            }
        } catch (error) {
            console.error('批量删除项目失败:', error)
            ElMessage.error('批量删除失败')
            return false
        } finally {
            loading.value = false
        }
    }

    return {
        // 状态
        projects,
        loading,
        total,
        currentPage,
        pageSize,
        searchKeyword,
        sortType,

        // actions
        setSearchKeyword,
        setSortType,
        fetchProjects,
        createProject,
        updateProject,
        deleteProject,
        batchDeleteProjects
    }
}) 