import re

CCCD_NUMBER_PATTERN = re.compile(r"\d{9,12}")
DATE_PATTERN = re.compile(r"\d{2}/\d{2}/\d{4}")


def _clean_label_prefix(text: str) -> str:
    if ":" in text:
        return text.split(":", 1)[-1].strip()
    return text.strip()


def _extract_between(text: str, start_keywords: list[str], end_keywords: list[str]) -> str | None:
    """
    Trích phần giá trị nằm SAU 1 trong các start_keywords và TRƯỚC 1 trong các end_keywords
    (không phân biệt hoa/thường). Dùng cho các dòng có 2 giá trị dính liền như
    "Giới tính / Sex Nam Quốc tịch / Nationality: Việt Nam".
    """
    lower = text.lower()
    start_idx = None
    for kw in start_keywords:
        idx = lower.find(kw.lower())
        if idx != -1:
            start_idx = idx + len(kw)
            break
    if start_idx is None:
        return None

    end_idx = len(text)
    for kw in end_keywords:
        idx = lower.find(kw.lower(), start_idx)
        if idx != -1 and idx < end_idx:
            end_idx = idx

    value = text[start_idx:end_idx]
    # Dọn ký tự rác thường gặp: dấu ":", "/", "|", chữ "I" đơn lẻ (OCR đọc nhầm dấu "/")
    value = re.sub(r"^[\s:/|I.]+", "", value)
    value = re.sub(r"[\s:/|I.]+$", "", value)
    return value.strip() or None


def parse_cccd_fields(raw_texts: dict) -> dict:
    result = {
        "so_cccd": None, "ho_ten": None, "ngay_sinh": None,
        "gioi_tinh": None, "quoc_tich": None,
        "que_quan": None, "noi_thuong_tru": None,
        "raw_texts": raw_texts,
    }

    so_text = raw_texts.get("so_cccd", "")
    match = CCCD_NUMBER_PATTERN.search(so_text)
    result["so_cccd"] = match.group() if match else _clean_label_prefix(so_text)

    result["ho_ten"] = _clean_label_prefix(raw_texts.get("ho_ten", "")).upper()

    ngay_sinh_text = raw_texts.get("ngay_sinh", "")
    match_date = DATE_PATTERN.search(ngay_sinh_text)
    result["ngay_sinh"] = match_date.group() if match_date else _clean_label_prefix(ngay_sinh_text)

    # Tách "Giới tính / Sex Nam Quốc tịch / Nationality: Việt Nam" thành 2 giá trị riêng
    combined = raw_texts.get("gioi_tinh_quoc_tich", "")
    result["gioi_tinh"] = _extract_between(
        combined,
        start_keywords=["sex"],
        end_keywords=["quốc tịch", "quoc tich", "nationality"],
    )
    result["quoc_tich"] = _extract_between(
        combined,
        start_keywords=["nationality", "quốc tịch", "quoc tich"],
        end_keywords=[],
    )

    result["que_quan"] = _clean_label_prefix(raw_texts.get("que_quan", ""))

    # noi_thuong_tru: dòng 1 chứa nhãn + giá trị đầu, dòng 2 (nếu có) là phần tiếp theo
    ntt_lines = raw_texts.get("noi_thuong_tru", "").split("\n")
    ntt_line0 = _clean_label_prefix(ntt_lines[0]) if ntt_lines else ""
    # Bỏ phần nhãn tiếng Anh "Place of residence" còn sót nếu chưa có dấu ":"
    ntt_line0 = re.sub(
        r"(nơi thường trú|noi thuong tru|place of residence)",
        "", ntt_line0, flags=re.IGNORECASE
    ).strip(" :")
    ntt_rest = [l.strip() for l in ntt_lines[1:] if l.strip()]
    result["noi_thuong_tru"] = ", ".join([p for p in [ntt_line0] + ntt_rest if p])

    return result