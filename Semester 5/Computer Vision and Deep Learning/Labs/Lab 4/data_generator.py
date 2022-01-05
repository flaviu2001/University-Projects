import cv2.cv2 as cv2
import numpy as np
import tensorflow as tf


class DataGenerator(tf.keras.utils.Sequence):
    def __init__(self, labels_file, label_names_file, batch_size, input_size, shuffle=True):
        self.input_size = input_size
        self.batch_size = batch_size
        self.shuffle = shuffle
        self.class_names = {}
        with open(label_names_file) as f:
            for line in f.readlines():
                class_id, name = line[:-1].split(",")
                self.class_names[int(class_id)] = name
        self.num_classes = len(self.class_names)
        self.data, self.labels = self.get_data(labels_file)
        self.indices = np.arange(len(self.data))
        self.on_epoch_end()

    def get_data(self, root_dir):
        """"
        Loads the paths to the images and their corresponding labels from the database directory
        """
        self.data = []
        self.labels = []
        with open(root_dir) as file:
            lines = file.readlines()
            for line in lines:
                path, class_id = line[:-1].split(",")
                self.data.append(path)
                self.labels.append(int(class_id))
        return self.data, np.asarray(self.labels)

    def __len__(self):
        """
        Returns the number of batches per epoch: the total size of the dataset divided by the batch size
        """
        return int(np.floor(len(self.data) / self.batch_size))

    def __getitem__(self, index):
        """"
        Generates a batch of data
        """
        batch_indices = self.indices[index * self.batch_size: min(len(self.indices), (index + 1) * self.batch_size)]
        batch_x = []
        batch_y = []
        for i in batch_indices:
            image = cv2.imread(self.data[i])
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            image = DataGenerator.resize_image(image, self.input_size)
            image = image / 255.0
            batch_x.append(image)
            batch_y.append(self.labels[i])
        return np.asarray(batch_x), np.asarray(batch_y)

    def on_epoch_end(self):
        """"
        Called at the end of each epoch
        """
        self.indices = np.arange(len(self.data))
        if self.shuffle:
            np.random.shuffle(self.indices)

    @staticmethod
    def pad_image(image):
        width_pad = 0
        height_pad = 0
        if image.shape[0] > image.shape[1]:
            width_pad = (image.shape[0] - image.shape[1]) // 2
        else:
            height_pad = (image.shape[1] - image.shape[0]) // 2
        return np.pad(image, ((height_pad, height_pad), (width_pad, width_pad), (0, 0)), mode="edge")

    @staticmethod
    def resize_image(image, shape):
        image = DataGenerator.pad_image(image)
        return cv2.resize(image, shape)
