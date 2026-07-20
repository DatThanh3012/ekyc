import cv2
from app.services.ocr_service import extract_fields_by_fixed_roi
from app.services.field_parser import parse_cccd_fields

image_np = cv2.imread("test_images/trinhquangduy_front.jpg")
raw_texts = extract_fields_by_fixed_roi(image_np, debug_dir="debug_output")
fields = parse_cccd_fields(raw_texts)

for k, v in fields.items():
    print(f"{k}: {v}")