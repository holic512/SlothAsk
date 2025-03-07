<template>
  <div class="editor-container">
    <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        mode="simple"
        class="editor-toolbar"
    />

    <Editor
        :defaultConfig="editorConfig"
        mode="simple"
        v-model="valueHtml"
        @onCreated="handleCreated"
        @onChange="handleChange"
        @onDestroyed="handleDestroyed"
        @onFocus="handleFocus"
        @onBlur="handleBlur"
        style="height: 300px"
    />
  </div>
</template>

<script setup lang="ts">
import {ModelRef, onBeforeUnmount, ref, shallowRef, watch} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import {ApiPostUploadImage} from "@/view/MainView/question/list/service/ApiPostUploadImage";
import {ElMessage} from "element-plus";


// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml: ModelRef<unknown | undefined, string, string | undefined, string | undefined> = defineModel()

// 工具栏配置
const toolbarConfig = {
  excludeKeys: ['fullScreen'],
}

// 编辑器配置
const editorConfig = {
  autoFocus: false,
  MENU_CONF: {
    uploadImage: {
      // 自定义图片上传
      customUpload: async (file: File, insertFn: any) => {
        try {
          // 限制文件大小 (5MB)
          const maxSize = 5 * 1024 * 1024 // 5MB
          if (file.size > maxSize) {
            ElMessage.error('上传失败: 图片大小不能超过 5MB')
            return
          }

          // 限制文件类型
          const allowedTypes = ['image/jpeg', 'image/png', 'image/gif']
          if (!allowedTypes.includes(file.type)) {
            ElMessage.error('上传失败: 仅支持 jpg, png, gif 格式的图片')
            return
          }

          const result = await ApiPostUploadImage(file)
          if (result.status === 200) {
            // 获取 fileName 和 previewUrl
            const {fileName, previewUrl} = result.data
            // 插入图片 并声明 alt 属性
            insertFn(previewUrl, '#' + fileName, previewUrl)
          }
        } catch (error) {
          console.error('图片上传失败:', error)
        }
      }

    }
  }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

// 编辑器创建完成时的回调
const handleCreated = (editor: any) => {
  editorRef.value = editor
}

// 编辑器内容变化时的回调
const handleChange = (editor: any) => {

}

// 编辑器获得焦点时的回调
const handleFocus = (editor: any) => {

}

// 编辑器失去焦点时的回调
const handleBlur = (editor: any) => {

}

// 编辑器销毁时的回调
const handleDestroyed = () => {
  editorRef.value = null
}
</script>

<style scoped>
.editor-container {
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
}
</style>