#!/usr/bin/env bash
set -e

echo "Criando estrutura do frontend..."

# root files
cat > package.json <<'JSON'
{
  "name": "frontend-turismo",
  "version": "1.0.0",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "axios": "^1.6.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.22.0"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.2.0",
    "vite": "^5.0.0"
  }
}
JSON

cat > vite.config.js <<'JS'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()]
})
JS

# src tree
mkdir -p src/{api,components,context,pages,services} public

# main.jsx
cat > src/main.jsx <<'JSX'
import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './styles.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
)
JSX

# App.jsx
cat > src/App.jsx <<'JSX'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/Home";
import PontoDetalhes from "./pages/PontoDetalhes";
import CriarPonto from "./pages/CriarPonto";
import EditarPonto from "./pages/EditarPonto";
import Hospedagens from "./pages/Hospedagens";
import Favoritos from "./pages/Favoritos";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";

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
JSX

# API helper
cat > src/api/api.js <<'JS'
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api" // <- ajuste aqui para seu backend
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default api;
JS

# AuthContext
cat > src/context/AuthContext.jsx <<'JSX'
import React, { createContext, useEffect, useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();
  const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")) || null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      const userJson = localStorage.getItem("user");
      if (userJson) setUser(JSON.parse(userJson));
    }
    setLoading(false);
  }, []);

  const login = async (loginOrEmail, password) => {
    const res = await api.post("/auth/login", { login: loginOrEmail, password });
    // Ajuste conforme sua resposta do backend:
    // espera { token: "...", usuario: { login: "...", role: "ADMIN" } } ou { token, login, role }
    const token = res.data.token || res.data.accessToken || res.data.jwt;
    let usuario = res.data.usuario || res.data.user || { login: res.data.login, role: res.data.role };
    if (!usuario) {
      // se backend retorna apenas token, tenta buscar perfil
      const profile = await api.get("/auth/me").then(r => r.data).catch(() => null);
      usuario = profile || { login: loginOrEmail, role: "USER" };
    }
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(usuario));
    localStorage.setItem("role", usuario.role || "USER");
    api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    setUser(usuario);
    navigate("/");
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("role");
    setUser(null);
    navigate("/login");
  };

  return (
    <AuthContext.Provider value={{ user, loading, authenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
JSX

# ProtectedRoute
cat > src/components/ProtectedRoute.jsx <<'JSX'
import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function ProtectedRoute({ children, adminOnly = false }) {
  const { authenticated, loading, user } = useContext(AuthContext);

  if (loading) return <p>Carregando...</p>;
  if (!authenticated) return <Navigate to="/login" replace />;

  if (adminOnly && (!user || (user.role || localStorage.getItem("role")) !== "ADMIN")) {
    return <Navigate to="/" replace />;
  }

  return children;
}
JSX

# Navbar
cat > src/components/Navbar.jsx <<'JSX'
import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useContext(AuthContext);
  const role = (user && user.role) || localStorage.getItem("role");

  return (
    <nav className="nav">
      <Link to="/">Home</Link>
      {user && <Link to="/favoritos">Favoritos</Link>}
      {role === "ADMIN" && <Link to="/criar">Criar ponto</Link>}
      {!user && <Link to="/login">Login</Link>}
      {!user && <Link to="/register">Registrar</Link>}
      {user && <button onClick={logout}>Sair</button>}
    </nav>
  );
}
JSX

# Pages - Login
cat > src/pages/Login.jsx <<'JSX'
import React, { useContext, useState } from "react";
import { AuthContext } from "../context/AuthContext";

export default function Login() {
  const { login } = useContext(AuthContext);
  const [loginInput, setLoginInput] = useState("");
  const [senha, setSenha] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    try {
      await login(loginInput, senha);
    } catch (err) {
      alert("Erro ao autenticar: " + (err?.response?.data?.message || err.message));
    }
  };

  return (
    <div className="card">
      <h2>Login</h2>
      <form onSubmit={submit}>
        <input placeholder="Login ou email" value={loginInput} onChange={e => setLoginInput(e.target.value)} />
        <input placeholder="Senha" type="password" value={senha} onChange={e => setSenha(e.target.value)} />
        <button type="submit">Entrar</button>
      </form>
    </div>
  );
}
JSX

# Pages - Register
cat > src/pages/Register.jsx <<'JSX'
import React, { useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const nav = useNavigate();
  const [form, setForm] = useState({ login: "", email: "", password: "" });

  const submit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/auth/register", form);
      alert("Registrado! Faça login.");
      nav("/login");
    } catch (err) {
      alert("Erro ao registrar: " + (err?.response?.data?.message || err.message));
    }
  };

  return (
    <div className="card">
      <h2>Registrar</h2>
      <form onSubmit={submit}>
        <input placeholder="Login" value={form.login} onChange={e => setForm({...form, login: e.target.value})} />
        <input placeholder="Email" value={form.email} onChange={e => setForm({...form, email: e.target.value})} />
        <input placeholder="Senha" type="password" value={form.password} onChange={e => setForm({...form, password: e.target.value})} />
        <button type="submit">Registrar</button>
      </form>
    </div>
  );
}
JSX

