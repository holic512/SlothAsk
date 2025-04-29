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
    console.log('加载的项目:', projectStore.projects); // 调试日志
  } catch (error) {
    console.error('获取项目时出错:', error);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="container">
    <div class="page-content">
        <div v-for="project in projectStore.projects" :key="project.id" class="project-section">
          <h2 class="project-title">{{ project.name }}</h2>
          <CategoryClass :projectId="project.id" />
        </div>
    </div>
  </div>
</template>


<style scoped>
.container {
  flex: 1;
  display: flex;
  justify-content: center;
  height: 100%;

}


.page-content {
  width: 1380px;
  margin-top: 50px;
  padding: 12px;
  transition: all 0.3s ease;
}

.project-title {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #1a1a1a;
  position: relative;
  padding-left: 60px;
}


</style>