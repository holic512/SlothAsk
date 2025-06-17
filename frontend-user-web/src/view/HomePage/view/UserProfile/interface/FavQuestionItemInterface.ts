/**
 * 用户收藏的问题项接口
 */
export interface FavQuestionItemInterface {
  /**
   * 问题唯一标识符
   */
  questionId: string;

  /**
   * 问题标题
   */
  questionTitle: string;

  /**
   * 关联标签列表
   */
  tags: string[];

  /**
   * 问题的浏览次数
   */
  views: number;

  /**
   * 收藏该问题的时间（ISO 字符串格式）
   */
  savedAt: string;
}
