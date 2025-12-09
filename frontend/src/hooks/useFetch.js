import { useEffect, useState, useCallback } from 'react';

export default function useFetch(asyncFn, deps = []) {
	const [data, setData] = useState(null);
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);

	const fetchData = useCallback(async () => {
		setLoading(true);
		setError(null);
		try {
			const result = await asyncFn();
			setData(result);
		} catch (err) {
			setError(err);
		} finally {
			setLoading(false);
		}
	}, deps);

	useEffect(() => {
		fetchData();
	}, [fetchData]);

	return { data, loading, error, refetch: fetchData };
}

