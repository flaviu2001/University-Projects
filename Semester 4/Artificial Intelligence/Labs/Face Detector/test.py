import os

import torchvision.transforms as transforms
import torch
from PIL import Image

from constants import PIC_SIZE
from network import SimpleNet
from utils import test_transformations

filePath = "models/network_19.model" # path to model
ann = SimpleNet()
ann.load_state_dict(torch.load(filePath))
ann.eval()
success = 0
total = 0
for index in (1, 2, 3, 4, 6,):
    which = 0
    bad = 0
    for file in (file_list := os.listdir(f'dataset{index}')):
        which += 1
        total += 1
        files = f"./dataset{index}/" + file
        image = test_transformations(Image.open(files).convert('RGB'))
        image = image.unsqueeze(0)
        output = ann(image)
        other, prediction = torch.max(output.data, 1)
        if "man" in file:
            expected = 1
        elif "woman" in file:
            expected = 1
        else:
            expected = 0
        if expected == prediction.numpy()[0]:
            success += 1
        else:
            bad += 1
            print(files, f"{which} out of {len(file_list)}")
    print(f"{bad} bad out of {len(file_list)} in dataset{index}")
print(f"The accuracy is {100 * success / total}")
