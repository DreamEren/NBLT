import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api';
import './PostList.css';

function PostList() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize] = useState(10);
  const [total, setTotal] = useState(0);
  const navigate = useNavigate();

  const fetchPosts = async (keyword = '', page = 1) => {
    try {
      setLoading(true);
      const res = await api.getPostList(page, pageSize, keyword);
      if (res.code === 200) {
        setPosts(res.data?.list || res.data || []);
        setTotal(res.data?.total || 0);
      } else {
        alert(res.message || '获取帖子列表失败');
      }
    } catch (error) {
      alert('获取帖子列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, []);

  const handleSearch = () => {
    setCurrentPage(1);
    fetchPosts(searchKeyword, 1);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    fetchPosts(searchKeyword, page);
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="post-list-container">
      <div className="post-list-header">
        <h1>论坛首页</h1>
        <button onClick={() => navigate('/create-post')} className="btn-primary">
          发布帖子
        </button>
      </div>

      <div className="search-bar">
        <input
          type="text"
          className="search-input"
          placeholder="搜索帖子..."
          value={searchKeyword}
          onChange={(e) => setSearchKeyword(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <button onClick={handleSearch} className="btn-search">搜索</button>
      </div>

      {loading ? (
        <div className="loading">加载中...</div>
      ) : (
        <>
          <div className="posts">
            {posts.length === 0 ? (
              <div className="empty">暂无帖子</div>
            ) : (
              posts.map((post) => (
                <div key={post.id} className="post-item">
                  <Link to={`/post/${post.id}`} className="post-title">
                    {post.title}
                  </Link>
                  <p className="post-summary">{post.summary}</p>
                  <div className="post-meta">
                    <span>作者: {post.authorName}</span>
                    <span>{post.createdAt}</span>
                  </div>
                </div>
              ))
            )}
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button
                onClick={() => handlePageChange(currentPage - 1)}
                disabled={currentPage <= 1}
                className="page-btn"
              >
                上一页
              </button>
              <span className="page-info">
                第 {currentPage} / {totalPages} 页 (共 {total} 条)
              </span>
              <button
                onClick={() => handlePageChange(currentPage + 1)}
                disabled={currentPage >= totalPages}
                className="page-btn"
              >
                下一页
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default PostList;
