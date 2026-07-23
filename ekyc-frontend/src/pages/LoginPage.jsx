import { useState } from 'react'
import { login, register, setAuthToken } from '../services/api'

function LoginPage({ onLoginSuccess }) {
  const [isRegisterMode, setIsRegisterMode] = useState(false)
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [email, setEmail] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    try {
      const data = isRegisterMode
        ? await register(username, password, email)
        : await login(username, password)

      setAuthToken(data.token)
      onLoginSuccess(data.token)
    } catch (err) {
      const message = err.response?.data?.message || 'Co loi xay ra, vui long thu lai'
      setError(message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center px-6">
      <div className="w-full max-w-sm">
        <h1 className="text-2xl font-bold text-slate-900 text-center">
          eKYC {isRegisterMode ? 'Đăng ký' : 'Đăng nhập'}
        </h1>

        <form onSubmit={handleSubmit} className="mt-8 space-y-4">
          <input
            type="text"
            placeholder="Tên đăng nhập"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="w-full px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          {isRegisterMode && (
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          )}

          <input
            type="password"
            placeholder="Mật khẩu"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="w-full px-4 py-3 border border-slate-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          {error && (
            <p className="text-red-600 text-sm">{error}</p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 bg-blue-600 hover:bg-blue-500 text-white font-medium
                       rounded-xl transition-colors duration-150 disabled:opacity-50"
          >
            {loading ? 'Đang xử lý...' : (isRegisterMode ? 'Đăng ký' : 'Đăng nhập')}
          </button>
        </form>

        <button
          onClick={() => setIsRegisterMode(!isRegisterMode)}
          className="mt-4 w-full text-center text-sm text-blue-600 hover:underline"
        >
          {isRegisterMode ? 'Đã có tài khoản? Đăng nhập' : 'Chưa có tài khoản? Đăng ký'}
        </button>
      </div>
    </div>
  )
}

export default LoginPage