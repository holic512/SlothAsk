import { defineStore } from 'pinia'

export const useProjectStore = defineStore('project', {
    state: () => ({
        projects: [],
        currentProject: null,
        loading: false
    }),

    actions: {
        async fetchProjects() {
            // 获取项目列表
        },
        async createProject(project) {
            // 创建项目
        },
        async updateProject(project) {
            // 更新项目
        },
        async deleteProject(id) {
            // 删除项目
        }
    },

    getters: {
        sortedProjects: (state) => {
            return [...state.projects].sort((a, b) => a.sort_order - b.sort_order)
        }
    }
}) 