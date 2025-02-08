import {useCategoryStore} from '../pinia/categoryStore'
import {apiGetCategoryList} from './ApiGetCategoryList'

interface SearchParams {
    keyword?: string | null
    projectId?: number | null
    status?: number
    pageNum: number
    pageSize: number
}

export const handleSearch = async (params: SearchParams) => {
    const categoryStore = useCategoryStore()

    const res = await apiGetCategoryList(params)

    if (res.status === 200) {
        categoryStore.tableData = res.data.list
        categoryStore.total = res.data.total
    }
} 