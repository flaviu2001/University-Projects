import torch
import torch.nn as nn
from matplotlib import pyplot
from torch.autograd import Variable
from torch.optim import Adam
from torch.utils.data import DataLoader

from constants import BATCH_SIZE, PIC_SIZE, PERCENT_TEST, LEARNING_RATE, EPOCHS, OUTPUT_NODES, LEARNING_RATE_DECAY
from utils import get_image_list, random_subset, test_transformations, train_transformations

from dataset import ImageClassifierDataset

device = torch.device('cuda:0' if torch.cuda.is_available() else 'cpu')


class Unit(nn.Module):
    def __init__(self, in_channels, out_channels):
        super(Unit, self).__init__()
        self.conv = nn.Conv2d(in_channels=in_channels, kernel_size=3, out_channels=out_channels, stride=1, padding=1)
        self.bn = nn.BatchNorm2d(num_features=out_channels)
        self.relu = nn.ReLU()

    def forward(self, input_tensor):
        output = self.conv(input_tensor)
        output = self.bn(output)
        output = self.relu(output)
        return output


class SimpleNet(nn.Module):
    def __init__(self):
        super(SimpleNet, self).__init__()

        # Create 14 layers of the unit with max pooling in between
        self.unit1 = Unit(in_channels=3, out_channels=PIC_SIZE)
        self.unit2 = Unit(in_channels=PIC_SIZE, out_channels=PIC_SIZE)
        self.unit3 = Unit(in_channels=PIC_SIZE, out_channels=PIC_SIZE)

        self.pool1 = nn.MaxPool2d(kernel_size=2)

        self.unit4 = Unit(in_channels=PIC_SIZE, out_channels=PIC_SIZE*2)
        self.unit5 = Unit(in_channels=PIC_SIZE*2, out_channels=PIC_SIZE*2)
        self.unit6 = Unit(in_channels=PIC_SIZE*2, out_channels=PIC_SIZE*2)
        self.unit7 = Unit(in_channels=PIC_SIZE*2, out_channels=PIC_SIZE*2)

        self.pool2 = nn.MaxPool2d(kernel_size=2)

        self.unit8 = Unit(in_channels=PIC_SIZE*2, out_channels=PIC_SIZE*4)
        self.unit9 = Unit(in_channels=PIC_SIZE*4, out_channels=PIC_SIZE*4)
        self.unit10 = Unit(in_channels=PIC_SIZE*4, out_channels=PIC_SIZE*4)
        self.unit11 = Unit(in_channels=PIC_SIZE*4, out_channels=PIC_SIZE*4)

        self.pool3 = nn.MaxPool2d(kernel_size=2)

        self.unit12 = Unit(in_channels=PIC_SIZE*4, out_channels=PIC_SIZE*4)
        self.unit13 = Unit(in_channels=PIC_SIZE*4, out_channels=PIC_SIZE*4)
        self.unit14 = Unit(in_channels=PIC_SIZE*4, out_channels=PIC_SIZE*4)

        self.avgpool = nn.AvgPool2d(kernel_size=5)

        # Add all the units into the Sequential layer in exact order
        self.net = nn.Sequential(self.unit1, self.unit2, self.unit3, self.pool1, self.unit4, self.unit5, self.unit6,
                                 self.unit7, self.pool2, self.unit8, self.unit9, self.unit10, self.unit11, self.pool3,
                                 self.unit12, self.unit13, self.unit14, self.avgpool)

        self.fc = nn.Linear(in_features=PIC_SIZE*4, out_features=OUTPUT_NODES)

    def forward(self, input_tensor):
        output = self.net(input_tensor)
        output = output.view(-1, PIC_SIZE*4)
        output = self.fc(output)
        return output


def adjust_learning_rate():
    for param_group in optimizer.param_groups:
        param_group['lr'] *= LEARNING_RATE_DECAY


def save_models(epoch):
    torch.save(model.state_dict(), f"models/network_{epoch}.model") # make sure models folder exists


def test():
    model.eval()
    test_acc = 0.0
    for i, (images, labels) in enumerate(test_loader):
        if cuda_avail:
            images = Variable(images.cuda())
            labels = Variable(labels.cuda())
        outputs = model(images)
        _, prediction = torch.max(outputs.data, 1)
        test_acc += torch.sum(torch.eq(prediction, labels.data))
    test_acc = test_acc / test_size

    return test_acc


def train(num_epochs):
    best_acc = 0.0
    best_at = 0
    loss_list = []
    least_loss = None
    for epoch in range(num_epochs):
        model.train()
        train_acc = 0.0
        train_loss = 0.0
        for images, labels in train_loader:
            if cuda_avail:
                images = Variable(images.cuda())
                labels = Variable(labels.cuda())
            optimizer.zero_grad()
            outputs = model(images)
            loss = loss_fn(outputs, labels)
            loss.backward()
            optimizer.step()
            train_loss += loss.cpu().data.item() * images.size(0)
            _, prediction = torch.max(outputs.data, 1)
            train_acc += torch.sum(prediction == labels.data)
        adjust_learning_rate()
        train_acc = train_acc / train_size
        train_loss = train_loss / train_size
        loss_list.append(train_loss)
        if least_loss is None:
            least_loss = (epoch, train_loss)
        model.eval()
        test_acc = test()
        save_models(epoch)
        if test_acc > best_acc:
            best_acc = test_acc
            best_at = epoch
        print(f"Epoch {epoch}, "
              f"Train Accuracy: {train_acc}, "
              f"TrainLoss: {train_loss}, "
              f"Test Accuracy: {test_acc}, "
              f"Best Test Accuracy: {best_acc}, "
              f"Best at {best_at}")
        if train_loss < least_loss[1]:
            least_loss = (epoch, train_loss)
        if epoch-least_loss[0] > 10:
            print("Loss function not getting any better, aborting")
            break
    pyplot.plot(loss_list)
    pyplot.ylim((0, None))
    pyplot.show()


if __name__ == "__main__":
    train_images, test_images = random_subset(get_image_list(), 1-PERCENT_TEST)

    train_size = len(train_images)
    train_set = ImageClassifierDataset(train_images, train_transformations)
    train_loader = DataLoader(train_set, batch_size=BATCH_SIZE, shuffle=True, num_workers=4)

    print("Loaded train photos")

    test_size = len(test_images)
    test_set = ImageClassifierDataset(test_images, test_transformations)
    test_loader = DataLoader(test_set, batch_size=BATCH_SIZE, shuffle=False, num_workers=4)

    print("Loaded test photos")

    # Check if gpu support is available
    cuda_avail = torch.cuda.is_available()
    model = SimpleNet()
    if cuda_avail:
        model.cuda()

    optimizer = Adam(model.parameters(), lr=LEARNING_RATE, weight_decay=0.0000)
    loss_fn = nn.CrossEntropyLoss()

    print("Starting training")
    train(EPOCHS)
