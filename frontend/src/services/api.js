const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Helper to get stored auth token
 */
export const getStoredToken = () => {
  return localStorage.getItem('reglog_token');
};

/**
 * Helper to get stored user data
 */
export const getStoredUser = () => {
  const userJson = localStorage.getItem('reglog_user');
  if (!userJson) return null;
  try {
    return JSON.parse(userJson);
  } catch (e) {
    return null;
  }
};

/**
 * Store auth session
 */
export const setAuthSession = (token, user) => {
  localStorage.setItem('reglog_token', token);
  localStorage.setItem('reglog_user', JSON.stringify(user));
};

/**
 * Clear auth session
 */
export const clearAuthSession = () => {
  localStorage.removeItem('reglog_token');
  localStorage.removeItem('reglog_user');
};

/**
 * Signup (Register) API call -> User Service
 */
export const registerUser = async (userData) => {
  const response = await fetch(`${API_BASE_URL}/users/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(userData)
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Registration failed. Please verify your details.');
  }
  return data;
};

/**
 * Login API call -> Authentication Service
 */
export const loginUser = async (credentials) => {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials)
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Invalid username or password.');
  }
  return data;
};

/**
 * Validate JWT Token -> Authentication Service
 */
export const validateToken = async (token) => {
  const response = await fetch(`${API_BASE_URL}/auth/validate`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ token })
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Token validation failed.');
  }
  return data;
};

/**
 * Get Current User Profile (Protected endpoint)
 */
export const fetchCurrentUser = async () => {
  const token = getStoredToken();
  if (!token) throw new Error('No token found');

  const response = await fetch(`${API_BASE_URL}/users/me`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || 'Failed to fetch user profile.');
  }
  return data;
};

/**
 * Logout -> Authentication Service
 */
export const logoutUser = async () => {
  const token = getStoredToken();
  try {
    if (token) {
      await fetch(`${API_BASE_URL}/auth/logout`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token })
      });
    }
  } catch (err) {
    console.warn('Logout API notification failed:', err);
  } finally {
    clearAuthSession();
  }
};
