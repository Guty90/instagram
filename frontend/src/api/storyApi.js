const BASE_URL = import.meta.env.VITE_API_URL;

const authHeaders = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
});

export const getActiveStories = async () => {
    const res = await fetch(`${BASE_URL}/api/stories/active`, {
        headers: authHeaders()
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};

export const createStory = async (data) => {
    const res = await fetch(`${BASE_URL}/api/stories`, {
        method: 'POST',
        headers: authHeaders(),
        body: JSON.stringify(data)
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};

export const deleteStory = async (id) => {
    const res = await fetch(`${BASE_URL}/api/stories/${id}`, {
        method: 'DELETE',
        headers: authHeaders()
    });
    if (!res.ok) throw new Error(await res.text());
};