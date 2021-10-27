import statistics

from repository import deepcopy, Individual


class Controller:
    def __init__(self, repository):
        self.repository = repository

    def iteration(self, population_size, mutation_probability, crossover_probability):
        new_population = []
        for _ in range(population_size):
            parent1 = self.repository.population.selection()
            parent2 = self.repository.population.selection()
            offspring, _ = Individual.crossover(self.repository.drone_map, parent1, parent2, crossover_probability)
            offspring.mutate(mutation_probability)
            new_population.append(offspring)
        self.repository.set_new_population(new_population)

    def run(self, population_size, number_of_runs, mutation_probability, crossover_probability):
        fitness_list_avg = []
        fitness_list_max = []
        best_solution = None
        for _ in range(number_of_runs):
            self.iteration(population_size, mutation_probability, crossover_probability)
            fitness_list_avg.append(
                statistics.mean([individual.fitness for individual in self.repository.population.population])
            )
            fitness_list_max.append(
                max([individual.fitness for individual in self.repository.population.population])
            )
            for individual in self.repository.population.population:
                if best_solution is None or best_solution.fitness < individual.fitness:
                    best_solution = deepcopy(individual)
        path = best_solution.path_of_chromosome()
        return path, fitness_list_avg, fitness_list_max, best_solution.fitness

    def solver(self, population_size, individual_size, number_of_runs, battery, going_back, mutation_probability,
               crossover_probability):
        self.repository.create_population(battery, population_size, individual_size, going_back)
        return self.run(population_size, number_of_runs, mutation_probability, crossover_probability)
