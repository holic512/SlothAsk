import axios from '@/axios/axios';
import {
    ApiResponse,
    HistoryQuestionDTO,
    HistoryGroup,
    HistoryRecord,
    difficultyMap
} from './types';
import {format, parseISO, isSameDay, subDays} from 'date-fns';
import {zhCN} from 'date-fns/locale';

/**
 * 获取用户历史记录
 * @param page 页码
 * @returns 用户历史记录
 */
export async function getUserHistory(page: number = 1): Promise<HistoryGroup[]> {
    try {
        const response = await axios.get<ApiResponse<HistoryQuestionDTO>>(
            'service-question/user/history/questions', {
                params: {
                    page: page
                }
            });

        if (response.data.status === 200 && response.data.data) {
            return convertToHistoryGroups(response.data.data);
        }
        return [];
    } catch (error) {
        console.error('获取历史记录失败:', error);
        return [];
    }
}

/**
 * 将后端数据转换为前端需要的格式
 * @param data 后端返回的数据
 * @returns 按日期分组的历史记录
 */
function convertToHistoryGroups(data: HistoryQuestionDTO): HistoryGroup[] {
    const {userHistoryList, tagSimpleList} = data;

    // 创建标签ID到名称的映射，提高查找效率
    const tagMap = new Map<number, string>();
    tagSimpleList.forEach(tag => {
        tagMap.set(tag.id, tag.name);
    });

    // 将历史记录按日期分组
    const groupedByDate = new Map<string, HistoryRecord[]>();

    // 处理每条历史记录
    userHistoryList.forEach(historyItem => {
        // 转换访问时间
        const visitDate = parseISO(historyItem.visitTime);

        // 获取日期分组键
        const dateKey = formatDateGroup(visitDate);

        // 处理标签 - 直接使用tagIds匹配优化性能
        const tags = extractTagsById(historyItem.tagCategoryIds, tagMap);

        // 创建前端记录对象
        const record: HistoryRecord = {
            id: historyItem.virtualId,
            title: historyItem.title,
            visitTime: format(visitDate, 'HH:mm'),
            category: historyItem.categoryName,
            tags,
            difficulty: difficultyMap[historyItem.difficulty] || '未知'
        };

        // 添加到对应日期组
        if (!groupedByDate.has(dateKey)) {
            groupedByDate.set(dateKey, []);
        }
        groupedByDate.get(dateKey)?.push(record);
    });

    // 转换为数组格式
    const result: HistoryGroup[] = [];
    groupedByDate.forEach((records, date) => {
        result.push({
            date,
            records: records.sort((a, b) =>
                a.visitTime > b.visitTime ? -1 : 1
            )
        });
    });

    // 按日期排序
    return result.sort((a, b) => {
        // 今天、昨天、前天排在最前面
        if (a.date === '今天') return -1;
        if (b.date === '今天') return 1;
        if (a.date === '昨天') return -1;
        if (b.date === '昨天') return 1;
        if (a.date === '前天') return -1;
        if (b.date === '前天') return 1;

        // 其他日期按日期逆序排序
        return b.date.localeCompare(a.date);
    });
}

/**
 * 从标签分类ID字符串中提取标签 - 针对 [1,2] 格式的优化版本
 * @param tagCategoryIds 标签分类ID字符串，格式为 "[1,2]"
 * @param tagMap 标签ID到名称的映射
 * @returns 标签名称数组
 */
function extractTagsById(tagCategoryIds: string, tagMap: Map<number, string>): string[] {
    if (!tagCategoryIds || tagCategoryIds.trim() === '[]') return [];

    try {
        // 步骤 1: 去除方括号并分割字符串
        const cleanString = tagCategoryIds
            .replace(/^\[|]$/g, '')  // 移除开头结尾的方括号
            .replace(/\s/g, '');      // 移除所有空格

        // 步骤 2: 分割字符串为数字数组
        const tagIds = cleanString.split(',')
            .map(id => parseInt(id, 10))
            .filter(id => !isNaN(id));

        // 步骤 3: 映射为标签名称并去重
        const uniqueTags = new Set<string>();
        tagIds.forEach(id => {
            const tagName = tagMap.get(id);
            if (tagName) uniqueTags.add(tagName);
        });

        return Array.from(uniqueTags);
    } catch (error) {
        console.error('解析标签失败:', error, '原始字符串:', tagCategoryIds);
        return [];
    }
}

/**
 * 格式化日期为分组名称
 * @param date 日期对象
 * @returns 日期分组名称
 */
function formatDateGroup(date: Date): string {
    const today = new Date();

    if (isSameDay(date, today)) {
        return '今天';
    }

    if (isSameDay(date, subDays(today, 1))) {
        return '昨天';
    }

    if (isSameDay(date, subDays(today, 2))) {
        return '前天';
    }

    return format(date, 'yyyy-MM-dd', {locale: zhCN});
} 