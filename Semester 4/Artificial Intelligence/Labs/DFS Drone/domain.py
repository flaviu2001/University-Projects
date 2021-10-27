from random import random

import numpy as np

from constants import *


class Environment:
    def __init__(self, rows=ROWS, columns=COLUMNS):
        self.__rows = rows
        self.__columns = columns
        self.__surface = np.zeros((self.__rows, self.__columns))

    def set_to(self, rows, columns, surface):
        self.__rows = rows
        self.__columns = columns
        self.__surface = surface

    def __str__(self):
        to_return = ""
        for i in range(self.__rows):
            for j in range(self.__columns):
                to_return = "{0} {1}".format(to_return, self.__surface[i][j])
            to_return += "\n"
        return to_return

    def wall_at(self, x, y):
        return self.__surface[x][y] == 1

    def get_rows(self):
        return self.__rows

    def get_columns(self):
        return self.__columns

    def get_surface(self):
        return self.__surface

    def generate_random_map(self, fill_factor=0.2):
        for i in range(self.__rows):
            for j in range(self.__columns):
                if random() <= fill_factor:
                    self.__surface[i][j] = 1

    def read_udm_sensors(self, x, y):
        readings = [0, 0, 0, 0]
        if x is None or y is None:
            return readings
        for i in range(len(DIRECTIONS)):
            direction = DIRECTIONS[i]
            current_direction = DIRECTIONS_ORDER[i]
            x2 = x + direction[0]
            y2 = y + direction[1]
            while 0 <= x2 < self.__rows and 0 <= y2 < self.__columns and self.__surface[x2][y2] == 0:
                x2 = x2 + direction[0]
                y2 = y2 + direction[1]
                readings[current_direction] += 1
        return readings


class DroneMap:
    def __init__(self, rows=ROWS, columns=COLUMNS):
        self.__rows = rows
        self.__columns = columns
        self.__surface = np.zeros((self.__rows, self.__columns))
        for i in range(self.__rows):
            for j in range(self.__columns):
                self.__surface[i][j] = -1

    def get_surface(self):
        return self.__surface

    def mark_detected_walls(self, readings, x, y):
        if x is None or y is None:
            return
        for direction in DIRECTIONS:
            x2, y2 = x + direction[0], y + direction[1]
            while 0 <= x2 < self.__rows and 0 <= y2 < self.__columns and\
                    x - readings[UP] <= x2 <= x + readings[DOWN] and y - readings[LEFT] <= y2 <= y + readings[RIGHT]:
                self.__surface[x2][y2] = 0
                x2 = x2 + direction[0]
                y2 = y2 + direction[1]
            if 0 <= x2 < self.__rows and 0 <= y2 < self.__columns:
                self.__surface[x2][y2] = 1


class Drone:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def move(self, destination):
        self.x = destination[0]
        self.y = destination[1]
