// 卡片配置文件
export const cardConfig = {
  // 卡片颜色配置 - 多彩背景与图标对比度优化
  colors: [
    { front: '#2c3e50', back: '#e74c3c' }, // 深蓝灰 - 适配黄色图标
    { front: '#8e44ad', back: '#f39c12' }, // 紫色 - 适配黄蓝图标
    { front: '#f8f9fa', back: '#1abc9c' }, // 白色 - 适配蓝红图标
    { front: '#e8f4fd', back: '#34495e' }, // 浅蓝 - 适配蓝色图标
    { front: '#2c3e50', back: '#e91e63' }, // 深蓝灰 - 适配蓝绿图标
    { front: '#fff3cd', back: '#8e44ad' }, // 浅黄 - 适配蓝色图标
    { front: '#e1f5fe', back: '#d35400' }, // 浅青 - 适配蓝色图标
    { front: '#f8f9fa', back: '#c0392b' }, // 白色 - 适配黑色图标
    { front: '#e8f5e8', back: '#27ae60' }, // 浅绿 - 适配黑色图标
    { front: '#fce4ec', back: '#e74c3c' }, // 浅粉 - 适配蓝红图标
    { front: '#2c3e50', back: '#f39c12' }, // 深蓝灰 - 适配绿色图标
    { front: '#1a237e', back: '#1abc9c' }, // 深蓝 - 适配绿色图标
    { front: '#fff8e1', back: '#9b59b6' }, // 浅橙 - 适配蓝色图标
    { front: '#f3e5f5', back: '#3498db' }, // 浅紫 - 适配蓝色图标
    { front: '#e0f2f1', back: '#2980b9' }, // 浅青绿 - 适配蓝色图标
    { front: '#fff9c4', back: '#16a085' }, // 浅黄绿 - 适配蓝色图标
    { front: '#f8f9fa', back: '#e74c3c' }, // 白色 - 适配红色图标
    { front: '#1b5e20', back: '#27ae60' }, // 深绿 - 适配绿色图标
    { front: '#ffebee', back: '#c0392b' }, // 浅红 - 适配红色图标
    { front: '#fff3e0', back: '#8b4513' }, // 浅橙 - 适配棕色图标
    { front: '#263238', back: '#ff6b35' }, // 深青灰 - 适配橙色图标
    { front: '#1a237e', back: '#ff8c42' }, // 深蓝 - 适配橙色图标
    { front: '#f1f8e9', back: '#dc3545' }, // 浅绿 - 适配红色图标
    { front: '#e3f2fd', back: '#007bff' }, // 浅蓝 - 适配蓝色图标
    { front: '#f3e5f5', back: '#0056b3' }  // 浅紫 - 适配蓝色图标
  ],

  // 卡片数据配置（5x5网格）- 编程语言和技术栈
  cards: [
    { name: 'JavaScript', frontText: '', frontIcon: '/IntroPage/second/JavaScript.png', label: 'JS能做一切，从前端到后端，无所不能！' },
    { name: 'Python', frontText: '', frontIcon: '/IntroPage/second/Python.png', label: '人生苦短，我用Python！' },
    { name: 'Java', frontText: '', frontIcon: '/IntroPage/second/java.png', label: '一次编写，到处运行，Java永远的神！' },
    { name: 'React', frontText: '', frontIcon: '/IntroPage/second/React.png', label: 'React让前端开发如丝般顺滑！' },
    { name: 'Vue', frontText: '', frontIcon: '/IntroPage/second/Vue.png', label: 'Vue.js - 渐进式框架，简单易学！' },
    { name: 'TypeScript', frontText: '', frontIcon: '/IntroPage/second/typescript.png', label: 'TypeScript让JavaScript有了类型的翅膀！' },
    { name: 'Golang', frontText: '', frontIcon: '/IntroPage/second/golang.png', label: 'Go语言：简单、快速、可靠！' },
    { name: 'Rust', frontText: '', frontIcon: '/IntroPage/second/rust.png', label: 'Rust：内存安全与性能的完美结合！' },
    { name: 'C', frontText: '', frontIcon: '/IntroPage/second/C.png', label: 'C语言：编程语言之母，永远的经典！' },
    { name: 'Kotlin', frontText: '', frontIcon: '/IntroPage/second/kotlin.png', label: 'Kotlin：Android开发的新宠儿！' },
    { name: 'Spring Boot', frontText: '', frontIcon: '/IntroPage/second/SPRINGBOOT.png', label: 'Spring Boot让Java开发飞起来！' },
    { name: 'Django', frontText: '', frontIcon: '/IntroPage/second/django.png', label: 'Django：完美主义者的Web框架！' },
    { name: 'Docker', frontText: '', frontIcon: '/IntroPage/second/docker.png', label: 'Docker：一次构建，到处运行！' },
    { name: 'Kubernetes', frontText: '', frontIcon: '/IntroPage/second/k8s.png', label: 'K8s：容器编排的王者！' },
    { name: 'MySQL', frontText: '', frontIcon: '/IntroPage/second/mysql.png', label: 'MySQL：世界上最流行的开源数据库！' },
    { name: 'MongoDB', frontText: '', frontIcon: '/IntroPage/second/mongoDB.png', label: 'MongoDB：文档数据库的领导者！' },
    { name: 'Redis', frontText: '', frontIcon: '/IntroPage/second/Redis.png', label: 'Redis：缓存界的闪电侠！' },
    { name: 'Nginx', frontText: '', frontIcon: '/IntroPage/second/nginx.png', label: 'Nginx：高性能Web服务器之王！' },
    { name: 'Apache', frontText: '', frontIcon: '/IntroPage/second/apache.png', label: 'Apache：Web服务器的老大哥！' },
    { name: 'Kafka', frontText: '', frontIcon: '/IntroPage/second/Kafka.png', label: 'Kafka：消息队列的高速公路！' },
    { name: 'RabbitMQ', frontText: '', frontIcon: '/IntroPage/second/rabbitmq.png', label: 'RabbitMQ：消息传递的可靠伙伴！' },
    { name: 'Elasticsearch', frontText: '', frontIcon: '/IntroPage/second/elasticsearch-Elasticsearch.png', label: 'ES：搜索引擎界的瑞士军刀！' },
    { name: 'Zookeeper', frontText: '', frontIcon: '/IntroPage/second/zookeeper.png', label: 'Zookeeper：分布式系统的协调者！' },
    { name: 'MinIO', frontText: '', frontIcon: '/IntroPage/second/minio.png', label: 'MinIO：对象存储的轻量级选手！' },
    { name: 'Element Plus', frontText: '', frontIcon: '/IntroPage/second/element-plus.png', label: 'Element Plus：Vue3的UI组件库之光！' }
  ]
}

// 获取指定数量的卡片配置
export const getCardConfigs = (count = 25) => {
  const configs = []
  for (let i = 0; i < count; i++) {
    const cardIndex = i % cardConfig.cards.length
    const colorIndex = i % cardConfig.colors.length
    
    configs.push({
      id: i,
      ...cardConfig.cards[cardIndex],
      frontBg: cardConfig.colors[colorIndex].front
    })
  }
  return configs
}