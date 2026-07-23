import { useRef, useState } from 'react'
import Webcam from 'react-webcam'
import StepIndicator from '../components/StepIndicator'

// Selfie luon dung camera truoc (user-facing), khac voi man CCCD dung camera sau
const videoConstraints = {
  width: 720,
  height: 900,
  facingMode: 'user',
}

function CaptureSelfiePage({ onNext }) {
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
      <StepIndicator currentStep={2} />

      <div className="flex-1 flex flex-col items-center justify-center px-6 pb-10">
        <div className="relative w-full max-w-sm aspect-[4/5] bg-slate-900 rounded-2xl overflow-hidden flex items-center justify-center">
          {cameraError ? (
            <div className="text-center px-6">
              <p className="text-red-400 text-sm">
                Không thể truy cập camera. Vui lòng cấp quyền camera cho trình duyệt.
              </p>
            </div>
          ) : capturedImage ? (
            <img
              src={capturedImage}
              alt="Ảnh selfie đã chụp"
              className="w-full h-full object-cover"
            />
          ) : (
            <>
              <Webcam
                ref={webcamRef}
                audio={false}
                screenshotFormat="image/jpeg"
                videoConstraints={videoConstraints}
                onUserMediaError={() => setCameraError(true)}
                mirrored={true}
                className="w-full h-full object-cover"
              />
              <div className="absolute w-56 h-72 border-2 border-blue-500 rounded-[50%] pointer-events-none" />
            </>
          )}
        </div>

        <div className="mt-8 text-center">
          <h1 className="text-white text-xl font-semibold">
            {capturedImage ? 'Kiểm tra ảnh vừa chụp' : 'Giữ mặt trong khung, nhìn thẳng'}
          </h1>
          <p className="text-slate-400 text-sm mt-2">
            {capturedImage
              ? 'Đảm bảo nhìn rõ khuôn mặt, không bị mờ hoặc thiếu sáng'
              : 'Đảm bảo khuôn mặt rõ nét, không đeo khẩu trang hoặc kính râm'}
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
              Xác thực
            </button>
          </div>
        ) : (
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

export default CaptureSelfiePage