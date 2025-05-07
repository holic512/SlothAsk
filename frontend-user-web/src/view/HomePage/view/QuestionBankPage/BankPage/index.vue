<script setup>
import {onMounted, ref} from 'vue';
import {setTitle} from '@/utils/title';
import CategoryClass from './components/CategoryClass.vue';
import {useProjectStore} from "./store/ProjectStore.ts"

const projectStore = useProjectStore();
const isLoading = ref(true);

onMounted(async () => {
  setTitle('题库');
  try {
    await projectStore.fetchProjects();
  } catch (error) {
    console.error('获取项目时出错:', error);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="bank-container">
    <el-scrollbar height="100%">
      <div class="page-content">
        <div v-if="isLoading" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else>
          <div v-for="project in projectStore.projects" :key="project.id" class="project-section">
            <h2 class="project-title">{{ project.name }}</h2>
            <CategoryClass :projectId="project.id" />
          </div>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>


<style scoped>
.bank-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding: 0 36px;
}

.page-content {
  width: 100%;
  max-width: 1380px;
  margin: 2rem auto 0;
  box-sizing: border-box;
}

.project-title {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: #1a1a1a;
  position: relative;
  padding-left: 1rem;
  border-left: 4px solid #409eff;
}

.loading-container {
  padding: 2rem;
}

@media (max-width: 768px) {
  .page-content {
    margin-top: 1rem;
  }
  
  .project-title {
    font-size: 1.25rem;
  }
}
</style>