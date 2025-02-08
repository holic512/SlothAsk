<script setup lang="ts">
import {ref, reactive, computed, watch} from 'vue'
import {useCategoryStore} from '../pinia/categoryStore'
import {ElMessage, FormInstance, FormRules} from 'element-plus'
import {apiGetCategoryList} from "@/view/MainView/question/category/service/ApiGetCategoryList";
import {Plus} from '@element-plus/icons-vue'
import type {ICategoryForm} from '../types/types'
import {ApiAddCategory} from "@/view/MainView/question/category/service/ApiAddCategory";
import {ApiUpdateCategory} from "@/view/MainView/question/category/service/ApiUpdateCategory";

const categoryStore = useCategoryStore()
const formRef = ref<FormInstance>()

const defaultForm: ICategoryForm = {
  projectId: null,
  name: '',
  description: '',
  avatarUrl: '',
  sortOrder: 0,
  status: 1
};

const formData = reactive<ICategoryForm>({...defaultForm});

// 表单校验规则
const rules = reactive<FormRules>({
  projectId: [
    {required: true, message: '请选择所属项目', trigger: 'change'}
  ],
  name: [
    {required: true, message: '请输入分类名称', trigger: 'blur'},
    {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  avatarUrl: [
    {
      required: true,
      message: '请上传分类头像',
      trigger: 'change',
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请上传分类头像'));
        } else {
          callback();
        }
      }
    }
  ],
  description: [
    {max: 200, message: '描述最多 200 个字符', trigger: 'blur'}
  ],
  sortOrder: [
    {type: 'number', message: '请输入有效的排序号', trigger: 'blur'}
  ]
})

// 状态选项
const statusOptions = [
  {label: '正常', value: 1},
  {label: '禁用', value: 0}
]

// 监听弹窗显示状态
const dialogVisible = computed({
  get: () => categoryStore.isFormVisible,
  set: (value) => {
    categoryStore.isFormVisible = value
    if (!value) {
      Object.assign(formData, defaultForm);
      uploadFile = null;
      formRef.value?.resetFields();
    }
  }
})

// 监听编辑数据
watch(
    () => categoryStore.currentCategory,
    (newVal) => {
      if (newVal) {
        formData.name = newVal.name
        formData.projectId = newVal.projectId
        formData.sortOrder = newVal.sortOrder
        formData.status = newVal.status
        formData.description = newVal.description
        formData.avatarUrl = newVal.avatarUrl
      }
    }
)

// 监听新增模式
watch(
    () => categoryStore.formType,
    (newVal) => {
      if (newVal == 'add') {
        // 重置表单数据
        Object.assign(formData, defaultForm);
      }
    }
)

// 修改文件处理方法
const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];

  if (file) {
    // 验证文件类型
    if (!file.type.includes('image/')) {
      ElMessage.error('请上传图片文件');
      input.value = ''; // 清空输入
      formData.avatarUrl = ''; // 清空预览
      return;
    }

    // 验证文件大小（限制为2MB）
    if (file.size > 2 * 1024 * 1024) {
      ElMessage.error('图片大小不能超过2MB');
      input.value = ''; // 清空输入
      formData.avatarUrl = ''; // 清空预览
      return;
    }

    // 将 file 转换成临时 url 预览
    formData.avatarUrl = URL.createObjectURL(file);
    uploadFile = file;

    // 手动触发表单验证
    formRef.value?.validateField('avatarUrl');
  }
}

let uploadFile;

// 修改提交表单方法
const handleSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const submitData = {
          id: null,
          ...formData,
        }

        if (categoryStore.formType === 'add') {
          // 新增逻辑
          const result = await ApiAddCategory(submitData, uploadFile)
          if (result.status == 200) {
            ElMessage.success("添加分类成功")
            await loadList()
          } else {
            ElMessage.warning("添加分类失败")
          }

        } else {

          if (categoryStore.currentCategory != null)
            submitData.id = categoryStore.currentCategory.id

          // 编辑逻辑
          const result = await ApiUpdateCategory(submitData, uploadFile)
          if (result.status == 200) {
            ElMessage.success("更新分类成功")
            await loadList()
          } else {
            ElMessage.warning("更新分类失败")
          }
        }

        dialogVisible.value = false
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

const loadList = async () => {
  // 重置 页面内容
  categoryStore.resetList();

  const categoryListData = await apiGetCategoryList({
    keyword: categoryStore.searchKeyword,
    projectId: categoryStore.searchProjectId,
    status: categoryStore.searchStatus,
    pageNum: categoryStore.pageNum,
    pageSize: categoryStore.pageSize
  })

  if (categoryListData.status === 200) {
    categoryStore.tableData = categoryListData.data.list
    categoryStore.total = categoryListData.data.total
  }
}
</script>

<template>
  <el-dialog
      :model-value="dialogVisible"
      :title="categoryStore.formType === 'add' ? '新增分类' : '编辑分类'"
      width="600px"
      @update:model-value="dialogVisible = false"
      :close-on-click-modal="false"
      class="category-dialog"
  >
    <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
        class="category-form"
    >
      <div class="form-content">
        <div class="form-left">
          <el-form-item label="所属项目" prop="projectId">
            <el-select v-model="formData.projectId" placeholder="请选择项目" class="w-full">
              <el-option
                  v-for="item in categoryStore.projectOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="分类名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入分类名称"/>
          </el-form-item>

          <el-form-item label="排序" prop="sortOrder">
            <el-input-number
                v-model="formData.sortOrder"
                :min="0"
                :max="999"
                class="w-full"
            />
          </el-form-item>

          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio
                  v-for="option in statusOptions"
                  :key="option.value"
                  :value="option.value"
                  class="status-radio"
              >
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </div>

        <div class="form-right">
          <el-form-item label="头像" prop="avatarUrl">
            <div class="avatar-uploader">
              <input
                  type="file"
                  accept="image/*"
                  class="file-input"
                  @change="handleFileChange"
              >
              <div class="avatar-container">
                <img v-if="formData.avatarUrl" :src="formData.avatarUrl" class="avatar"/>
                <div v-else class="avatar-placeholder">
                  <el-icon class="upload-icon">
                    <Plus/>
                  </el-icon>
                  <span class="upload-text">点击上传</span>
                </div>
              </div>
            </div>
          </el-form-item>
        </div>
      </div>

      <el-form-item label="描述" prop="description">
        <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入分类描述"
            resize="none"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit(formRef)">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.category-form {
  padding-right: 20px;
}

.form-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 12px;
}

.form-left {
  flex: 1;
}

.form-right {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.avatar-uploader {
  position: relative;
  display: inline-block;
}

.file-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
  z-index: 1;
}

.avatar-container {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px dashed var(--el-border-color);
  transition: all 0.3s;
}

.avatar-container:hover {
  border-color: var(--el-color-primary);
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: var(--el-fill-color-lighter);
}

.upload-icon {
  font-size: 24px;
  color: var(--el-text-color-secondary);
  margin-bottom: 8px;
}

.upload-text {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.w-full {
  width: 100%;
}

.status-radio {
  margin-right: 24px;
}

.dialog-footer {
  padding: 16px 24px;
  border-top: 1px solid var(--el-border-color-lighter);
  text-align: right;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: var(--el-text-color-regular);
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
  box-shadow: none;
  border: 1px solid var(--el-border-color);
  transition: all 0.3s;
}

:deep(.el-input__wrapper:hover),
:deep(.el-textarea__inner:hover) {
  border-color: var(--el-color-primary);
}

:deep(.el-input__wrapper.is-focus),
:deep(.el-textarea__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 1px var(--el-color-primary-light-8);
}
</style>