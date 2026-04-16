import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api'
import { isLoggedIn } from '../utils/token'
import './CreatePost.css'

function CreatePost() {
  const [formData, setFormData] = useState({
    title: '',
    content: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    if (!isLoggedIn()) {
      navigate('/login')
    }
  }, [navigate])

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
      await api.createPost(formData)
      alert('发布成功！')
      navigate('/', { replace: true })
    } catch (err) {
      setError(err.message || '发布失败')
      setLoading(false)
    }
  }

  return (
    <div className="create-post">
      <div className="post-list-header">
        <h1>发布新帖子</h1>
      </div>
      <div className="search-bar" style={{padding: '20px'}}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="标题：请输入帖子标题"
              style={{fontSize: '16px', padding: '12px'}}
            />
          </div>
          <div className="form-group">
            <textarea
              name="content"
              value={formData.content}
              onChange={handleChange}
              required
              rows={12}
              placeholder="内容：请输入帖子内容..."
              style={{minHeight: '300px', lineHeight: '1.8'}}
            />
          </div>
          {error && <div className="error">{error}</div>}
          <div className="form-actions">
            <button
              type="button"
              className="btn-secondary"
              onClick={() => navigate('/')}
            >
              取消
            </button>
            <button
              type="submit"
              className="btn-primary"
              disabled={loading}
            >
              {loading ? '发布中...' : '发布'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default CreatePost
