import { useState } from 'react'
import { ApiError, api, setToken } from './api'

export default function Login({ onLogin }: { onLogin: () => void }) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')

    async function submit(event: React.FormEvent) {
        event.preventDefault()
        try {
            const result = await api.login(username, password)
            setToken(result.token)
            onLogin()
        } catch (e) {
            setError(e instanceof ApiError && e.status === 401 ? 'Wrong username or password' : 'Login failed')
        }
    }

    return (
        <form className="login" onSubmit={submit}>
            <h1>DealFlow</h1>
            <input placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
            <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
            <button type="submit">Sign in</button>
            {error && <p className="error">{error}</p>}
        </form>
    )
}