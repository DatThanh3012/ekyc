from pydantic import BaseModel
from typing import Optional


class FaceVerifyResponse(BaseModel):
    status: str
    message: str
    verified: Optional[bool] = None
    distance: Optional[float] = None
    threshold: Optional[float] = None
    model: Optional[str] = None
    similarity_percent: Optional[float] = None