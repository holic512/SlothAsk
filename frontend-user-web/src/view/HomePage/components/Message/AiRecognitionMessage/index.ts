// 导出所有AI识别消息组件
export {default as ToastMessage} from './components/ToastMessage.vue'
export {default as MiniWindowMessage} from './components/MiniWindowMessage.vue'
export {default as FullPageMessage} from './components/FullPageMessage.vue'

// 导出消息显示模式枚举
export {MessageDisplayMode} from '../MessageInterface'
import {BaseMessage} from "../MessageInterface";

export interface AiRecognitionMessageData {
    questionId: string;
    questionTitle: string;
    accuracy: number;          // AI解析正确率（如 87 表示 87%）
}

export type AiRecognitionMessage = BaseMessage<AiRecognitionMessageData>;
