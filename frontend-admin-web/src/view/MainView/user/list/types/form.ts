export interface IPasswordForm {
    id: number
    password: string
    confirmPassword: string
}

export interface IUserForm {
    username: string
    nickname?: string
    email: string
    phone: string
    password?: string
    confirmPassword?: string
    status: number
    gender: number
    age?: number
    bio?: string
    avatar?: string
}

export interface IFormRules {
    [key: string]: {
        required?: boolean
        message?: string
        trigger?: string
        min?: number
        max?: number
        type?: string
        pattern?: RegExp
        validator?: (rule: any, value: any, callback: any) => void
    }[]
} 