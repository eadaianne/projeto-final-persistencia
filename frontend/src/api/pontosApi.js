import axios from './axiosInstance';

export const listPontos = async (params) => {
	const res = await axios.get('/pontos', { params });
	return res.data;
};

export const getPonto = async (id) => {
	const res = await axios.get(`/pontos/${id}`);
	return res.data;
};

export const createPonto = async (payload) => {
	const res = await axios.post('/pontos', payload);
	return res.data;
};

export const updatePonto = async (id, payload) => {
	const res = await axios.put(`/pontos/${id}`, payload);
	return res.data;
};

export const deletePonto = async (id) => {
	const res = await axios.delete(`/pontos/${id}`);
	return res.data;
};

export default { listPontos, getPonto, createPonto, updatePonto, deletePonto };

