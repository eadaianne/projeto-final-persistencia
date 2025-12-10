// src/components/Navbar.jsx
import { Link } from "react-router-dom";
export default function Navbar() {
  return (
    <nav style={{padding:12, background:'#222', color:'#fff', display:'flex', gap:12}}>
      <Link to="/" style={{color:'#fff'}}>Home</Link>
      <Link to="/login" style={{color:'#fff'}}>Login</Link>
      <Link to="/criar" style={{color:'#fff'}}>Criar ponto</Link>
    </nav>
  );
}
