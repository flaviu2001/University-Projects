import numpy as np
import tensorflow as tf

import cifar10
from activations import softmax
import matplotlib.pyplot as plt


def exercise_1():
    # let's check that you obtained the same values
    # as the softmax implementation in tensorflow
    arr = np.asarray([2, 4, 10, 100, 2.0])
    assert (np.allclose(tf.nn.softmax(arr).numpy(), softmax(arr)))

    arr = np.asarray([0.0, 0, 0, 1, 0])
    assert (np.allclose(tf.nn.softmax(arr).numpy(), softmax(arr)))
    arr = np.asarray([-750.0, 23, 9, 10, 230])
    assert (np.allclose(tf.nn.softmax(arr).numpy(), softmax(arr)))
    arr = np.ones((4,))
    assert (np.allclose(tf.nn.softmax(arr).numpy(), softmax(arr)))
    del arr


def exercise_2():
    x = np.asarray([20, 30, -15, 45, 39, -10])
    n = len(x)
    temperatures = [0.25, 0.75, 1, 1.5, 2, 5, 10, 20, 30]
    for temperature in temperatures:
        plt.plot(range(n), softmax(x, temperature))
    plt.legend(tuple(f"{temperature}" for temperature in temperatures))
    plt.show()


def exercise_3():
    cifar_root_dir = 'cifar-10-batches-py'
    _, _, X_test, y_test = cifar10.load_cifar10(cifar_root_dir)
    indices = np.random.choice(len(X_test), 15)

    display_images, display_labels = X_test[indices], y_test[indices]
    for idx, (img, label) in enumerate(zip(display_images, display_labels)):
        plt.subplot(3, 5, idx + 1)
        plt.imshow(img)
        plt.title(cifar10.LABELS[label])
        plt.tight_layout()
    plt.show()


exercise_3()
