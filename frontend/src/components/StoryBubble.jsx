import '../styles/Stories.css';

const StoryBubble = ({ group, isOwn, onClick }) => {
    const firstStory = group[0];
    const count = group.length;

    const initial = firstStory.username
        ? firstStory.username.charAt(0).toUpperCase()
        : '?';

    const getRingClass = () => {
        if (count === 1) return 'story-bubble__ring';
        if (count === 2) return 'story-bubble__ring story-bubble__ring--multiple';
        return 'story-bubble__ring story-bubble__ring--triple';
    };

    return (
        <div className="story-bubble" onClick={() => onClick(group, 0)}>
            <div className={getRingClass()}>
                <div className="story-bubble__inner">
                    {firstStory.imageUrl ? (
                        <img
                            src={firstStory.imageUrl}
                            alt={firstStory.username}
                            className="story-bubble__avatar"
                            onError={e => { e.target.style.display = 'none'; }}
                        />
                    ) : (
                        <div className="story-bubble__avatar-placeholder">
                            {initial}
                        </div>
                    )}
                </div>
            </div>

            <span className="story-bubble__username">
                {isOwn ? 'Your history' : (firstStory.username || `user_${firstStory.userId}`)}
            </span>

            {/* Indicadores de múltiples historias */}
            {count > 1 && (
                <div className="story-bubble__dots">
                    {group.map((_, i) => (
                        <div
                            key={i}
                            className={`story-bubble__dot ${i === 0 ? 'story-bubble__dot--active' : ''}`}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default StoryBubble;