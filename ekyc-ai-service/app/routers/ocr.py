import cv2
import numpy as np
from fastapi import APIRouter, UploadFile, File, HTTPException

from app.services.ocr_service import extract_fields_by_fixed_roi
from app.services.field_parser import parse_cccd_fields
from app.models.ocr_models import CccdExtractResponse

router = APIRouter(prefix="/ocr", tags=["OCR"])


@router.post("/extract-cccd", response_model=CccdExtractResponse)
async def extract_cccd(file: UploadFile = File(...)):
    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="File tải lên phải là ảnh")

    contents = await file.read()
    np_arr = np.frombuffer(contents, np.uint8)
    image_np = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

    if image_np is None:
        raise HTTPException(status_code=400, detail="Không đọc được ảnh, file có thể bị lỗi")

    try:
        raw_texts = extract_fields_by_fixed_roi(image_np)
        fields = parse_cccd_fields(raw_texts)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Lỗi xử lý OCR: {str(e)}")

    return CccdExtractResponse(
        status="success",
        message="Trích xuất thành công",
        **fields,
    )