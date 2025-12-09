import React from 'react';
import { Link } from 'react-router-dom';

const NotFound = () => (
	<div className="container text-center">
		<h1>404</h1>
		<p>Página não encontrada</p>
		<Link to="/" className="btn">Voltar</Link>
	</div>
);

export default NotFound;

