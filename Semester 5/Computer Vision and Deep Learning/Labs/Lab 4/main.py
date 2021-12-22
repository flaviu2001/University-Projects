import random

from keras import layers
from matplotlib import pyplot as plt
from tensorflow import keras

from data_generator import DataGenerator

OUTPUTS = 2
INPUT_SHAPE = (64, 64)
INPUT_SHAPE_RGB = (*INPUT_SHAPE, 3)
BATCH_SIZE = 8
EPOCHS = 10


def generate_test_train():
    with open("data/photos.csv", "r") as file:
        with open("data/test.csv", "w") as test:
            with open("data/train.csv", "w") as train:
                for line in file.readlines():
                    if random.random() < 0.1:
                        test.write(line)
                    else:
                        train.write(line)


def resnet_block(input_layer, filter_size=3, no_filters=16):
    layer1 = layers.Conv2D(kernel_size=filter_size, filters=no_filters, padding="same")(input_layer)
    layer2 = layers.Conv2D(kernel_size=filter_size, filters=no_filters, padding="same")(layer1)
    return layers.Add()([input_layer, layer2])


def build_mini_resnet(input_size, num_classes):
    inputs = layers.Input(shape=input_size)
    x = layers.Conv2D(kernel_size=3, filters=16, strides=2)(inputs)
    x = resnet_block(x)
    x = resnet_block(x)
    x = layers.GlobalAvgPool2D()(x)
    x = layers.Dense(num_classes)(x)
    return keras.Model(inputs=inputs, outputs=x, name="mini_resnet")


def plot_history(history_to_plot):
    plt.subplot(1, 2, 1)
    plt.plot(history_to_plot.history['accuracy'], label='accuracy')
    plt.plot(history_to_plot.history['val_accuracy'], label='val_accuracy')
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy')
    plt.ylim([0, 1])
    plt.legend(loc='upper right')
    plt.subplot(1, 2, 2)
    plt.plot(history_to_plot.history['loss'], label='loss')
    plt.plot(history_to_plot.history['val_loss'], label='val_loss')
    plt.xlabel('Epoch')
    plt.ylabel('Loss')
    plt.legend(loc='upper right')
    plt.show()


train_set = DataGenerator("data/train.csv", "data/classes.csv", BATCH_SIZE, INPUT_SHAPE)
test_set = DataGenerator("data/test.csv", "data/classes.csv", BATCH_SIZE, INPUT_SHAPE)


def train(optimizer):
    model = build_mini_resnet(INPUT_SHAPE_RGB, OUTPUTS)
    model.summary()

    model.compile(
        loss=keras.losses.SparseCategoricalCrossentropy(from_logits=True),
        optimizer=optimizer,
        metrics=["accuracy"],
    )

    history = model.fit(x=train_set, validation_data=test_set, batch_size=BATCH_SIZE, epochs=EPOCHS, shuffle=True)
    plot_history(history)


lr_schedule = keras.optimizers.schedules.ExponentialDecay(
                initial_learning_rate=3e-4,
                decay_steps=5,
                decay_rate=0.96)
# train(keras.optimizers.Adam(lr_schedule))
train(keras.optimizers.Adam(1e-3))
