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
