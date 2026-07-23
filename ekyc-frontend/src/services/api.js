import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

// Tu dong dinh kem token vao moi request neu co (dat sau khi login thanh cong)
export function setAuthToken(token) {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`
  } else {
    delete api.defaults.headers.common['Authorization']
  }
}

export async function login(username, password) {
  const response = await api.post('/auth/login', { username, password })
  return response.data
}

export async function register(username, password, email) {
  const response = await api.post('/auth/register', { username, password, email })
  return response.data
}

// dataUrl la chuoi base64 tu webcam (dang "data:image/jpeg;base64,...")
// Can chuyen thanh Blob truoc khi gui bang multipart/form-data
function dataUrlToBlob(dataUrl) {
  const [header, base64Data] = dataUrl.split(',')
  const mime = header.match(/:(.*?);/)[1]
  const binary = atob(base64Data)
  const array = new Uint8Array(binary.length)
  for (let i = 0; i < binary.length; i++) {
    array[i] = binary.charCodeAt(i)
  }
  return new Blob([array], { type: mime })
}

export async function uploadCccd(imageDataUrl) {
  const blob = dataUrlToBlob(imageDataUrl)
  const formData = new FormData()
  formData.append('file', blob, 'cccd.jpg')

  const response = await api.post('/kyc/cccd', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return response.data
}

export async function verifyFace(imageDataUrl) {
  const blob = dataUrlToBlob(imageDataUrl)
  const formData = new FormData()
  formData.append('selfie', blob, 'selfie.jpg')

  const response = await api.post('/kyc/verify-face', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return response.data
}

export default api