import axios from './axiosInstance';

export const listHospedagens = async (params) => {
	const res = await axios.get('/hospedagens', { params });
	return res.data;
};

export const getHospedagem = async (id) => {
	const res = await axios.get(`/hospedagens/${id}`);
	return res.data;
};

export const createHospedagem = async (payload) => {
	const res = await axios.post('/hospedagens', payload);
	return res.data;
};

export const updateHospedagem = async (id, payload) => {
	const res = await axios.put(`/hospedagens/${id}`, payload);
	return res.data;
};

export const deleteHospedagem = async (id) => {
	const res = await axios.delete(`/hospedagens/${id}`);
	return res.data;
};

export default { listHospedagens, getHospedagem, createHospedagem, updateHospedagem, deleteHospedagem };

