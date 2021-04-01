import random
from copy import deepcopy

import numpy

from constants import DIRECTIONS, MAX_SENSOR_COVERAGE


class DroneMap:
    def __init__(self, n, m, battery):
        self.surface = None
        self.sighted_cells = None
        self.x = None
        self.y = None
        self.battery = battery
        self.n = n
        self.m = m

    def update_sighted_cells(self):
        self.sighted_cells = []
        for i in range(self.n):
            self.sighted_cells.append([])
            for j in range(self.m):
                if self.surface[i][j] != 1:
                    self.sighted_cells[-1].append([0] * (MAX_SENSOR_COVERAGE + 1))
                else:
                    self.sighted_cells[-1].append([0] * (MAX_SENSOR_COVERAGE + 1))
                    neighbours = [[i, j], [i, j], [i, j], [i, j]]
                    cells = 0
                    for k in range(1, MAX_SENSOR_COVERAGE + 1):
                        for direction_index in range(len(DIRECTIONS)):
                            new_neighbour = deepcopy(neighbours[direction_index])
                            new_neighbour[0] += DIRECTIONS[direction_index][0]
                            new_neighbour[1] += DIRECTIONS[direction_index][1]
                            if 0 <= new_neighbour[0] < self.n and 0 <= new_neighbour[1] < self.m:
                                if self.surface[new_neighbour[0]][new_neighbour[1]] != 2:
                                    cells += 1
                                    neighbours[direction_index] = new_neighbour
                        self.sighted_cells[-1][-1][k] = cells

    def random_init(self, wall_fill_factor=0.2, sensor_fill_factor=0.1):
        self.surface = [
            [2 if random.random() < wall_fill_factor else 1 if random.random() < sensor_fill_factor else 0 for _ in
             range(self.m)]
            for _ in range(self.n)]
        self.x = random.randrange(self.n)
        self.y = random.randrange(self.m)
        while self.surface[self.x][self.y] == 2:
            self.x = random.randrange(self.n)
            self.y = random.randrange(self.m)
        self.update_sighted_cells()

    def file_init(self):
        self.surface = []
        with open("map.txt") as file:
            self.n, self.m = map(int, file.readline().strip().split(" "))
            for row in range(self.n):
                self.surface.append(list(map(int, file.readline().strip().split(" "))))
            self.x, self.y = list(map(int, file.readline().strip().split(" ")))
        self.update_sighted_cells()


class Ant:
    def __init__(self, drone_map: DroneMap):
        sensor_energy = 0
        if drone_map.surface[drone_map.x][drone_map.y] == 1:
            sensor_energy = min(drone_map.battery, random.randint(0, MAX_SENSOR_COVERAGE))
        self.path = [(drone_map.x, drone_map.y, sensor_energy)]
        self.drone_map = drone_map
        self.spent_energy = [[0 for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
        self.spent_energy[drone_map.x][drone_map.y] = sensor_energy
        self.battery_left = drone_map.battery - sensor_energy

    def coverage(self):
        marked = [[0 for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
        for cell in self.path:
            if cell[2] != 0:
                i = cell[0]
                j = cell[1]
                neighbours = [[i, j], [i, j], [i, j], [i, j]]
                for _ in range(cell[2]):
                    for direction_index in range(len(DIRECTIONS)):
                        new_neighbour = deepcopy(neighbours[direction_index])
                        new_neighbour[0] += DIRECTIONS[direction_index][0]
                        new_neighbour[1] += DIRECTIONS[direction_index][1]
                        if 0 <= new_neighbour[0] < self.drone_map.n and 0 <= new_neighbour[1] < self.drone_map.m:
                            if self.drone_map.surface[new_neighbour[0]][new_neighbour[1]] != 2:
                                marked[new_neighbour[0]][new_neighbour[1]] = 1
                                neighbours[direction_index] = new_neighbour
        return sum([sum(row) for row in marked])

    def increase_path(self, pheromone_matrix, alpha, beta, q0):
        current_cell = (self.path[-1][0], self.path[-1][1])
        possible_next_cells = []
        for direction_index in range(len(DIRECTIONS)):
            for spend_energy in range(min(MAX_SENSOR_COVERAGE + 1, self.battery_left)):
                if (tau := pheromone_matrix[current_cell[0]][current_cell[1]][direction_index][spend_energy]) != 0:
                    cost = (1 / (spend_energy + 1)) ** beta + tau ** alpha
                    next_cell = [(current_cell[0] + DIRECTIONS[direction_index][0],
                                  current_cell[1] + DIRECTIONS[direction_index][1], spend_energy), cost]
                    if self.spent_energy[next_cell[0][0]][next_cell[0][1]] <= spend_energy:
                        possible_next_cells.append(next_cell)
        if len(possible_next_cells) == 0:
            return
        if random.random() < q0:
            self.path.append(max(possible_next_cells, key=lambda x: x[1])[0])
            self.battery_left -= self.path[-1][2] + 1
            self.spent_energy[self.path[-1][0]][self.path[-1][1]] = self.path[-1][2]
        else:
            probabilities_sum = sum([move[1] for move in possible_next_cells])
            next_cell = numpy.random.choice([i for i in range(len(possible_next_cells))],
                                            p=[move[1] / probabilities_sum for move in possible_next_cells])
            self.path.append(possible_next_cells[next_cell][0])
            self.battery_left -= self.path[-1][2] + 1
            self.spent_energy[self.path[-1][0]][self.path[-1][1]] = self.path[-1][2]
