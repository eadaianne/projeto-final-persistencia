import axios from 'axios';

const baseURL = import.meta.env?.VITE_API_URL || '/api';

const instance = axios.create({
	baseURL,
	withCredentials: true,
});

// Attach token if available
// Avoid circular dependency by reading token directly from localStorage
instance.interceptors.request.use((config) => {
	const token = localStorage.getItem('auth_token');
	if (token) config.headers.Authorization = `Bearer ${token}`;
	return config;
});

// Optional: global response interceptor to auto-logout on unauthorized
instance.interceptors.response.use(
	(res) => res,
	(err) => {
		if (err?.response?.status === 401) {
			// Clear auth token
			localStorage.removeItem('auth_token');
			// redirect to login if needed
			try { window.location.href = '/login'; } catch (e) { /* ignore */ }
		}
		return Promise.reject(err);
	}
);

export default instance;

