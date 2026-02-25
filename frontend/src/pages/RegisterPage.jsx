import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { register } from '../api/authApi';
import { useAuth } from '../context/authContext';
import '../styles/Feed.css';

const RegisterPage = () => {
    const [form, setForm] = useState({ username: '', email: '', password: '' });
    const [error, setError] = useState('');
    const { saveUser } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const data = await register(form);
            saveUser(data);
            navigate('/');
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-box">
                <div className="auth-box__card">
                    <h1 className="auth-box__logo">Instagram</h1>
                    {error && <p className="auth-box__error">{error}</p>}
                    <form onSubmit={handleSubmit} className="auth-box__form">
                        <input
                            className="auth-box__input"
                            placeholder="Username"
                            value={form.username}
                            onChange={e => setForm({ ...form, username: e.target.value })}
                        />
                        <input
                            className="auth-box__input"
                            placeholder="Email"
                            type="email"
                            value={form.email}
                            onChange={e => setForm({ ...form, email: e.target.value })}
                        />
                        <input
                            className="auth-box__input"
                            type="password"
                            placeholder="Contraseña"
                            value={form.password}
                            onChange={e => setForm({ ...form, password: e.target.value })}
                        />
                        <button className="auth-box__btn" type="submit">
                            Registrarse
                        </button>
                    </form>
                </div>
                <div className="auth-box__footer">
                    ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;