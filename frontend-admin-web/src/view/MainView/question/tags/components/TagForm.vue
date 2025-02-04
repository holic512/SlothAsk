<script setup lang="ts">
import {ref, watch, computed, onMounted} from 'vue'
import {QuestionFilled} from '@element-plus/icons-vue'
import type {TagCategory} from '../types/TagCategoryType'
import {useTagStore} from "@/view/MainView/question/tags/pinia/tags";
import {handleFormSubmit} from "../services/handleFormSubmit";

// 定义store
const tagStore = useTagStore()

// 使用 defineModel 来定义双向绑定的属性
const visible = defineModel('visible', {default: false})
const type = defineModel('type', {default: 'add'})
const data = defineModel<TagCategory | null>('data')

const formData = ref<TagCategory>({
  id: undefined,
  name: '',
  description: '',
  projectId: null,
  sortOrder: 0,
  status: 1,
  updateTime: "",
})

const rules = {
  name: [
    {required: true, message: '请输入标签名称', trigger: 'blur'},
    {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  projectId: [
    {required: true, message: '请选择所属项目', trigger: 'change'}
  ],
  description: [
    {max: 200, message: '最大长度为 200 个字符', trigger: 'blur'}
  ],
  sortOrder: [
    {required: true, message: '请输入权重', trigger: 'blur'},
    {type: 'number', message: '权重必须为数字', trigger: 'blur'}
  ]
}

const formRef = ref()

watch(
    () => data.value,
    (newVal) => {
      if (type.value === 'edit' && newVal) {
        formData.value = {...newVal}
      } else {
        formData.value = {
          id: undefined,
          name: '',
          description: '',
          projectId: null,
          sortOrder: 0,
          status: 1,
          updateTime: '',
        }
      }
    }
)

const handleClose = () => {
  visible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      const success = await handleFormSubmit(type.value, formData.value);
      if (success) {
        handleClose();
      }
    }
  })
}

</script>

<template>
  <el-dialog
      :title="type === 'add' ? '新增标签' : '编辑标签'"
      v-model="visible"
      width="650px"
      :close-on-click-modal="false"
      @close="handleClose"
      style="padding: 20px"
  >
    <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="tag-form"
    >
      <div class="form-section">
        <div class="section-title">基本信息</div>
        <el-form-item label="标签名称" prop="name">
          <el-input
              v-model="formData.name"
              placeholder="请输入标签名称"
              maxlength="50"
              show-word-limit
          />
        </el-form-item>

        <el-form-item label="所属项目" prop="project_id">
          <el-select
              v-model="formData.projectId"
              placeholder="请选择所属项目"
              style="width: 100%"
          >
            <el-option
                v-for="project in tagStore.projectList"
                :key="project.id"
                :label="project.name"
                :value="project.id"
            >
              <div class="project-option">
                <span>{{ project.name }}</span>
                <el-tooltip
                    :content="project.description"
                    placement="right"
                    :show-after="500"
                    v-if="project.description"
                >
                  <el-icon class="info-icon">
                    <QuestionFilled/>
                  </el-icon>
                </el-tooltip>
              </div>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="标签描述" prop="description">
          <el-input
              v-model="formData.description"
              type="textarea"
              :rows="4"
              placeholder="请输入标签描述（选填）"
              maxlength="200"
              show-word-limit
          />
        </el-form-item>
      </div>

      <div class="form-section">
        <div class="section-title">配置信息</div>
        <div class="config-row">
          <el-form-item label="权重" prop="sort_order">
            <el-tooltip
                content="权重值越大，排序越靠前"
                placement="top"
                effect="light"
            >
              <div class="weight-wrapper">
                <el-input-number
                    v-model="formData.sortOrder"
                    :min="0"
                    :max="9999"
                    controls-position="right"
                />
              </div>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="状态">
            <el-radio-group v-model="formData.status">
              <el-radio :label="1">
                <el-tag size="small" type="success" effect="plain">正常</el-tag>
              </el-radio>
              <el-radio :label="0">
                <el-tag size="small" type="danger" effect="plain">禁用</el-tag>
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
      </div>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>

</style> 