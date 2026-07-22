import StepIndicator from '../components/StepIndicator'

function ResultPage() {
  // Du lieu mau de dung layout truoc - se noi API that o task sau
  const mockData = {
    ho_ten: 'TRỊNH QUANG DUY',
    so_cccd: '037094012351',
    ngay_sinh: '04/09/1994',
    similarity_percent: 92,
  }

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <StepIndicator currentStep={3} light/>

      <div className="flex-1 flex flex-col items-center justify-center px-6 pb-10">
        {/* Icon dau mo - signature element noi tiep tu stepper */}
        <div className="w-20 h-20 rounded-full bg-emerald-500 flex items-center justify-center shadow-lg shadow-emerald-500/20">
          <span className="text-white text-4xl">✓</span>
        </div>

        <h1 className="mt-6 text-slate-900 text-xl font-semibold">
          Xác thực thành công
        </h1>

        {/* Card thong tin */}
        <div className="mt-8 w-full max-w-sm bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
          <InfoRow label="Họ và tên" value={mockData.ho_ten} />
          <InfoRow label="Số CCCD" value={mockData.so_cccd} tabular />
          <InfoRow label="Ngày sinh" value={mockData.ngay_sinh} tabular />

          <div className="pt-3 border-t border-slate-100">
            <div className="flex items-center justify-between">
              <span className="text-slate-500 text-sm">Độ khớp khuôn mặt</span>
              <span className="text-emerald-600 font-semibold tabular-nums">
                {mockData.similarity_percent}%
              </span>
            </div>
            <div className="mt-2 h-2 bg-slate-100 rounded-full overflow-hidden">
              <div
                className="h-full bg-emerald-500 rounded-full transition-all duration-500"
                style={{ width: `${mockData.similarity_percent}%` }}
              />
            </div>
          </div>
        </div>

        <button className="mt-8 w-full max-w-sm py-3.5 bg-blue-600 hover:bg-blue-500
                           text-white font-medium rounded-xl transition-colors duration-150">
          Tiếp tục
        </button>
      </div>
    </div>
  )
}

function InfoRow({ label, value, tabular = false }) {
  return (
    <div className="flex items-center justify-between">
      <span className="text-slate-500 text-sm">{label}</span>
      <span className={`text-slate-900 font-medium ${tabular ? 'tabular-nums' : ''}`}>
        {value}
      </span>
    </div>
  )
}

export default ResultPage