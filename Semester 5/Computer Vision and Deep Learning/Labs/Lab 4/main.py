import random

from keras import layers
from matplotlib import pyplot as plt
from tensorflow import keras

from data_generator import DataGenerator

OUTPUTS = 1
with open("data/classes.csv") as f:
    for line in f.readlines():
        class_id = int(line[:-1].split(",")[0])
        OUTPUTS = max(OUTPUTS, class_id+1)
print(OUTPUTS)
INPUT_SHAPE = (32, 32)
INPUT_SHAPE_RGB = (*INPUT_SHAPE, 3)
BATCH_SIZE = 32
EPOCHS = 10


def generate_test_train():
    with open("data/photos.csv", "r") as file:
        with open("data/test.csv", "w") as test:
            with open("data/train.csv", "w") as train:
                for line in file.readlines():
                    if random.random() < 0.2:
                        test.write(line)
                    else:
                        train.write(line)


def resnet_block(input_layer, filter_size=3, no_filters=16):
    layer1 = layers.Conv2D(kernel_size=filter_size, filters=no_filters, padding="same", activation='relu', kernel_regularizer=keras.regularizers.l2(0.001))(input_layer)
    layer2 = layers.Conv2D(kernel_size=filter_size, filters=no_filters, padding="same", activation='relu', kernel_regularizer=keras.regularizers.l2(0.001))(layer1)
    return layers.Add()([input_layer, layer2])


def build_mini_resnet(input_size, num_classes):
    inputs = layers.Input(shape=input_size)
    x = layers.Conv2D(kernel_size=3, filters=32, strides=2, kernel_regularizer=keras.regularizers.l2(0.001))(inputs)
    x = resnet_block(x, no_filters=32)
    x = resnet_block(x, no_filters=32)
    x = layers.Conv2D(kernel_size=3, filters=64, strides=2, kernel_regularizer=keras.regularizers.l2(0.001))(x)
    x = resnet_block(x, no_filters=64)
    x = resnet_block(x, no_filters=64)
    x = layers.Conv2D(kernel_size=3, filters=128, strides=2, kernel_regularizer=keras.regularizers.l2(0.001))(x)
    x = resnet_block(x, no_filters=128)
    x = layers.Flatten()(x)
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


def save_model(model, name):
    test_generator = DataGenerator("data/test.csv", "data/classes.csv", BATCH_SIZE, INPUT_SHAPE)
    val_loss, val_acc = model.evaluate(test_generator, verbose=2)
    model.save(f"./weights/acc_{str(val_acc)[:5]}_{name}")


train_set = DataGenerator("data/train.csv", "data/classes.csv", BATCH_SIZE, INPUT_SHAPE)
test_set = DataGenerator("data/test.csv", "data/classes.csv", BATCH_SIZE, INPUT_SHAPE)


def train(optimizer, name):
    model = build_mini_resnet(INPUT_SHAPE_RGB, OUTPUTS)
    model.summary()

    model.compile(
        loss=keras.losses.SparseCategoricalCrossentropy(from_logits=True),
        optimizer=optimizer,
        metrics=["accuracy"],
    )

    history = model.fit(x=train_set, validation_data=test_set, batch_size=BATCH_SIZE, epochs=EPOCHS, shuffle=True)
    save_model(model, name)
    plot_history(history)


# lr_schedule = keras.optimizers.schedules.ExponentialDecay(
#                 initial_learning_rate=3e-4,
#                 decay_steps=5,
#                 decay_rate=0.96)
# # train(keras.optimizers.Adam(lr_schedule))
train(keras.optimizers.Adam(), "adam")


def make_ensemble(input_size, paths):
    inputs = layers.Input(shape=input_size)
    models = [keras.models.load_model(path) for path in paths]
    for i, ensemble_model in enumerate(models):
        ensemble_model._name += str(i)
    x = layers.Average()([model(inputs) for model in models])
    return keras.Model(inputs=inputs, outputs=x, name="ensemble")


# evaluate the ensemble
# model = make_ensemble(INPUT_SHAPE_RGB, [
#     "weights/acc_0.604_adam_exp_decay",
#     "weights/acc_0.658_adam_1e3-3",
#     "weights/acc_0.661_adam_1e3-3/"
# ])
# model.compile(optimizer=keras.optimizers.Adam(),
#               loss=keras.losses.SparseCategoricalCrossentropy(from_logits=True),
#               metrics=['accuracy'],
#               )
# val_loss, val_acc = model.evaluate(test_set, verbose=2)
