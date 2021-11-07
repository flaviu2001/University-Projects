import os
import pickle

import numpy as np

LABELS = ['airplane', 'automobile', 'bird', 'cat', 'deer', 'dog', 'frog', 'horse', 'ship', 'truck']
NUM_CLASSES = len(LABELS)


def load_batch(filepath):
    """"
    Loads a batch from the cifar-10 dataset.
    A batch file contains a dictionary with the following elements:
    data -- a 10000x3072 numpy array of uint8s.
            Each row of the array stores a 32x32 colour image.
            The first 1024 entries contain the red channel values, the next 1024 the green, and the final 1024 the blue.
            The image is stored in row-major order, so that the first 32 entries of the array are the red channel values
            of the first row of the image.
    labels -- a list of 10000 numbers in the range 0-9.
            The number at index i indicates the label of the ith image in the array data.
    """
    with open(filepath, 'rb') as f:
        data = pickle.load(f, encoding='latin1')
        X = data['data']
        y = data['labels']
        # transform the X vector such that each element from the vector is 32x32 color image
        # 0. first reshape the vector to (num_images, 3, 32, 32)
        # then transpose it, such that the images are stored in (rows, cols, channels) order
        X = X.reshape(X.shape[0], 3, 32, 32)
        X = np.transpose(X, axes=[0, 2, 3, 1])
        return X, np.asarray(y)


def load_cifar10(root_dir):
    """"
    This function loads the entire cifar-10 dataset.
    It returns the training set (each element in the training set is a 32x32 color image), the training set labels,
    the test set (each element in the test set is a 32x32 color image) and the test set labels

    The root_dir is the path to the directory where the dataset was downloaded
    """
    cifar10_train_batches = [os.path.join(root_dir, 'data_batch_{}'.format(i)) for i in range(1, 6)]

    # training data and labels
    X_train = []
    y_train = []

    for batch in cifar10_train_batches:
        xb, yb = load_batch(batch)
        X_train.extend(xb)
        y_train.extend(yb)

    X_train = np.asarray(X_train)
    y_train = np.asarray(y_train)

    assert (X_train.shape[0] == 50000)
    assert (X_train[0].shape == (32, 32, 3))
    assert (X_train.shape[0] == y_train.shape[0])

    # test data and labels
    cifar10_test_batch = os.path.join(root_dir, 'test_batch')
    X_test, y_test = load_batch(cifar10_test_batch)

    assert (X_test.shape[0] == 10000)
    assert (X_test[0].shape == (32, 32, 3))
    assert (X_test.shape[0] == y_test.shape[0])

    return X_train, y_train, X_test, y_test
