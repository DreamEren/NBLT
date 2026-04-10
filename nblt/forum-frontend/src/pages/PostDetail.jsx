import { useEffect, useState } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'
import { api } from '../api'
import { getToken, getCurrentUserId } from '../utils/token'
import './PostDetail.css'

function PostDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [commentContent, setCommentContent] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    loadPostDetail()
    loadComments()
  }, [id])

  const loadPostDetail = async () => {
    try {
      setLoading(true)
      const res = await api.getPostDetail(id)
      setPost(res.data)
    } catch (err) {
      setError(err.message || '加载帖子详情失败')
    } finally {
      setLoading(false)
    }
  }

  const loadComments = async () => {
    try {
      const res = await api.getCommentsByPost(id)
      const data = res.data || []
      // 按 createdAt 降序排列，最新评论显示在顶部
      data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      setComments(data)
    } catch (err) {
      // 评论加载失败不阻塞页面，静默处理或只打日志
      setComments([])
    }
  }

  const handleSubmitComment = async (e) => {
    e.preventDefault()

    if (!getToken()) {
      alert('请先登录')
      navigate('/login')
      return
    }

    if (!commentContent.trim()) {
      return
    }

    setSubmitting(true)
    try {
      const res = await api.createComment({
        postId: parseInt(id),
        content: commentContent,
      })
      setCommentContent('')
      // 乐观更新：立即将新评论添加到列表
      const newComment = res.data
      if (newComment) {
        setComments(prev => [newComment, ...prev])
      }
      // 刷新评论列表（确保数据同步）
      await loadComments()
    } catch (err) {
      alert(err.message || '评论失败')
    } finally {
      setSubmitting(false)
    }
  }

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('确定要删除这条评论吗？')) {
      return
    }

    try {
      await api.deleteComment(commentId)
      alert('删除成功')
      await loadComments()
    } catch (err) {
      alert(err.message || '删除评论失败')
    }
  }

  const handleDeletePost = async () => {
    if (!window.confirm('确定要删除这个帖子吗？')) {
      return
    }

    try {
      await api.deletePost(id)
      alert('删除成功')
      navigate('/')
    } catch (err) {
      alert(err.message || '删除帖子失败')
    }
  }

  if (loading) {
    return <div className="loading">加载中...</div>
  }

  if (error) {
    return <div className="error">{error}</div>
  }

  if (!post) {
    return <div className="error">帖子不存在</div>
  }

  const currentUserId = getCurrentUserId()

  return (
    <div className="post-detail">
      <Link to="/" className="back-link">← 返回列表</Link>

      <div className="card post-content">
        <h1>{post.title}</h1>
        <div className="post-meta">
          <span>作者: {post.authorName || post.authorId}</span>
          <span>{new Date(post.createdAt).toLocaleString()}</span>
          {String(post.authorId) === String(currentUserId) && (
            <button
              type="button"
              className="btn-text"
              onClick={handleDeletePost}
            >
              删除帖子
            </button>
          )}
        </div>
        <div className="post-body">{post.content}</div>
      </div>

      <div className="comments-section">
        <h2>评论 ({comments.length})</h2>

        <form onSubmit={handleSubmitComment} className="comment-form">
          <textarea
            value={commentContent}
            onChange={(e) => setCommentContent(e.target.value)}
            placeholder={getToken() ? '写下你的评论...' : '请先登录后评论'}
            disabled={!getToken() || submitting}
          />
          <button
            type="submit"
            className="btn-primary"
            disabled={!getToken() || submitting || !commentContent.trim()}
          >
            {submitting ? '提交中...' : '发表评论'}
          </button>
        </form>

        <div className="comments-list">
          {comments.length === 0 ? (
            <div className="empty">暂无评论，来抢沙发吧！</div>
          ) : (
            comments.map((comment) => (
              <div key={comment.id} className="comment-item">
                <div className="comment-header">
                  <span className="comment-author">
                    {comment.authorName || comment.authorId}
                  </span>
                  <span className="comment-time">
                    {new Date(comment.createdAt).toLocaleString()}
                  </span>
                  {String(comment.authorId) === String(currentUserId) && (
                    <button
                      type="button"
                      className="btn-text"
                      onClick={() => handleDeleteComment(comment.id)}
                    >
                      删除
                    </button>
                  )}
                </div>
                <div className="comment-body">{comment.content}</div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}

export default PostDetail
