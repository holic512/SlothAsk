// 用户状态枚举
export enum UserStatus {
    Disabled = 0,
    Active = 1
}

// 性别枚举
export enum UserGender {
    Unknown = 0,
    Male = 1,
    Female = 2
}

// 用户基础信息接口
export interface IUser {
    id: number
    username: string
    nickname: string
    email: string
    phone: string
    status: UserStatus
    gender: UserGender
    age: number
    avatar: string
    bio: string
    createTime: string
    password: string | null
}

// 分页响应数据接口
export interface IPageResponse<T> {
    list: T[]
    total: number
}

// 用户列表响应接口
export interface IUserListResponse {
    status: number
    data: IPageResponse<IUser>
}

// 分页请求参数接口
export interface IPageParams {
    pageNum: number
    pageSize: number
}

// 用户搜索参数接口
export interface IUserSearchParams extends IPageParams {
    keyword?: string
    status?: UserStatus | ''
} 