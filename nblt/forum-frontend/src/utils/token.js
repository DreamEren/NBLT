const TOKEN_KEY = 'token'
const USER_KEY = 'user'

export const setToken = (token) => {
  localStorage.setItem(TOKEN_KEY, token)
}

export const getToken = () => {
  return localStorage.getItem(TOKEN_KEY)
}

export const removeToken = () => {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

export const isLoggedIn = () => {
  return !!getToken()
}

// 存储当前用户信息
export const setUser = (user) => {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

// 获取当前用户信息
export const getUser = () => {
  const userStr = localStorage.getItem(USER_KEY)
  if (!userStr) return null
  try {
    return JSON.parse(userStr)
  } catch {
    return null
  }
}

// 获取当前用户ID
export const getCurrentUserId = () => {
  const user = getUser()
  return user ? user.id : null
}
