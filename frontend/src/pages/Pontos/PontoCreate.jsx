import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createPonto } from '../../api/pontosApi';
import InputText from '../../components/forms/InputText';
import FileUpload from '../../components/forms/FileUpload';

const PontoCreate = () => {
	const [title, setTitle] = useState('');
	const [description, setDescription] = useState('');
	const [file, setFile] = useState(null);
	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();
		const form = new FormData();
		form.append('title', title);
		form.append('description', description);
		if (file) form.append('image', file);
		await createPonto(form);
		navigate('/pontos');
	};

	return (
		<div className="container" style={{ maxWidth: 780 }}>
			<h2>Novo Ponto</h2>
			<form onSubmit={handleSubmit}>
				<InputText label="Título" name="title" value={title} onChange={setTitle} />
				<label style={{ display: 'block', marginBottom: '.75rem' }}>
					Descrição
					<textarea name="description" value={description} onChange={(e) => setDescription(e.target.value)} style={{ width: '100%', padding: '.5rem', borderRadius: '6px' }} />
				</label>
				<FileUpload label="Imagem" onChange={setFile} accept="image/*" />
				<div style={{ marginTop: '.5rem' }}>
					<button className="btn" type="submit">Salvar</button>
				</div>
			</form>
		</div>
	);
};

export default PontoCreate;

