export interface UserInfo {
  /** 用户头像URL（必填） */
  avatar: string;
  /** 用户昵称/用户名（必填） */
  nickname: string;
  /** 编程年龄（如："3年"） */
  codingAge: string;
  /** 关注人数（必填） */
  followingCount: number;
  /** 粉丝数量（必填） */
  followersCount: number;
  /** 加入日期（格式："YYYY-MM-DD"） */
  joinDate: string;
  /** 个人简介/签名（可选） */
  bio?: string;
  /** 性别（可选：0保密,1男性,2女性） */
  gender?: number;
  /** 年龄（可选） */
  age?: number;
  /** 当前登录用户是否已关注该用户（可选） */
  isFollowed?: boolean;
  /** 是否为用户本人访问自己的主页（可选） */
  isSelf?: boolean;
  /** 生日（格式："YYYY-MM-DD"，可选） */
  birthday?: string;
  /** 所在省份（如："广东省"，可选） */
  province?: string;
}