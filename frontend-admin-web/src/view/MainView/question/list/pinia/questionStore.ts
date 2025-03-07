import {defineStore} from 'pinia'
import {ICategoryList, IQuestion, IQuestionSearchParams, ITagList} from '../types/types'
import {IProjectOption} from "@/view/MainView/question/category/service/ApiGetProjectOptions";
import {ref} from "vue";
export const useQuestionStore = defineStore('question', {
    state: () => ({
        // 列表数据
        tableData: [] as IQuestion[],
        // 分页
        pageNum: 1,
        pageSize: 10,
        total: 0,

        // 选中的行
        selectedRows: [] as IQuestion[],
        // 加载状态
        loading: false,
        // 表单可见性
        formVisible: false,
        // 表单类型
        formType: 'add' as 'add' | 'edit',
        // 当前编辑的题目
        currentQuestion: null as IQuestion | null,
        // 详情抽屉可见性
        detailVisible: false,

        // 项目列表
        projectOptions: [] as IProjectOption[],
        // 当前项目id下的 分类列表
        categoryOptionsByProjectId: [] as ICategoryList[],
        // 标签列表
        tagOptionsByProjectId: [] as ITagList[],

        // 全部分类列表
        categoryOptions: [] as ICategoryList[],
        // 全部标签列表
        tagOptions: [] as ITagList[],

    }),

    actions: {
        setLoading(status: boolean) {
            this.loading = status
        },
        setFormVisible(visible: boolean) {
            this.formVisible = visible
        },
        setDetailVisible(visible: boolean) {
            this.detailVisible = visible
        },
        setCurrentQuestion(question: IQuestion | null) {
            this.currentQuestion = question
        },
        setSelectedRows(rows: IQuestion[]) {
            this.selectedRows = rows
        }
    }
}) 