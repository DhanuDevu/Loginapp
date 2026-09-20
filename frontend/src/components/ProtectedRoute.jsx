import React from 'react';
import { Navigate } from 'react-router-dom';
import { getStoredToken } from '../services/api';

const ProtectedRoute = ({ children }) => {
  const token = getStoredToken();

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
