// 导出消息相关的接口和枚举
export { MessageDisplayMode, MessageType, ReadStatus } from './MessageInterface'
export type { BaseMessage } from './MessageInterface'

// 导出动态消息组件
export { default as DynamicMessageComponent } from './DynamicMessageComponent.vue'

// 导出消息通知容器组件
export { default as MessageNotificationContainer } from './MessageNotificationContainer.vue'

// 导出AI识别消息相关内容
export * from './AiRecognitionMessage/index'

// 导出演示组件
export { default as DynamicMessageDemo } from './DynamicMessageDemo.vue'

/**
 * 消息组件使用指南：
 * 
 * 1. 基础用法：
 *    import { DynamicMessageComponent, MessageDisplayMode } from './Message'
 *    
 *    <DynamicMessageComponent 
 *      :message="yourMessage" 
 *      :display-mode="MessageDisplayMode.TOAST" 
 *    />
 * 
 * 2. 消息通知容器用法：
 *    import { MessageNotificationContainer } from './Message'
 *    
 *    <MessageNotificationContainer />
 *    
 *    特性：
 *    - 自动定位到右上角
 *    - 每3秒生成一条AI解析消息（示例效果）
 *    - 最多显示8条消息
 *    - 每条消息3秒后自动消失
 *    - 支持手动关闭
 *    - 美丽的进入/离开动画
 * 
 * 3. 如何添加新的消息类型：
 *    - 在 MessageInterface.ts 中添加新的消息类型枚举
 *    - 创建对应的消息组件文件夹
 *    - 实现三种显示模式的组件（Toast、MiniWindow、FullPage）
 *    - 在 DynamicMessageComponent.vue 中添加组件映射
 * 
 * 4. 支持的显示模式：
 *    - TOAST: 弹出框模式，适用于简短通知
 *    - MINI_WINDOW: 小窗模式，适用于信息预览
 *    - FULL_PAGE: 完整模式，适用于详细展示
 */