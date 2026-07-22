function StepIndicator({ currentStep, light = false }) {
  const steps = [1, 2, 3]

  return (
    <div className="flex items-center justify-center gap-3 py-6">
      {steps.map((step) => {
        const isDone = step < currentStep
        const isActive = step === currentStep
        const isUpcoming = !isDone && !isActive

        return (
          <div
            key={step}
            className={`
              w-10 h-10 rounded-lg border-2 flex items-center justify-center
              text-sm font-semibold transition-all duration-300
              ${isDone ? 'bg-emerald-500 border-emerald-500 text-white' : ''}
              ${isActive ? 'border-blue-600 text-blue-600 animate-pulse' : ''}
              ${isUpcoming && !light ? 'border-slate-300 text-slate-300' : ''}
              ${isUpcoming && light ? 'border-slate-200 text-slate-300' : ''}
            `}
          >
            {isDone ? '✓' : step}
          </div>
        )
      })}
    </div>
  )
}

export default StepIndicator