# Pages - Home
cat > src/pages/Home.jsx <<'JSX'
import React, { useEffect, useState } from "react";
import api from "../api/api";
import { Link } from "react-router-dom";

export default function Home() {
  const [pontos, setPontos] = useState([]);
  const [cidade, setCidade] = useState("");

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await api.get("/pontos");
      setPontos(res.data.content ? res.data.content : res.data);
    } catch (e) {
      console.error(e);
      setPontos([]);
    }
  };

  const buscar = async () => {
    try {
      const res = await api.get(`/pontos?cidade=${encodeURIComponent(cidade)}`);
      setPontos(res.data.content ? res.data.content : res.data);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="container">
      <h1>Pontos turísticos</h1>
      <div style={{marginBottom:12}}>
        <input placeholder="Filtrar por cidade" value={cidade} onChange={e=>setCidade(e.target.value)} />
        <button onClick={buscar}>Buscar</button>
        <button onClick={load}>Limpar</button>
      </div>
      {pontos.map(p => (
        <div key={p.id} className="card">
          <h3>{p.nome} {p.cidade && `- ${p.cidade}`}</h3>
          <p>{p.descricao}</p>
          <Link to={`/ponto/${p.id}`}>Ver detalhes</Link>
        </div>
      ))}
    </div>
  );
}
JSX

