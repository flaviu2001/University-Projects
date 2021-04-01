from copy import deepcopy

from constants import DIRECTIONS, MAX_SENSOR_COVERAGE
from model import DroneMap, Ant
from params import Params


class Controller:
    def __init__(self, drone_map: DroneMap):
        params = Params()
        self.ants = params.ants
        self.alpha = params.alpha
        self.beta = params.beta
        self.rho = params.rho
        self.q0 = params.q0
        self.drone_map = drone_map
        self.pheromone_matrix = []
        for i in range(self.drone_map.n):
            self.pheromone_matrix.append([])
            for j in range(self.drone_map.m):
                self.pheromone_matrix[-1].append([])
                for direction_index in range(len(DIRECTIONS)):
                    neighbour = [i + DIRECTIONS[direction_index][0], j + DIRECTIONS[direction_index][1]]
                    if 0 <= neighbour[0] < self.drone_map.n and 0 <= neighbour[1] < self.drone_map.m:
                        if self.drone_map.surface[neighbour[0]][neighbour[1]] != 2:
                            if self.drone_map.surface[neighbour[0]][neighbour[1]] == 1:
                                self.pheromone_matrix[-1][-1].append([1, 1, 1, 1, 1, 1])
                            else:
                                self.pheromone_matrix[-1][-1].append([1, 0, 0, 0, 0, 0])
                        else:
                            self.pheromone_matrix[-1][-1].append([0] * (MAX_SENSOR_COVERAGE + 1))
                    else:
                        self.pheromone_matrix[-1][-1].append([0] * (MAX_SENSOR_COVERAGE + 1))
        self.initial_pheromone_matrix = deepcopy(self.pheromone_matrix)

    def iterate(self):
        population = []
        for i in range(self.ants):
            ant = Ant(self.drone_map)
            population.append(ant)
        for i in range(self.drone_map.battery):
            for ant in population:
                ant.increase_path(self.pheromone_matrix, self.alpha, self.beta, self.q0)
        for i in range(self.drone_map.n):
            for j in range(self.drone_map.m):
                for direction_index in range(len(DIRECTIONS)):
                    for spent_energy in range(MAX_SENSOR_COVERAGE + 1):
                        self.pheromone_matrix[i][j][direction_index][spent_energy] = (1 - self.rho) * \
                                                                                     self.pheromone_matrix[i][j][
                                                                                         direction_index][
                                                                                         spent_energy] + self.rho * \
                                                                                     self.initial_pheromone_matrix[i][
                                                                                         j][direction_index][
                                                                                         spent_energy]
        best_solution = population[max([[population[i].coverage(), i] for i in range(len(population))])[1]]
        best_coverage = best_solution.coverage()
        for ant in population:
            ant_coverage = ant.coverage()
            for i in range(len(ant.path) - 1):
                x = ant.path[i]
                y = ant.path[i + 1]
                dir_idx = 0
                for direction_index in range(len(DIRECTIONS)):
                    if (x[0] + DIRECTIONS[direction_index][0], x[1] + DIRECTIONS[direction_index][1]) == (y[0], y[1]):
                        dir_idx = direction_index
                        break
                self.pheromone_matrix[x[0]][x[1]][dir_idx][y[2]] += (ant_coverage + 1) / ((best_coverage + 1) * len(ant.path))
        return best_solution
