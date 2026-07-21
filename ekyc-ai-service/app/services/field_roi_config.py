# Mỗi field giờ là 1 LIST các ROI (thường 1 ROI, riêng field nhiều dòng thì 2+ ROI theo thứ tự trên->dưới)
FIELD_ROI = {
    "so_cccd":             [(0.40, 0.75, 0.45, 0.52)],
    "ho_ten":               [(0.30, 0.65, 0.56, 0.63)],
    "ngay_sinh":            [(0.30, 0.75, 0.63, 0.67)],
    "gioi_tinh_quoc_tich":  [(0.30, 0.90, 0.67, 0.72)],
    "que_quan":             [(0.30, 0.77, 0.76, 0.82)],
    "noi_thuong_tru":       [
        (0.30, 0.80, 0.82, 0.875),   # dòng 1: nhãn + phần giá trị đầu (VD "Xóm 6")
        (0.30, 0.80, 0.875, 0.93),   # dòng 2: phần giá trị tiếp theo (VD "Tân Thành, Kim Sơn, Ninh Bình")
    ],
}