import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = () => {
	return (
		<aside style={{ width: 220, padding: '1rem', borderRight: '1px solid rgba(0,0,0,0.06)' }}>
			<nav>
				<ul style={{ listStyle: 'none', padding: 0, display: 'flex', flexDirection: 'column', gap: '.5rem' }}>
					<li><Link to="/">Dashboard</Link></li>
					<li><Link to="/pontos">Pontos</Link></li>
					<li><Link to="/hospedagens">Hospedagens</Link></li>
					<li><Link to="/comentarios">Comentários</Link></li>
				</ul>
			</nav>
		</aside>
	);
};

export default Sidebar;

