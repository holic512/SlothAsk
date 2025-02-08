export interface DeptInfo {
  id: number
  name: string
  parentId: number
  sort: number
  children?: DeptInfo[]
}

export interface DeptForm {
  name: string
  parentId?: number
  sort: number
}

export interface DeptSearchParams {
  name?: string
  status?: string
} 