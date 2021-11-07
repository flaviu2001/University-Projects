import numpy as np


def confusion_matrix(y_true: np.ndarray, y_pred: np.ndarray, num_classes=None) -> np.ndarray:
    """"
    Computes the confusion matrix from labels (y_true) and predictions (y_pred).
    The matrix columns represent the prediction labels and the rows represent the ground truth labels.
    The confusion matrix is always a 2-D array of shape `[num_classes, num_classes]`,
    where `num_classes` is the number of valid labels for a given classification task.
    The arguments y_true and y_pred must have the same shapes in order for this function to work

    num_classes represents the number of classes for the classification problem. If this is not provided,
    it will be computed from both y_true and y_pred
    """
    # even here try to use vectorization, so NO for loops

    # 0. if the number of classes is not provided, compute it based on the y_true and y_pred arrays

    # 1. create a confusion matrix of shape (num_classes, num_classes) and initialize it to 0

    # 2. use argmax to get the maximal prediction for each sample
    # hint: you might find np.add.at useful: https://numpy.org/doc/stable/reference/generated/numpy.ufunc.at.html

    if num_classes is None:
        num_classes = len(np.unique(np.concatenate([y_true, y_pred])))
    conf_mat = np.zeros((num_classes, num_classes))
    np.add.at(conf_mat, (y_true, y_pred), 1)
    return conf_mat


def precision_score(y_true: np.ndarray, y_pred: np.ndarray, num_classes=None) -> float:
    """"
    Computes the precision score.
    For binary classification, the precision score is defined as the ratio tp / (tp + fp)
    where tp is the number of true positives and fp the number of false positives.

    For multiclass classification, the precision and recall scores are obtained by summing over the rows / columns
    of the confusion matrix.

    num_classes represents the number of classes for the classification problem. If this is not provided,
    it will be computed from both y_true and y_pred
    """
    if num_classes is None:
        num_classes = len(np.unique(np.concatenate([y_true, y_pred])))
    conf = confusion_matrix(y_true, y_pred, num_classes)
    tp = np.diag(conf)
    tp_plus_fp = np.sum(conf, axis=0)
    precision = np.divide(tp, tp_plus_fp, where=tp_plus_fp != 0)
    return precision


def recall_score(y_true: np.ndarray, y_pred: np.ndarray, num_classes=None) -> float:
    """"
    Computes the recall score.
    For binary classification, the recall score is defined as the ratio tp / (tp + fn)
    where tp is the number of true positives and fn the number of false negatives

    For multiclass classification, the precision and recall scores are obtained by summing over the rows / columns
    of the confusion matrix.

    num_classes represents the number of classes for the classification problem. If this is not provided,
    it will be computed from both y_true and y_pred
    """
    if num_classes is None:
        num_classes = len(np.unique(np.concatenate([y_true, y_pred])))
    conf = confusion_matrix(y_true, y_pred, num_classes)
    tp = np.diag(conf)
    tp_plus_fn = np.sum(conf, axis=1)
    precision = np.divide(tp, tp_plus_fn, where=tp_plus_fn != 0)
    return precision


def accuracy_score(y_true: np.ndarray, y_pred: np.ndarray) -> float:
    # remember, use vectorization, so no for loops
    # hint: you might find np.trace useful here https://numpy.org/doc/stable/reference/generated/numpy.trace.html
    return np.trace(confusion_matrix(y_true, y_pred)) / len(y_true)


def f1_score(y_true: np.ndarray, y_pred: np.ndarray, num_classes=None) -> float:
    precision = precision_score(y_true, y_pred)
    recall = recall_score(y_true, y_pred)
    if num_classes is None:
        num_classes = len(np.unique(np.concatenate([y_true, y_pred])))
    multiply = 2 * precision * recall
    pr_sum = precision + recall
    return np.divide(multiply, pr_sum, where=pr_sum != 0)


def print_stats(y_true, y_pred):
    print("Confusion")
    print(confusion_matrix(y_true, y_pred))
    print("Precision")
    print(precision_score(y_true, y_pred))
    print("Recall")
    print(recall_score(y_true, y_pred))
    print("Accuracy")
    print(accuracy_score(y_true, y_pred))
    print("F1 score")
    print(f1_score(y_true, y_pred))


if __name__ == '__main__':
    y_true = np.array([2, 1, 4, 3, 6, 5, 8, 7, 0, 9])
    y_pred = np.array([2, 2, 2, 3, 6, 5, 7, 8, 0, 0])
    from sklearn import metrics
    assert np.allclose(metrics.confusion_matrix(y_true, y_pred), confusion_matrix(y_true, y_pred))
    assert np.allclose(metrics.precision_score(y_true, y_pred, average=None, zero_division=0), precision_score(y_true, y_pred))
    assert np.allclose(metrics.recall_score(y_true, y_pred, average=None, zero_division=0), recall_score(y_true, y_pred))
    assert metrics.accuracy_score(y_true, y_pred) == accuracy_score(y_true, y_pred)
    assert np.allclose(metrics.f1_score(y_true, y_pred, average=None, zero_division=0), f1_score(y_true, y_pred))
    print_stats(y_true, y_pred)
