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
