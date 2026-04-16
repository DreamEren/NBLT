import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api'
import { getUser, removeToken } from '../utils/token'
import './Profile.css'

function Profile() {
  const navigate = useNavigate()
  const [user, setUser] = useState(null)
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const currentUser = getUser()
    if (!currentUser) {
      navigate('/login')
      return
    }
    setUser(currentUser)
    loadUserPosts()
  }, [navigate])

  const loadUserPosts = async () => {
    try {
      setLoading(true)
      setError('')
      const res = await api.getMyPosts()
      setPosts(res.data || [])
    } catch (err) {
      setError(err.message || '加载失败')
    } finally {
      setLoading(false)
    }
  }

  const handleDeletePost = async (postId) => {
    if (!window.confirm('确定要删除这个帖子吗？')) {
      return
    }

    try {
      await api.deletePost(postId)
      await loadUserPosts()
      alert('删除成功')
    } catch (err) {
      alert(err.message || '删除失败')
    }
  }

  const handleLogout = () => {
    if (!window.confirm('确定要退出登录吗？')) {
      return
    }
    removeToken()
    navigate('/')
  }

  if (!user) {
    return <div className="loading">加载中...</div>
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <h1>个人中心</h1>
        <div className="user-info">
          <div className="info-row">
            <span className="label">用户名：</span>
            <span className="value">{user.username}</span>
          </div>
          <div className="info-row">
            <span className="label">昵称：</span>
            <span className="value">{user.nickname}</span>
          </div>
        </div>
        <button onClick={handleLogout} className="btn-secondary">
          退出登录
        </button>
      </div>

      <div className="my-posts-section">
        <h2>我发布的帖子</h2>

        {loading ? (
          <div className="loading">加载中...</div>
        ) : error ? (
          <div className="error">{error}</div>
        ) : posts.length === 0 ? (
          <div className="empty">
            <p>还没有发布过帖子</p>
            <a href="/create-post" target="_blank" rel="noopener noreferrer" className="btn-primary">去发帖</a>
          </div>
        ) : (
          <div className="posts-list">
            {posts.map((post) => (
              <div key={post.id} className="post-item">
                <div className="post-item-main">
                  <Link to={`/post/${post.id}`} className="post-item-title">
                    {post.title}
                  </Link>
                  <p className="post-item-summary">
                    {post.content?.substring(0, 100)}...
                  </p>
                </div>
                <button
                  onClick={() => handleDeletePost(post.id)}
                  className="btn-text"
                >
                  删除
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default Profile
