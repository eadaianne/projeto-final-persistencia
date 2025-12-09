import axios from './axiosInstance';

export const login = async (credentials) => {
	const res = await axios.post('/auth/login', credentials);
	return res.data;
};

export const me = async () => {
	const res = await axios.get('/auth/me');
	return res.data;
};

export default { login, me };

