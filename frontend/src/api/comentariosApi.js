import axios from './axiosInstance';

export const listComentarios = async (params) => {
	const res = await axios.get('/comentarios', { params });
	return res.data;
};

export const createComentario = async (payload) => {
	const res = await axios.post('/comentarios', payload);
	return res.data;
};

export const deleteComentario = async (id) => {
	const res = await axios.delete(`/comentarios/${id}`);
	return res.data;
};

export default { listComentarios, createComentario, deleteComentario };

