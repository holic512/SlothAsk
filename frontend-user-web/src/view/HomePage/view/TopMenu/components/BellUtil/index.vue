<!-- 消息通知 -->
<script lang="ts" setup>

import BButton from "@/view/HomePage/view/TopMenu/components/BellUtil/BButton.vue";
import TopBaseBox from "@/view/HomePage/view/TopMenu/components/BellUtil/TopBaseBox.vue";
</script>

<template>

  <el-popover
      :hide-after="50"
      :popper-style="{ padding: '0', borderRadius: '16px' }"
      :width="442"
      placement="bottom"
      popper-class="custom-avatar-popover"
      trigger="click"
  >
    <template #reference>
      <BButton/>
    </template>
    <div>
      <TopBaseBox/>
    </div>


  </el-popover>

</template>

<style scoped>
/* 使用深度选择器修改当前页面的popover样式 */
:deep(.custom-avatar-popover) {
  padding: 0 !important;
  border-radius: 16px !important;
}

/* 针对Element Plus的具体类名进行更精确的覆盖 */
:deep(.el-popover.custom-avatar-popover) {
  padding: 0 !important;
  border-radius: 16px !important;
}

:deep(.custom-avatar-popover .el-popper__arrow) {
  display: none;
}
</style>