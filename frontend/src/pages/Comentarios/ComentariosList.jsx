import React, { useState } from 'react';
import { listComentarios, createComentario, deleteComentario } from '../../api/comentariosApi';
import useFetch from '../../hooks/useFetch';
import InputText from '../../components/forms/InputText';

const ComentariosList = () => {
	const { data, loading, error, refetch } = useFetch(() => listComentarios(), []);
	const items = data?.items || [];
	const [text, setText] = useState('');

	const handleSubmit = async (e) => {
		e.preventDefault();
		await createComentario({ text });
		setText('');
		refetch();
	};

	const handleDelete = async (id) => {
		await deleteComentario(id);
		refetch();
	};

	return (
		<div className="container">
			<h2>Comentários</h2>
			<form onSubmit={handleSubmit} style={{ marginBottom: '1rem' }}>
				<InputText label="Comentário" value={text} onChange={setText} />
				<div>
					<button className="btn" type="submit">Publicar</button>
				</div>
			</form>
			{loading && <div>Carregando...</div>}
			{items.map((c) => (
				<div key={c.id} className="card" style={{ marginBottom: '.5rem' }}>
					<p>{c.text}</p>
					<div style={{ display: 'flex', gap: '.5rem' }}>
						<button onClick={() => handleDelete(c.id)}>Remover</button>
					</div>
				</div>
			))}
			{error && <div>Erro: {error.message}</div>}
		</div>
	);
};

export default ComentariosList;

