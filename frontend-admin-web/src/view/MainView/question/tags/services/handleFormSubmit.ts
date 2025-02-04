// 处理表单提交
import {ElMessage} from "element-plus";
import {TagCategory} from "@/view/MainView/question/tags/types/TagCategoryType";
import {ApiAddTag} from "@/view/MainView/question/tags/services/ApiAddTag";
import {ApiUpdateTag} from "@/view/MainView/question/tags/services/ApiUpdateTag";
import {handleLoadTagsList} from "@/view/MainView/question/tags/services/handleLoadTagsList";

export const handleFormSubmit = async (formType: string, formData: TagCategory) => {
    try {
        let response;
        if (formType === 'add') {
            response = await ApiAddTag(formData);
        } else {
            response = await ApiUpdateTag(formData);
        }

        if (response.status === 200) {
            ElMessage.success(formType === 'add' ? '新增成功' : '更新成功');
            await handleLoadTagsList();
            return true;
        } else {
            ElMessage.error(response.message || (formType === 'add' ? '新增失败' : '更新失败'));
            return false;
        }
    } catch (error) {
        ElMessage.error('操作失败');
        return false;
    }
}