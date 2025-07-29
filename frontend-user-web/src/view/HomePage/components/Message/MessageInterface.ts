// 展示模式枚举
export enum MessageDisplayMode {
    TOAST = 'toast',
    MINI_WINDOW = 'mini',
    FULL_PAGE = 'full',
}

// 消息类型枚举
export enum MessageType {
    AI_RECOGNITION = 1,
    USER_FOLLOW = 2,
    COMMENT_REPLY = 3,
    // 可继续扩展...
}

// 阅读状态枚举（可选）
export enum ReadStatus {
    UNREAD = 0,
    READ = 1,
}

// 通用消息接口
export interface BaseMessage<T = any> {
    messageId: string;         // 唯一标识
    type: MessageType;         // 消息类型
    readStatus: ReadStatus;    // 阅读状态：0 未读，1 已读
    createTime: string;     //  消息时间展示
    messageData: T;            // 消息内容（根据 type 匹配）
}

