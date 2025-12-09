import React from 'react';
import { useParams, Link } from 'react-router-dom';
import { getPonto } from '../../api/pontosApi';
import useFetch from '../../hooks/useFetch';
import uploadPath from '../../utils/uploadPath';
import formatDate from '../../utils/formatDate';

const PontoDetalhes = () => {
	const { id } = useParams();
	const { data, loading, error } = useFetch(() => getPonto(id), [id]);
	const ponto = data?.item || data;

	if (loading) return <div>Carregando...</div>;
	if (error) return <div>Erro: {error.message}</div>;
	if (!ponto) return <div>Não encontrado</div>;

	return (
		<div className="container">
			<h2>{ponto.title}</h2>
			{ponto.image && <img src={`${uploadPath}/${ponto.image}`} alt={ponto.title} style={{ maxWidth: 400 }} />}
			<div className="muted">{formatDate(ponto.createdAt)}</div>
			<p>{ponto.description}</p>
			<div style={{ display: 'flex', gap: '.5rem' }}>
				<Link className="btn" to={`/pontos/${id}/edit`}>Editar</Link>
			</div>
		</div>
	);
};

export default PontoDetalhes;

