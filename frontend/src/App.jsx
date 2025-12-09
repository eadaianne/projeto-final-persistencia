import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/layout/navbar.jsx';
import Sidebar from './components/layout/Sidebar.jsx';
import ProtectedRoute from './components/layout/ProtectedRoute';
import Login from './pages/Login/Login';
import NotFound from './pages/NotFound';
import Dashboard from './pages/Dashboard/Dashboard';
import PontosList from './pages/Pontos/PontosList';
import PontoCreate from './pages/Pontos/PontoCreate';
import PontoDetalhes from './pages/Pontos/PontoDetalhes';
import PontoEdit from './pages/Pontos/PontoEdit';
import HospedagensList from './pages/Hospedagens/HospedagensList';
import HospedagemForm from './pages/Hospedagens/HospedagemForm';
import ComentariosList from './pages/Comentarios/ComentariosList';

const App = () => {
	return (
		<Router>
			<div className="app-root">
				<Navbar />
				<div style={{ display: 'flex', minHeight: 'calc(100vh - 56px)' }}>
					<Sidebar />
					<main style={{ flex: 1, padding: '1rem' }}>
						<Routes>
							<Route path="/login" element={<Login />} />
							<Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
							<Route path="/pontos" element={<ProtectedRoute><PontosList /></ProtectedRoute>} />
							<Route path="/pontos/new" element={<ProtectedRoute><PontoCreate /></ProtectedRoute>} />
							<Route path="/pontos/:id" element={<ProtectedRoute><PontoDetalhes /></ProtectedRoute>} />
							<Route path="/pontos/:id/edit" element={<ProtectedRoute><PontoEdit /></ProtectedRoute>} />
							<Route path="/hospedagens" element={<ProtectedRoute><HospedagensList /></ProtectedRoute>} />
							<Route path="/hospedagens/new" element={<ProtectedRoute><HospedagemForm /></ProtectedRoute>} />
							<Route path="/comentarios" element={<ProtectedRoute><ComentariosList /></ProtectedRoute>} />
							<Route path="*" element={<NotFound />} />
						</Routes>
					</main>
				</div>
			</div>
		</Router>
	);
};

export default App;