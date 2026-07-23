import { useRef, useState } from 'react'
import Webcam from 'react-webcam'
import StepIndicator from '../components/StepIndicator'

// Cau hinh camera: uu tien camera sau (environment) tren mobile,
// vi CCCD can chup bang camera chinh, khong phai camera truoc (selfie)
const videoConstraints = {
  width: 1280,
  height: 800,
  facingMode: { ideal: 'environment' },
}

function CaptureCccdPage({ onNext }) {
  const webcamRef = useRef(null)
  const [capturedImage, setCapturedImage] = useState(null)
  const [cameraError, setCameraError] = useState(null)

  const handleCapture = () => {
    const imageSrc = webcamRef.current.getScreenshot()
    if (imageSrc) {
      setCapturedImage(imageSrc)
    }
  }

  const handleRetake = () => {
    setCapturedImage(null)
  }

  return (
    <div className="min-h-screen bg-slate-950 flex flex-col">
      <StepIndicator currentStep={1} />

      <div className="flex-1 flex flex-col items-center justify-center px-6 pb-10">
        <div className="relative w-full max-w-sm aspect-[16/10] bg-slate-900 rounded-2xl overflow-hidden flex items-center justify-center">
          {cameraError ? (
            <div className="text-center px-6">
              <p className="text-red-400 text-sm">
                Không thể truy cập camera. Vui lòng cấp quyền camera cho trình duyệt.
              </p>
            </div>
          ) : capturedImage ? (
            // Da chup - hien anh vua chup thay cho video
            <img
              src={capturedImage}
              alt="Ảnh CCCD đã chụp"
              className="w-full h-full object-cover"
            />
          ) : (
            // Chua chup - hien video camera + khung can chinh
            <>
              <Webcam
                ref={webcamRef}
                audio={false}
                screenshotFormat="image/jpeg"
                videoConstraints={videoConstraints}
                onUserMediaError={() => setCameraError(true)}
                className="w-full h-full object-cover"
              />
              <div className="absolute inset-6 border-2 border-blue-500 rounded-xl pointer-events-none" />
            </>
          )}
        </div>

        <div className="mt-8 text-center">
          <h1 className="text-white text-xl font-semibold">
            {capturedImage ? 'Kiểm tra ảnh vừa chụp' : 'Đặt mặt trước CCCD khớp khung'}
          </h1>
          <p className="text-slate-400 text-sm mt-2">
            {capturedImage
              ? 'Đảm bảo ảnh rõ nét, đọc được số CCCD'
              : 'Đảm bảo đủ ánh sáng, không bị lóa hoặc mờ'}
          </p>
        </div>

        {capturedImage ? (
          <div className="mt-8 flex gap-4 w-full max-w-sm">
            <button
              onClick={handleRetake}
              className="flex-1 py-3.5 border-2 border-slate-700 text-white font-medium
                         rounded-xl transition-colors duration-150 hover:bg-slate-900"
            >
              Chụp lại
            </button>
            <button
              onClick={() => onNext(capturedImage)}
              className="flex-1 py-3.5 bg-blue-600 hover:bg-blue-500 text-white font-medium
                         rounded-xl transition-colors duration-150"
            >
              Tiếp tục
            </button>
          </div>
        ) : (
          // Truoc khi chup: nut chup tron
          <button
            onClick={handleCapture}
            disabled={!!cameraError}
            className="mt-10 w-16 h-16 rounded-full bg-blue-600 hover:bg-blue-500
                       active:scale-95 transition-all duration-150
                       flex items-center justify-center shadow-lg shadow-blue-600/30
                       disabled:opacity-40 disabled:cursor-not-allowed"
            aria-label="Chụp ảnh"
          >
            <div className="w-12 h-12 rounded-full bg-white" />
          </button>
        )}
      </div>
    </div>
  )
}

export default CaptureCccdPage