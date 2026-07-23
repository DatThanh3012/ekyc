import { useState } from 'react'
import CaptureCccdPage from './CaptureCccdPage'
import CaptureSelfiePage from './CaptureSelfiePage'
import ResultPage from './ResultPage'

function EkycFlow() {
  const [step, setStep] = useState(1)
  const [cccdImage, setCccdImage] = useState(null)
  const [selfieImage, setSelfieImage] = useState(null)

  const goToStep2 = (imageSrc) => {
    setCccdImage(imageSrc)
    setStep(2)
  }

  const goToStep3 = (imageSrc) => {
    setSelfieImage(imageSrc)
    setStep(3)
  }

  const resetFlow = () => {
    setCccdImage(null)
    setSelfieImage(null)
    setStep(1)
  }

  if (step === 1) {
    return <CaptureCccdPage onNext={goToStep2} />
  }

  if (step === 2) {
    return <CaptureSelfiePage onNext={goToStep3} />
  }

  return (
    <ResultPage
      cccdImage={cccdImage}
      selfieImage={selfieImage}
      onRestart={resetFlow}
    />
  )
}

export default EkycFlow