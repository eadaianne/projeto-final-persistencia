import { useState } from 'react';

export default function usePagination(initialPage = 1, initialSize = 10) {
	const [page, setPage] = useState(initialPage);
	const [pageSize, setPageSize] = useState(initialSize);
	const onPageChange = (p) => setPage(p);
	const onPageSizeChange = (s) => {
		setPageSize(s);
		setPage(1);
	};
	return { page, pageSize, onPageChange, onPageSizeChange, setPage, setPageSize };
}

