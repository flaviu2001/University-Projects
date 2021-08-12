import math

import torch
import model
import matplotlib.pyplot as plt

filepath = "network.pt"
ann = model.Net()

ann.load_state_dict(torch.load(filepath))
ann.eval()

matrix = []
with open("training.csv", "r") as file:
    n = int(file.readline()[:-1])
    for _ in range(n):
        matrix.append(tuple(map(float, file.readline().strip().split(","))))

ans_matrix = []
for vector in matrix:
    ans_matrix.append(math.sin(vector[0] + vector[1] / math.pi))

x = torch.Tensor(matrix)
y = torch.unsqueeze(torch.Tensor(ans_matrix), dim=1)

ans = ann(x)
data = []
for i, y in enumerate(zip(ans, y)):
    data.append((i, y[0].tolist()[0] - y[1].tolist()[0]))
for val in data:
    plt.scatter(val[0], val[1], color="black")
plt.show()

# x = float(input("x = "))
# y = float(input("y = "))
# x = torch.tensor([x, y])
# print(ann(x).tolist()[0])
