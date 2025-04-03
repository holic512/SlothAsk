export interface Category {
    id: number; // 自增主键
    projectId: number; // 所属项目ID
    name: string; // 分类名称
    description: string; // 分类描述
    creatorId: number; // 创建者ID
    avatarUrl: string; // 分类头像URL
    sortOrder: number; // 排序序号
    viewCount: number; // 访问数量
    status: number; // 1:正常 0:禁用
    createTime: string; // 创建时间
    updateTime: string; // 更新时间
}