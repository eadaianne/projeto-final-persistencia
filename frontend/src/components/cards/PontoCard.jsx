import React from 'react';
import { Link } from 'react-router-dom';
import formatDate from '../../utils/formatDate';
import uploadPath from '../../utils/uploadPath';

const PontoCard = ({ ponto }) => {
	if (!ponto) return null;
	return (
		<article className="card" style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
			{ponto?.image && (
				<img src={`${uploadPath}/${ponto.image}`} alt={ponto.title} style={{ width: 120, height: 90, objectFit: 'cover', borderRadius: 6 }} />
			)}
			<div style={{ flex: 1 }}>
				<h4 style={{ margin: 0 }}>{ponto.title}</h4>
				<div className="muted">{formatDate(ponto.createdAt)}</div>
				<p className="muted">{ponto.description?.slice(0, 120)}{ponto.description?.length > 120 ? '...' : ''}</p>
			</div>
			<div>
				<Link to={`/pontos/${ponto.id}`} className="btn">Ver</Link>
			</div>
		</article>
	);
};

export default PontoCard;

