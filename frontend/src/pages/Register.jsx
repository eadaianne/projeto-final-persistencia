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
