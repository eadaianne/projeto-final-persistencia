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
