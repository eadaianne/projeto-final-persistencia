import React, { createContext, useEffect, useState } from 'react';
import authService from '../services/authService';
import { me } from '../api/authApi';

export const AuthContext = createContext({
	user: null,
	loading: true,
	login: async () => {},
	logout: () => {},
});

export const AuthProvider = ({ children }) => {
	const [user, setUser] = useState(null);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		const initialize = async () => {
			try {
				authService.setAuthorizationHeader();
				const data = await me();
				setUser(data?.user || null);
			} catch (err) {
				setUser(null);
			} finally {
				setLoading(false);
			}
		};
		initialize();
	}, []);

	const login = async (credentials) => {
		const data = await authService.login(credentials);
		// after login, try to fetch user
		try {
			const meData = await me();
			setUser(meData?.user || null);
		} catch (err) {
			// ignore
		}
		return data;
	};

	const logout = () => {
		authService.logout();
		setUser(null);
	};

	return (
		<AuthContext.Provider value={{ user, loading, login, logout }}>
			{children}
		</AuthContext.Provider>
	);
};

export default AuthProvider;

