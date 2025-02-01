import { defineStore } from 'pinia'

export const useProjectStore = defineStore('project', {
    state: () => ({
        projects: [
            {
                id: 1001,
                sort_order: 1,
                project_name: '2024用户满意度调查',
                creator_id: 'admin001',
                status: 1,
                create_time: '2024-03-20 10:00:00',
                update_time: '2024-03-20 10:00:00'
            },
            {
                id: 1002,
                sort_order: 2,
                project_name: '产品功能反馈问卷',
                creator_id: 'admin002',
                status: 1,
                create_time: '2024-03-19 14:30:00',
                update_time: '2024-03-19 15:45:00'
            },
            {
                id: 1003,
                sort_order: 3,
                project_name: '员工培训需求调研',
                creator_id: 'admin001',
                status: 0,
                create_time: '2024-03-18 09:15:00',
                update_time: '2024-03-18 16:20:00'
            }
        ],
        loading: false,
        currentProject: null
    }),

    actions: {
        // 获取项目列表
        async fetchProjects() {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // const response = await api.get('/projects')
                // this.projects = response.data
            } catch (error) {
                console.error('获取项目列表失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        // 创建项目
        async createProject(project) {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // const response = await api.post('/projects', project)
                const newProject = {
                    ...project,
                    id: Date.now(),
                    create_time: new Date().toLocaleString('zh-CN'),
                    update_time: new Date().toLocaleString('zh-CN')
                }
                this.projects.unshift(newProject)
                return newProject
            } catch (error) {
                console.error('创建项目失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        // 更新项目
        async updateProject(project) {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // await api.put(`/projects/${project.id}`, project)
                const index = this.projects.findIndex(p => p.id === project.id)
                if (index !== -1) {
                    this.projects[index] = {
                        ...this.projects[index],
                        ...project,
                        update_time: new Date().toLocaleString('zh-CN')
                    }
                }
            } catch (error) {
                console.error('更新项目失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        // 删除项目
        async deleteProject(id) {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // await api.delete(`/projects/${id}`)
                const index = this.projects.findIndex(p => p.id === id)
                if (index !== -1) {
                    this.projects.splice(index, 1)
                }
            } catch (error) {
                console.error('删除项目失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        // 批量删除项目
        async batchDeleteProjects(ids) {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // await api.post('/projects/batch-delete', { ids })
                this.projects = this.projects.filter(p => !ids.includes(p.id))
            } catch (error) {
                console.error('批量删除项目失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        // 设置当前项目
        setCurrentProject(project) {
            this.currentProject = project
        }
    },

    getters: {
        // 获取排序后的项目列表
        sortedProjects: (state) => (order = 'asc') => {
            return [...state.projects].sort((a, b) => {
                return order === 'asc'
                    ? a.sort_order - b.sort_order
                    : b.sort_order - a.sort_order
            })
        },

        // 根据关键字搜索项目
        searchProjects: (state) => (keyword) => {
            if (!keyword) return state.projects
            const loweredKeyword = keyword.toLowerCase()
            return state.projects.filter(project =>
                project.project_name.toLowerCase().includes(loweredKeyword) ||
                project.creator_id.toLowerCase().includes(loweredKeyword)
            )
        }
    }
})