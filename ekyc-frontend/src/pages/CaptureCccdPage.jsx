import StepIndicator from '../components/StepIndicator'

function CaptureCccdPage() {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col">
      <StepIndicator currentStep={1} />

      <div className="flex-1 flex flex-col items-center justify-center px-6 pb-10">
        {/* Khung camera - placeholder, se thay bang video that o task sau */}
        <div className="relative w-full max-w-sm aspect-[16/10] bg-slate-900 rounded-2xl overflow-hidden flex items-center justify-center">
          {/* Khung can chinh hinh chu nhat cho CCCD */}
          <div className="absolute inset-6 border-2 border-blue-500 rounded-xl" />

          <span className="text-slate-600 text-sm">Camera preview se hien o day</span>
        </div>

        <div className="mt-8 text-center">
          <h1 className="text-white text-xl font-semibold">
            Đặt mặt trước CCCD khớp khung
          </h1>
          <p className="text-slate-400 text-sm mt-2">
            Đảm bảo đủ ánh sáng, không bị lóa hoặc mờ
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

export default CaptureCccdPage