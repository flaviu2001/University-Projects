import numpy as np


def softmax(x: np.ndarray, t=1):
    """"
    Applies the softmax temperature on the input x, using the temperature t
    """
    temp_adjusted_x = x
    temp_adjusted_x -= np.max(temp_adjusted_x)
    temp_adjusted_x = np.divide(temp_adjusted_x, t)
    return np.divide(np.exp(temp_adjusted_x), np.sum(np.exp(temp_adjusted_x)))
