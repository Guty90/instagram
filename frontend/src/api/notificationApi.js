const BASE_URL = import.meta.env.VITE_API_URL;

const authHeaders = () => ({
    'Authorization': `Bearer ${localStorage.getItem('token')}`
});

export const getNotifications = async (userId) => {
    const res = await fetch(`${BASE_URL}/api/notifications/${userId}`, {
        headers: authHeaders()
    });
    if (!res.ok) throw new Error(await res.text());
    return res.json();
};