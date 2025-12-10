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
        {fotos.map(f => <img key={f.id} src={(f.url || f.path || `/uploads/${f.filename}`)} alt="" style={{width:120,height:80,objectFit:'cover'}} />)}
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
