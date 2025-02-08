import { useCategoryStore } from '../pinia/categoryStore'

export const handleSelectionChange = (rows: any[]) => {
  const categoryStore = useCategoryStore()
  categoryStore.selectedRows = rows
} 