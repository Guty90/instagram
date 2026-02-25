import { useState, useEffect } from 'react';
import { toggleLike, getLikeCount, hasLiked } from '../api/likeApi';
import { useAuth } from '../context/authContext';

const StoryCard = ({ story }) => {
    const { user } = useAuth();
    const [likes, setLikes] = useState(0);
    const [liked, setLiked] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        getLikeCount(story.id).then(setLikes);
        hasLiked(story.id, user.userId).then(setLiked);
    }, [story.id]);

    const handleLike = async () => {
        if (loading) return;
        setLoading(true);
        try {
            const res = await toggleLike(story.id, user.userId);
            if (res === null) {
                setLiked(false);
                setLikes(prev => prev - 1);
            } else {
                setLiked(true);
                setLikes(prev => prev + 1);
            }
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={styles.card}>
            <img src={story.imageUrl} alt="story" style={styles.img} />
            <div style={styles.info}>
                <p style={styles.content}>{story.content}</p>
                <div style={styles.footer}>
                    <button onClick={handleLike} style={{ ...styles.likeBtn, color: liked ? 'red' : '#555' }}>
                        {liked ? '❤️' : '🤍'} {likes}
                    </button>
                    <span style={styles.time}>
                        {new Date(story.createdAt).toLocaleTimeString()}
                    </span>
                </div>
            </div>
        </div>
    );
};

const styles = {
    card: { background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', marginBottom: 16 },
    img: { width: '100%', height: 300, objectFit: 'cover' },
    info: { padding: 16 },
    content: { margin: '0 0 12px', fontSize: 15 },
    footer: { display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    likeBtn: { background: 'none', border: 'none', fontSize: 18, cursor: 'pointer' },
    time: { color: '#aaa', fontSize: 12 }
};

export default StoryCard;