import { defineStore } from 'pinia'

export const useCreatorStore = defineStore('creator', {
    state: () => ({
        creators: {
            'admin001': {
                id: 'admin001',
                name: '张三',
                role: '系统管理员',
                department: '技术部',
                email: 'zhangsan@example.com',
                phone: '13800138001'
            },
            'admin002': {
                id: 'admin002',
                name: '李四',
                role: '项目经理',
                department: '产品部',
                email: 'lisi@example.com',
                phone: '13800138002'
            }
        },
        loading: false
    }),

    actions: {
        // 获取创建者信息
        async fetchCreator(id) {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // const response = await api.get(`/creators/${id}`)
                // this.creators[id] = response.data
            } catch (error) {
                console.error('获取创建者信息失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        // 获取所有创建者列表
        async fetchCreators() {
            try {
                this.loading = true
                // TODO: 调用后端 API
                // const response = await api.get('/creators')
                // this.creators = response.data.reduce((acc, creator) => {
                //   acc[creator.id] = creator
                //   return acc
                // }, {})
            } catch (error) {
                console.error('获取创建者列表失败:', error)
                throw error
            } finally {
                this.loading = false
            }
        }
    },

    getters: {
        // 根据ID获取创建者信息
        getCreatorById: (state) => (id) => {
            return state.creators[id] || null
        },

        // 获取创建者列表
        creatorList: (state) => {
            return Object.values(state.creators)
        }
    }
})