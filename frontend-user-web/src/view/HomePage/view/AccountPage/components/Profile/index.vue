<script setup lang="ts">
import {ref, computed, onMounted} from 'vue';
import {ElMessage} from 'element-plus';
import {Edit, Upload, Delete, Location, Calendar} from '@element-plus/icons-vue';
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import {apiUpdateAvatar} from "@/view/HomePage/view/AccountPage/components/Profile/service/ApiUpdateAvatar";
import {apiDeleteAvatar} from "@/view/HomePage/view/AccountPage/components/Profile/service/ApiDeleteAvatar";
import {getUserProfile} from "@/view/HomePage/view/AccountPage/components/Profile/service/ApiGetUserProfile";
import {updateUserProfile} from "@/view/HomePage/view/AccountPage/components/Profile/service/ApiUpdateUserProfile";


interface UserProfileData {
  avatar: string;
  nickname: string;
  gender: number;
  age: number | null;
  bio: string | null;
  birthday: string | null;
  location: string | null;
  occupation: number | null;
}

const userInfo = ref<UserProfileData>({
  avatar: '',
  nickname: '',
  gender: 0, // 性别：男性、女性、保密
  age: null,
  bio: null,
  birthday: null,
  location: null,
  occupation: null
});

// 性别选项
const genderOptions = [
  {label: '男性', value: 1, color: '#409EFF'},
  {label: '女性', value: 2, color: '#FF69B4'},
  {label: '保密', value: 0, color: '#909399'}
];


// 处理头像上传
const handleAvatarUpload = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target && target.files && target.files[0]) {
    const file = target.files[0];

    // 文件类型限制：只允许 jpg 和 png 格式
    const allowedTypes = ['image/jpeg', 'image/png'];
    if (!allowedTypes.includes(file.type)) {
      ElMessage.error('只能上传 JPG 或 PNG 格式的头像');
      return;
    }

    // 文件大小限制：最大 5MB
    const maxSize = 5 * 1024 * 1024; // 5MB
    if (file.size > maxSize) {
      ElMessage.error('头像文件最大为 5MB');
      return;
    }

    try {
      // 上传头像
      const response = await apiUpdateAvatar(file);

      if (response.status === 200) {
        // 更新用户头像
        userInfo.value.avatar = response.data;
        ElMessage.success('头像上传成功');
      } else {
        ElMessage.error('头像上传失败');
      }
    } catch (error) {
      ElMessage.error('上传头像时发生错误');
    }
  }
};


// 处理头像删除
const handleAvatarDelete = () => {
  userInfo.value.avatar = '';
  apiDeleteAvatar()
  ElMessage.success('头像已删除');
};

// 处理个人资料更新
const handleUpdateProfile = async () => {
  try {
    // 调用更新用户资料API
    const response = await updateUserProfile({
      nickname: userInfo.value.nickname,
      gender: userInfo.value.gender,
      age: userInfo.value.age,
      birthday: userInfo.value.birthday,
      location: userInfo.value.location,
      occupation: userInfo.value.occupation,
      bio: userInfo.value.bio
    });

    if (response.status === 200) {
      ElMessage.success('个人资料更新成功');
    } else {
      ElMessage.error(response.message || '更新失败');
    }
  } catch (error) {
    console.error('更新个人资料时发生错误:', error);
    ElMessage.error('更新个人资料时发生错误');
  }
};

// 获取头像显示内容
const avatarContent = computed(() => {
  if (userInfo.value.nickname[0])
    return userInfo.value.avatar || userInfo.value.nickname[0].toUpperCase();
});

// 省份数据
const provinces = [
  '北京市', '上海市', '广东省', '江苏省', '浙江省', '四川省', '湖北省', '河南省',
  '山东省', '河北省', '陕西省', '福建省', '湖南省', '安徽省', '江西省', '重庆市'
];

onMounted(async () => {
  const response = await getUserProfile();
  if(response.status === 200){
    userInfo.value = response.data as UserProfileData;
  }


})
</script>

