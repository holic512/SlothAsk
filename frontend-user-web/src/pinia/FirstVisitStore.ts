import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useFirstVisitStore = defineStore('firstVisit', () => {
  // 检查是否是首次访问
  const isFirstVisit = ref<boolean>(!localStorage.getItem('hasVisited'))
  
  // 标记为已访问
  const markAsVisited = () => {
    isFirstVisit.value = false
    localStorage.setItem('hasVisited', 'true')
  }
  
  // 重置首次访问状态（用于测试或重置功能）
  const resetFirstVisit = () => {
    isFirstVisit.value = true
    localStorage.removeItem('hasVisited')
  }
  
  return {
    isFirstVisit,
    markAsVisited,
    resetFirstVisit
  }
})