/**
 * File Name: FavoriteMessage.java
 * Description: 用户收藏/取消收藏题目的消息体，用于在 RabbitMQ 中传递收藏事件。
 * Author: holic512
 * Created Date: 2025-04-24
 * Version: 1.0
 * Usage:
 * 此类用于封装用户的收藏操作行为，包含用户ID、题目ID、操作类型（fav/unfav）及操作时间戳，
 * 主要在发送到 RabbitMQ 队列中进行异步处理时使用。
 */

package org.example.servicequestion.user.favQuestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteMessage{
    private Long userId;
    private Long questionId;
    private String action; // "fav" or "unfav"
    private Long timestamp;

}

