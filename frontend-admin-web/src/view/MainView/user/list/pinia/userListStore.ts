import { defineStore } from "pinia"
import { ref } from "vue"
import type { IUser, IUserSearchParams } from "../types/user"
import { UserStatus } from "../types/user"

export const useUserListStore = defineStore('userList', () => {
    // 表格数据
    const tableData = ref<IUser[]>([])
    
    // 数据总行数
    const total = ref<number>(0)
    
    // 分页参数
    const pageNum = ref<number>(1)
    const pageSize = ref<number>(10)
    
    // 抽屉相关
    const drawerVisible = ref<boolean>(false)
    const currentUser = ref<IUser | null>(null)
    
    // 选中行
    const selectedRows = ref<IUser[]>([])
    
    // 表单相关
    const formVisible = ref<boolean>(false)
    const formType = ref<'add' | 'edit'>('add')
    const currentEditUser = ref<IUser | null>(null)
    
    // 密码表单相关
    const passwordFormVisible = ref<boolean>(false)
    const currentPasswordUser = ref<IUser | null>(null)
    
    // 搜索参数
    const searchKeyword = ref<string>('')
    const searchStatus = ref<UserStatus | ''>('')

    return {
        tableData,
        total,
        pageNum,
        pageSize,
        drawerVisible,
        currentUser,
        selectedRows,
        formVisible,
        formType,
        currentEditUser,
        passwordFormVisible,
        currentPasswordUser,
        searchKeyword,
        searchStatus
    }
}) 