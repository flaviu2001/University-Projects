import pickle

import numpy as np

from lab.activations import softmax


class SoftmaxClassifier:
    def __init__(self, input_shape, num_classes):
        self.input_shape = input_shape
        self.num_classes = num_classes
        self.W: np.ndarray or None = None
        self.initialize()

    def initialize(self):
        # initialize the weight matrix (remember the bias trick) with small random variables
        # you might find np.random.randn userful here *0.001
        self.W = np.random.randn(self.input_shape - 1, self.num_classes) * 0.001
        self.W = np.concatenate([self.W, np.ones((1, self.num_classes))])

    def predict_proba(self, input_array: np.ndarray) -> np.ndarray:
        # 0. compute the dot product between the weight matrix and the input X
        # remember about the bias trick!
        # 1. apply the softmax function on the scores
        # 2, returned the normalized scores
        product = softmax(input_array.dot(self.W))
        return product

    def predict(self, input_array: np.ndarray) -> np.ndarray:
        # 0. compute the dot product between the weight matrix and the input array as the scores
        # 1. compute the prediction by taking the argmax of the class scores
        return np.argmax(input_array.dot(self.W), axis=1)

    def fit(self, x_train: np.ndarray, y_train: np.ndarray,
            **kwargs) -> list:
        history = []
        bs = kwargs['bs'] if 'bs' in kwargs else 128
        reg_strength = kwargs['reg_strength'] if 'reg_strength' in kwargs else 1e3
        steps = kwargs['steps'] if 'steps' in kwargs else 100
        lr = kwargs['lr'] if 'lr' in kwargs else 1e-3

        for iteration in range(0, steps):
            actual_bs = min(bs, len(x_train))
            indices = np.random.choice(len(x_train), actual_bs, replace=False)
            x_batch, y_batch = x_train[indices], y_train[indices]
            output = np.dot(x_batch, self.W)
            stabilized_output = output - np.max(output, axis=1, keepdims=True)
            CT = softmax(stabilized_output)
            CT[range(actual_bs), y_batch] -= 1
            dW = np.dot(np.transpose(x_batch), CT) + reg_strength * self.W
            loss = -stabilized_output[range(actual_bs), y_batch] + np.log(np.sum(np.exp(stabilized_output), axis=1))
            loss = np.mean(loss) + reg_strength * np.sum(np.square(self.W))
            self.W -= lr * dW
            history.append(loss)

        return history

    def load(self, path: str) -> bool:
        # load the input shape, the number of classes and the weight matrix from a file
        try:
            with open(path, "rb") as file:
                triple = pickle.load(file)
            self.input_shape = triple[0]
            self.num_classes = triple[1]
            self.W = triple[2]
            return True
        except OSError:
            return False

    def save(self, path: str) -> bool:
        # save the input shape, the number of classes and the weight matrix to a file
        # you might find np.save useful for this
        try:
            with open(path, "wb") as file:
                pickle.dump((self.input_shape, self.num_classes, self.W), file)
            return True
        except OSError:
            return False

    def get_weights(self, img_shape):
        # 0. ignore the bias term
        # 1. reshape the weights to (*image_shape, num_classes)
        return self.W[:-1].reshape(self.num_classes, *img_shape)
