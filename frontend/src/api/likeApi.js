const BASE_URL = import.meta.env.VITE_API_URL;

const authHeaders = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
});

export const toggleLike = async (storyId, userId) => {
    const res = await fetch(`${BASE_URL}/api/likes/stories/toggle`, {
        method: 'POST',
        headers: authHeaders(),
        body: JSON.stringify({ storyId, userId })
    });
    if (res.status === 204) return null;
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};

export const getLikeCount = async (storyId) => {
    const res = await fetch(`${BASE_URL}/api/likes/stories/${storyId}/count`, {
        headers: authHeaders()
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};

export const hasLiked = async (storyId, userId) => {
    const res = await fetch(`${BASE_URL}/api/likes/stories/${storyId}/hasLiked/${userId}`, {
        headers: authHeaders()
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};

export const getUsersWhoLiked = async (storyId) => {
    const res = await fetch(`${BASE_URL}/api/likes/stories/${storyId}/users`, {
        headers: authHeaders()
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};