import numpy as np
from deepface import DeepFace


def verify_faces(img1_np: np.ndarray, img2_np: np.ndarray, model_name: str = "Facenet") -> dict:
    result = DeepFace.verify(
        img1_path=img1_np,
        img2_path=img2_np,
        model_name=model_name,
        enforce_detection=True,
    )

    distance = float(result["distance"])
    threshold = float(result["threshold"])

    # Cong thuc moi: giu tuyen tinh trong khoang [0, 2*threshold] roi clamp ve [0, 100]
    # de tranh phong dai chenh lech khi distance gan bang threshold
    similarity_percent = max(0.0, min(100.0, (1 - distance / (threshold * 2)) * 100))

    return {
        "verified": bool(result["verified"]),
        "distance": round(distance, 4),
        "threshold": round(threshold, 4),
        "model": result["model"],
        "similarity_percent": round(similarity_percent, 2),
    }