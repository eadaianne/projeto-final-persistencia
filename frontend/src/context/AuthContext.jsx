import React, { createContext, useEffect, useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const navigate = useNavigate();
  const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")) || null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      const stored = localStorage.getItem("user");
      if (stored) setUser(JSON.parse(stored));
    }
    setLoading(false);
  }, []);

  const login = async (loginOrEmail, password) => {
    const res = await api.post("/auth/login", { loginOrEmail, password });

    const token = res.data.token || res.data.accessToken || res.data.jwt;
    let usuario = res.data.usuario || res.data.user || null;

    if (!usuario) {
      try {
        api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        const me = await api.get("/auth/me");
        usuario = me.data;
      } catch (e) {
        usuario = { login: loginOrEmail, role: "USER" };
      }
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
}
