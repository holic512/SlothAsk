<script lang="ts" setup>
import {defineEmits, defineProps} from 'vue';
import {UserInfo} from '@/view/HomePage/view/UserProfile/interface/UserInfoInterface';

const props = defineProps<{
  userInfo: UserInfo;
}>();

const emit = defineEmits<{
  (e: 'edit-profile'): void;
  (e: 'toggle-follow'): void;
}>();

const genderIcons = {
  0: '⚧',
  1: '♂',
  2: '♀'
};

const genderMap = {
  0: '保密',
  1: '男',
  2: '女'
};

const handleEditProfile = () => {
  emit('edit-profile');
};
</script>

<template>
  <div class="profile">
    <!-- 头像 -->
    <div class="avatar">
      <el-avatar
          :src="userInfo.avatar"
          alt="用户头像"
          class="img"
      >{{ userInfo.nickname.charAt(1) }}
      </el-avatar>
    </div>

    <!-- 信息区 -->
    <div class="details">
      <!-- 名称 & 操作 -->
      <div class="header-row">
        <div class="name-block">
          <h1 class="name">{{ userInfo.nickname || '用户名' }}</h1>
          <el-tag :disable-transitions="true" effect="light" type="primary">{{ userInfo.codingAge || '0年' }} 码龄
          </el-tag>
        </div>
        <div class="action-block">
          <button
              v-if="userInfo.isSelf"
              class="btn edit"
              @click="handleEditProfile"
          >编辑资料
          </button>
          <button
              v-else
              :class="{ followed: userInfo.isFollowed }"
              class="btn follow"
              @click="$emit('toggle-follow')"
          >{{ userInfo.isFollowed ? '已关注' : '关注' }}
          </button>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="info-row">
        <div class="info-item">
          <span class="label">性别：</span>
          <el-tag v-if="userInfo.gender && userInfo.gender !== 0" effect="light">
            {{ genderIcons[userInfo.gender] }} {{ genderMap[userInfo.gender] }}
          </el-tag>
          <el-tag v-else :disable-transitions="true" effect="light" size="small" type="info">保密</el-tag>
        </div>
        <el-divider direction="vertical"/>
        <div class="info-item">
          <span class="label">年龄：</span>
          <el-tag v-if="userInfo.age" :disable-transitions="true" effect="light">{{ userInfo.age }} 岁</el-tag>
          <el-tag v-else :disable-transitions="true" effect="light" size="small" type="info">保密</el-tag>
        </div>
        <el-divider direction="vertical"/>
        <div class="info-item">
          <span class="label">加入时间：</span>
          <el-text>{{ userInfo.joinDate || '未知' }}</el-text>
        </div>
        <el-divider direction="vertical"/>
        <div class="info-item">
          <span class="label">生日：</span>
          <el-tag v-if="userInfo.birthday" :disable-transitions="true" effect="light">{{ userInfo.birthday }}</el-tag>
          <el-tag v-else :disable-transitions="true" effect="light" size="small" type="info">未设置</el-tag>
        </div>
        <el-divider direction="vertical"/>
        <div class="info-item">
          <span class="label">省份：</span>
          <el-tag v-if="userInfo.province" :disable-transitions="true" effect="light">{{ userInfo.province }}</el-tag>
          <el-tag v-else :disable-transitions="true" effect="light" size="small" type="info">未知</el-tag>
        </div>
      </div>

<!--      &lt;!&ndash; 统计数据 &ndash;&gt;-->
<!--      <div class="stats-row">-->
<!--        <div class="stat">-->
<!--          <div class="value">{{ userInfo.followingCount }}</div>-->
<!--          <div class="name">关注</div>-->
<!--        </div>-->
<!--        <div class="stat">-->
<!--          <div class="value">{{ userInfo.followersCount }}</div>-->
<!--          <div class="name">粉丝</div>-->
<!--        </div>-->
<!--      </div>-->

      <!-- 个人简介 -->
      <div class="bio-row">
        <h3 class="bio-title">个人简介</h3>
        <p class="bio-content">{{ userInfo.bio || '暂无个人简介' }}</p>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.profile {
  display: flex;
  gap: 20px;
  padding: 20px;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.avatar {
  flex-shrink: 0;

  .img {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    object-fit: cover;
    border: 3px solid rgba(184, 160, 130, 0.3);
    transition: border-color 0.3s ease;
  }

  .img:hover {
    border-color: rgba(184, 160, 130, 0.5);
  }
}

.details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 标题行 */
.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.name-block {
  display: flex;
  align-items: center;
  gap: 12px;
}

.name {
  font-size: 26px;
  margin: 0;
  color: #6b5b73;
  font-weight: 600;
}

.action-block .btn {
  padding: 8px 18px;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 14px;
}

.btn.edit {
  background: #888;
  color: #fff;
}

.btn.follow {
  background: #666;
  color: #fff;
}

.btn.follow.followed {
  background: #777;
  color: #fff;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.btn:active {
  transform: translateY(0);
}

/* 基本信息行 */
.info-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 12px 0;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.info-item .label {
  font-weight: 500;
  color: #8b7355;
  font-size: 14px;
}

/* 统计行 */
.stats-row {
  display: flex;
  gap: 48px;
}

.stat {
  text-align: center;
}

.stat .value {
  font-size: 20px;
  font-weight: 600;
}

.stat .name {
  font-size: 14px;
  color: var(--text-secondary);
}

/* 简介行 */
.bio-row {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.bio-title {
  flex-shrink: 0;
  width: 80px;
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.bio-content {
  margin: 0;
  line-height: 1.6;
  max-height: 120px;
  overflow-y: auto;
}

/* 响应式 */
@media (max-width: 768px) {
  .profile {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .header-row,
  .info-row,
  .stats-row,
  .bio-row {
    flex-direction: column;
    gap: 12px;
  }

  .bio-row {
    text-align: left;
  }
}
</style>
