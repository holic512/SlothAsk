import {defineStore} from "pinia";
import {ref} from "vue";
import {
    getCategoriesByProjectIdPaged,
    getCategoryById
} from "@/view/HomePage/view/QuestionBankPage/BankPage/service/ApiGetCategory";
import {Category} from "../interface/CategoryInterface"

export const useCategoryStore = defineStore('category', () => {
    const categories = ref<Category[]>([]);
    const category = ref<Category>();
    const totalCategories = ref<number>(0);
    const currentPage = ref<number>(1);
    const pageSize = ref<number>(12);
    const loading = ref<boolean>(false);

    // 分页获取分类列表
    const fetchCategoriesByProjectIdPaged = async (projectId: number, page: number = 1, size: number = 12) => {
        loading.value = true;
        try {
            currentPage.value = page;
            pageSize.value = size;
            const response = await getCategoriesByProjectIdPaged(projectId, page, size);
            const data = response.data.data;
            categories.value = data.list;
            totalCategories.value = data.total;
        } finally {
            loading.value = false;
        }
    };

    // 加载更多分类
    const loadMoreCategories = async (projectId: number) => {
        if (loading.value) return;
        
        const nextPage = currentPage.value + 1;
        loading.value = true;
        
        try {
            const response = await getCategoriesByProjectIdPaged(projectId, nextPage, pageSize.value);
            const data = response.data.data;
            
            // 追加新数据
            categories.value = [...categories.value, ...data.list];
            currentPage.value = nextPage;
        } finally {
            loading.value = false;
        }
    };

    const fetchCategoryById = async (categoryId: number) => {
        const response = await getCategoryById(categoryId);
        category.value = response.data.data;
    };

    return {
        category,
        categories,
        totalCategories,
        currentPage,
        pageSize,
        loading,
        fetchCategoryById,
        fetchCategoriesByProjectIdPaged,
        loadMoreCategories
    };
});