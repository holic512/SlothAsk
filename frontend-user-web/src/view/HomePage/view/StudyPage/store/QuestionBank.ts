import {defineStore} from 'pinia';
import {ref} from 'vue';

/**
 * 题目接口定义
 */
interface Question {
    id: number;
    categoryId: number;
    title: string;
    content: string;
    answer: string | string[] | boolean;
    difficulty: number; // 1:简单 2:中等 3:困难
    type: number; // 1:单选 2:多选 3:判断 4:简答
    tagCategoryIds: Array<number>;
    status: number; // 1:正常 0:禁用
    view_count: number;
    create_time: string;
    update_time: string;
}

/**
 * 分类接口定义
 */
interface Category {
    id: number;
    project_id: number;
    name: string;
    description: string;
    creator_id: number;
    avatar_url: string;
    sort_order: number;
    view_count: number;
    status: number;
    create_time: string;
    update_time: string;
}

/**
 * 用于过滤题库的 分类接口 也就是只有id 和 name
 */
interface FilterCategoryList {
    id: number,
    name: string,
}

/**
 * 用于过滤题库的 标签接口 也就是只有id 和 name
 */
interface FilterTagList {
    id: number,
    name: string,
}

/**
 * 分页状态接口
 */
export interface QuestionFilter {
    searchText: string | null;
    // 条件匹配模式：true=全部满足(AND)，false=任一满足(OR)
    matchAllConditions: boolean;
    // 分类过滤 - 等于/不等于
    filterCategoryEquals: number | null;
    filterCategoryNotEquals: number | null;
    // 类型过滤 - 等于/不等于
    filterTypeEquals: number | null;
    filterTypeNotEquals: number | null;
    // 难度过滤 - 等于/不等于
    filterDifficultyEquals: number | null;
    filterDifficultyNotEquals: number | null;
    // 标签过滤
    filterTags: Array<number>;
    pageNum: number;
    total?: number; // 添加总数字段
    
    // 为了向后兼容，保留原有字段
    filterCategory?: number | null;
    filterType?: number | null;
    filterDifficulty?: number | null;
}

/**
 * Pinia 题库 Store
 */
export const useQuestionBankStore = defineStore('questionBank', () => {
    // 过滤列表题目 - 分类
    const FilterCategoryList = ref<FilterCategoryList[]>([])

    // 过滤列表题目 - 标签
    const FilterTagList = ref<FilterTagList[]>([])
    // 题目列表
    const questions = ref<Question[]>([]);

    // 分类列表
    const categories = ref<Category[]>([]);

    // 分页信息
     const pagination = ref<QuestionFilter>({
        searchText: null,
        matchAllConditions: true,
        filterCategoryEquals: null,
        filterCategoryNotEquals: null,
        filterTypeEquals: null,
        filterTypeNotEquals: null,
        filterDifficultyEquals: null,
        filterDifficultyNotEquals: null,
        filterTags: [],
        pageNum: 1,
        total: 0, // 初始化总数
        // 向后兼容字段
        filterCategory: null,
        filterType: null,
        filterDifficulty: null,
    });

    return {
        FilterCategoryList,
        FilterTagList,
        questions,
        categories,
        pagination,
    };
});
