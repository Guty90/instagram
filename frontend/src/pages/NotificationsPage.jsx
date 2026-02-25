import { useState, useEffect } from "react";
import { getNotifications } from "../api/notificationApi";
import { useAuth } from "../context/authContext";
import "../styles/Feed.css";

import { useNavigate } from "react-router-dom";

const NotificationsPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    getNotifications(user.userId)
      .then(setNotifications)
      .catch(console.error);
  }, [user.userId]);

  const formatTime = (dateStr) => {
    const diff = Date.now() - new Date(dateStr).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 1) return "ahora";
    if (mins < 60) return `${mins}m`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `${hrs}h`;
    return `${Math.floor(hrs / 24)}d`;
  };

  return (
    <div className="notif-page">
      <div className="notif-page__container">
        <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: 12,
            marginBottom: 20,
          }}
        >
          <button
            onClick={() => navigate(-1)}
            style={{
              background: "none",
              border: "none",
              color: "var(--text-primary)",
              fontSize: 22,
              cursor: "pointer",
              padding: "4px 8px",
              borderRadius: 8,
            }}
          >
            ←
          </button>
          <h2 className="notif-page__title" style={{ margin: 0 }}>
            Notificaciones
          </h2>
        </div>
        {notifications.length === 0 ? (
          <p className="notif-page__empty">No tienes notificaciones aún</p>
        ) : (
          notifications.map((n, i) => (
            <div
              key={n.id}
              className="notif-card"
              style={{ animationDelay: `${i * 0.05}s` }}
            >
              <div className="notif-card__icon">❤️</div>
              <p className="notif-card__text">
                <strong>{n.fromUsername || `user_${n.fromUserId}`}</strong> le
                dio like a tu historia <strong>#{n.storyId}</strong>
              </p>
              <span className="notif-card__time">
                {formatTime(n.createdAt)}
              </span>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default NotificationsPage;
