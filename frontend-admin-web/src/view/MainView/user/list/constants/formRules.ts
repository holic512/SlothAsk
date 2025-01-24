import type { IFormRules } from '../types/form'

/**
 * 用户表单校验规则
 * @param type - 表单类型，'add' 或 'edit'
 */
export const getUserFormRules = (type: 'add' | 'edit'): IFormRules => ({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
    ],
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
    ],
    password: [
        { required: type === 'add', message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: type === 'add', message: '请确认密码', trigger: 'blur' },
        {
            validator: (rule: any, value: any, callback: any) => {
                if (value !== (document.querySelector('input[name="password"]') as HTMLInputElement)?.value) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ]
})

/**
 * 密码表单校验规则
 */
export const passwordFormRules = {
    password: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        { 
            validator: (rule: any, value: string, callback: Function) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'))
                } else if (value !== form.value.password) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            }, 
            trigger: 'blur' 
        }
    ]
} 