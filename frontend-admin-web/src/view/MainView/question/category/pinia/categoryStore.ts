import {defineStore} from 'pinia'
import type {ICategory} from '../types/types'
import {apiGetCategoryList} from '../service/ApiGetCategoryList'

interface CategoryState {
    searchKeyword: string
    searchProjectId: number | null
    searchStatus: number | null
    pageNum: number
    pageSize: number
    total: number
    tableData: ICategory[]
    selectedRows: ICategory[]
    isFormVisible: boolean
    drawerVisible: boolean
    currentCategory: ICategory | null
    formType: 'add' | 'edit'
    projectOptions: IProjectOption[]
    loading: boolean
}

interface IProjectOption {
    id: number
    name: string
    description: string
}

export const useCategoryStore = defineStore('category', {
    state: (): CategoryState => ({
        searchKeyword: '',
        searchProjectId: null,
        searchStatus: null,
        pageNum: 1,
        pageSize: 10,
        total: 0,
        tableData: [],
        selectedRows: [],
        isFormVisible: false,
        drawerVisible: false,
        currentCategory: null,
        formType: 'add',

        // 项目列表
        projectOptions: [],
        loading: false
    }),

    actions: {
        resetSearchForm() {
            this.searchKeyword = ''
            this.searchProjectId = null
            this.searchStatus = null
            this.pageNum = 1
        },

        openAddForm() {
            this.formType = 'add'
            this.currentCategory = null
            this.isFormVisible = true
        },

        openEditForm(category: any) {
            this.formType = 'edit'
            this.currentCategory = category
            this.isFormVisible = true
        },

        openDetailDrawer(category: any) {
            this.currentCategory = category
            this.drawerVisible = true
        },

        resetList() {
            this.searchKeyword = '';
            this.searchProjectId = null;
            this.searchStatus = null;
            this.pageNum = 1;
            this.pageSize = 10;
        },

        setLoading(value: boolean) {
            this.loading = value
        },
        setProjectOptions(options: IProjectOption[]) {
            this.projectOptions = options
        },

        async loadCategoryList() {
            this.setLoading(true)
            try {
                const response = await apiGetCategoryList({
                    keyword: this.searchKeyword,
                    projectId: this.searchProjectId,
                    status: this.searchStatus,
                    pageNum: this.pageNum,
                    pageSize: this.pageSize
                })

                if (response.status === 200) {
                    this.tableData = response.data.list
                    this.total = response.data.total
                }
            } catch (error) {
                console.error('加载分类列表失败:', error)
            } finally {
                this.setLoading(false)
            }
        }
    }
}) 