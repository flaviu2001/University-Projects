from copy import deepcopy

from constants import MAX_SENSOR_COVERAGE
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
        for i in range(self.drone_map.number_of_sensors):
            self.pheromone_matrix.append([])
            for j in range(self.drone_map.number_of_sensors):
                if i != j:
                    self.pheromone_matrix[-1].append([1] * (MAX_SENSOR_COVERAGE+1))
                else:
                    self.pheromone_matrix[-1].append([0] * (MAX_SENSOR_COVERAGE+1))
        self.initial_pheromone_matrix = deepcopy(self.pheromone_matrix)

    def iterate(self):
        population = []
        for i in range(self.ants):
            ant = Ant(self.drone_map)
            population.append(ant)
        for i in range(self.drone_map.battery):
            for ant in population:
                ant.go_to_next_sensor(self.pheromone_matrix, self.alpha, self.beta, self.q0)
        for i in range(self.drone_map.number_of_sensors):
            for j in range(self.drone_map.number_of_sensors):
                for spent_energy in range(MAX_SENSOR_COVERAGE + 1):
                    self.pheromone_matrix[i][j][spent_energy] = (1 - self.rho) * \
                                                                self.pheromone_matrix[i][j][
                                                                    spent_energy] + self.rho * \
                                                                self.initial_pheromone_matrix[i][
                                                                    j][
                                                                    spent_energy]
        for ant in population:
            for i in range(len(ant.sensor_path) - 1):
                x = ant.sensor_path[i]
                y = ant.sensor_path[i + 1]
                self.pheromone_matrix[x[0]][y[0]][y[1]] += 1 / (len(ant.sensor_path) - 1)
        best_solution = population[max([[population[i].coverage(), i] for i in range(len(population))])[1]]
        return best_solution
