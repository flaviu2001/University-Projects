import torch
import model

filepath = "network.pt"
ann = model.Net()

ann.load_state_dict(torch.load(filepath))
ann.eval()

x = float(input("x = "))
y = float(input("y = "))
x = torch.tensor([x, y])
print(ann(x).tolist()[0])
