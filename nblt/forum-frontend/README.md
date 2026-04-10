# Forum Frontend

论坛系统前端项目 - React + Vite

## 技术栈

- React 18
- Vite 5
- React Router 6
- JavaScript (ES6+)

## 项目结构

```
forum-frontend/
├── index.html          # 入口 HTML
├── package.json        # 项目依赖
├── vite.config.js      # Vite 配置
├── README.md           # 项目说明
└── src/
    ├── main.jsx        # 应用入口
    ├── App.jsx         # 根组件
    ├── App.css         # 根组件样式
    ├── index.css       # 全局样式
    ├── api/
    │   └── index.js    # API 请求封装
    ├── utils/
    │   └── token.js    # Token 处理工具
    └── pages/
        ├── Login.jsx       # 登录页
        ├── Register.jsx    # 注册页
        ├── PostList.jsx    # 帖子列表页
        ├── PostDetail.jsx  # 帖子详情页
        ├── CreatePost.jsx  # 发帖页
        ├── Auth.css        # 认证页面样式
        ├── PostList.css    # 帖子列表样式
        ├── PostDetail.css  # 帖子详情样式
        └── CreatePost.css  # 发帖页面样式
```

## 页面路由

| 路径 | 页面 | 说明 |
|------|------|------|
| `/` | 帖子列表 | 展示所有帖子 |
| `/post/:id` | 帖子详情 | 查看帖子内容及评论 |
| `/create` | 发布帖子 | 创建新帖子（需登录） |
| `/login` | 登录 | 用户登录 |
| `/register` | 注册 | 用户注册 |

## 后端接口

默认后端地址: `http://127.0.0.1:8080/api`

| 接口 | 方法 | 说明 |
|------|------|------|
| `/user/register` | POST | 用户注册 |
| `/auth/login` | POST | 用户登录 |
| `/post/list` | GET | 获取帖子列表 |
| `/post/:id` | GET | 获取帖子详情 |
| `/post/create` | POST | 发布帖子（需 Bearer Token） |
| `/comment/create` | POST | 发表评论（需 Bearer Token） |

## 开发命令

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 功能特性

- ✅ 用户注册/登录
- ✅ JWT Token 存储（localStorage）
- ✅ 帖子列表展示
- ✅ 帖子详情查看
- ✅ 发表评论（需登录）
- ✅ 发布帖子（需登录）
- ✅ 自动携带 Token 进行认证请求

## 开发服务器配置

开发服务器默认运行在 `http://localhost:3000`

Vite 配置中已添加代理，将 `/api` 请求转发到后端 `http://127.0.0.1:8080`
