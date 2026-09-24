import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { loginUser, setAuthSession } from '../services/api';

const Login = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: '',
    password: ''
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [slowNotice, setSlowNotice] = useState(false);
  const [serverError, setServerError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }));
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: ''
      }));
    }
    if (serverError) setServerError('');
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.name.trim()) {
      newErrors.name = 'User name is required';
    }

    if (!formData.password) {
      newErrors.password = 'Password is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setServerError('');

    if (!validate()) {
      return;
    }

    setLoading(true);
    setSlowNotice(false);

    // If server is cold-starting on Render, alert user gently after 2.5s
    const timer = setTimeout(() => {
      setSlowNotice(true);
    }, 2500);

    try {
      const response = await loginUser({
        name: formData.name.trim(),
        password: formData.password
      });

      // Response contains token, id, name, email, phone_no
      const { token, id, name, email, phone_no } = response;
      setAuthSession(token, { id, name, email, phone_no });

      navigate('/');
    } catch (err) {
      setServerError(err.message || 'Invalid credentials. Please try again.');
    } finally {
      clearTimeout(timer);
      setLoading(false);
      setSlowNotice(false);
    }
  };

  return (
    <div className="auth-page-wrapper">
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-brand-badge">
            <span className="auth-brand-dot"></span>
            Reglog Portal
          </div>
          <h1 className="auth-title">Welcome Back</h1>
          <p className="auth-subtitle">Sign in to your account to continue</p>
        </div>

        {serverError && (
          <div className="alert-banner alert-error" role="alert">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="12" y1="8" x2="12" y2="12"></line>
              <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
            <span>{serverError}</span>
          </div>
        )}

        <form className="auth-form" onSubmit={handleSubmit} noValidate>
          {/* User Name */}
          <div className="form-group">
            <label className="form-label" htmlFor="login-name">
              User Name
            </label>
            <div className="input-container">
              <input
                id="login-name"
                name="name"
                type="text"
                className={`form-input ${errors.name ? 'input-error' : ''}`}
                placeholder="Enter your user name"
                value={formData.name}
                onChange={handleChange}
                autoComplete="username"
              />
            </div>
            {errors.name && <span className="field-error">{errors.name}</span>}
          </div>

          {/* Password */}
          <div className="form-group">
            <label className="form-label" htmlFor="login-password">
              Password
            </label>
            <div className="input-container">
              <input
                id="login-password"
                name="password"
                type="password"
                className={`form-input ${errors.password ? 'input-error' : ''}`}
                placeholder="••••••••"
                value={formData.password}
                onChange={handleChange}
                autoComplete="current-password"
              />
            </div>
            {errors.password && <span className="field-error">{errors.password}</span>}
          </div>

          {/* Login Button */}
          <button
            id="login-submit-btn"
            type="submit"
            className="btn-primary"
            disabled={loading}
          >
            {loading ? (
              <>
                <span className="spinner"></span>
                <span>Signing In...</span>
              </>
            ) : (
              'Login'
            )}
          </button>

          {loading && slowNotice && (
            <div className="server-status-pill">
              <span className="server-status-dot"></span>
              <span>Waking up cloud server (Render free tier can take ~30-50s on initial load)...</span>
            </div>
          )}
        </form>

        {/* Hyperlink below it to redirect to the Signup page for users who do not have an account */}
        <div className="auth-redirect-footer">
          Don't have an account?{' '}
          <Link id="link-to-signup" to="/signup">
            Sign Up
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
