import {defineStore} from 'pinia';
import {ref} from 'vue';

// 定义用户个人信息的类型
interface UserProfile {
    username?: string;
    nickname?: string;
    avatarUrl?: string;
    email?: string;
    phone?: string;
    bio?: string;
    createTime?: string;
    updateTime?: string;

    [key: string]: any; // 允许额外的字段
}

// 存储当前用户的 个人信息  仅用于数据展示 会 set一次 其他就调用这个就可以了
export const useUserProfileStore = defineStore('UserProfile', () => {
    // 使用 `ref` 创建响应式用户个人信息
    const userProfile = ref<UserProfile>({});

    // 获取用户个人信息
    const getUserProfile = (): UserProfile => {
        return userProfile.value;
    };

    // 设置用户个人信息
    const setUserProfile = (newProfile: UserProfile) => {
        userProfile.value = newProfile;
    };

    // 更新用户名
    const updateUsername = (username: string) => {
        userProfile.value.username = username;
    };

    // 更新昵称
    const updateNickname = (nickname: string) => {
        userProfile.value.nickname = nickname;
    };

    // 更新头像
    const updateAvatar = (avatarUrl: string) => {
        userProfile.value.avatarUrl = avatarUrl;
    };

    // 更新用户基本信息
    const updateBasicInfo = (username: string, nickname: string, avatarUrl: string) => {
        userProfile.value.username = username;
        userProfile.value.nickname = nickname;
        userProfile.value.avatarUrl = avatarUrl;
    };

    // 更新邮箱
    const updateEmail = (email: string) => {
        userProfile.value.email = email;
    };

    // 更新手机号
    const updatePhone = (phone: string) => {
        userProfile.value.phone = phone;
    };

    // 更新个人简介
    const updateBio = (bio: string) => {
        userProfile.value.bio = bio;
    };

    // 清除用户个人信息
    const clearUserProfile = () => {
        userProfile.value = {};
    };

    // 检查是否有用户信息
    const hasUserProfile = (): boolean => {
        return Object.keys(userProfile.value).length > 0;
    };

    return {
        userProfile,
        getUserProfile,
        setUserProfile,
        updateUsername,
        updateNickname,
        updateAvatar,
        updateBasicInfo,
        updateEmail,
        updatePhone,
        updateBio,
        clearUserProfile,
        hasUserProfile
    };
}, {
    persist: true // 启用持久化
});