import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/authContext";
import { useState, useEffect, useRef } from "react";
import "../styles/Navbar.css";

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [unread, setUnread] = useState(0);
  const wsRef = useRef(null);

  useEffect(() => {
    if (!user) return;

    const ws = new WebSocket(
      `ws://localhost:8082/ws/notifications?userId=${user.userId}`,
    );
    wsRef.current = ws;

    ws.onopen = () => console.log("✅ WebSocket conectado");

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === "LIKED") {
        setUnread((prev) => prev + 1);
      }
    };

    ws.onclose = () => console.log("❌ WebSocket desconectado");

    return () => ws.close();
  }, [user]);

  const handleLogout = () => {
    if (!window.confirm("¿Seguro que quieres cerrar sesión?")) return;
    if (wsRef.current) wsRef.current.close();
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar__logo">
        Instagram
      </Link>
      {user && (
        <div className="navbar__actions">
          <span className="navbar__username">@{user.username}</span>
          <Link
            to="/notifications"
            className="navbar__notif-btn"
            onClick={() => setUnread(0)}
          >
            🔔
            {unread > 0 && (
              <span className="navbar__badge">
                {unread > 9 ? "9+" : unread}
              </span>
            )}
          </Link>
          <button onClick={handleLogout} className="navbar__logout-btn">
            Salir
          </button>
        </div>
      )}
    </nav>
  );
};

export default Navbar;
