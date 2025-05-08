import {defineStore} from 'pinia';
import {ref} from 'vue';
import type {ScrollbarInstance} from 'element-plus';

export const useScrollbarStore = defineStore('scrollbar', () => {
  // el-scrollbar引用，使用正确的类型定义
  const scrollbarRef = ref<ScrollbarInstance | null>(null);

  // 设置scrollbar到顶部
  const scrollToTop = () => {
    if (scrollbarRef.value) {
      scrollbarRef.value.setScrollTop(0);
    } else {
      console.warn('Scrollbar reference not found when trying to scroll to top');
    }
  };

  // 设置scrollbar到指定位置
  const scrollTo = (top: number) => {
    if (scrollbarRef.value) {
      scrollbarRef.value.setScrollTop(top);
    } else {
      console.warn(`Scrollbar reference not found when trying to scroll to ${top}px`);
    }
  };

  return {
    scrollbarRef,
    scrollToTop,
    scrollTo
  };
}); 