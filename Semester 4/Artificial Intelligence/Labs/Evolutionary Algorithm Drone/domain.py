from copy import deepcopy
from queue import Queue
from random import *

from utils import DIRECTIONS, MAP_LENGTH, OPPOSITE


class DroneMap:
    def __init__(self, n=MAP_LENGTH, m=MAP_LENGTH, x=None, y=None):
        self.n = n
        self.m = m
        if x is None or y is None:
            x = randrange(n)
        self.x = x
        if y is None:
            y = randrange(m)
        self.y = y
        self.surface = [[0 for _ in range(m)] for _ in range(n)]

    def random_map(self, fill=0.2):
        self.surface = [[0 for _ in range(self.m)] for _ in range(self.n)]
        for i in range(self.n):
            for j in range(self.m):
                if random() <= fill and (i != self.x or j != self.y):
                    self.surface[i][j] = 1


class Individual:
    def __init__(self, drone_map, battery, size=0, chromosome=None, go_back=False):
        self.drone_map = drone_map
        if chromosome is None:
            chromosome = [randrange(0, 4) for _ in range(size)]
        self.chromosome = chromosome
        self.battery = battery
        self.fitness = None
        self.go_back = go_back

    def path_of_chromosome(self):
        drone = [self.drone_map.x, self.drone_map.y]
        bfs_distances = None
        prev = None
        if self.go_back:
            bfs_distances = [[None for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
            prev = [[None for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
            position = (drone[0], drone[1])
            bfs_distances[drone[0]][drone[1]] = 0
            queue = Queue()
            queue.put(position)
            while not queue.empty():
                current_position = queue.get()
                for i in range(len(DIRECTIONS)):
                    new_position = (
                        current_position[0] + DIRECTIONS[i][0], current_position[1] + DIRECTIONS[i][1])
                    if 0 <= new_position[0] < self.drone_map.n and 0 <= new_position[1] < self.drone_map.m:
                        if self.drone_map.surface[new_position[0]][new_position[1]] == 0:
                            if bfs_distances[new_position[0]][new_position[1]] is None:
                                queue.put(new_position)
                                bfs_distances[new_position[0]][new_position[1]] = bfs_distances[current_position[0]][
                                                                                      current_position[1]] + 1
                                prev[new_position[0]][new_position[1]] = OPPOSITE[i]
        path = [drone]
        going_back = False
        if self.go_back and self.battery <= 1:
            going_back = True
        for i in range(len(self.chromosome)):
            if going_back:
                if prev[drone[0]][drone[1]] is None:
                    break
                new_drone = [drone[0] + DIRECTIONS[prev[drone[0]][drone[1]]][0],
                             drone[1] + DIRECTIONS[prev[drone[0]][drone[1]]][1]]
            else:
                new_drone = [drone[0] + DIRECTIONS[self.chromosome[i]][0], drone[1] + DIRECTIONS[self.chromosome[i]][1]]
            if 0 <= new_drone[0] < self.drone_map.n and 0 <= new_drone[1] < self.drone_map.m:
                if self.drone_map.surface[new_drone[0]][new_drone[1]] != 1:
                    if self.battery >= len(path):
                        drone = new_drone
                        path.append(drone)
                        if self.go_back and len(path) + bfs_distances[drone[0]][drone[1]] >= self.battery:
                            going_back = True
        return path

    def update_fitness(self):
        path = self.path_of_chromosome()
        marked = [[0 for _ in range(self.drone_map.m)] for _ in range(self.drone_map.n)]
        for position in path:
            marked[position[0]][position[1]] = 1
            for direction in DIRECTIONS:
                sight = deepcopy(position)
                while True:
                    sight[0] += direction[0]
                    sight[1] += direction[1]
                    valid = False
                    if 0 <= sight[0] < self.drone_map.n and 0 <= sight[1] < self.drone_map.m:
                        if self.drone_map.surface[sight[0]][sight[1]] != 1:
                            valid = True
                    if not valid:
                        break
                    marked[sight[0]][sight[1]] = 1
        self.fitness = sum([sum(row) for row in marked])

    def mutate(self, mutate_probability):  # swap mutation
        if random() < mutate_probability and len(self.chromosome) >= 2:
            i = 0
            j = 0
            while i == j:
                i = randrange(len(self.chromosome))
                j = randrange(len(self.chromosome))
            self.chromosome[i], self.chromosome[j] = self.chromosome[j], self.chromosome[i]

    @staticmethod
    def crossover(drone_map, first_parent, other_parent, crossover_probability):  # 1-cutting point crossover
        size = len(first_parent.chromosome)
        if random() < crossover_probability:
            cutting_point = randint(0, size)
            offspring1 = Individual(drone_map, first_parent.battery, chromosome=[
                first_parent.chromosome[i] if i < cutting_point else other_parent.chromosome[i] for i in range(size)],
                                    go_back=first_parent.go_back)
            offspring2 = Individual(drone_map, first_parent.battery, chromosome=[
                other_parent.chromosome[i] if i < cutting_point else first_parent.chromosome[i] for i in range(size)],
                                    go_back=first_parent.go_back)
        else:
            offspring1, offspring2 = Individual(drone_map, first_parent.battery, size,
                                                go_back=first_parent.go_back), Individual(drone_map,
                                                                                          first_parent.battery,
                                                                                          size,
                                                                                          go_back=first_parent.go_back)
        return offspring1, offspring2


class Population:
    def __init__(self, drone_map, battery=10, population_size=10, individual_size=10, going_back=False,
                 population=None):
        self.population_size = population_size
        if population is None:
            population = [Individual(drone_map, battery, individual_size, go_back=going_back) for _ in
                          range(population_size)]
        self.population = population
        self.evaluate()

    def evaluate(self):
        for x in self.population:
            x.update_fitness()

    def selection(self, k=2):
        return sorted(sample(self.population, k), key=lambda x: x.fitness, reverse=True)[0]
