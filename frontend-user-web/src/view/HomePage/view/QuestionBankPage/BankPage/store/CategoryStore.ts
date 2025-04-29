import {defineStore} from "pinia";
import {ref} from "vue";
import {
    getCategoriesByProjectId,
    getCategoryById
} from "@/view/HomePage/view/QuestionBankPage/BankPage/service/ApiGetCategory";
import {Category} from "../interface/CategoryInterface"

export const useCategoryStore = defineStore('category', () => {
    const categories = ref<Category[]>([]);
    const category = ref<Category>();

    // 获取分类列表
    const fetchCategoriesByProjectId = async (projectId: number) => {
        const response = await getCategoriesByProjectId(projectId);
        categories.value = response.data.data;
    };

    const fetchCategoryById = async (categoryId: number) => {
        const response = await getCategoryById(categoryId);
        category.value = response.data.data;
    };

    return {
        category,
        categories,
        fetchCategoryById,
        fetchCategoriesByProjectId
    };
});