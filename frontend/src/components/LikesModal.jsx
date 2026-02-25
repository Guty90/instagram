import { useState, useEffect } from 'react';
import { getUsersWhoLiked } from '../api/likeApi';
import '../styles/Stories.css';

const LikesModal = ({ storyId, likeCount, onClose }) => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getUsersWhoLiked(storyId)
            .then(setUsers)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, [storyId]);

    return (
        <div className="likes-modal-overlay" onClick={onClose}>
            <div className="likes-modal" onClick={e => e.stopPropagation()}>
                <div className="likes-modal__header">
                    <span className="likes-modal__title">
                        ❤️ {likeCount} {likeCount === 1 ? 'like' : 'likes'}
                    </span>
                    <button className="likes-modal__close" onClick={onClose}>✕</button>
                </div>

                {loading && (
                    <p className="likes-modal__empty">Cargando...</p>
                )}

                {!loading && users.length === 0 && (
                    <p className="likes-modal__empty">Nadie ha dado like aún</p>
                )}

                {users.map((userId, i) => (
                    <div
                        key={userId}
                        className="likes-modal__item"
                        style={{ animationDelay: `${i * 0.04}s` }}
                    >
                        <div className="likes-modal__avatar">
                            {String(userId).charAt(0)}
                        </div>
                        <span className="likes-modal__username">user_{userId}</span>
                        <span className="likes-modal__heart">❤️</span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default LikesModal;