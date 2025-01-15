<script setup>
import { ref } from 'vue'
import {useRouter} from "vue-router"


const time = ref({
  year: new Date().getFullYear(),
  month: new Date().getMonth() + 1,
  day: new Date().getDate(),
});

const router = useRouter();

const loginIn = () => {
  router.push({ name: 'mailLogin' });
};
</script>

<template>
  <div class="container">

    <div class="left-space">
      <p>SlOTH</p>
      <p>ASK</p>
      <div class="button-container">
        <el-button type="primary" @click="loginIn" class="login-btn">LOGIN</el-button>
      </div>
    </div>

    <div class="right-space">
      <p class="time-label">NOW TIME</p>
      <div class="time-box">
        <div class="time-item">
          <p>{{ time.year }}</p>
          <div class="separator"></div>
          <p>YEAR</p>
        </div>
        <div class="time-item">
          <p>{{ time.month }}</p>
          <div class="separator"></div>
          <p>MON</p>
        </div>
        <div class="time-item">
          <p>{{ time.day }}</p>
          <div class="separator"></div>
          <p>DAY</p>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

.button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-btn {
  background-color: rgb(0,47,167);
  border-radius: 20px;
  color: #fff;
  font-size: 30px;
  padding: 40px 80px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border: none;
  transition: all 0.3s ease;
}
.login-btn:hover {
  transform: translateY(-3px);
}
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.left-space {
  text-align: center;
  font-size: xxx-large;
  font-weight: 900;
  line-height: 10px;
  width: 40%;
  height: 100%;
  background-color:#FCFCFC;
  padding: 10px;
}

.right-space {
  width: 60%;
  height: 100%;
  background: linear-gradient(to right,rgb(0,47,167), rgb(255,255,255));
  padding: 10px;
  text-align: center;
}
.time-label{
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 20px;
}
.time-box {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px; /* 设置盒子之间的间隔 */
}

.time-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  background-color: rgba(169, 169, 169, 0.5);
  padding: 20px;
  border-radius: 10px;
}

.separator {
  width: 40px;
  height: 2px;
  background-color: #000;
  margin: 10px 0;
}
</style>