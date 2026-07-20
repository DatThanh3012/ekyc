import cv2

image_path = "test_images/trinhquangduy_front.jpg"
img = cv2.imread(image_path)
h, w = img.shape[:2]

# Vẽ lưới mỗi 5% chiều rộng/cao để dễ đọc tọa độ tương đối
for i in range(0, 101, 5):
    x = int(w * i / 100)
    y = int(h * i / 100)
    cv2.line(img, (x, 0), (x, h), (0, 255, 0), 1)
    cv2.line(img, (0, y), (w, y), (0, 255, 0), 1)
    if i % 10 == 0:
        cv2.putText(img, f"{i}", (x + 2, 15), cv2.FONT_HERSHEY_SIMPLEX, 0.4, (0, 0, 255), 1)
        cv2.putText(img, f"{i}", (2, y + 12), cv2.FONT_HERSHEY_SIMPLEX, 0.4, (0, 0, 255), 1)

cv2.imwrite("debug_output/grid_overlay.png", img)
print(f"Kích thước ảnh: {w}x{h}")
print("Đã lưu debug_output/grid_overlay.png — mở file này để đọc tọa độ %")