<template>
  <div class="profile-container">
    <!-- 头像部分 -->
    <div class="avatar-section">
      <div class="avatar-wrapper">
        <el-avatar :size="120" :src="userInfo.avatar" v-if="userInfo.avatar">
          {{ avatarContent }}
        </el-avatar>
        <el-avatar :size="120" v-else>{{ avatarContent }}</el-avatar>

        <!-- 头像编辑按钮和下拉菜单 -->
        <div class="avatar-edit-btn">
          <el-dropdown trigger="click">

            <el-button class="edit-icon-btn" :icon="Edit" circle size="large"/>

            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <label class="upload-label">
                    <el-icon>
                      <Upload/>
                    </el-icon>
                    上传头像
                    <input type="file" accept="image/*" @change="handleAvatarUpload" style="display: none">
                  </label>
                </el-dropdown-item>
                <el-dropdown-item @click="handleAvatarDelete">
                  <el-icon>
                    <Delete/>
                  </el-icon>
                  删除头像
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 基本信息表单 -->
    <div class="form-container">
      <div class="form-row">
        <div class="form-group flex-1">
          <label class="form-label">昵称</label>
          <el-input
              v-model="userInfo.nickname"
              class="slim-input"
              placeholder="请输入昵称"
          />
        </div>

        <div class="form-group flex-1">
          <label class="form-label">性别</label>
          <div class="gender-selector">
            <div
                v-for="option in genderOptions"
                :key="option.value"
                class="gender-option"
                :class="{ active: userInfo.gender === option.value }"
                :style="{ '--gender-color': option.color }"
                @click="userInfo.gender = option.value"
            >
              {{ option.label }}
            </div>
          </div>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group flex-1">
          <label class="form-label">生日</label>
          <el-date-picker
              v-model="userInfo.birthday"
              type="date"
              class="slim-input"
              placeholder="选择生日"
              :disabled-date="date => date > new Date()"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              :locale="zhCn"
          />
        </div>

        <div class="form-group flex-1">
          <label class="form-label">现居地</label>
          <el-select
              v-model="userInfo.location"
              class="slim-input"
              placeholder="请选择现居地"
          >
            <el-option
                v-for="province in provinces"
                :key="province"
                :label="province"
                :value="province"
            />
          </el-select>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group flex-1">
          <label class="form-label">年龄</label>
          <el-input-number
              v-model="userInfo.age"
              :min="1"
              :max="100"
              class="slim-input age-input"
              placeholder="请输入年龄"
          />
        </div>

        <div class="form-group flex-1">
          <label class="form-label">职业</label>
          <el-select
              v-model="userInfo.occupation"
              class="slim-input"
              placeholder="请选择职业"
          >
            <el-option label="在校学生" :value="1"/>
            <el-option label="自由职业者（创业者/个体户）" :value="2"/>
            <el-option label="上班族（职员/白领）" :value="3"/>
            <el-option label="技术工程师（IT/制造业）" :value="4"/>
            <el-option label="创意设计师（平面/UI/工业）" :value="5"/>
            <el-option label="市场与销售（业务员/顾问）" :value="6"/>
            <el-option label="教育工作者（教师/讲师）" :value="7"/>
            <el-option label="医疗从业者（医生/护士）" :value="8"/>
            <el-option label="管理与行政（HR/运营）" :value="9"/>
            <el-option label="其他（暂未归类）" :value="10"/>

          </el-select>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label">个人简介</label>
        <el-input
            v-model="userInfo.bio"
            type="textarea"
            class="slim-textarea"
            :rows="4"
            placeholder="请输入个人简介"
        />
      </div>
    </div>

    <div class="form-footer">
      <button class="save-button" @click="handleUpdateProfile">保存修改</button>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 700px;
  margin: 0 auto;
  padding: 40px 40px;

  background-color: white;
  border-radius: 12px;
}

.profile-title {
  text-align: center;
  margin: 0 0 40px;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 48px;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
}

.avatar-edit-btn {
  position: absolute;
  right: -6px;
  bottom: -6px;
}

.edit-icon-btn {
  background: #fff;
  border: 2px solid #eee;
  padding: 6px;
  font-size: 16px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-row {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
}

.flex-1 {
  flex: 1;
}

.form-group {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.form-label {
  width: 70px;
  font-size: 14px;
  font-weight: 500;
  color: #1a1a1a;
  line-height: 32px;
  text-align: right;
  flex-shrink: 0;
}

.slim-input {
  flex: 1;
}

:deep(.el-input__wrapper) {
  padding: 1px 11px;
}

:deep(.el-input__inner) {
  height: 32px;
  font-size: 13px;
}

:deep(.el-textarea__inner) {
  padding: 8px 11px;
  font-size: 13px;
  min-height: 32px;
}

.gender-selector {
  display: flex;
  gap: 12px;
  flex: 1;
}

.gender-option {
  flex: 1;
  text-align: center;
  padding: 6px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #f5f5f5;
  color: #1a1a1a;
  font-size: 13px;
}

.gender-option:hover {
  background-color: #ebebeb;
}

.gender-option.active {
  background-color: var(--gender-color);
  color: white;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  font-size: 13px;
}

.upload-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  width: 100%;
}

.age-slider :deep(.el-slider__button) {
  border-color: #60a5fa;
}

.age-slider :deep(.el-slider__bar) {
  background-color: #60a5fa;
}

.form-footer {
  display: flex;
  justify-content: center;
  margin-top: 48px;
}

.save-button {
  padding: 10px 32px;
  font-size: 16px;
  font-weight: 500;
  color: white;
  background-color: #60a5fa;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.save-button:hover {
  background-color: #3b82f6;
}

:deep(.el-date-editor.el-input),
:deep(.el-select) {
  width: 100%;
}
</style> 