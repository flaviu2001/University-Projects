import pickle

from domain import *


class Repository:
    def __init__(self):
        self.population = None
        self.drone_map = DroneMap()

    def create_population(self, battery, population_size, individual_size, going_back):
        self.population = Population(self.drone_map, battery, population_size, individual_size, going_back)

    def set_new_population(self, population_list):
        self.population = Population(self.drone_map, population=population_list)

    def load_random_map(self, fill_factor):
        self.drone_map.random_map(fill_factor)

    def save_map(self):
        with open("file.map", "wb") as file:
            pickle.dump(self.drone_map, file)

    def load_map(self):
        with open("file.map", "rb") as file:
            self.drone_map = pickle.load(file)
