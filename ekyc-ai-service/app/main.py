from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import ocr, face

app = FastAPI(
    title="eKYC AI Service",
    description="OCR CCCD + Face Matching Service",
    version="0.1.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(ocr.router)
app.include_router(face.router)


@app.get("/")
def root():
    return {"service": "eKYC AI Service", "status": "running"}


@app.get("/health")
def health_check():
    return {"status": "ok"}