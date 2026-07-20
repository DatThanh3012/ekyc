from vietocr.tool.predictor import Predictor
from vietocr.tool.config import Cfg

# Load config có sẵn cho model VGG-Transformer (chính xác cao, tốc độ vừa phải)
config = Cfg.load_config_from_name('vgg_transformer')
config['cnn']['pretrained'] = False
config['device'] = 'cpu'   # đổi thành 'cuda:0' nếu máy có GPU NVIDIA

detector = Predictor(config)

from PIL import Image
img = Image.open('test_images/test.jpg')
result = detector.predict(img)

print("Kết quả OCR:", result)