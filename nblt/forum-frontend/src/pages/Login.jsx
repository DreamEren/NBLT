import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'
import { setToken, setUser } from '../utils/token'
import './Auth.css'

function Login({ onLoginSuccess }) {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
    setError('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      const res = await api.login(formData)
      const token = res.data?.token

      if (token) {
        setToken(token)
        // 后端返回的字段在 data 中，组装成 user 对象
        const user = res.data?.user || {
          id: res.data?.userId,
          username: res.data?.username,
          nickname: res.data?.nickname,
          avatar: res.data?.avatar,
        }
        if (user.id) {
          setUser(user)
        }
        if (onLoginSuccess) {
          onLoginSuccess()
        }
      } else {
        setError('登录失败，未获取到 token')
      }
    } catch (err) {
      setError(err.message || '登录失败')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2>用户登录</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>用户名</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              placeholder="请输入用户名"
            />
          </div>
          <div className="form-group">
            <label>密码</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="请输入密码"
            />
          </div>
          {error && <div className="error">{error}</div>}
          <button
            type="submit"
            className="btn-primary"
            disabled={loading}
            style={{ width: '100%', marginTop: '16px' }}
          >
            {loading ? '登录中...' : '登录'}
          </button>
        </form>
        <p className="auth-link">
          还没有账号？<Link to="/register">立即注册</Link>
        </p>
      </div>
    </div>
  )
}

export default Login
