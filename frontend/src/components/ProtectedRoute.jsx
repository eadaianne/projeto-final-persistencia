import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function ProtectedRoute({ children, adminOnly = false }) {
  const { authenticated, loading, user } = useContext(AuthContext);

  if (loading) return <p>Carregando...</p>;
  if (!authenticated) return <Navigate to="/login" replace />;

  const role = (user && user.role) || localStorage.getItem("role") || "USER";
  if (adminOnly && role !== "ADMIN") return <Navigate to="/" replace />;

  return children;
}
