import { useState } from 'react'
import { getToken, setToken } from './api'
import Board from './Board'
import Login from './Login'
import './App.css'

export default function App() {
  const [loggedIn, setLoggedIn] = useState(() => getToken() !== null)

  function logout() {
    setToken(null)
    setLoggedIn(false)
  }

  if (!loggedIn) return <Login onLogin={() => setLoggedIn(true)} />

  return (
      <main>
        <header>
          <h1>DealFlow</h1>
          <button onClick={logout}>Log out</button>
        </header>
        <Board onUnauthorized={logout} />
      </main>
  )
}