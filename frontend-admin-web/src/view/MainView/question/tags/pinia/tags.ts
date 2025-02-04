import {defineStore} from 'pinia'
import type {TagCategory} from '../types/TagCategoryType'
import {ref} from "vue";

// 功能接口
interface TagState {
    tags: TagCategory[]
    loading: boolean
    currentTag: TagCategory | null
    // 分页相关
    total: number
    currentPage: number
    pageSize: number
    // 排序相关
    sortBy: 'id' | 'sort_order' | 'create_time' | 'update_time'
    sortOrder: 'asc' | 'desc'


    projectList: TagCategory[]

    // 查询
    searchKeyword: string
}

export const useTagStore = defineStore('tags', {
    state: (): TagState => ({
        tags: [],
        loading: false,
        currentTag: null,
        total: 0,
        currentPage: 1,
        pageSize: 10,
        sortBy: 'sort_order',
        sortOrder: 'desc',
        projectList: [],

        // 搜索
        searchKeyword: ''
    }),
}) 