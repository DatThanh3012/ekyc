import os
import cv2
import numpy as np
from PIL import Image
from vietocr.tool.predictor import Predictor
from vietocr.tool.config import Cfg

from app.services.field_roi_config import FIELD_ROI

_config = Cfg.load_config_from_name('vgg_transformer')
_config['cnn']['pretrained'] = False
_config['device'] = 'cpu'
_detector = Predictor(_config)


def _crop_roi(image_np: np.ndarray, roi: tuple) -> np.ndarray:
    h, w = image_np.shape[:2]
    x0, x1, y0, y1 = roi
    return image_np[int(y0 * h):int(y1 * h), int(x0 * w):int(x1 * w)]


def _ocr_single_crop(crop: np.ndarray) -> str:
    if crop.size == 0:
        return ""
    pil_img = Image.fromarray(cv2.cvtColor(crop, cv2.COLOR_BGR2RGB))
    return _detector.predict(pil_img).strip()


def extract_fields_by_fixed_roi(image_np: np.ndarray, debug_dir: str | None = None) -> dict:
    """
    Cắt ảnh CCCD theo tọa độ % cố định. Field có nhiều dòng (list nhiều ROI)
    sẽ được OCR riêng từng dòng rồi nối lại bằng ký tự xuống dòng "\n"
    để field_parser tự xử lý logic ghép nối phù hợp.
    """
    raw_texts = {}

    if debug_dir:
        os.makedirs(debug_dir, exist_ok=True)

    for field_name, roi_list in FIELD_ROI.items():
        line_texts = []
        for i, roi in enumerate(roi_list):
            crop = _crop_roi(image_np, roi)

            if debug_dir:
                suffix = f"_line{i}" if len(roi_list) > 1 else ""
                cv2.imwrite(os.path.join(debug_dir, f"roi_{field_name}{suffix}.png"), crop)

            text = _ocr_single_crop(crop)
            if text:
                line_texts.append(text)

        raw_texts[field_name] = "\n".join(line_texts)

    return raw_texts