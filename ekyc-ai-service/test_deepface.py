from deepface import DeepFace

result = DeepFace.verify(
    img1_path="test_images/face1.jpg",
    img2_path="test_images/face2.jpg",
    model_name="Facenet",   # có thể đổi thành "VGG-Face" sau để so sánh
)

print("Kết quả so khớp khuôn mặt:")
print("Verified:", result["verified"])
print("Khoảng cách (distance):", result["distance"])
print("Ngưỡng (threshold):", result["threshold"])
print("Model dùng:", result["model"])