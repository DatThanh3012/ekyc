import { useState } from 'react'
import LoginPage from './pages/LoginPage'

function App() {
  const [token, setToken] = useState(null)

  if (!token) {
    return <LoginPage onLoginSuccess={(t) => setToken(t)} />
  }

  return <div className="p-8">Đăng nhập thành công! Token: {token.slice(0, 20)}...</div>
}

export default App