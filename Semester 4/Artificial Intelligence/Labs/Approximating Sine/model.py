import torch
import torch.nn as nn


class Net(torch.nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.model = nn.Sequential(
            nn.Linear(2, 20),
            nn.GELU(),
            nn.Linear(20, 50),
            nn.GELU(),
            nn.Linear(50, 1)
        )

    def forward(self, x):
        return self.model(x)
