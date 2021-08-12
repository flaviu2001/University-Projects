from matplotlib import pyplot

from controller import *
from gui import *
from repository import *


def main():
    repository = Repository()
    controller = Controller(repository)
    loaded_map = False
    parameters_setup = False
    program_solved = False
    population_size = None
    individual_size = None
    number_of_runs = None
    battery = None
    going_back = None
    path = None
    fitness_avg = None
    fitness_max = None
    mutation_probability = None
    crossover_probability = None
    speed = None
    while True:
        print("1. map options")
        print("2. evolutionary algorithm options")
        print("0. exit")
        choice1 = input("> ")
        if choice1 == "1":
            print("1. create a random map")
            print("2. load a map")
            print("3. save a map")
            print("4. visualise map")
            choice2 = input("> ")
            if choice2 == "1":
                loaded_map = True
                fill = input("fill factor (0.2): ")
                if len(fill) != 0:
                    fill = float(fill)
                else:
                    fill = 0.2
                controller.repository.load_random_map(fill)
            elif choice2 == "2":
                loaded_map = True
                controller.repository.load_map()
            elif choice2 == "3":
                if loaded_map:
                    controller.repository.save_map()
                else:
                    print("Map not yet loaded")
            elif choice2 == "4":
                if loaded_map:
                    just_the_drone(controller.repository.drone_map,
                                   (controller.repository.drone_map.x, controller.repository.drone_map.y))
                else:
                    print("Map not yet loaded")
            else:
                print("Incorrect choice")
        elif choice1 == "2":
            if not loaded_map:
                print("Map not yet loaded")
                continue
            print("1. parameters setup")
            print("2. run the solver once")
            print("3. run the solver many times and compute mean and standard deviation")
            print("4. visualise the statistics")
            print("5. view the drone on a path")
            choice2 = input("> ")
            if choice2 == "1":
                parameters_setup = True
                program_solved = False
                battery = input("battery (30): ")
                if len(battery) != 0:
                    battery = int(battery)
                else:
                    battery = 30
                going_back = input("going back (y/N): ")
                going_back = going_back == "y"
                population_size = input("population size (100): ")
                if len(population_size) != 0:
                    population_size = int(population_size)
                else:
                    population_size = 100
                individual_size = input(f"individual size ({battery * 2}): ")
                if len(individual_size) != 0:
                    individual_size = int(individual_size)
                else:
                    individual_size = battery * 2
                number_of_runs = input("number of runs (100): ")
                if len(number_of_runs) != 0:
                    number_of_runs = int(number_of_runs)
                else:
                    number_of_runs = 100
                mutation_probability = input("probability of mutation (0.05): ")
                if len(mutation_probability) != 0:
                    mutation_probability = float(mutation_probability)
                else:
                    mutation_probability = 0.05
                crossover_probability = input("probability of crossover (0.8): ")
                if len(crossover_probability) != 0:
                    crossover_probability = float(crossover_probability)
                else:
                    crossover_probability = 0.8
                speed = input("speed of drone in seconds per move (0.5): ")
                if len(speed) != 0:
                    speed = float(speed)
                else:
                    speed = 0.5
            elif choice2 == "2":
                if not parameters_setup:
                    print("Parameters not set up")
                    continue
                if program_solved:
                    print("Program already solved")
                    continue
                start = time.time()
                path, fitness_avg, fitness_max, solution_fitness = controller.solver(population_size, individual_size,
                                                                                     number_of_runs,
                                                                                     battery, going_back,
                                                                                     mutation_probability,
                                                                                     crossover_probability)
                end = time.time()
                print(f"Evolutionary algorithm ran in {end - start} seconds")
                print(f"It found a run with {len(path) - 1} moves and discovered {solution_fitness} cells")
                program_solved = True
            elif choice2 == "3":
                if not parameters_setup:
                    print("Parameters not set up")
                    continue
                values = []
                for i in range(30):
                    seed(i)
                    _, _, _, fitness = controller.solver(population_size, individual_size, number_of_runs, battery,
                                                         going_back, mutation_probability, crossover_probability)
                    values.append(fitness)
                avg = statistics.mean(values)
                stdev = statistics.stdev(values)
                print(f"Average solution fitness was found to be {avg} and it has a stdev of {stdev}")
                pyplot.plot(values)
                pyplot.ylim([0, None])
                pyplot.savefig("checker.png")
                pyplot.close()
            elif choice2 == "4":
                if not program_solved:
                    print("Program not yet solved")
                    continue
                pyplot.plot(fitness_avg)
                pyplot.plot(fitness_max)
                pyplot.savefig("fitness.png")
                pyplot.close()
            elif choice2 == "5":
                moving_drone(controller.repository.drone_map, path, speed=speed)
            else:
                print("Incorrect choice")
        elif choice1 == "0":
            break
        else:
            print("Incorrect choice")


if __name__ == "__main__":
    main()
