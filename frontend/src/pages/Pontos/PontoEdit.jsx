import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getPonto, updatePonto } from '../../api/pontosApi';
import useFetch from '../../hooks/useFetch';
import InputText from '../../components/forms/InputText';
import FileUpload from '../../components/forms/FileUpload';

const PontoEdit = () => {
	const { id } = useParams();
	const navigate = useNavigate();
	const { data, loading } = useFetch(() => getPonto(id), [id]);
	const ponto = data?.item || data;
	const [title, setTitle] = useState('');
	const [description, setDescription] = useState('');
	const [file, setFile] = useState(null);

	useEffect(() => {
		if (ponto) {
			setTitle(ponto.title || '');
			setDescription(ponto.description || '');
		}
	}, [ponto]);

	if (loading) return <div>Carregando...</div>;
	if (!ponto) return <div>Não encontrado</div>;

	const handleSubmit = async (e) => {
		e.preventDefault();
		const form = new FormData();
		form.append('title', title);
		form.append('description', description);
		if (file) form.append('image', file);
		await updatePonto(id, form);
		navigate(`/pontos/${id}`);
	};

	return (
		<div className="container" style={{ maxWidth: 780 }}>
			<h2>Editar Ponto</h2>
			<form onSubmit={handleSubmit}>
				<InputText label="Título" name="title" value={title} onChange={setTitle} />
				<label style={{ display: 'block', marginBottom: '.75rem' }}>
					Descrição
					<textarea name="description" value={description} onChange={(e) => setDescription(e.target.value)} style={{ width: '100%', padding: '.5rem', borderRadius: '6px' }} />
				</label>
				<FileUpload label="Imagem (novo arquivo)" onChange={setFile} accept="image/*" />
				<div style={{ marginTop: '.5rem' }}>
					<button className="btn" type="submit">Salvar</button>
				</div>
			</form>
		</div>
	);
};

export default PontoEdit;

