import statistics

from repository import deepcopy, DIRECTIONS, Individual


class Controller:
    def __init__(self, repository):
        self.repository = repository

    def iteration(self, population_size):
        new_population = []
        for _ in range(population_size):
            parent1 = self.repository.population.selection()
            parent2 = self.repository.population.selection()
            offspring, _ = Individual.crossover(self.repository.drone_map, parent1, parent2)
            offspring.mutate()
            new_population.append(offspring)
        self.repository.set_new_population(new_population)

    def run(self, population_size, number_of_runs, battery):
        fitness_list_avg = []
        fitness_list_max = []
        best_solution = None
        for _ in range(number_of_runs):
            self.iteration(population_size)
            fitness_list_avg.append(
                statistics.mean([individual.fitness for individual in self.repository.population.population])
            )
            fitness_list_max.append(
                max([individual.fitness for individual in self.repository.population.population])
            )
            for individual in self.repository.population.population:
                if best_solution is None or best_solution.fitness < individual.fitness:
                    best_solution = deepcopy(individual)
        drone = self.repository.drone_map.x, self.repository.drone_map.y
        path = [drone]
        moves_made = 0
        for x in best_solution.chromosome:
            next_position = drone[0] + DIRECTIONS[x][0], drone[1] + DIRECTIONS[x][1]
            if 0 <= next_position[0] < self.repository.drone_map.n and\
                    0 <= next_position[1] < self.repository.drone_map.m:
                if self.repository.drone_map.surface[next_position[0]][next_position[1]] != 1:
                    if battery >= moves_made+1:
                        moves_made += 1
                        drone = next_position
                        path.append(drone)
        return path, fitness_list_avg, fitness_list_max, best_solution.fitness

    def solver(self, population_size, individual_size, number_of_runs, battery):
        self.repository.create_population(battery, population_size, individual_size)
        return self.run(population_size, number_of_runs, battery)
