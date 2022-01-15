import os
import random
from functools import reduce

import matplotlib.pyplot as plt
import numpy as np

import cifar10
from metrics import print_stats
from softmax import SoftmaxClassifier

cifar_root_dir = 'cifar-10-batches-py'

# the number of trains performed with different hyper-parameters
search_iter = 10
# the batch size
batch_size = 200
# number of training steps per training process
train_steps = 5000

# load cifar10 dataset
X_train, y_train, X_test, y_test = cifar10.load_cifar10(cifar_root_dir)

# convert the training and test data to floating point
X_train = X_train.astype(np.float32)
X_test = X_test.astype(np.float32)

# Reshape the training data such that we have one image per row
X_train = np.reshape(X_train, (X_train.shape[0], -1))
X_test = np.reshape(X_test, (X_test.shape[0], -1))

# pre-processing: subtract mean image
mean_image = np.mean(X_train, axis=0)
X_train -= mean_image
X_test -= mean_image

# Bias trick - add 1 to each training example
X_train = np.hstack([X_train, np.ones((X_train.shape[0], 1))])
X_test = np.hstack([X_test, np.ones((X_test.shape[0], 1))])

os.system('rm -rf train')
os.mkdir('train')

best_acc = -1
best_cls_path = ''

# the search limits for the learning rate and regularization strength
# we'll use log scale for the search
lr_bounds = [-7, -5]
reg_bounds = [3000, 80000]

input_size_flattened = reduce((lambda a, b: a * b), X_train[0].shape)
results = []

for index in range(0, search_iter):
    # use log scale for sampling the learning rate
    lr = pow(10, random.uniform(lr_bounds[0], lr_bounds[1]))
    reg_strength = random.uniform(reg_bounds[0], reg_bounds[1])

    cls = SoftmaxClassifier(input_shape=input_size_flattened, num_classes=cifar10.NUM_CLASSES)
    history = cls.fit(X_train, y_train, lr=lr, reg_strength=reg_strength,
                      steps=train_steps, bs=batch_size)
    results.append({
        'lr': lr,
        'reg': reg_strength,
        'history': history
    })

    y_train_pred = cls.predict(X_train)
    y_val_pred = cls.predict(X_test)

    train_acc = np.mean(y_train == y_train_pred)

    test_acc = np.mean(y_test == y_val_pred)
    print(f'lr: {lr}\nreg_strength: {reg_strength}\ntest_acc: {test_acc}\ntrain_acc: {train_acc}\n')
    cls_path = os.path.join('train', 'softmax_lr{:.8f}_reg{:.4f}-test{:.2f}.npy'.format(lr, reg_strength, test_acc))
    cls.save(cls_path)

    if test_acc > best_acc:
        best_acc = test_acc
        best_cls_path = cls_path

num_rows = search_iter // 5 + 1
for idx, res in enumerate(results):
    plt.subplot(num_rows, 5, idx + 1)
    plt.plot(res['history'])
plt.show()

best_softmax = SoftmaxClassifier(input_shape=input_size_flattened, num_classes=cifar10.NUM_CLASSES)
best_softmax.load(best_cls_path)

plt.rcParams['image.cmap'] = 'gray'
# now let's display the weights for the best model
weights = best_softmax.get_weights((32, 32, 3))
w_min = np.amin(weights)
w_max = np.amax(weights)

for idx in range(0, cifar10.NUM_CLASSES):
    plt.subplot(2, 5, idx + 1)
    # normalize the weights
    template = 255.0 * (weights[idx, :, :, :].squeeze() - w_min) / (w_max - w_min)
    template = template.astype(np.uint8)
    plt.imshow(template)
    plt.title(cifar10.LABELS[idx])

plt.show()

cls = SoftmaxClassifier(input_shape=input_size_flattened, num_classes=cifar10.NUM_CLASSES)
cls.load(best_cls_path)
print_stats(y_test, cls.predict(X_test))
