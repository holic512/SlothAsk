import { ref } from 'vue';

export function useTimeFormat() {
  // 缓存已格式化的时间
  const timeCache = ref<Map<string, string>>(new Map());
  
  // 格式化时间
  const formatTime = (timeStr: string): string => {
    // 检查缓存
    if (timeCache.value.has(timeStr)) {
      return timeCache.value.get(timeStr) as string;
    }
    
    const date = new Date(timeStr);
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    
    let result: string;
    
    // 小于1分钟
    if (diff < 60 * 1000) {
      result = '刚刚';
    }
    // 小于1小时
    else if (diff < 60 * 60 * 1000) {
      result = `${Math.floor(diff / (60 * 1000))}分钟前`;
    }
    // 小于24小时
    else if (diff < 24 * 60 * 60 * 1000) {
      result = `${Math.floor(diff / (60 * 60 * 1000))}小时前`;
    }
    // 小于7天
    else if (diff < 7 * 24 * 60 * 60 * 1000) {
      result = `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`;
    }
    // 其他情况显示完整日期
    else {
      result = date.toLocaleDateString();
    }
    
    // 存入缓存
    timeCache.value.set(timeStr, result);
    return result;
  };
  
  return {
    formatTime
  };
} 