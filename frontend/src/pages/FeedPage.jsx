import { useState, useEffect } from 'react';
import { getActiveStories, createStory } from '../api/storyApi';
import { useAuth } from '../context/authContext';
import StoryBubble from '../components/StoryBubble';
import StoryViewer from '../components/StoryViewer';
import '../styles/Feed.css';
import '../styles/Stories.css';

const FeedPage = () => {
    const { user } = useAuth();
    const [stories, setStories] = useState([]);
    const [form, setForm] = useState({ content: '', imageUrl: '' });
    const [showForm, setShowForm] = useState(false);
    const [viewerData, setViewerData] = useState(null); // { stories, index }

    useEffect(() => {
        getActiveStories().then(setStories).catch(console.error);
    }, []);

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            const newStory = await createStory({ ...form, userId: user.userId });
            setStories(prev => [newStory, ...prev]);
            setForm({ content: '', imageUrl: '' });
            setShowForm(false);
        } catch (err) {
            console.error(err);
        }
    };

    // Agrupa historias por userId
    const groupedStories = stories.reduce((acc, story) => {
        const key = story.userId;
        if (!acc[key]) acc[key] = [];
        acc[key].push(story);
        return acc;
    }, {});

    // Convierte a array de grupos
    const storyGroups = Object.values(groupedStories);

    // Tu grupo primero
    const myGroup = storyGroups.find(g => Number(g[0].userId) === Number(user.userId));
    const otherGroups = storyGroups.filter(g => Number(g[0].userId) !== Number(user.userId));

    const handleOpenStory = (group, storyIndex = 0) => {
        setViewerData({ stories: group, index: storyIndex });
    };

    return (
        <div className="feed-page">
            <div className="feed-page__container">
                <div className="feed-page__stories-section">
                    <button
                        className="feed-page__new-story-btn"
                        onClick={() => setShowForm(!showForm)}
                    >
                        + Nueva historia
                    </button>

                    {showForm && (
                        <form onSubmit={handleCreate} className="feed-page__new-story-form">
                            <input
                                placeholder="¿Qué está pasando?"
                                value={form.content}
                                onChange={e => setForm({ ...form, content: e.target.value })}
                            />
                            <input
                                placeholder="URL de imagen"
                                value={form.imageUrl}
                                onChange={e => setForm({ ...form, imageUrl: e.target.value })}
                            />
                            <button type="submit" className="feed-page__new-story-submit">
                                Publicar
                            </button>
                        </form>
                    )}

                    <div className="stories-bar">
                        {/* Tu historia primero separada */}
                        {myGroup && (
                            <>
                                <StoryBubble
                                    group={myGroup}
                                    isOwn={true}
                                    onClick={handleOpenStory}
                                />
                                {otherGroups.length > 0 && <div className="stories-bar__divider" />}
                            </>
                        )}

                        {/* Historias de los demás */}
                        {otherGroups.map(group => (
                            <StoryBubble
                                key={group[0].userId}
                                group={group}
                                isOwn={false}
                                onClick={handleOpenStory}
                            />
                        ))}

                        {storyGroups.length === 0 && (
                            <p style={{ color: 'var(--text-muted)', fontSize: 13, padding: '8px 0' }}>
                                No hay historias activas
                            </p>
                        )}
                    </div>
                </div>
            </div>

            {viewerData && (
                <StoryViewer
                    stories={viewerData.stories}
                    initialIndex={viewerData.index}
                    onClose={() => setViewerData(null)}
                />
            )}
        </div>
    );
};

export default FeedPage;