# Pages - PontoDetalhes
cat > src/pages/PontoDetalhes.jsx <<'JSX'
import React, { useContext, useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import api from "../api/api";
import { AuthContext } from "../context/AuthContext";

export default function PontoDetalhes() {
  const { id } = useParams();
  const [ponto, setPonto] = useState(null);
  const [avaliacoes, setAvaliacoes] = useState([]);
  const [fotos, setFotos] = useState([]);
  const { user } = useContext(AuthContext);

  useEffect(() => {
    load();
    loadAvaliacoes();
    loadFotos();
  }, [id]);

  const load = async () => {
    const res = await api.get(`/pontos/${id}`);
    setPonto(res.data);
  };
  const loadAvaliacoes = async () => {
    const res = await api.get(`/avaliacoes/ponto/${id}`);
    setAvaliacoes(res.data);
  };
  const loadFotos = async () => {
    const res = await api.get(`/fotos/ponto/${id}`);
    setFotos(res.data);
  };

  if (!ponto) return <p>Carregando...</p>;

  return (
    <div className="container">
      <h1>{ponto.nome}</h1>
      <p>{ponto.descricao}</p>

      <h3>Fotos</h3>
      <div style={{display:'flex', gap:10, flexWrap:'wrap'}}>
        {fotos.map(f => <img key={f.id} src={`/uploads/${f.filename || f.path || f.id}`} alt="" style={{width:120,height:80,objectFit:'cover'}} />)}
      </div>

      <h3>Avaliações</h3>
      {avaliacoes.map(a => (
        <div key={a.id} className="card">
          <strong>{a.usuarioLogin || a.usuario || a.login}</strong>
          <p>{a.nota} ★</p>
          <p>{a.comentario}</p>
        </div>
      ))}

      <Link to={`/hospedagens/${id}`}>Ver hospedagens</Link>
      { (user && (user.role === 'ADMIN' || (ponto.criadoPor && ponto.criadoPor.login === user.login))) && (
        <Link to={`/editar/${id}`} className="btn-blue">Editar</Link>
      )}
    </div>
  );
}
JSX

# Pages - CriarPonto
cat > src/pages/CriarPonto.jsx <<'JSX'
import React, { useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

export default function CriarPonto() {
  const nav = useNavigate();
  const [form, setForm] = useState({ nome: "", cidade: "", descricao: "", estado: "", pais: "", latitude: null, longitude: null, endereco: "" });

  const salvar = async (e) => {
    e.preventDefault();
    try {
      await api.post("/pontos", form);
      alert("Ponto criado");
      nav("/");
    } catch (err) {
      alert("Erro: " + (err?.response?.data?.message || err.message));
    }
  };

  return (
    <div className="card">
      <h2>Criar ponto</h2>
      <form onSubmit={salvar}>
        <input placeholder="Nome" value={form.nome} onChange={e=>setForm({...form, nome:e.target.value})} />
        <input placeholder="Cidade" value={form.cidade} onChange={e=>setForm({...form, cidade:e.target.value})} />
        <input placeholder="Estado" value={form.estado} onChange={e=>setForm({...form, estado:e.target.value})} />
        <input placeholder="País" value={form.pais} onChange={e=>setForm({...form, pais:e.target.value})} />
        <textarea placeholder="Descrição" value={form.descricao} onChange={e=>setForm({...form, descricao:e.target.value})} />
        <input placeholder="Endereço" value={form.endereco} onChange={e=>setForm({...form, endereco:e.target.value})} />
        <button type="submit">Salvar</button>
      </form>
    </div>
  );
}
JSX

# Pages - EditarPonto
cat > src/pages/EditarPonto.jsx <<'JSX'
import React, { useEffect, useState } from "react";
import api from "../api/api";
import { useParams, useNavigate } from "react-router-dom";

export default function EditarPonto() {
  const { id } = useParams();
  const nav = useNavigate();
  const [form, setForm] = useState({ nome: "", cidade: "", descricao: "", estado: "", pais: "", endereco: "" });

  useEffect(() => {
    api.get(`/pontos/${id}`).then(res => {
      setForm({
        nome: res.data.nome || "",
        cidade: res.data.cidade || "",
        descricao: res.data.descricao || "",
        estado: res.data.estado || "",
        pais: res.data.pais || "",
        endereco: res.data.endereco || ""
      });
    });
  }, [id]);

  const salvar = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/pontos/${id}`, form);
      nav(`/ponto/${id}`);
    } catch (err) {
      alert("Erro: " + (err?.response?.data?.message || err.message));
    }
  };

  return (
    <div className="card">
      <h2>Editar ponto</h2>
      <form onSubmit={salvar}>
        <input value={form.nome} onChange={e=>setForm({...form,nome:e.target.value})} />
        <input value={form.cidade} onChange={e=>setForm({...form,cidade:e.target.value})} />
        <textarea value={form.descricao} onChange={e=>setForm({...form,descricao:e.target.value})} />
        <button type="submit">Salvar</button>
      </form>
    </div>
  );
}
JSX

# Pages - Hospedagens
cat > src/pages/Hospedagens.jsx <<'JSX'
import React, { useEffect, useState } from "react";
import api from "../api/api";
import { useParams } from "react-router-dom";

export default function Hospedagens() {
  const { pontoId } = useParams();
  const [lista, setLista] = useState([]);

  useEffect(() => {
    api.get(`/hospedagens?pontoId=${pontoId}`).then(res => setLista(res.data));
  }, [pontoId]);

  return (
    <div className="container">
      <h2>Hospedagens</h2>
      {lista.map(h => (
        <div className="card" key={h.id}>
          <h3>{h.nome}</h3>
          <p>{h.endereco}</p>
          <p>Preço médio: R$ {h.precoMedio}</p>
        </div>
      ))}
    </div>
  );
}
JSX

# Pages - Favoritos
cat > src/pages/Favoritos.jsx <<'JSX'
import React, { useEffect, useState } from "react";
import api from "../api/api";

export default function Favoritos() {
  const [lista, setLista] = useState([]);

  useEffect(() => {
    api.get("/favoritos/me").then(res => setLista(res.data));
  }, []);

  return (
    <div className="container">
      <h2>Meus Favoritos</h2>
      {lista.map(p => (
        <div className="card" key={p.id}>
          <h3>{p.nome}</h3>
          <p>{p.descricao}</p>
        </div>
      ))}
    </div>
  );
}
JSX

# styles.css
cat > src/styles.css <<'CSS'
body { background:#f4f4f4; margin:0; font-family: Inter, Arial, sans-serif; }
.nav { display:flex; gap:12px; padding:12px 20px; background:#1f2937; color:white; align-items:center; }
.nav a, .nav button { color:white; text-decoration:none; background:none; border:none; cursor:pointer; }
.container { padding:20px; max-width:1000px; margin:0 auto; }
.card { background:white; padding:16px; margin-bottom:14px; border-radius:8px; box-shadow: 0 1px 2px rgba(0,0,0,0.04); }
input, textarea { width:100%; padding:8px; margin-bottom:10px; border:1px solid #ddd; border-radius:4px; }
button { padding:8px 12px; background:#0066cc; color:white; border:none; border-radius:6px; cursor:pointer; }
.btn-blue { padding:8px 10px; background:#0047a3; color:white; border-radius:6px; text-decoration:none; display:inline-block; }
CSS

echo "Arquivos criados com sucesso!"
echo "Agora rode: npm install"
