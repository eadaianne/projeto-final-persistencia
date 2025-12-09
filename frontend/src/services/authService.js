import axios from 'axios';
import axiosInstance from '../api/axiosInstance';

// Use the project's axios instance if available, otherwise fallback to axios
const client = axiosInstance || axios;

const AUTH_TOKEN_KEY = 'auth_token';

const login = async (credentials) => {
  const resp = await client.post('/auth/login', credentials);
  const token = resp?.data?.token;
  if (token) {
    localStorage.setItem(AUTH_TOKEN_KEY, token);
    setAuthorizationHeader(token);
  }
  return resp?.data;
};

const logout = () => {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  if (client?.defaults) delete client.defaults.headers.common.Authorization;
};

const getToken = () => localStorage.getItem(AUTH_TOKEN_KEY) || null;

const setAuthorizationHeader = (token = null) => {
  const t = token || getToken();
  if (!t) return;
  if (client?.defaults) client.defaults.headers.common.Authorization = `Bearer ${t}`;
};

const isAuthenticated = () => !!getToken();

export default {
  login,
  logout,
  getToken,
  setAuthorizationHeader,
  isAuthenticated,
};
