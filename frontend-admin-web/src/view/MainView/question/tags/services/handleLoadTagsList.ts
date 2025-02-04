import {ApiGetTagsList} from "@/view/MainView/question/tags/services/ApiGetTagsList";
import {useTagStore} from "@/view/MainView/question/tags/pinia/tags";

export const handleLoadTagsList = async () => {
    const tagStore = useTagStore()
    const result = await ApiGetTagsList({
        keyword: tagStore.searchKeyword,
        pageNum: tagStore.currentPage,
        pageSize: tagStore.pageSize
    })

    tagStore.tags = result.list
    tagStore.total = result.total
}