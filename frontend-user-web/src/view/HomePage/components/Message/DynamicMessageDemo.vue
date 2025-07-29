<script lang="ts" setup>
import {ref} from 'vue'
import DynamicMessageComponent from './DynamicMessageComponent.vue'
import type {BaseMessage} from './MessageInterface'
import {MessageType, MessageDisplayMode, ReadStatus} from './MessageInterface'
import type {AiRecognitionMessage} from './AiRecognitionMessage/index'

// 示例数据 - AI识别消息
const aiMessage: AiRecognitionMessage = {
  messageId: 'ai001',
  type: MessageType.AI_RECOGNITION,
  readStatus: ReadStatus.UNREAD,
  createTime: '2024-01-15T10:30:00Z',
  messageData: {
    questionId: 'q001',
    questionTitle: '关于Vue 3组合式API的使用方法，下列说法正确的是？',
    accuracy: 85
  }
}

// 示例数据 - 用户关注消息（暂未实现）
const userFollowMessage: BaseMessage = {
  messageId: 'follow001',
  type: MessageType.USER_FOLLOW,
  readStatus: ReadStatus.UNREAD,
  createTime: '2024-01-15T09:15:00Z',
  messageData: {
    userId: 'user123',
    userName: '张三',
    userAvatar: 'https://example.com/avatar.jpg'
  }
}

// 示例数据 - 评论回复消息（暂未实现）
const commentReplyMessage: BaseMessage = {
  messageId: 'reply001',
  type: MessageType.COMMENT_REPLY,
  readStatus: ReadStatus.UNREAD,
  createTime: '2024-01-15T08:45:00Z',
  messageData: {
    commentId: 'comment123',
    replyContent: '这是一个回复内容',
    replierName: '李四'
  }
}

// 当前选择的显示模式
const currentDisplayMode = ref<MessageDisplayMode>(MessageDisplayMode.FULL_PAGE)

// 显示模式选项
const displayModeOptions = [
  {label: 'Toast模式', value: MessageDisplayMode.TOAST},
  {label: '小窗模式', value: MessageDisplayMode.MINI_WINDOW},
  {label: '完整模式', value: MessageDisplayMode.FULL_PAGE}
]
</script>

<template>
  <div class="demo-container">
    <h2>动态消息组件演示</h2>

    <!-- 显示模式选择器 -->
    <div class="mode-selector">
      <span class="selector-label">显示模式：</span>
      <el-radio-group v-model="currentDisplayMode">
        <el-radio
            v-for="option in displayModeOptions"
            :key="option.value"
            :label="option.value"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
    </div>

    <div class="demo-sections">
      <!-- AI识别消息演示 -->
      <div class="demo-section">
        <h3>1. AI识别消息（已实现）</h3>
        <div class="demo-content">
          <DynamicMessageComponent
              :display-mode="currentDisplayMode"
              :message="aiMessage"
          />
        </div>
      </div>

      <!-- 用户关注消息演示 -->
      <div class="demo-section">
        <h3>2. 用户关注消息（待实现）</h3>
        <div class="demo-content">
          <DynamicMessageComponent
              :display-mode="currentDisplayMode"
              :message="userFollowMessage"
          />
        </div>
      </div>

      <!-- 评论回复消息演示 -->
      <div class="demo-section">
        <h3>3. 评论回复消息（待实现）</h3>
        <div class="demo-content">
          <DynamicMessageComponent
              :display-mode="currentDisplayMode"
              :message="commentReplyMessage"
          />
        </div>
      </div>
    </div>

    <!-- 使用说明 -->
    <div class="usage-guide">
      <h3>使用说明</h3>
      <div class="guide-content">
        <h4>组件特性：</h4>
        <ul>
          <li>✅ <strong>懒加载</strong>：组件按需加载，提升性能</li>
          <li>✅ <strong>类型安全</strong>：完整的TypeScript类型支持</li>
          <li>✅ <strong>可扩展</strong>：新增消息类型只需添加对应的组件映射</li>
          <li>✅ <strong>错误处理</strong>：优雅处理组件加载失败的情况</li>
          <li>✅ <strong>占位符</strong>：未实现的组件显示友好的占位符</li>
        </ul>

        <h4>如何添加新的消息类型：</h4>
        <ol>
          <li>在 <code>MessageInterface.ts</code> 中添加新的消息类型枚举</li>
          <li>创建对应的消息组件文件夹（如 <code>UserFollowMessage/</code>）</li>
          <li>实现三种显示模式的组件（Toast、MiniWindow、FullPage）</li>
          <li>在 <code>DynamicMessageComponent.vue</code> 中添加组件映射</li>
        </ol>
      </div>
    </div>
  </div>
</template>

<style scoped>
.demo-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.demo-container h2 {
  text-align: center;
  color: #303133;
  margin-bottom: 32px;
}

.mode-selector {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 32px;
  padding: 16px;
  background: #F5F7FA;
  border-radius: 8px;
}

.selector-label {
  font-weight: 600;
  color: #303133;
}

.demo-sections {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.demo-section {
  border: 1px solid #EBEEF5;
  border-radius: 8px;
  overflow: hidden;
}

.demo-section h3 {
  margin: 0;
  padding: 16px 20px;
  background: #409EFF;
  color: white;
  font-size: 16px;
  font-weight: 600;
}

.demo-content {
  padding: 24px;
  background: white;
  min-height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.usage-guide {
  margin-top: 48px;
  padding: 24px;
  background: #F9F9F9;
  border-radius: 8px;
  border-left: 4px solid #67C23A;
}

.usage-guide h3 {
  margin-top: 0;
  color: #67C23A;
  font-size: 18px;
}

.usage-guide h4 {
  color: #303133;
  margin-top: 20px;
  margin-bottom: 12px;
}

.usage-guide ul,
.usage-guide ol {
  color: #606266;
  line-height: 1.6;
}

.usage-guide li {
  margin-bottom: 8px;
}

.usage-guide code {
  background: #E4E7ED;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .mode-selector {
    flex-direction: column;
    align-items: flex-start;
  }

  .demo-content {
    padding: 16px;
  }
}
</style>