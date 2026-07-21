import cv2
import numpy as np
from fastapi import APIRouter, UploadFile, File, HTTPException

from app.services.face_service import verify_faces
from app.models.face_models import FaceVerifyResponse

router = APIRouter(prefix="/face", tags=["Face Verification"])


def _read_upload_to_np(file_bytes: bytes) -> np.ndarray:
    np_arr = np.frombuffer(file_bytes, np.uint8)
    image_np = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
    return image_np


@router.post("/verify", response_model=FaceVerifyResponse)
async def verify(
    cccd_image: UploadFile = File(..., description="Anh mat truoc CCCD (co anh chan dung)"),
    selfie_image: UploadFile = File(..., description="Anh selfie chup truc tiep"),
):
    if not cccd_image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="File CCCD phai la anh")
    if not selfie_image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="File selfie phai la anh")

    cccd_bytes = await cccd_image.read()
    selfie_bytes = await selfie_image.read()

    cccd_np = _read_upload_to_np(cccd_bytes)
    selfie_np = _read_upload_to_np(selfie_bytes)

    if cccd_np is None or selfie_np is None:
        raise HTTPException(status_code=400, detail="Khong doc duoc anh, file co the bi loi")

    try:
        result = verify_faces(cccd_np, selfie_np)
    except ValueError as e:
        # DeepFace nem ValueError khi khong tim thay khuon mat trong anh
        raise HTTPException(
            status_code=422,
            detail=f"Khong nhan dien duoc khuon mat trong anh. Vui long chup lai anh ro net hon. Chi tiet: {str(e)}",
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Loi xu ly so khop khuon mat: {str(e)}")

    return FaceVerifyResponse(
        status="success",
        message="So khop thanh cong",
        **result,
    )