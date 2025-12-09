import React from 'react';
import { Link } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';

const Navbar = () => {
  const { user, logout } = useAuth() || {};
  return (
    <header style={{ height: 56, display: 'flex', alignItems: 'center', padding: '0 1rem', background: 'var(--surface)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
        <Link to="/">Home</Link>
        <Link to="/pontos">Pontos</Link>
        <Link to="/hospedagens">Hospedagens</Link>
        <Link to="/comentarios">Comentários</Link>
      </div>
      <div style={{ marginLeft: 'auto', display: 'flex', gap: '1rem', alignItems: 'center' }}>
        {user ? (
          <>
            <span>{user?.name || user?.email}</span>
            <button className="btn" onClick={() => logout()}>Sair</button>
          </>
        ) : (
          <Link to="/login" className="btn">Entrar</Link>
        )}
      </div>
    </header>
  );
};

export default Navbar;

