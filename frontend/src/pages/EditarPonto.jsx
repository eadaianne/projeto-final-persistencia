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
