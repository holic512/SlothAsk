// VIP会员套餐配置
export interface VipPlan {
  id: number;
  name: string;
  price: number;
  unit: string;
  className: string;
  buttonClassName: string;
  iconColor: string;
  features: string[];
}

export const vipPlansConfig: VipPlan[] = [
  {
    id: 0,
    name: '免费会员',
    price: 0,
    unit: '元/月',
    className: 'free-plan',
    buttonClassName: 'free-button',
    iconColor: '#909399',
    features: [
      '可浏览公开内容',
      '限制部分高级功能',
      '带水印的简历生成'
    ]
  },
  {
    id: 1,
    name: '月付会员',
    price: 5.9,
    unit: '元/月',
    className: 'monthly-plan',
    buttonClassName: 'monthly-button',
    iconColor: '#409EFF',
    features: [
      '招聘信息一键直达',
      'AI高阶解析',
      '无水印的简历生成',
      '智能笔试组卷'
    ]
  },
  {
    id: 2,
    name: '永久会员',
    price: 59,
    unit: '元/永久',
    className: 'lifetime-plan',
    buttonClassName: 'lifetime-button',
    iconColor: '#F56C6C',
    features: [
      '招聘信息一键直达',
      'AI高阶解析',
      '无水印的简历生成',
      '智能笔试组卷'
    ]
  }
];

// 公告配置
export const announcementConfig = {
  text: '加v:pink996611,获取免费会员',
  iconColor: '#67C23A'
};