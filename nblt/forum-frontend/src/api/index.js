import { getToken } from '../utils/token'

const API_BASE_URL = '/api'

async function request(url, options = {}) {
  const fullUrl = url.startsWith('http') ? url : `${API_BASE_URL}${url}`

  const response = await fetch(fullUrl, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  })

  const data = await response.json().catch(() => null)

  // HTTP 请求失败
  if (!response.ok) {
    throw new Error(data?.message || data?.error || `请求失败: ${response.status}`)
  }

  // 检查后端统一返回结构中的 code 字段
  // 后端返回格式: { code: 200, message: "...", data: ... }
  if (data && data.code !== undefined && data.code !== 200) {
    throw new Error(data.message || '操作失败')
  }

  return data
}

// 需要认证的请求
async function authRequest(url, options = {}) {
  const token = getToken()
  
  if (!token) {
    throw new Error('用户未登录')
  }

  return request(url, {
    ...options,
    headers: {
      ...options.headers,
      'Authorization': `Bearer ${token}`,
    },
  })
}

export const api = {
  // 用户相关
  register: (data) => request('/user/register', {
    method: 'POST',
    body: JSON.stringify(data),
  }),

  login: (data) => request('/auth/login', {
    method: 'POST',
    body: JSON.stringify(data),
  }),

  // 获取当前登录用户信息
  getCurrentUser: () => authRequest('/user/me'),

  // 获取当前登录用户的帖子列表
  getMyPosts: () => authRequest('/user/my-posts'),

  // 帖子相关
  getPostList: (page = 1, size = 10, keyword = '') => {
    const params = new URLSearchParams({ page, size })
    if (keyword && keyword.trim()) {
      params.append('keyword', keyword.trim())
    }
    return request(`/post/list?${params.toString()}`)
  },
  
  getPostDetail: (id) => request(`/post/${id}`),
  
  createPost: (data) => authRequest('/post/create', {
    method: 'POST',
    body: JSON.stringify(data),
  }),

  deletePost: (id) => authRequest(`/post/${id}`, {
    method: 'DELETE',
  }),

  // 评论相关
  getCommentsByPost: (postId) => request(`/comment/list?postId=${postId}`),

  createComment: (data) => authRequest('/comment/create', {
    method: 'POST',
    body: JSON.stringify(data),
  }),

  deleteComment: (id) => authRequest(`/comment/${id}`, {
    method: 'DELETE',
  }),
}

export default api
