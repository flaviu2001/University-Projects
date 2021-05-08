import math

import torch
import tqdm
import matplotlib.pyplot as plt

import model

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

ann = model.Net()
lossFunction = torch.nn.MSELoss()
optimizer_batch = torch.optim.Adam(ann.parameters())

loss_list = []
batch_size = 1
n_batches = int(len(x) / batch_size)
print(f"Running {n_batches} batches")

for epoch in tqdm.trange(n):
    for batch in range(n_batches):
        batch_x, batch_y = x[batch * batch_size:(batch + 1) * batch_size], \
                           y[batch * batch_size:(batch + 1) * batch_size]
        prediction = ann(batch_x)
        loss = lossFunction(prediction, batch_y)
        loss_list.append(loss.tolist())
        optimizer_batch.zero_grad()
        loss.backward()
        optimizer_batch.step()
    if epoch % 100 == 99:
        y_pred = ann(x)
        loss = lossFunction(y_pred, y)
        print('\repoch: {}\tLoss =  {:.5f}'.format(epoch, loss))
filepath = "network.pt"
torch.save(ann.state_dict(), filepath)

plt.plot(loss_list)
plt.ylim((0, None))
plt.show()
