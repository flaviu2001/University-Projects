import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers


def resnetBlock(inputs, number_of_filters, filter_dimension, strides):
    x1 = layers.Conv2D(number_of_filters, filter_dimension, padding="same", strides=strides, activation="relu")(inputs)
    x2 = layers.Conv2D(number_of_filters, filter_dimension, padding="same", strides=strides, activation="relu")(x1)

    added = tf.keras.layers.Add()([inputs, x2])

    return added


def buildModel(image_shape=(32, 32, 3)):
    img_inputs = keras.Input(shape=image_shape)

    filters = 16
    kernel_size = (3, 3)
    strides = (1, 1)
    x = layers.Conv2D(filters, kernel_size, strides=strides, activation="relu")(img_inputs)

    x = resnetBlock(x, filters, kernel_size, strides)

    x = tf.keras.layers.GlobalAveragePooling2D()(x)

    number_of_classes = 37
    x = tf.keras.layers.Dense(number_of_classes, kernel_initializer="he_uniform", activation="relu")(x)

    model = tf.keras.Model(img_inputs, x)
    model.summary()


buildModel()
