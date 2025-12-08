import React from 'react';
import { Link } from 'react-router-dom';
import { listPontos } from '../../api/pontosApi';
import useFetch from '../../hooks/useFetch';
import PontoCard from '../../components/cards/PontoCard';
import usePagination from '../../hooks/usePagination';
import Pagination from '../../components/common/Pagination';

const PontosList = () => {
	const { page, pageSize, onPageChange } = usePagination(1, 8);
	const { data, loading, error, refetch } = useFetch(() => listPontos({ page, pageSize }), [page, pageSize]);
	const pontos = data?.items || [];
	const total = data?.total || 0;

	return (
		<div className="container">
			<div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
				<h2>Pontos</h2>
				<Link to="/pontos/new" className="btn">Novo</Link>
			</div>
			{loading && <div>Carregando...</div>}
			{error && <div>Erro</div>}
			<div style={{ display: 'grid', gap: '1rem' }}>
				{pontos.map((p) => <PontoCard key={p.id} ponto={p} />)}
			</div>
			<div style={{ marginTop: '1rem' }}>
				<Pagination page={page} pageSize={pageSize} total={total} onPageChange={onPageChange} />
			</div>
		</div>
	);
};

export default PontosList;

