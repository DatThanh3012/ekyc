import StepIndicator from '../components/StepIndicator'

function CaptureSelfiePage() {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col">
      <StepIndicator currentStep={2} />

      <div className="flex-1 flex flex-col items-center justify-center px-6 pb-10">
        {/* Khung camera - placeholder */}
        <div className="relative w-full max-w-sm aspect-[4/5] bg-slate-900 rounded-2xl overflow-hidden flex items-center justify-center">
          {/* Khung can chinh dang oval cho khuon mat */}
          <div className="absolute w-56 h-72 border-2 border-blue-500 rounded-[50%]" />

          <span className="text-slate-600 text-sm">Camera preview se hien o day</span>
        </div>

        <div className="mt-8 text-center">
          <h1 className="text-white text-xl font-semibold">
            Giữ mặt trong khung, nhìn thẳng
          </h1>
          <p className="text-slate-400 text-sm mt-2">
            Đảm bảo khuôn mặt rõ nét, không đeo khẩu trang hoặc kính râm
          </p>
        </div>

        <button
          className="mt-10 w-16 h-16 rounded-full bg-blue-600 hover:bg-blue-500
                     active:scale-95 transition-all duration-150
                     flex items-center justify-center shadow-lg shadow-blue-600/30"
          aria-label="Chụp ảnh"
        >
          <div className="w-12 h-12 rounded-full bg-white" />
        </button>
      </div>
    </div>
  )
}

export default CaptureSelfiePage