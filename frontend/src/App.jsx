
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext.jsx";
import Navbar from "./components/Navbar.jsx";
import Home from "./pages/Home.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import PontoDetalhes from "./pages/PontoDetalhes.jsx";
import CriarPonto from "./pages/CriarPonto.jsx";
import EditarPonto from "./pages/EditarPonto.jsx";
import Hospedagens from "./pages/Hospedagens.jsx";
import Favoritos from "./pages/Favoritos.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";

console.log('App.jsx carregado')

export default function App() {
	return (
		<BrowserRouter>
			<AuthProvider>
				<Navbar />
				<Routes>
					<Route path="/" element={<Home />} />
					<Route path="/login" element={<Login />} />
					<Route path="/register" element={<Register />} />
					<Route path="/ponto/:id" element={<PontoDetalhes />} />
					<Route path="/favoritos" element={<ProtectedRoute><Favoritos/></ProtectedRoute>} />
					<Route path="/criar" element={<ProtectedRoute adminOnly={true}><CriarPonto /></ProtectedRoute>} />
					<Route path="/editar/:id" element={<ProtectedRoute adminOnly={true}><EditarPonto/></ProtectedRoute>} />
					<Route path="/hospedagens/:pontoId" element={<Hospedagens/>} />
				</Routes>
			</AuthProvider>
		</BrowserRouter>
	);
}
