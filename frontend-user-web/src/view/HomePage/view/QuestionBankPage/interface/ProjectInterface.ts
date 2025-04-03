export interface Project {
    id: number; // 自增主键
    name: string; // 分类名称
    description: string; // 分类描述
    sortOrder: number; // 排序序号
    status: number; // 1:正常 0:禁用
    createTime: string; // 创建时间
    updateTime: string; // 更新时间
}