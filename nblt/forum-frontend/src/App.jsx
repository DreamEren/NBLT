import { Routes, Route, Link, useNavigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import PostList from './pages/PostList'
import PostDetail from './pages/PostDetail'
import Login from './pages/Login'
import Register from './pages/Register'
import CreatePost from './pages/CreatePost'
import Profile from './pages/Profile'
import { isLoggedIn, removeToken, getUser } from './utils/token'
import './App.css'

function App() {
  const navigate = useNavigate()
  const [loggedIn, setLoggedIn] = useState(false)
  const [user, setUser] = useState(null)

  useEffect(() => {
    setLoggedIn(isLoggedIn())
    setUser(getUser())
  }, [])

  const handleLogout = () => {
    removeToken()
    setLoggedIn(false)
    setUser(null)
    navigate('/')
    window.location.reload()
  }

  const handleLoginSuccess = () => {
    setLoggedIn(true)
    setUser(getUser())
    navigate('/')
  }

  const leftSections = [
    { title: '公告', color: 'blue', items: ['系统升级通知', '新功能上线', '社区规则'] },
    { title: '置顶', color: '', items: ['如何优雅地提问', '发帖规范指南', '精华帖汇总'] },
    { title: '热榜', color: 'red', items: ['今日热帖', '本周热议', '本月推荐'] },
    { title: '精华', color: 'orange', items: ['优秀文章', '技术分享', '经验总结'] },
    { title: '关注', color: 'green', items: ['关注的作者', '收藏的话题', '订阅的标签'] },
    { title: '投票', color: '', items: ['社区投票', '功能投票', '建议征集'] },
  ]

  const rightSections = [
    { 
      title: '当前用户', 
      type: 'user',
      user: user 
    },
    { 
      title: '我要发言', 
      type: 'button',
      text: '发帖',
      action: '/create-post'
    },
    { 
      title: '热门标签', 
      type: 'tags',
      items: ['#前端', '#后端', '#Java', '#React', '#Vue', '#Node.js', '#数据库', '#微服务']
    },
    { 
      title: '用户协议', 
      type: 'text',
      items: ['用户服务协议', '隐私保护政策', '社区规范']
    },
  ]

  return (
    <div className="app">
      <header className="navbar">
        <div className="nav-container">
          <Link to="/" className="logo">
            论坛
          </Link>
          <nav className="nav-links">
            {loggedIn ? (
              <>
                <span className="user-greeting">{user?.nickname || user?.username}</span>
                <a href="/create-post" target="_blank" rel="noopener noreferrer" className="btn btn-primary btn-sm">发帖</a>
                <Link to="/profile" className="btn btn-secondary btn-sm">个人中心</Link>
                <button onClick={handleLogout} className="btn btn-text btn-sm">退出</button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-secondary btn-sm">登录</Link>
                <Link to="/register" className="btn btn-primary btn-sm">注册</Link>
              </>
            )}
          </nav>
        </div>
      </header>

      <main className="main-content">
        <div className="main-container">
          <aside className="left-sidebar">
            {leftSections.map((section, index) => (
              <div key={index} className="sidebar-section">
                <div className={`sidebar-section-header ${section.color}`}>{section.title}</div>
                <div className="sidebar-section-list">
                  {section.items.map((item, i) => (
                    <div key={i} className="sidebar-item">{item}</div>
                  ))}
                </div>
              </div>
            ))}
          </aside>

          <div className="main-area">
            <Routes>
              <Route path="/" element={<PostList />} />
              <Route path="/post/:id" element={<PostDetail />} />
              <Route path="/login" element={<Login onLoginSuccess={handleLoginSuccess} />} />
              <Route path="/register" element={<Register />} />
              <Route path="/create-post" element={<CreatePost />} />
              <Route path="/profile" element={<Profile />} />
            </Routes>
          </div>

          <aside className="right-sidebar">
            {rightSections.map((section, index) => (
              <div key={index} className="sidebar-section">
                <div className="sidebar-section-header">{section.title}</div>
                {section.type === 'user' && (
                  <div className="user-info-box">
                    <div className="user-avatar"></div>
                    <div className="user-name">{user?.nickname || user?.username || '未登录'}</div>
                    <div className="user-stats">
                      <div className="stat-item">
                        <div className="stat-value">0</div>
                        <div className="stat-label">帖子</div>
                      </div>
                      <div className="stat-item">
                        <div className="stat-value">0</div>
                        <div className="stat-label">粉丝</div>
                      </div>
                      <div className="stat-item">
                        <div className="stat-value">0</div>
                        <div className="stat-label">获赞</div>
                      </div>
                    </div>
                  </div>
                )}
                {section.type === 'button' && (
                  <Link to={loggedIn ? section.action : '/login'} className="sidebar-btn">
                    {section.text}
                  </Link>
                )}
                {section.type === 'tags' && (
                  <div className="tag-list">
                    {section.items.map((tag, i) => (
                      <span key={i} className="tag-item">{tag}</span>
                    ))}
                  </div>
                )}
                {section.type === 'text' && (
                  <div className="agreement-text">
                    {section.items.map((item, i) => (
                      <div key={i}><a href="#">{item}</a></div>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </aside>
        </div>
      </main>
    </div>
  )
}

export default App