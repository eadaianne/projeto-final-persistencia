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
