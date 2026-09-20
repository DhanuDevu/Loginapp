import React from 'react';
import { useNavigate } from 'react-router-dom';
import { getStoredUser, logoutUser } from '../services/api';

const Home = () => {
  const navigate = useNavigate();
  const user = getStoredUser();
  const userName = user?.name || 'Valued User';

  const handleLogout = async () => {
    await logoutUser();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      {/* Navigation Header */}
      <header className="dashboard-nav">
        <div className="dashboard-logo">
          <span className="auth-brand-dot"></span>
          Reglog Portal
        </div>
        <button
          id="home-logout-btn"
          className="btn-logout"
          onClick={handleLogout}
        >
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
            <polyline points="16 17 21 12 16 7"></polyline>
            <line x1="21" y1="12" x2="9" y2="12"></line>
          </svg>
          Logout
        </button>
      </header>

      {/* Welcome Card — username only */}
      <section className="hero-welcome-card">
        <div className="welcome-user-tag">Authentication Success</div>
        <h1 className="welcome-heading">
          Welcome back, <span id="home-user-name" className="welcome-highlight-name">{userName}</span>!
        </h1>
      </section>
    </div>
  );
};

export default Home;
