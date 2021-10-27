import math

import torch
import tqdm
import matplotlib.pyplot as plt

import model

training_percentage = 0.8
epochs = 100

matrix = []
with open("training.csv", "r") as file:
    n = int(file.readline()[:-1])
    for _ in range(n):
        matrix.append(tuple(map(float, file.readline().strip().split(","))))
cutoff = int(len(matrix) * training_percentage)
test_matrix = matrix[cutoff:]
matrix = matrix[:cutoff]

ans_matrix = []
for vector in matrix:
    ans_matrix.append(math.sin(vector[0] + vector[1] / math.pi))
ans_test_matrix = []
for vector in test_matrix:
    ans_test_matrix.append(math.sin(vector[0] + vector[1] / math.pi))

x = torch.Tensor(matrix)
# print(matrix)
# print(x)
test_x = torch.Tensor(test_matrix)
y = torch.unsqueeze(torch.Tensor(ans_matrix), dim=1)
# print(ans_matrix)
# print(y)
# exit()
test_y = torch.unsqueeze(torch.Tensor(ans_test_matrix), dim=1)

ann = model.Net()
lossFunction = torch.nn.MSELoss()
# computes how the error is propagated to left
optimizer_batch = torch.optim.Adam(ann.parameters())

loss_list = []
batch_size = 1
n_batches = int(len(x) / batch_size)
print(f"Running {n_batches} batches")

for epoch in tqdm.trange(epochs):
    for batch in range(n_batches):
        batch_x, batch_y = x[batch * batch_size:(batch + 1) * batch_size], \
                           y[batch * batch_size:(batch + 1) * batch_size]
        prediction = ann(batch_x)
        loss = lossFunction(prediction, batch_y)
        optimizer_batch.zero_grad()
        loss.backward()
        optimizer_batch.step()
    loss = lossFunction(ann(test_x), test_y)
    loss_list.append(loss.tolist())
    if epoch % 100 == 99:
        print('\repoch: {}\tLoss =  {:.5f}'.format(epoch, loss))
filepath = "network.pt"
torch.save(ann.state_dict(), filepath)

plt.plot(loss_list)
plt.ylim((0, None))
plt.show()
