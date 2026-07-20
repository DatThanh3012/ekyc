from pydantic import BaseModel
from typing import Optional


class CccdExtractResponse(BaseModel):
    status: str
    message: str
    so_cccd: Optional[str] = None
    ho_ten: Optional[str] = None
    ngay_sinh: Optional[str] = None
    gioi_tinh: Optional[str] = None
    quoc_tich: Optional[str] = None
    que_quan: Optional[str] = None
    noi_thuong_tru: Optional[str] = None
    raw_texts: dict = {}