import React from 'react';
import { Link } from 'react-router-dom';
import { listHospedagens } from '../../api/hospedagensApi';
import useFetch from '../../hooks/useFetch';
import usePagination from '../../hooks/usePagination';
import Pagination from '../../components/common/Pagination';

const HospedagensList = () => {
	const { page, pageSize, onPageChange } = usePagination(1, 8);
	const { data, loading, error } = useFetch(() => listHospedagens({ page, pageSize }), [page, pageSize]);
	const items = data?.items || [];
	const total = data?.total || 0;

	return (
		<div className="container">
			<div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
				<h2>Hospedagens</h2>
				<Link to="/hospedagens/new" className="btn">Novo</Link>
			</div>
			{loading && <div>Carregando...</div>}
			<div style={{ display: 'grid', gap: '1rem' }}>
				{items.map((i) => (
					<div key={i.id} className="card" style={{ padding: '1rem' }}>
						<h4>{i.title}</h4>
						<p className="muted">{i.description}</p>
					</div>
				))}
			</div>
			<div style={{ marginTop: '1rem' }}>
				<Pagination page={page} pageSize={pageSize} total={total} onPageChange={onPageChange} />
			</div>
		</div>
	);
};

export default HospedagensList;

