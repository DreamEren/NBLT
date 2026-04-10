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

  return (
    <div className="app">
      <header className="navbar">
        <div className="nav-container">
          <Link to="/" className="logo">
            📝 论坛
          </Link>
          <nav className="nav-links">
            {loggedIn ? (
              <>
                <span className="user-greeting">👋 {user?.nickname || user?.username}</span>
                <Link to="/create-post" className="btn btn-primary btn-sm">发帖</Link>
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
        <Routes>
          <Route path="/" element={<PostList />} />
          <Route path="/post/:id" element={<PostDetail />} />
          <Route path="/login" element={<Login onLoginSuccess={handleLoginSuccess} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/create-post" element={<CreatePost />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
