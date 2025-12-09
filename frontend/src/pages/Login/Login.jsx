import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import InputText from '../../components/forms/InputText';

const Login = () => {
	const { login } = useAuth();
	const navigate = useNavigate();
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const [error, setError] = useState(null);

	const handleSubmit = async (e) => {
		e.preventDefault();
		try {
			await login({ email, password });
			navigate('/');
		} catch (err) {
			setError(err?.response?.data?.message || 'Erro ao autenticar');
		}
	};

	return (
		<div className="container" style={{ maxWidth: 460 }}>
			<h2>Entrar</h2>
			<form onSubmit={handleSubmit}>
				<InputText label="Email" name="email" value={email} onChange={setEmail} />
				<InputText label="Senha" name="password" type="password" value={password} onChange={setPassword} />
				{error && <div style={{ color: 'var(--danger)' }}>{error}</div>}
				<div style={{ marginTop: '.5rem' }}>
					<button className="btn" type="submit">Entrar</button>
				</div>
			</form>
		</div>
	);
};

export default Login;

