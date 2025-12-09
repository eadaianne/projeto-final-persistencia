import React from 'react';

const Pagination = ({ page, pageSize, total, onPageChange }) => {
	const totalPages = Math.max(1, Math.ceil(total / pageSize));
	return (
		<div style={{ display: 'flex', gap: '.5rem', alignItems: 'center' }}>
			<button onClick={() => onPageChange(Math.max(1, page - 1))} disabled={page <= 1}>Prev</button>
			<div>Page {page} of {totalPages}</div>
			<button onClick={() => onPageChange(Math.min(totalPages, page + 1))} disabled={page >= totalPages}>Next</button>
		</div>
	);
};

export default Pagination;

